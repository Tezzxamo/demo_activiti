package org.example.service;

import org.example.dto.ProcessDefinitionDTO;

import java.util.Collection;

public interface ProcessDefinitionService {

    ProcessDefinitionDTO getProcessDefinitionByName(String processDefinitionName);

    Collection<ProcessDefinitionDTO> getProcessDefinitions();

    void deleteProcessDefinitionByName(String processDefinitionName);

    void cascadeDeleteProcessDefinitionByName(String processDefinitionName);
}
