package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.service.ProcessInstanceService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Validated
@RestController
@RequestMapping("/v1/processInstance")
@RequiredArgsConstructor
public class ProcessInstanceController {

    private final ProcessInstanceService processInstanceService;


    public void createProcessInstance(String processDefinitionName, Map<String, Object> processData) {

    }

    public void claimAndCompleteAllProcessInstances(String userName) {
    }

    public void clearAllProcessInstances(String reason) {
        // reason = "一键废弃";
    }

    public void clearAllProcessInstancesByProcessDefinitionName(String processDefinitionName) {
    }

    public void listProcessInstancesByProcessDefinitionName(String processDefinitionName) {
    }


    public void deleteProcessInstance(String processInstanceId, String reason) {
    }

    public void claimAndCompleteProcessInstance(String processInstanceId, String userName) {
    }

}
