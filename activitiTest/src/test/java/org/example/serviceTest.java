package org.example;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.example.service.ProcessHistoryService;
import org.example.service.ProcessInstanceService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class serviceTest {

    @Autowired
    ProcessHistoryService processHistoryService;
    @Autowired
    ProcessInstanceService processInstanceService;
    @Autowired
    RuntimeService runtimeService;


    @Test
    public void t1() {// ProcessDefinitionService的测试
//        ProcessDefinition a = (ProcessDefinition) processDefinitionService.getProcessDefinitionByName("Abandonment");
//        System.out.println(a.isSuspended());
        processInstanceService.clearAllProcessInstancesByProcessDefinitionName("Abandonment");
    }

    @Test
    public void t2(){
        List<ProcessInstance> list = runtimeService.createProcessInstanceQuery()
                .processDefinitionName("Abandonment")
                .list();
        System.out.println(list);
    }


}
