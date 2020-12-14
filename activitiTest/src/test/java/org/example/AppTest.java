package org.example;

import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.model.builders.TaskPayloadBuilder;
import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.runtime.api.impl.TaskRuntimeImpl;
import org.apache.commons.io.FileUtils;
import org.example.service.ImageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AppTest {
//    /**
//     * 先建数据库，然后执行下面代码
//     * CREATE DATABASE activiti DEFAULT CHARACTER SET utf8mb4;
//     * 就可以在数据库activiti中获取到25张表
//     */
//    public static void main(String[] args) {
//        //创建ProcessEngineConfiguration对象
//        //使用activiti-cfg.xml中设置的数据库
//        ProcessEngineConfiguration configuration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti-cfg.xml");
//        //创建ProcessEngine对象
//        ProcessEngine processEngine = configuration.buildProcessEngine();
//        System.out.println("processEngine = " + processEngine);
//    }

    private static final String GROUP_LEADER_APPROVAL = "组长审批";
    private static final String APPLY = "申请";
    private Logger logger = LoggerFactory.getLogger(AppTest.class);

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

    @Autowired
    ImageService imageService;

    /**
     * 使用内嵌数据库H2成功建立activiti数据库并创建25张表
     */
    @Test
    public void testEngine() {
        //创建ProcessEngineConfiguration对象
        //createStandaloneInMemProcessEngineConfiguration使用H2数据库
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration();
        //创建ProcessEngine对象
        ProcessEngine processEngine = configuration.buildProcessEngine();
        System.out.println("processEngine = " + processEngine);
        System.out.println("" + processEngine.getName());
    }

    /**
     * 删除部署的两种方式
     * 根据部署id进行删除
     */
    @Test
    public void deleteProcessDefinition() {
        String deploymentId = "6904ebe8-232e-11eb-8163-94c691a0af85";
        // 普通删除，如果当前规则下有正在执行的流程，则抛异常
//        repositoryService.deleteDeployment(deploymentId);
        // 级联删除,会删除和当前规则相关的所有信息，包括历史
        repositoryService.deleteDeployment(deploymentId, true);
    }

    /**
     * 11111
     * ①部署流程定义
     */
    @Test
    public void deploymentProcessDefinition() {
        System.out.println("--测试步骤111①——");
        System.out.println("————————————————————————————————————————————————————");
        Deployment deployment = repositoryService.createDeployment()//添加一个部署对象
                .name("审批name")//添加部署的名字,任意
                .key("审批key")//添加部署的key,任意
                .addClasspathResource("processes/Parallel.bpmn20.xml")//加载资源
                .deploy();//完成部署
        System.out.println("部署ID： " + deployment.getId());
        System.out.println("部署名称： " + deployment.getName());
        System.out.println("————————————————————————————————————————————————————");
    }


    /**
     * 22222
     * ②启动流程实例
     * 对于一个流程，多次启动流程实例，会生成多个流程实例
     * 启动流程实例的三个参数：
     * 第一个参数（String）：流程定义processDefinitionKey
     * 第二个参数（String）：业务标识businessKey     自定义：1001
     * 第三个参数（Map）：启动流程实例，需要置入的user参数
     */
    @Test
    public void startProcessesInstance() {
        System.out.println("--测试步骤222②——");
        System.out.println("————————————————————————————————————————————————————");
        Map<String, Object> property = new HashMap<>();
        property.put("leaders", "salaboy,zzx");
        String processDefinitionKey = "审批key";
        ProcessInstance pi = runtimeService.startProcessInstanceByKey(processDefinitionKey, "1001", property);
        System.out.println("流程实例的ProcessInstanceId: " + pi.getId());
        System.out.println("流程实例的ProcessDefinitionKey: " + pi.getProcessDefinitionKey());
        System.out.println("流程实例的ProcessDefinitionId: " + pi.getProcessDefinitionId());
        System.out.println("流程实例的ProcessDefinitionName: " + pi.getProcessDefinitionName());
        System.out.println("流程实例的ProcessDefinitionVersion: " + pi.getProcessDefinitionVersion());
        System.out.println("流程实例的BusinessKey:" + pi.getBusinessKey());
        System.out.println("————————————————————————————————————————————————————");
    }


    @Test
    public void tyt() {
        Map<String, Object> property = new HashMap<>();
        property.put("leaders", "salaboy,zzx");
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("A", property);
        System.out.println(pi.toString());
        runtimeService.deleteProcessInstance(pi.getId(), "废弃");
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        HistoryService historyService = processEngine.getHistoryService();
        historyService.createHistoricProcessInstanceQuery()
                .deleted()
                .list()
                .forEach(t -> System.out.println(t.getDeleteReason()));
    }


    /**
     * 55555
     * ⑤：完成任务：
     * 1:申请后，对自己的申请进行确认，然后提交，使用一次complete
     * 2:此时task来到小组审批，由小组审批确认后，使用一次complete，提交到网关
     * 3：如果是Sampler——判断提交的day和网关分支的判别条件，走相应流程，一条分支如第二步继续审批，审批结束后结束。另一条分支直接结束。
     * 如果是Parallel——传递给之后的两个用户task，只有当两个用户task全部审批结束后，使用了complete之后，才可以继续向后
     * 4：若后续无其他task，此时走到end，结束。
     */
    @Test
    public void completeMyPersonalTask() {
        System.out.println("--测试步骤555⑤——");
        System.out.println("########################################################");
        String processDefinitionKey = "A";
        String candidateUser = "zzx";
        Map<String, Object> map = new HashMap<>();
//        property.put("hr", "wyy");
//        property.put("day", 2);
        Task task = taskService.createTaskQuery()
                .processDefinitionKey(processDefinitionKey)
                .taskAssignee(candidateUser)
                .singleResult();
        map.put("requireGroup", "activitiTeam,otherTeam,GROUP_otherTeam1");
        //申请，置入下一任务需要的参数
        if (APPLY.equals(task.getName())) {
            map.put("leader", "salaboy");
        }
        //组长审批
        if (GROUP_LEADER_APPROVAL.equals(task.getName())) {
            map.put("day", 2);
//            map.put("day",4);
//            map.put("leader","erdemedeiros");
        }


        taskService.complete(task.getId(), map);
//        taskService.complete("c9827570-232e-11eb-9749-94c691a0af85");
        System.out.println("任务完成");
        System.out.println("########################################################");
    }

    /**
     * 33333
     * ③查看assignee和candidateUser
     */
    @Test
    public void findMyPersonalTask() {
        System.out.println("--测试步骤333③——");
        String processDefinitionKey = "A";
        String candidateUser = "salaboy";
        String assignee = "leader1";

        //查询
        List<Task> list = taskService.createTaskQuery()
                .processDefinitionKey(processDefinitionKey)//使用流程定义Key查询
//                .processDefinitionId(processDefinitionId)//使用流程定义ID查询
//                .processInstanceId(processInstanceId)//使用流程实例ID查询
//                .executionId(executionId)//使用执行对象ID查询
//                .taskCandidateUser(candidateUser)//使用候选人查询
                .taskAssignee(candidateUser)//使用办理人查询
                /*返回结果集*/
//                .singleResult()//返回惟一结果集
//                .count()//返回结果集的数量
//                .listPage(firstResult, maxResults);//分页查询
                .list();

        //输出
        if (list != null && list.size() > 0) {
            for (Task task : list) {
                System.out.println("########################################################");
                System.out.println("任务ID:" + task.getId());
                System.out.println("任务名称:" + task.getName());
                System.out.println("任务的创建时间:" + task.getCreateTime());
                System.out.println("任务的办理人:" + task.getAssignee());
                System.out.println("流程实例ID：" + task.getProcessInstanceId());
                System.out.println("执行对象ID:" + task.getExecutionId());
                System.out.println("流程定义ID:" + task.getProcessDefinitionId());
                System.out.println("########################################################");
            }
        }
    }

    /**
     * 组任务拾取，组成员任意一人拾取即可，拾取后，assignee被设置为这个组成员
     */
    @Test
    public void claimTest() {
        System.out.println("--测试步骤444④——");
        System.out.println("########################################################");
        String processDefinitionKey = "A";
        String candidateUser = "salaboy";
        Task task = taskService.createTaskQuery()
                .processDefinitionKey(processDefinitionKey)
                .taskCandidateUser(candidateUser)
                .singleResult();
        if (task != null) {
            taskService.claim(task.getId(), candidateUser);
            System.out.println("用户：" + candidateUser + "  任务拾取完毕！");
        }
        System.out.println("########################################################");
    }

    /**
     * 强行更改代理人
     */
    @Test
    public void setAss() {
        System.out.println("########################################################");
        String processDefinitionKey = "A";
        String candidateUser = "other1";
        String candidateGroup = "GROUP_otherTeam1";
        Task task = taskService.createTaskQuery()
                .processDefinitionKey(processDefinitionKey)
//                .taskCandidateUser(candidateUser)
                .taskCandidateGroup(candidateGroup)
                .singleResult();
        if (task != null) {
            taskService.setAssignee(processDefinitionKey, candidateUser);
            System.out.println("用户：" + candidateUser + "  任务拾取完毕！");
        }
        System.out.println("########################################################");
    }

    /**
     * 判断流程状态
     * 流程实例若是还存在，就证明流程尚未结束
     */
    @Test
    public void isInstanceEnd() {
        String processInstanceId = "3bca89fa-2266-11eb-ad8c-94c691a0af85";
        ProcessInstance pi = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        if (pi != null) {
            System.out.println("流程未结束");
        } else {
            System.out.println("流程结束");
        }
    }

    /**
     * 开启流程实例后，可以通过taskService.complete(task.getId())来完成流程实例，该方法还有两种多参数的重载方法，可以适应其他场景。
     * 此处实例使用的bpmn流程是:start->userTask->end
     * 所以不需要多余参数即可完成
     * <p>
     * 流程实例结束后，会完整的出现在act_hi_actinst表中，而act_ru_task表中的流程实例则会消失
     */
    @Test
    public void taskServiceTest() {
        //active()的意思是被激活的，正在使用的
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().active().list();
        String id = "";
        for (ProcessDefinition pd : list) {
            if (pd.getKey().equals("PROCESS_1")) {
                id = pd.getId();
            }
            System.out.println(pd.getKey());
        }
        System.out.println("taskId：" + id + "_______________________________");

        List<Task> tasks = /*taskService.createTaskQuery().taskCandidateGroup("").list();*/
                taskService.createTaskQuery().active().list();
        for (Task task : tasks) {
            if (id.equals(task.getProcessDefinitionId())) {
                System.out.println("-——————————————" + task.getId());
            }
            logger.info("Task available: " + task.getName());
        }
        Task task = tasks.get(0);
        System.out.println(task.getId());

//        Map<String, Object> variables =new HashMap<>();
//        variables.put("key1","value1");
//        variables.put("key2","value2");

        //只是userTask的话，complete只需要一个参数，task.getId()
        taskService.complete(task.getId());
    }

    /**
     * getVariables
     */
    @Test
    public void getVariablesTest() {
        Task task = taskService.createTaskQuery().singleResult();
        Map<String, Object> variables = taskService.getVariables(task.getId());
        for (Object o : variables.keySet()) {
            System.out.println(o.toString());
        }
    }

    @Test
    public void completeTest() {

        System.out.println("--测试步骤555⑤——");
        System.out.println("########################################################");
        String processDefinitionKey = "A";
        String candidateUser = "salaboy";
        Map<String, Object> map = new HashMap<>();
        Task task = taskService.createTaskQuery()
                .processDefinitionKey(processDefinitionKey)
//                .taskAssignee(candidateUser)
                .singleResult();
        map.put("requireGroup", "activitiTeam,otherTeam,GROUP_otherTeam1");
        taskService.complete(task.getId(), map);
        System.out.println("任务完成");
        System.out.println("########################################################");
    }

    @Test
    public void suspendProcessDefinition() {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .latestVersion()
//                .suspended()
                .processDefinitionKey("Parallel")
                .singleResult();
//        repositoryService.suspendProcessDefinitionById(processDefinition.getId());


        repositoryService.activateProcessDefinitionById(processDefinition.getId());

        System.out.println(processDefinition.getVersion());

    }

    @Test
    public void susPt() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("user","zzx");
        runtimeService.startProcessInstanceByKey("Parallel",map);
//        Map<String, Object> map = new HashMap<>();
        List<Task> zzx = taskService.createTaskQuery().taskAssignee("zzx")
                .list();
        Task task = zzx.get(0);
        map.put("uuid",123123);
//        taskService.setAssignee(task.getId(),"sala");
        taskService.setVariables(task.getId(),map);
    }

    /**
     * 将原有的assignee变为owner，将第二个参数变成assignee:taskService.delegateTask(task.getId(),"userId");
     */
    @Test
    public void susPpit(){
        List<Task> list = taskService.createTaskQuery()
                .processDefinitionKey("Parallel")
                .active()
                .list();
        // 第二个参数是删除原因
        runtimeService.deleteProcessInstance(list.get(0).getProcessInstanceId(),"ddd");
    }

    /**
     * HistoricProcessInstance的 Id 就是 ProcessInstance的 Id
     * @throws Exception e
     */
    @Test
    public void imageDelShow() throws Exception {
        List<HistoricProcessInstance> parallel = historyService.createHistoricProcessInstanceQuery()
                .processDefinitionKey("Parallel")
                .list();
        HistoricProcessInstance historicProcessInstance = parallel.get(0);
        String proInsId = historicProcessInstance.getId();
        if (proInsId==null){
            return;
        }
        InputStream image = imageService.getFlowImgByProcInstId(proInsId);
        String imageName = "Parallel-Delete" + Instant.now().getEpochSecond() + ".svg";
        FileUtils.copyInputStreamToFile(image, new File("processes/" + imageName));
    }
}
