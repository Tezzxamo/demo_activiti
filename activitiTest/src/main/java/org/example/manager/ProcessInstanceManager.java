package org.example.manager;

import org.activiti.engine.*;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
public class ProcessInstanceManager {
    private static final Logger logger = LoggerFactory.getLogger(ProcessInstanceManager.class);

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
    public void createProcessInstance(String processDefinitionName, String businessKey) {
        checkProcessDefinitionByName(processDefinitionName);
        runtimeService.startProcessInstanceByKey(processDefinitionName, businessKey);
    }

    /**
     * Desc：删除一个现有的流程实例，并提交删除原因（用于废弃操作）
     *
     * @param processInstanceId 流程实例id
     * @param reason            删除原因
     */
    public void deleteProcessInstance(String processInstanceId, String reason) {
        checkProcessInstanceById(processInstanceId);
        runtimeService.deleteProcessInstance(processInstanceId, reason);
    }


    /**
     * Desc：完成当前的流程实例，使其走向下一步
     *
     * @param processInstanceId 流程实例id
     */
    public void completeProcessInstance(String processInstanceId) {
        checkProcessInstanceById(processInstanceId);
        taskService.complete(processInstanceId);
    }

    /**
     * Desc:是userName这个用户成为对于该流程实例的审批人
     *
     * @param processInstanceId 流程实例id
     * @param userName          审批人
     */
    public void claimProcessInstance(String processInstanceId, String userName) {
        checkProcessInstanceById(processInstanceId);
        Task task = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        taskService.claim(task.getId(), userName);
    }

    /**
     * Desc:使userName这个用户成为对于该流程实例的审批人（强制获取、用于强制更改）
     *
     * @param processInstanceId 流程实例id
     * @param userName          审批人
     */
    public void mandatoryClaimProcessInstance(String processInstanceId, String userName) {
        checkProcessInstanceById(processInstanceId);
        Task task = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        taskService.setAssignee(task.getId(), userName);
    }

    /**
     * Desc:通过流程定义name获取有关于其所有的流程实例
     * (想要同时出现不同版本但是name相同的流程定义是不可能的，如果想要修改流程定义，必须使其现有的所有流程实例都结束或废弃)
     *
     * @param processDefinitionName 流程定义name
     * @return 返回相关的所有流程实例，有可能是一个空的list
     */
    public Collection<ProcessInstance> getProcInstancesByProDefName(String processDefinitionName) {
        checkProcessDefinitionByName(processDefinitionName);
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
        checkProcessDefinitionByName(processDefinitionName);
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
        checkProcessDefinitionByName(processDefinitionName);
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
        checkHistoricProcessInstanceById(processInstanceId);
        return runtimeService.createExecutionQuery()
                .processInstanceId(processInstanceId)
                .list();
    }

    /**
     * 检查该流程实例是否存在
     *
     * @param processInstanceId 流程实例name
     * @return 流程实例是否存在(存在 - > processInstance ； 不存在 - > 抛出异常)
     */
    public ProcessInstance checkProcessInstanceById(String processInstanceId) {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        if (Objects.isNull(processInstance)) {
            throw new ActivitiObjectNotFoundException("流程实例未找到");
        }
        return processInstance;
    }

    /**
     * 检查该历史流程实例是否存在
     *
     * @param processInstanceId 流程实例name
     * @return 流程实例是否存在(存在 - > historicProcessInstance ； 不存在 - > 抛出异常)
     */
    public HistoricProcessInstance checkHistoricProcessInstanceById(String processInstanceId) {
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        if (Objects.isNull(historicProcessInstance)) {
            throw new ActivitiObjectNotFoundException("历史流程实例未找到");
        }
        return historicProcessInstance;
    }

    /**
     * 检查该流程定义是否存在
     *
     * @param processDefinitionName 流程定义name
     * @return 流程定义是否存在(存在 - > processDefinition ； 不存在 - > 抛出异常)
     */
    public ProcessDefinition checkProcessDefinitionByName(String processDefinitionName) {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionName(processDefinitionName)
                .latestVersion()
                .singleResult();
        if (Objects.isNull(processDefinition)) {
            throw new ActivitiObjectNotFoundException("流程定义未找到");// 待修改-整合
        }
        return processDefinition;
    }
}
