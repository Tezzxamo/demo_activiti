package org.example.service;

import java.util.Date;
import java.util.Map;

public interface ProcessConfigService {

    boolean suspendProcessDefinitionByName(String processDefinitionName);

    boolean cascadeSuspendProcessDefinitionByName(String processDefinitionName, boolean cascade, Date suspensionDate);

    boolean activateProcessDefinitionByName(String processDefinitionName);

    boolean cascadeActivateProcessDefinitionByName(String processDefinitionName, boolean cascade, Date activationDate);

    boolean getProcessDefinitionStatusByName(String processDefinitionName);

    Map<String,Boolean> getProcessConfigStatusMap();

}
