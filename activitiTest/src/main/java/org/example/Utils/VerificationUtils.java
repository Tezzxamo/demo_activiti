package org.example.Utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.example.Utils.function.FunctionUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 校验工具类
 *
 * @author Tethamo_zzx
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
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
        // 判断错误并抛出
        FunctionUtils.isWrong(CollectionUtils.isEmpty(processDefinition)).throwCustomException("流程定义" + processDefinitionName + "未找到");
        FunctionUtils.isWrong(processDefinition.size() > 1).throwCustomException("根据给定的流程名称" + processDefinitionName + ", 查找到多个流程定义");
        return processDefinition.get(0);
    }

    /**
     * Desc:检查历史流程实例是否存在，存在即返回该历史流程实例
     * 一对一寻找
     *
     * @param processInstanceId 流程实例ID
     * @return historicProcessInstance
     */
    public static HistoricProcessInstance checkHistoricProcessInstanceById(String processInstanceId) {
        List<HistoricProcessInstance> historicProcessInstance = ActivitiUtil.instance().getHistoryService().createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .list();
        // 判断错误并抛出
        FunctionUtils.isWrong(CollectionUtils.isEmpty(historicProcessInstance)).throwCustomException("根据流程实例Id" + processInstanceId + "未找到历史流程实例");
        FunctionUtils.isWrong(historicProcessInstance.size() > 1).throwCustomException("根据给定的流程实例Id" + processInstanceId + ", 查找到多个历史流程实例");
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
        // 判断错误并抛出
        FunctionUtils.isWrong(CollectionUtils.isEmpty(processInstance)).throwCustomException("根据流程实例Id" + processInstanceId + "未找到流程实例");
        FunctionUtils.isWrong(processInstance.size() > 1).throwCustomException("根据给定的流程实例Id" + processInstanceId + ", 查找到多个流程实例");
        return processInstance.get(0);
    }

}
