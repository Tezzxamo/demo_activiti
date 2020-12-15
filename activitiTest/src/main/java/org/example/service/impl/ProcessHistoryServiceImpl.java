package org.example.service.impl;

import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.example.service.ProcessHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProcessHistoryServiceImpl implements ProcessHistoryService {

    @Autowired
    HistoryService historyService;


    /**
     * Desc: 通过流程实例ID获取历史流程实例
     *
     * @param processInstanceId 流程实例Id
     * @return 历史流程实例
     */
    @Override
    public HistoricProcessInstance getHistoricProcessInstance(String processInstanceId) {
        return historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
    }

    /**
     * Desc: 通过流程实例ID获取流程中已经执行的结点，按照执行先后顺序排序
     *
     * @param processInstanceId 流程实例Id
     * @return 已经执行的节点
     */
    @Override
    public List<HistoricActivityInstance> getHistoricActivityInstancesAsc(String processInstanceId) {
        return historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId)
                .orderByHistoricActivityInstanceId()
                .asc()
                .list();
    }

    /**
     * Desc: 通过流程实例ID获取已经完成的历史流程实例
     *
     * @param processInstanceId 流程实例ID
     * @return 已经完成的历史流程实例
     */
    @Override
    public List<HistoricProcessInstance> getHistoricFinishedProcessInstance(String processInstanceId) {
        return historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .finished()
                .list();
    }
}
