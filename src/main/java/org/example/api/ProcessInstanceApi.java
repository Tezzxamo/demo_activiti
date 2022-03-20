package org.example.api;

import java.util.Map;

public class ProcessInstanceApi {

    public void claimAndCompleteAllProcessInstances(String userName){}

    public void clearAllProcessInstances(String reason){
        // reason = "一键废弃";
    }

    public void clearAllProcessInstancesByProcessDefinitionName(String processDefinitionName){}

    public void listProcessInstancesByProcessDefinitionName(String processDefinitionName){}

    public void createProcessInstance(String processDefinitionName, Map<String, Object> processData){


    }

    public void deleteProcessInstance(String processInstanceId,String reason){}

    public void claimAndCompleteProcessInstance(String processInstanceId,String userName){}

}
