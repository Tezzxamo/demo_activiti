package org.example.service;

import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;

import java.util.Collection;
import java.util.List;

public interface ProcessInstanceService {

    /**
     * Desc：创建流程实例，开始一个流程
     *
     * @param processDefinitionName 流程定义name
     * @param businessKey           businessKey代表一份业务数据的标识(businessKey)
     */
    void createProcessInstance(String processDefinitionName, String businessKey);

    /**
     * Desc：删除一个现有的流程实例，并提交删除原因（用于废弃操作）
     *
     * @param processInstanceId 流程实例id
     * @param reason            删除原因
     */
    void deleteProcessInstance(String processInstanceId, String reason);

    /**
     * Desc:无论任何情况，清除所有的流程实例，实质上还是一个一个删除
     * 当本身没有流程实例的时候使用此方法也不会报错
     */
    void clearAllProcessInstances();

    /**
     * Desc：清除所有给定的流程定义name有关的流程实例
     *
     * @param processDefinitionName 流程定义name
     */
    void clearAllProcessInstancesByProcessDefinitionName(String processDefinitionName);

    /**
     * Desc：完成当前的流程实例，使其走向下一步
     *
     * @param processInstanceId 流程实例id
     * @param username          用户
     */
    void completeProcessInstance(String processInstanceId, String username);

    /**
     * Desc:是username这个用户成为对于该流程实例的审批人
     *
     * @param processInstanceId 流程实例id
     * @param username          审批人
     */
    void claimProcessInstance(String processInstanceId, String username);

    /**
     * Desc:使username这个用户成为对于该流程实例的审批人（强制获取、用于强制更改）
     *
     * @param processInstanceId 流程实例id
     * @param username          审批人
     */
    void mandatoryClaimProcessInstance(String processInstanceId, String username);

    /**
     * Desc:通过流程定义name获取有关于其所有的流程实例
     * (想要同时出现不同版本但是name相同的流程定义是不可能的，如果想要修改流程定义，必须使其现有的所有流程实例都结束或废弃)
     *
     * @param processDefinitionName 流程定义name
     * @return 返回相关的所有流程实例，有可能是一个空的list
     */
    Collection<ProcessInstance> getProcInstancesByProDefName(String processDefinitionName);

    /**
     * Desc:通过流程定义name和status获取所有有关的流程实例
     *
     * @param processDefinitionName 流程定义name
     * @param status                获取激活/挂起的流程实例
     * @return 返回相关的所有流程实例，有可能是一个空的list
     */
    Collection<ProcessInstance> getProcInstancesByProDefNameAndStatus(String processDefinitionName, boolean status);

    /**
     * Desc:根据流程定义name和业务标识进行寻找流程实例
     * (在存储的时候使用uuid当做businessKey，最大可能性的避免重复)
     *
     * @param processDefinitionName 流程定义name
     * @param businessKey           业务标识
     * @return processInstance
     */
    ProcessInstance getProcInstanceByProDefNameAndBusinessKey(String processDefinitionName, String businessKey);

    /**
     * Desc: 通过流程实例ID获取流程中正在执行的节点
     *
     * @param processInstanceId 流程实例ID
     * @return 正在执行的节点
     */
    List<Execution> getRunningActivityInstance(String processInstanceId);

}
