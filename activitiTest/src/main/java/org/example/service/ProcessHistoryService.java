package org.example.service;

import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;

import java.util.List;

public interface ProcessHistoryService {

    /**
     * Desc: 通过流程实例ID获取历史流程实例
     *
     * @param processInstanceId 流程实例Id
     * @return 历史流程实例
     */
    HistoricProcessInstance getHistoricProcessInstance(String processInstanceId);

    /**
     * Desc: 通过流程实例ID获取流程中已经执行的结点，按照执行先后顺序排序
     *
     * @param processInstanceId 流程实例Id
     * @return 已经执行的节点
     */
    List<HistoricActivityInstance> getHistoricActivityInstancesAsc(String processInstanceId);

    /**
     * Desc: 通过流程实例ID获取已经完成的历史流程实例
     *
     * @param processInstanceId 流程实例ID
     * @return 已经完成的历史流程实例
     */
    List<HistoricProcessInstance> getHistoricFinishedProcessInstance(String processInstanceId);

}
