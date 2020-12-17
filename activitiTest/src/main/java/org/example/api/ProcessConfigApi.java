package org.example.api;

import org.example.service.ProcessConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProcessConfigApi {

    ProcessConfigService processConfigService;

    @Autowired
    public ProcessConfigApi(ProcessConfigService processConfigService) {
        this.processConfigService = processConfigService;
    }

    public boolean getProcessDefinitionStatusByName(String processDefinitionName) {
        return processConfigService.getProcessDefinitionStatusByName(processDefinitionName);
    }

    public boolean changeProcessConfig(boolean status, String processDefinitionName) {
        if (status) {
            return processConfigService.suspendProcessDefinitionByName(processDefinitionName);
        }
        return processConfigService.activateProcessDefinitionByName(processDefinitionName);
    }
}
