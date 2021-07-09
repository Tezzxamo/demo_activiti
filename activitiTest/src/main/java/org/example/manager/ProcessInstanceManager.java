package org.example.manager;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.*;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.example.Utils.VerificationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * functions:
 * ①创建一个流程实例
 * ②删除一个流程实例
 * ③完成当前流程实例
 * ④拾取当前流程实例
 * ⑤强制拾取当前流程实例
 * ⑥通过流程定义name获取所有的流程实例
 * ⑦通过流程定义name和流程实例状态获取所有的流程实例
 * ⑧通过流程定义name和业务标识获取对应的流程实例
 * ⑨获取当前正在执行的流程实例
 * 10检查流程实例、历史流程实例、流程定义是否存在
 */
@Component
@Slf4j
public class ProcessInstanceManager {

    RepositoryService repositoryService;
    HistoryService historyService;
    RuntimeService runtimeService;
    TaskService taskService;

    @Autowired
    public ProcessInstanceManager(RuntimeService runtimeService,
                                  TaskService taskService,
                                  RepositoryService repositoryService,
                                  HistoryService historyService) {
        this.runtimeService = runtimeService;
        this.taskService = taskService;
        this.repositoryService = repositoryService;
        this.historyService = historyService;
    }

    /**
     * Desc：创建流程实例，开始一个流程
     *
     * @param processDefinitionName 流程定义name
     * @param businessKey           businessKey代表一份业务数据的标识(businessKey)
     */
    @Transactional(rollbackFor = Exception.class)
    public void createProcessInstance(String processDefinitionName, String businessKey) {
        VerificationUtils.checkProcessDefinitionByName(processDefinitionName);
        runtimeService.startProcessInstanceByKey(processDefinitionName, businessKey);
    }

    /**
     * Desc：删除一个现有的流程实例，并提交删除原因（用于废弃操作）
     *
     * @param processInstanceId 流程实例id
     * @param reason            删除原因
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteProcessInstance(String processInstanceId, String reason) {
        VerificationUtils.checkProcessInstanceById(processInstanceId);
        runtimeService.deleteProcessInstance(processInstanceId, reason);
    }

    /**
     * Desc:无论任何情况，清除所有的流程实例，实质上还是一个一个删除
     *      当本身没有流程实例的时候使用此方法也不会报错
     */
    @Transactional(rollbackFor = Exception.class)
    public void clearAllProcessInstances() {
        log.info("\t=> Clear All ProcessInstances Anyway");
        runtimeService.createProcessInstanceQuery()
                .list()
                .forEach(processInstance -> runtimeService.deleteProcessInstance(processInstance.getId(),"清空所有的流程实例"));
    }

    /**
     * Desc：清除所有给定的流程定义name有关的流程实例
     * @param processDefinitionName 流程定义name
     */
    @Transactional(rollbackFor = Exception.class)
    public void clearAllProcessInstancesByProcessDefinitionName(String processDefinitionName) {
        log.info("\t=> Clear All ProcessInstances By ProcessDefinitionName");
        runtimeService.createProcessInstanceQuery()
                .processDefinitionName(processDefinitionName)
                .list()
                .forEach(processInstance -> runtimeService.deleteProcessInstance(processInstance.getId(),"清空所有"+processDefinitionName+"的流程实例"));
    }

    /**
     * Desc：完成当前的流程实例，使其走向下一步
     * 只有拥有审批人，且审批人是拾取人时才可以操作
     *
     * @param processInstanceId 流程实例id
     */
    @Transactional(rollbackFor = Exception.class)
    public void completeProcessInstance(String processInstanceId, String username) {
        VerificationUtils.checkProcessInstanceById(processInstanceId);
        Task task = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .taskAssignee(username)
                .singleResult();
        if (Objects.isNull(task)) {
            throw new ActivitiObjectNotFoundException("task不存在或task没有审批人");
        }
        taskService.complete(task.getId());
    }

    /**
     * Desc:是username这个用户成为对于该流程实例的审批人
     *
     * @param processInstanceId 流程实例id
     * @param username          审批人
     */
    @Transactional(rollbackFor = Exception.class)
    public void claimProcessInstance(String processInstanceId, String username) {
        VerificationUtils.checkProcessInstanceById(processInstanceId);
        Task task = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        taskService.claim(task.getId(), username);
    }

    /**
     * Desc:使username这个用户成为对于该流程实例的审批人（强制获取、用于强制更改）
     *
     * @param processInstanceId 流程实例id
     * @param username          审批人
     */
    @Transactional(rollbackFor = Exception.class)
    public void mandatoryClaimProcessInstance(String processInstanceId, String username) {
        VerificationUtils.checkProcessInstanceById(processInstanceId);
        Task task = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        taskService.setAssignee(task.getId(), username);
    }

    /**
     * Desc:通过流程定义name获取有关于其所有的流程实例
     * (想要同时出现不同版本但是name相同的流程定义是不可能的，如果想要修改流程定义，必须使其现有的所有流程实例都结束或废弃)
     *
     * @param processDefinitionName 流程定义name
     * @return 返回相关的所有流程实例，有可能是一个空的list
     */
    public Collection<ProcessInstance> getProcInstancesByProDefName(String processDefinitionName) {
        VerificationUtils.checkProcessDefinitionByName(processDefinitionName);
        return runtimeService.createProcessInstanceQuery()
                .processDefinitionName(processDefinitionName)
                .list();
    }

    /**
     * Desc:通过流程定义name和status获取所有有关的流程实例
     *
     * @param processDefinitionName 流程定义name
     * @param status                获取激活/挂起的流程实例
     * @return 返回相关的所有流程实例，有可能是一个空的list
     */
    public Collection<ProcessInstance> getProcInstancesByProDefNameAndStatus(String processDefinitionName, boolean status) {
        VerificationUtils.checkProcessDefinitionByName(processDefinitionName);
        if (status) {
            return runtimeService.createProcessInstanceQuery()
                    .processDefinitionName(processDefinitionName)
                    .active()
                    .list();
        }
        return runtimeService.createProcessInstanceQuery()
                .processDefinitionName(processDefinitionName)
                .suspended()
                .list();
    }

    /**
     * Desc:根据流程定义name和业务标识进行寻找流程实例
     * (在存储的时候使用uuid当做businessKey，最大可能性的避免重复)
     *
     * @param processDefinitionName 流程定义name
     * @param businessKey           业务标识
     * @return processInstance
     */
    public ProcessInstance getProcInstanceByProDefNameAndBusinessKey(String processDefinitionName, String businessKey) {
        VerificationUtils.checkProcessDefinitionByName(processDefinitionName);
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processDefinitionName(processDefinitionName)
                .processInstanceBusinessKey(businessKey)
                .singleResult();
        if (Objects.isNull(processInstance)) {
            throw new ActivitiObjectNotFoundException("没有所要找的流程实例，请检查业务标识是否正确");
        }
        return processInstance;
    }

    /**
     * Desc: 通过流程实例ID获取流程中正在执行的节点(只需要在历史表中寻找是否有过该流程实例即可)
     * (因为画图时可能已经走完所有的流程，此时已无还需执行的流程实例，只有历史表中才会有)
     *
     * @param processInstanceId 流程实例ID
     * @return 正在执行的节点
     */
    public List<Execution> getRunningActivityInstance(String processInstanceId) {
        VerificationUtils.checkHistoricProcessInstanceById(processInstanceId);
        return runtimeService.createExecutionQuery()
                .processInstanceId(processInstanceId)
                .list();
    }


}
