package org.example;


import org.activiti.api.model.shared.model.VariableInstance;
import org.activiti.api.process.model.ProcessDefinition;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.model.payloads.GetProcessInstancesPayload;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.model.builders.TaskPayloadBuilder;
import org.activiti.api.task.model.payloads.GetTasksPayload;
import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.apache.commons.io.FileUtils;
import org.example.Utils.SecurityUtil;
import org.example.service.ProcessImageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AcTest {

    @Autowired
    SecurityUtil securityUtil;
    @Autowired
    RepositoryService repositoryService;
    @Autowired
    RuntimeService runtimeService;
    @Autowired
    TaskService taskService;
    @Autowired
    TaskRuntime taskRuntime;
    @Autowired
    ProcessRuntime processRuntime;
    @Autowired
    ManagementService managementService;
    @Autowired
    HistoryService historyService;
    @Autowired
    ProcessImageService imageService;

    /**
     * 部署
     */
    @Test
    public void createProcess() {
        //只有登录了，才能在开启流程实例时将start_user_id赋值进去
        securityUtil.logInAs("zzx");

        //部署“A”
        String processResourceFile = "processes/A.bpmn20.xml";
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource(processResourceFile)
                .name("A")
                .key("A")
                .deploy();


        Map<String, Object> map = new HashMap<String, Object>();
        map.put("leaders", "zzx");
        String something = "sss";
        map.put("something", something);
        map.put("cas", "cas");
        processRuntime.start(ProcessPayloadBuilder.start()
                .withProcessDefinitionKey("A")
                .withVariables(map)
                .build());
//        Map<String, Object> property = new HashMap<>();
//        property.put("leaders", "salaboy,zzx");
//        String processDefinitionKey = "A";
//
//        for (int i = 1; i < 11; i++) {
////            ProcessInstance pi = runtimeService.startProcessInstanceByKey(processDefinitionKey, String.valueOf(i), property);
//            runtimeService.startProcessInstanceByKey(processDefinitionKey, String.valueOf(i), property);
//        }

//        Task task = taskService.createTaskQuery()
//                .processInstanceId(pi.getId())
//                .singleResult();
//        taskService.claim(task.getId(),"zzx");
//        taskService.complete(task.getId());
    }

    /**
     * 创建流程实例
     */
    @Test
    public void startProcessInstance() {
        securityUtil.logInAs("zzx");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("leaders", "zzx");
        String something = "sss";
        map.put("something", something);
        map.put("pp", "pp");
        processRuntime.start(ProcessPayloadBuilder.start()
                .withProcessDefinitionKey("审批key")
                .withVariables(map)
                .build());

        List<Task> zzx = taskService.createTaskQuery()
                .taskCandidateUser("zzx")
                .includeTaskLocalVariables()
                .includeProcessVariables()
                .active()
                .list();
        System.out.println(zzx.get(0).getTaskLocalVariables());
        System.out.println(zzx.get(0).getProcessVariables());

    }

    @Test
    public void t1() {
        securityUtil.logInAs("zzx");


        GetProcessInstancesPayload getProcessInstancesPayload = new GetProcessInstancesPayload();
        getProcessInstancesPayload.setBusinessKey("1003");
        Page<org.activiti.api.process.model.ProcessInstance> processInstances = processRuntime.processInstances(
                Pageable.of(0, 10),
                getProcessInstancesPayload);
        for (org.activiti.api.process.model.ProcessInstance processInstance : processInstances.getContent()) {
            System.out.println(processInstance);
        }


        GetTasksPayload getTasksPayload = new GetTasksPayload();
        getTasksPayload.setProcessInstanceId(processInstances.getContent().get(0).getId());
        Page<org.activiti.api.task.model.Task> tasks = taskRuntime.tasks(Pageable.of(0, 30));
        for (org.activiti.api.task.model.Task task : tasks.getContent()) {
            System.out.println(task);
        }
    }

    @Test
    public void t2() {
        securityUtil.logInAs("zzx");
        List<Task> tasks = taskService.createTaskQuery()
                .taskAssignee("zzx")
                .includeProcessVariables()
                .active()
                .list();
        tasks.forEach(task -> {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("A", "A");
//            taskRuntime.setVariables(TaskPayloadBuilder.setVariables()
//                    .withVariables(map)
//                    .withTaskId(task.getId())
//                    .build());

            taskRuntime.complete(TaskPayloadBuilder.complete()
                    .withVariables(map)
                    .withTaskId(task.getId()).build());
            System.out.println("PV:" + task.getProcessVariables());
            System.out.println("TL:" + task.getTaskLocalVariables());
        });

        List<Task> tasks1 = taskService.createTaskQuery()
                .taskAssignee("zzx")
                .includeProcessVariables()
                .active()
                .list();
        tasks1.forEach(task -> {
            System.out.println("PV:" + task.getProcessVariables());
            System.out.println("TL:" + task.getTaskLocalVariables());
        });


        List<VariableInstance> variables = taskRuntime.variables(
                TaskPayloadBuilder.variables()
                        .withTaskId(tasks.get(0).getId())
                        .build()
        );
        for (VariableInstance variableInstance : variables) {
            System.out.println(variableInstance.getName());
            System.out.println((String) variableInstance.getValue());
        }
    }

    /**
     * 查看流程定义
     */
    @Test
    public void contextLoad() {
        securityUtil.logInAs("zzx");
        Page<ProcessDefinition> processDefinitionPage = processRuntime.processDefinitions(Pageable.of(0, 10));
        System.out.println("可用流程定义数量：" + processDefinitionPage.getTotalItems());
        for (ProcessDefinition processDefinition : processDefinitionPage.getContent()) {
            System.out.println("流程定义：" + processDefinition);
        }
    }


    @Test
    public void taskGetAndComplete() {
        securityUtil.logInAs("zzx");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("leaders", "zzx");
        Page<org.activiti.api.task.model.Task> tasks = taskRuntime.tasks(Pageable.of(0, 5));
        if (tasks.getTotalItems() > 0) {
            System.out.println(tasks.getTotalItems());
            for (org.activiti.api.task.model.Task task : tasks.getContent()) {
                if (task.getAssignee() == null) {
                    taskRuntime.claim(TaskPayloadBuilder.claim().withTaskId(task.getId()).build());
                }
                System.out.println("任务：" + task);
                taskRuntime.complete(TaskPayloadBuilder.complete()
                        .withTaskId(task.getId())
                        .withVariables(map).build());
            }
        }
//        taskRuntime.tasks(Pageable.of(0, tasks.getTotalItems()))
//                .getContent()
//                .forEach(System.out::println);


    }

    @Test
    public void mt() throws Exception {
        securityUtil.logInAs("zzx");
        Deployment deployment = repositoryService.createDeployment()//添加一个部署对象
                .name("审批name")//添加部署的名字,任意
                .key("审批key")//添加部署的key,任意
                .addClasspathResource("processes/Abandonment.bpmn20.xml")//加载资源
                .deploy();//完成部署
        Map<String, Object> map = new HashMap<String, Object>();
        processRuntime.start(ProcessPayloadBuilder.start()
                .withProcessDefinitionKey("Abandonment")
                .withVariables(map)
                .build());

        Task task0 = taskService.createTaskQuery()
                .taskCandidateOrAssigned("zzx")
                .includeProcessVariables()
                .active()
                .singleResult();

        String proInsId = task0.getProcessInstanceId();

        //刚开始的绘图
//        InputStream image = imageService.getFlowImgByProcInstId(proInsId);
//        String imageName = "image-0" + Instant.now().getEpochSecond() + ".svg";
//        FileUtils.copyInputStreamToFile(image, new File("processes/" + imageName));

        taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(task0.getId()).build());

        //////////////////////////////////
        Task task1 = taskService.createTaskQuery()
                .taskCandidateOrAssigned("zzx")
                .includeProcessVariables()
                .active()
                .singleResult();

        //完成第一个申请的绘图
//        InputStream image1 = imageService.getFlowImgByProcInstId(proInsId);
//        String imageName1 = "image-1" + Instant.now().getEpochSecond() + ".svg";
//        FileUtils.copyInputStreamToFile(image1, new File("processes/" + imageName1));

        map.put("confirm", false);
        taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(task1.getId()).withVariables(map).build());

        //////////////////////////////////
        Task task2 = taskService.createTaskQuery()
                .taskCandidateOrAssigned("zzx")
                .includeProcessVariables()
                .active()
                .singleResult();

        //第一个网关走完的图
//        InputStream image2 = imageService.getFlowImgByProcInstId(proInsId);
//        String imageName2 = "image-2" + Instant.now().getEpochSecond() + ".svg";
//        FileUtils.copyInputStreamToFile(image2, new File("processes/" + imageName2));

        map.put("abandon",false);
        taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(task2.getId()).withVariables(map).build());

        Task task3 = taskService.createTaskQuery()
                .taskCandidateOrAssigned("zzx")
                .active()
                .singleResult();
        map.put("confirm", true);
        taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(task3.getId()).withVariables(map).build());
        //第二个网关走完的图
        InputStream image3 = imageService.getFlowImgByProcInstId(proInsId);
        String imageName3 = "image-3" + Instant.now().getEpochSecond() + ".svg";
        FileUtils.copyInputStreamToFile(image3, new File("processes/" + imageName3));

    }

    /**
     *
     * @throws Exception e
     */
    @Test
    public void ParallelImageTest() throws Exception {
        securityUtil.logInAs("zzx");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("user","zzx");
        processRuntime.start(ProcessPayloadBuilder.start()
                .withProcessDefinitionKey("Parallel")
                .withVariables(map)
                .build());


        Task task0 = taskService.createTaskQuery()
                .taskCandidateOrAssigned("zzx")
                .includeProcessVariables()
                .active()
                .singleResult();

        String proInsId = task0.getProcessInstanceId();

        //刚开始的绘图
//        InputStream image = imageService.getFlowImgByProcInstId(proInsId);
//        String imageName = "image-0" + Instant.now().getEpochSecond() + ".svg";
//        FileUtils.copyInputStreamToFile(image, new File("processes/" + imageName));

        map.put("leader", "zzx");
        map.put("hr","other");
        taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(task0.getId()).withVariables(map).build());

        //////////////////////////////////
        Task task1 = taskService.createTaskQuery()
                .taskCandidateOrAssigned("zzx")
                .includeProcessVariables()
                .active()
                .singleResult();

        //完成第一个申请的绘图
//        InputStream image1 = imageService.getFlowImgByProcInstId(proInsId);
//        String imageName1 = "image-1" + Instant.now().getEpochSecond() + ".svg";
//        FileUtils.copyInputStreamToFile(image1, new File("processes/" + imageName1));


//        taskRuntime.claim(TaskPayloadBuilder.claim().withTaskId(task1.getId()).build());
        taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(task1.getId()).withVariables(map).build());

        //////////////////////////////////

//        第一个网关走完的图
        InputStream image2 = imageService.getFlowImgByProcInstId(proInsId);
        String imageName2 = "image-2" + Instant.now().getEpochSecond() + ".svg";
        FileUtils.copyInputStreamToFile(image2, new File("processes/" + imageName2));


        securityUtil.logInAs("other");
        Task other = taskService.createTaskQuery().taskAssignee("other").singleResult();
        taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(other.getId()).build());


        InputStream image3 = imageService.getFlowImgByProcInstId(proInsId);
        String imageName3 = "image-33" + Instant.now().getEpochSecond() + ".svg";
        FileUtils.copyInputStreamToFile(image3, new File("processes/" + imageName3));


    }

    @Test
    public void tttt() throws Exception {
        securityUtil.logInAs("erdemedeiros");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("user", "erdemedeiros");
        processRuntime.start(ProcessPayloadBuilder.start()
                .withProcessDefinitionKey("Sampler")
                .withVariables(map)
                .build());

        Task task0 = taskService.createTaskQuery()
                .taskCandidateOrAssigned("erdemedeiros")
                .includeProcessVariables()
                .active()
                .singleResult();
        //刚开始的绘图
        InputStream image = imageService.getFlowImgByProcInstId(task0.getProcessInstanceId());
        String imageName = "image-0" + Instant.now().getEpochSecond() + ".svg";
        FileUtils.copyInputStreamToFile(image, new File("processes/" + imageName));

        //////////////////////////////////
        Task task1 = taskService.createTaskQuery()
                .taskCandidateOrAssigned("erdemedeiros")
                .includeProcessVariables()
                .active()
                .singleResult();

        map.put("leader", "erdemedeiros");
//        taskRuntime.claim(TaskPayloadBuilder.claim().withTaskId(task1.getId()).build());
        taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(task1.getId()).withVariables(map).build());

        //完成第一个申请的绘图
        InputStream image1 = imageService.getFlowImgByProcInstId(task1.getProcessInstanceId());
        String imageName1 = "image-1" + Instant.now().getEpochSecond() + ".svg";
        FileUtils.copyInputStreamToFile(image1, new File("processes/" + imageName1));

        //////////////////////////////////
        Task task2 = taskService.createTaskQuery()
                .taskCandidateOrAssigned("erdemedeiros")
                .includeProcessVariables()
                .active()
                .singleResult();
        map.put("day", 4);
        taskRuntime.claim(TaskPayloadBuilder.claim().withTaskId(task2.getId()).build());
        taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(task2.getId()).withVariables(map).build());

        //网关走完的图
        InputStream image2 = imageService.getFlowImgByProcInstId(task1.getProcessInstanceId());
        String imageName2 = "image-2" + Instant.now().getEpochSecond() + ".svg";
        FileUtils.copyInputStreamToFile(image2, new File("processes/" + imageName2));


    }

    @Test
    public void t1t() {
        securityUtil.logInAs("zzx");
        List<Task> tasks = taskService.createTaskQuery()
                .taskCandidateOrAssigned("zzx")
                .includeProcessVariables()
                .active()
                .list();
        Task task = tasks.get(0);
        List<VariableInstance> variables = taskRuntime.variables(TaskPayloadBuilder.variables().withTaskId(task.getId()).build());
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .variableValueEquals("cas", "cas")
                .singleResult();

        Map<String, Object> map = new HashMap<String, Object>();

        Person person = new Person("张三", 20);
        map.put("leaders", "salaboy");
        map.put("big", "bbb");
        map.put("chang", "chang");
        map.put("cas", "qweqqweqeq");
        map.put("person", person);
        taskRuntime.claim(TaskPayloadBuilder.claim().withTaskId(task.getId()).build());
        taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(task.getId()).withVariables(map).build());

        List<Task> salaboy = taskService.createTaskQuery()
                .taskCandidateUser("salaboy")
                .taskVariableValueEquals(person)
                .includeTaskLocalVariables()
                .includeProcessVariables()
                .active()
                .list();
        System.out.println(salaboy.get(0).getTaskLocalVariables());
        System.out.println(salaboy.get(0).getProcessVariables());
        System.out.println();
    }


    @Test
    public void t11t() {
        Person person = new Person("张三", 20);
        List<Task> salaboy = taskService.createTaskQuery()
                .taskCandidateUser("salaboy")
//                .taskVariableValueEquals("person",person.getName())
//                .taskVariableValueGreaterThanOrEqual("person",person)
                .taskVariableValueLessThanOrEqual("person", person)
                .taskVariableValueLike("chang", "%an%")
//                .taskVariableValueEquals("chang")
                .includeTaskLocalVariables()
//                .includeProcessVariables()

                .active()
                .list();
        for (Task task : salaboy) {
            System.out.println(task.getTaskLocalVariables() + "  " + task.getId());
        }
//        System.out.println(salaboy.get(0).getTaskLocalVariables());
//        System.out.println(salaboy.get(0).getProcessVariables());
    }


    @Test
    public void t2t() {
        securityUtil.logInAs("salaboy");

        List<Task> tasks = taskService.createTaskQuery()
                .taskCandidateUser("salaboy")
                .active()
                .list();
        Task task = tasks.get(0);
        taskRuntime.claim(TaskPayloadBuilder.claim().withTaskId(task.getId()).build());
        taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(task.getId()).build());
    }

    /**
     * 生成流程图
     *
     * @throws IOException 生成流程图异常！
     */
    @Test
    public void generateImage() throws IOException {
        securityUtil.logInAs("zzx");

        List<Task> tasks = taskService.createTaskQuery()
                .taskCandidateUser("zzx")
                .active()
                .list();
        Task task = tasks.get(1);
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(task.getProcessInstanceId())
                .singleResult();

        BpmnModel model = repositoryService.getBpmnModel(task.getProcessDefinitionId());

        ProcessDiagramGenerator generator = new DefaultProcessDiagramGenerator();

        //全部经过的结点
        List<HistoricActivityInstance> historicActivityInstances = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstance.getId())
                .orderByHistoricActivityInstanceId()
//                .asc()
                .desc()
                .list();
        List<String> highLightedActivityIds = historicActivityInstances.stream()
                .map(HistoricActivityInstance::getActivityId)
                .collect(Collectors.toList());

        //lastTask是当前任务执行到的位置
        List<HistoricActivityInstance> lastTasks =
                historyService.createHistoricActivityInstanceQuery()
                        .processInstanceId(processInstance.getId())
                        .orderByHistoricActivityInstanceStartTime()
                        .desc()
                        .list();
        List<String> lastTask = lastTasks.stream()
                .map(HistoricActivityInstance::getActivityId)
                .limit(1)
                .collect(Collectors.toList());
        lastTask.add(lastTasks.get(0).getActivityId());

        //七个参数分别是：
        //  BPMNModel
        //  高光节点
        //  高光顺序流
        //  活动字体名称
        //  标签字体名称
        //  批注字体名称
        //  生成默认关系图
        //  默认关系图映像文件名
        InputStream inputStream = generator.generateDiagram(
                model, lastTask, Collections.emptyList(), "宋体", "宋体", "宋体", true, "png");
        String imageName = "image-" + Instant.now().getEpochSecond() + ".svg";
        FileUtils.copyInputStreamToFile(inputStream, new File("processes/" + imageName));


    }


    @Test
    public void tttttttttt() throws Exception {
        securityUtil.logInAs("zzx");

        List<Task> tasks = taskService.createTaskQuery()
                .taskCandidateUser("zzx")
                .active()
                .list();
        Task task = tasks.get(1);
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(task.getProcessInstanceId())
                .singleResult();
        InputStream image = imageService.getFlowImgByProcInstId(processInstance.getId());
        String imageName = "image-" + Instant.now().getEpochSecond() + ".svg";
        FileUtils.copyInputStreamToFile(image, new File("processes/" + imageName));
    }
    @Test
    public void tetete(){
//        List<ProcessInstance> a = runtimeService.createProcessInstanceQuery()
//                .suspended()
//                .processDefinitionName("Parallel")
//                .list();

        repositoryService.suspendProcessDefinitionByKey("Abandonment");

        org.activiti.engine.repository.ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionName("Abandonment")
                .latestVersion()
                .active()
                .singleResult();
        if (Objects.isNull(processDefinition)){
            System.out.println("流程定义0未激活或无该流程");
        }

        org.activiti.engine.repository.ProcessDefinition processDefinition1 = repositoryService.createProcessDefinitionQuery()
                .processDefinitionName("Abandonment")
                .latestVersion()
                .suspended()
                .singleResult();
        if (Objects.isNull(processDefinition1)){
            System.out.println("流程定义1未激活或无该流程");
        }




    }
}
