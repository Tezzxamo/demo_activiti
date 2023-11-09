package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.example.service.ProcessHistoryService;
import org.example.service.manager.ProcessHistoryManager;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProcessHistoryServiceImpl implements ProcessHistoryService {

    private final ProcessHistoryManager processHistoryManager;

    /**
     * Desc: 通过流程实例ID获取历史流程实例
     *
     * @param processInstanceId 流程实例Id
     * @return 历史流程实例
     */
    @Override
    public HistoricProcessInstance getHistoricProcessInstance(String processInstanceId) {
        return processHistoryManager.getHistoricProcessInstance(processInstanceId);
    }

    /**
     * Desc: 通过流程实例ID获取流程中已经执行的结点，按照执行先后顺序排序
     *
     * @param processInstanceId 流程实例Id
     * @return 已经执行的节点
     */
    @Override
    public List<HistoricActivityInstance> getHistoricActivityInstancesAsc(String processInstanceId) {
        return processHistoryManager.getHistoricActivityInstancesAsc(processInstanceId);
    }

    /**
     * Desc: 通过流程实例ID获取已经完成的历史流程实例
     *
     * @param processInstanceId 流程实例ID
     * @return 已经完成的历史流程实例
     */
    @Override
    public List<HistoricProcessInstance> getHistoricFinishedProcessInstance(String processInstanceId) {
        return processHistoryManager.getHistoricFinishedProcessInstance(processInstanceId);
    }
}
