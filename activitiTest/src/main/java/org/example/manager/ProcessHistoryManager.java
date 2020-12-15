package org.example.manager;

import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class ProcessHistoryManager {

    private static final Logger logger = LoggerFactory.getLogger(ProcessHistoryManager.class);

    HistoryService historyService;

    @Autowired
    public ProcessHistoryManager(HistoryService historyService) {
        this.historyService = historyService;
    }


    /**
     * Desc: 通过流程实例ID获取历史流程实例
     *
     * @param processInstanceId 流程实例Id
     * @return 历史流程实例
     */
    public HistoricProcessInstance getHistoricProcessInstance(String processInstanceId) {
        return checkHistoricProcessInstanceByName(processInstanceId);
    }

    /**
     * Desc: 通过流程实例ID获取流程中已经执行的结点，按照执行先后顺序排序
     *
     * @param processInstanceId 流程实例Id
     * @return 已经执行的节点
     */
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
    public List<HistoricProcessInstance> getHistoricFinishedProcessInstance(String processInstanceId) {
        return historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .finished()
                .list();
    }

    /**
     * Desc:检查历史流程实例是否存在
     *      一对一寻找
     *
     * @param processInstanceId 流程实例ID
     * @return historicProcessInstance
     */
    public HistoricProcessInstance checkHistoricProcessInstanceByName(String processInstanceId) {
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        if (Objects.isNull(historicProcessInstance)) {
            throw new ActivitiObjectNotFoundException("历史流程实例未找到");// 待修改-整合
        }
        return historicProcessInstance;
    }
}
