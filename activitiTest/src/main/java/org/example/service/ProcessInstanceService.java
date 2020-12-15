package org.example.service;

import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;

import java.util.Collection;
import java.util.List;

public interface ProcessInstanceService {

    void createProcessInstance(String processDefinitionName, String businessKey);

    void deleteProcessInstance(String processInstanceId, String reason);

    void completeProcessInstance(String processInstanceId);

    void claimProcessInstance(String processInstanceId,String userName);

    void mandatoryClaimProcessInstance(String processInstanceId,String userName);

    Collection<ProcessInstance> getProcInstancesByProDefName(String processDefinitionName);

    Collection<ProcessInstance> getProcInstancesByProDefNameAndStatus(String processDefinitionName, boolean status);

    ProcessInstance getProcInstanceByProDefNameAndBusinessKey(String processDefinitionName, String businessKey);

    List<Execution> getRunningActivityInstance(String processInstanceId);

}
