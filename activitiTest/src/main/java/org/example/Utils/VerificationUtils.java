package org.example.Utils;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Slf4j
public class VerificationUtils {

    /**
     * 检查该流程定义是否存在
     *
     * @param processDefinitionName 流程定义name
     * @return 流程定义是否存在(存在 - > processDefinition ； 不存在 - > 抛出异常)
     */
    public static ProcessDefinition checkProcessDefinitionByName(String processDefinitionName) {
        // 不能在list后直接get，因为如果没有流程定义的话，会报错
        List<ProcessDefinition> processDefinition = ActivitiUtil.instance().getRepositoryService().createProcessDefinitionQuery()
                .processDefinitionName(processDefinitionName)
                .latestVersion()
                .list();
        if (CollectionUtils.isEmpty(processDefinition)) {
            log.error("流程定义" + processDefinitionName + "未找到");
            throw new ActivitiObjectNotFoundException("流程定义" + processDefinitionName + "未找到");// 待修改-整合
        }
        if (processDefinition.size() > 1) {
            log.error("根据给定的流程名称" + processDefinitionName + ", 查找到多个版本一致的流程定义");
            throw new ArrayIndexOutOfBoundsException("根据给定的流程名称" + processDefinitionName + ", 查找到多个流程定义");
        }
        return processDefinition.get(0);
    }

    /**
     * Desc:检查历史流程实例是否存在
     * 一对一寻找
     *
     * @param processInstanceId 流程实例ID
     * @return historicProcessInstance
     */
    public static HistoricProcessInstance checkHistoricProcessInstanceById(String processInstanceId) {
        List<HistoricProcessInstance> historicProcessInstance = ActivitiUtil.instance().getHistoryService().createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .list();
        if (CollectionUtils.isEmpty(historicProcessInstance)) {
            throw new ActivitiObjectNotFoundException("历史流程实例未找到");
        }
        if (historicProcessInstance.size() > 1) {
            throw new ArrayIndexOutOfBoundsException("同一历史流程实例id找到多个流程实例!");
        }
        return historicProcessInstance.get(0);
    }


    /**
     * 检查该流程实例是否存在
     *
     * @param processInstanceId 流程实例name
     * @return 流程实例是否存在(存在 - > processInstance ； 不存在 - > 抛出异常)
     */
    public static ProcessInstance checkProcessInstanceById(String processInstanceId) {
        List<ProcessInstance> processInstance = ActivitiUtil.instance().getRuntimeService().createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .list();
        if (CollectionUtils.isEmpty(processInstance)) {
            throw new ActivitiObjectNotFoundException("流程实例未找到");
        }
        if (processInstance.size() > 1) {
            throw new ArrayIndexOutOfBoundsException("同一流程实例id找到多个流程实例!");
        }
        return processInstance.get(0);
    }

}
