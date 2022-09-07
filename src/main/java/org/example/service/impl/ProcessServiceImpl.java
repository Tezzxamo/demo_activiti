package org.example.service.impl;

import org.example.model.vo.ProcessDefinitionVO;
import org.example.service.manager.ProcessManager;
import org.example.service.ProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

@Service
public class ProcessServiceImpl implements ProcessService {



    ProcessManager processManager;

    @Autowired
    public ProcessServiceImpl(ProcessManager processManager) {
        this.processManager = processManager;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void suspendProcessDefinitionByName(String processDefinitionName) {
        processManager.suspendProcessDefinitionByName(processDefinitionName);
    }

    @Override
    public void cascadeSuspendProcessDefinitionByName(String processDefinitionName, boolean cascade, Date suspensionDate) {
        processManager.cascadeSuspendProcessDefinitionByName(processDefinitionName, cascade, suspensionDate);
    }

    @Override
    public void activateProcessDefinitionByName(String processDefinitionName) {
        processManager.activateProcessDefinitionByName(processDefinitionName);
    }

    @Override
    public void cascadeActivateProcessDefinitionByName(String processDefinitionName, boolean cascade, Date activationDate) {
        processManager.cascadeActivateProcessDefinitionByName(processDefinitionName, cascade, activationDate);
    }

    @Override
    public Boolean getProcessDefinitionStatusByName(String processDefinitionName) {
        return processManager.getProcessDefinitionStatusByName(processDefinitionName);
    }

    @Override
    public Map<String, Boolean> getProcessConfigStatusMap() {
        return processManager.getProcessConfigStatusMap();
    }

    @Override
    public ProcessDefinitionVO getProcessDefinitionByName(String processDefinitionName) {
        return processManager.getProcessDefinitionByName(processDefinitionName);
    }

    @Override
    public Collection<ProcessDefinitionVO> getProcessDefinitions() {
        return processManager.getProcessDefinitions();
    }

    @Override
    public void deleteProcessDefinitionByName(String processDefinitionName) {
        processManager.deleteProcessDefinitionByName(processDefinitionName);
    }

    @Override
    public void cascadeDeleteProcessDefinitionByName(String processDefinitionName) {
        processManager.cascadeDeleteProcessDefinitionByName(processDefinitionName);
    }

    @Override
    public ProcessDefinitionVO deployProcessDefinition(String processDefinitionName, String bpmnPath) {
        return processManager.deployProcessDefinition(processDefinitionName, bpmnPath);
    }


}
