package org.example.service;

import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;

import java.util.List;

public interface ProcessHistoryService {

    HistoricProcessInstance getHistoricProcessInstance(String processInstanceId);

    List<HistoricActivityInstance> getHistoricActivityInstancesAsc(String processInstanceId);

    List<HistoricProcessInstance> getHistoricFinishedProcessInstance(String processInstanceId);

}
