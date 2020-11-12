package org.example;


import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.model.Task;
import org.activiti.api.task.model.builders.TaskPayloadBuilder;
import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.api.task.runtime.events.TaskAssignedEvent;
import org.activiti.api.task.runtime.events.TaskCompletedEvent;
import org.activiti.api.task.runtime.events.listener.TaskRuntimeEventListener;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * Hello world!
 */

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class, ManagementWebSecurityAutoConfiguration.class})
//@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

    private Logger logger = LoggerFactory.getLogger(DemoApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Autowired
    RepositoryService repositoryService;

    @Autowired
    private TaskRuntime taskRuntime;

    @Autowired
    private SecurityUtil securityUtil;

    /**
     * 读取application.yml中的配置，然后自动建表（或更新表）
     *
     * @param args
     */

    @Override
    public void run(String... args) {

//        // 使用SecurityUtil模拟登录用户
//        securityUtil.logInAs("salaboy");
//
//        // 让我们创建一个组任务(没有分配，该组的所有成员都可以claim它)
//        // 这里‘Salaboy’是创建的任务的所有者
//        logger.info("> Creating a Group Task for 'activitiTeam'");
//        taskRuntime.create(TaskPayloadBuilder.create()
//                .withName("First Team Task")
//                .withDescription("This is something really important")
//                .withCandidateGroup("activitiTeam")
//                .withPriority(10)
//                .build());
//
//        // 让我们以“other”用户的身份登录，该用户不属于“ActivtiTeam”组
//        securityUtil.logInAs("other");
//
//        // Let's get all my tasks (as 'other' user)
//        logger.info("> Getting all the tasks");
//        Page<Task> tasks = taskRuntime.tasks(Pageable.of(0, 10));
//
//        // No tasks are returned
//        logger.info(">  Other cannot see the task: " + tasks.getTotalItems());
//
//        // Now let's switch to a user that belongs to the activitiTeam
//        securityUtil.logInAs("erdemedeiros");
//
//        // Let's get 'erdemedeiros' tasks
//        logger.info("> Getting all the tasks");
//        tasks = taskRuntime.tasks(Pageable.of(0, 10));
//
//        // 'erdemedeiros' can see and claim the task
//        logger.info(">  erdemedeiros can see the task: " + tasks.getTotalItems());
//
//
//        String availableTaskId = tasks.getContent().get(0).getId();
//
//        // Let's claim the task, after the claim, nobody else can see the task and 'erdemedeiros' becomes the assignee
//        logger.info("> Claiming the task");
//        taskRuntime.claim(TaskPayloadBuilder.claim().withTaskId(availableTaskId).build());
//
//
//        // Let's complete the task
//        logger.info("> Completing the task");
//        taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(availableTaskId).build());



        List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery().active().list();
        logger.info("> 处于激活状态的流程数量: " + processDefinitionList.size());
        for (ProcessDefinition pd : processDefinitionList) {
            logger.info("\t ===> Process definition: " + pd.getKey());
        }
    }

    @Bean
    public TaskRuntimeEventListener<TaskAssignedEvent> taskAssignedListener() {
        return taskAssigned -> logger.info(">>> Task Assigned: '"
                + taskAssigned.getEntity().getName() +
                "' We can send a notification to the assginee: " + taskAssigned.getEntity().getAssignee());
    }

    @Bean
    public TaskRuntimeEventListener<TaskCompletedEvent> taskCompletedListener() {
        return taskCompleted -> logger.info(">>> Task Completed: '"
                + taskCompleted.getEntity().getName() +
                "' We can send a notification to the owner: " + taskCompleted.getEntity().getOwner());
    }

}