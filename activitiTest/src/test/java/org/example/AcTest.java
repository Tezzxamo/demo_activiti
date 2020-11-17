package org.example;


import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AcTest {

    @Autowired
    RepositoryService repositoryService ;
    @Autowired
    RuntimeService runtimeService;
    @Autowired
    TaskService taskService;


    @Test
    public void createProcess() {
//        String processResourceFile = "processes/EmailTest.bpmn20.xml";
//        Deployment deployment = repositoryService.createDeployment()
//                .addClasspathResource(processResourceFile)
//                .name("emailTest")
//                .key("keyEmail")
//                .deploy();

        Map<String, Object> property = new HashMap<>();
        property.put("user", "salaboy,zzx");
        String processDefinitionKey = "Email";
        ProcessInstance pi = runtimeService.startProcessInstanceByKey(processDefinitionKey, "1001", property);

        Task task = taskService.createTaskQuery()
                .processInstanceId(pi.getId())
                .singleResult();

        taskService.complete(task.getId());

    }
}
