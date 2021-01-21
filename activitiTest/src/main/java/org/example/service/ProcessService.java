package org.example.service;

import org.example.dto.ProcessDefinitionDTO;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

public interface ProcessService {
    boolean suspendProcessDefinitionByName(String processDefinitionName);

    boolean cascadeSuspendProcessDefinitionByName(String processDefinitionName, boolean cascade, Date suspensionDate);

    boolean activateProcessDefinitionByName(String processDefinitionName);

    boolean cascadeActivateProcessDefinitionByName(String processDefinitionName, boolean cascade, Date activationDate);

    boolean getProcessDefinitionStatusByName(String processDefinitionName);

    Map<String,Boolean> getProcessConfigStatusMap();

    ProcessDefinitionDTO getProcessDefinitionByName(String processDefinitionName);

    Collection<ProcessDefinitionDTO> getProcessDefinitions();

    void deleteProcessDefinitionByName(String processDefinitionName);

    void cascadeDeleteProcessDefinitionByName(String processDefinitionName);
}
