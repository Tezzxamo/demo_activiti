package org.example.service;

import org.activiti.engine.repository.ProcessDefinition;

import java.util.Collection;

public interface ProcessDefinitionService {

    ProcessDefinition getProcessDefinitionByName(String processDefinitionName);

    Collection<ProcessDefinition> getProcessDefinitions();

    void deleteProcessDefinitionByName(String processDefinitionName);

    void cascadeDeleteProcessDefinitionByName(String processDefinitionName);
}
