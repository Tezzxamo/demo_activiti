package org.example;

import org.activiti.api.task.runtime.TaskRuntime;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.example.Utils.SecurityUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AbandonmentTest {
    @Autowired
    RepositoryService repositoryService;

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    TaskService taskService;

    @Autowired
    TaskRuntime taskRuntime;

    @Autowired
    SecurityUtil securityUtil;

    @Autowired
    HistoryService historyService;


    @Test
    public void one(){
        //部署
        Deployment deployment = repositoryService.createDeployment()//添加一个部署对象
                .name("废弃test")//添加部署的名字
                .addClasspathResource("processes/Abandonment.bpmn20.xml")
                .deploy();//完成部署

        //开启流程实例
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("Abandonment", "500");

        //完成录入修改信息
        Task task = taskService.createTaskQuery()
                .processInstanceId(pi.getId())
                .singleResult();
        task.setOwner("AaAaAaA");
        taskService.complete(task.getId());

        //完成复核修改信息
        Map<String, Object> map = new HashMap();
        map.put("confirm",false);
        Task task1 = taskService.createTaskQuery()
                .taskAssignee("A")
                .singleResult();
        taskService.complete(task1.getId(),map);

        //废弃(此废弃只能在act_hi_procinst中看到，因为它不是task也不是activity，废弃的具体操作在监听器中)
        Map<String, Object> map1 = new HashMap();
        map1.put("abandon",true);
        taskService.complete(taskService.createTaskQuery().taskAssignee("A").singleResult().getId(),map1);

    }

    @Test
    public void two(){
        historyService.createHistoricProcessInstanceQuery()
                .list()
                .forEach(t-> System.out.println(t.getId()));
        System.out.println("###############################################");
        historyService.createHistoricTaskInstanceQuery()
                .list()
                .forEach(t-> System.out.println(t.getTaskDefinitionKey()));
        System.out.println("###############################################");
        historyService.createHistoricActivityInstanceQuery()
                .list()
                .forEach(t-> System.out.println(t.toString()));
    }
}
