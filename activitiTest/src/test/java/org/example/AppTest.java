package org.example;

import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
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
    SecurityUtil securityUtil;

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

    @Test
    public void deleteProcessDefinition() {
//——————————————————————————————————————————————————————————————————————————
//        String deploymentId="";
//        repositoryService.deleteDeployment(deploymentId);
//        repositoryService.createDeploymentQuery().list()
//                .stream()
//                .map(Deployment::getId)
//                .forEach(System.out::println);
//——————————————————————————————————————————————————————————————————————————
        //deploymentId是一个uuid，这里的deploymentId是一个示例，
        //在使用deploymentProcessDefinition()
        //以后会得到一个部署ID，可以通过这个部署ID来测试删除
        String deploymentId = "17963879-22f3-11eb-8a70-94c691a0af85";
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
                .name("审批")//添加部署的名字
//                .addClasspathResource("processes/A.bpmn20.xml")//加载资源
                .addClasspathResource("processes/Sampler.bpmn20.xml")
                .addClasspathResource("processes/Parallel.bpmn20.xml")
//                .addClasspathResource("processes/A.png")//加载资源
                .deploy();//完成部署
        System.out.println("部署ID： " + deployment.getId());
        System.out.println("部署名称： " + deployment.getName());
        System.out.println("————————————————————————————————————————————————————");
//        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
//                .latestVersion()
//                .processDefinitionName("第一个流程")
//                .processDefinitionKey("PROCESS_1")
//                .singleResult();
//        String diagramResourceName = processDefinition.getDiagramResourceName();
//        InputStream imageStream = repositoryService.getResourceAsStream(
//                processDefinition.getDeploymentId(),diagramResourceName);
//        System.out.println("diagramResourceName:"+diagramResourceName);
    }


    /**
     * 22222
     * ②启动流程实例
     * 对于一个流程，多次启动流程实例，会生成多个流程实例
     */
    @Test
    public void startProcessesInstance() {
        System.out.println("--测试步骤222②——");
        System.out.println("————————————————————————————————————————————————————");
        Map<String, Object> property = new HashMap<>();
        property.put("user", "zzx");
//        property.put("leader","Salaboy");

        //流程定义processDefinitionKey
        //可以在创建bpmn的时候自定义
        String processDefinitionKey = "Sampler";//Sampler//Parallel

        //第一个参数（String）：流程定义processDefinitionKey
        //第二个参数（String）：业务标识businessKey1001
        //第三个参数（Map）：启动流程实例，需要置入的user参数
        ProcessInstance pi = runtimeService.startProcessInstanceByKey(processDefinitionKey, "1001", property);

        System.out.println("流程实例的ProcessInstanceId: " + pi.getId());//3bca89fa-2266-11eb-ad8c-94c691a0af85(变化)
        System.out.println("流程实例的ProcessDefinitionKey: " + pi.getProcessDefinitionKey());//sampler/A/Parallel
        System.out.println("流程实例的ProcessDefinitionId: " + pi.getProcessDefinitionId());//379f7de8-2266-11eb-ad8c-94c691a0af85(变化)
        System.out.println("流程实例的ProcessDefinitionName: " + pi.getProcessDefinitionName());//Sampler/A/Parallel
        System.out.println("流程实例的ProcessDefinitionVersion: " + pi.getProcessDefinitionVersion());//1
        System.out.println("流程实例的BusinessKey:" + pi.getBusinessKey());//1001
        System.out.println("————————————————————————————————————————————————————");
    }

    /**
     * 55555
     * ⑤：完成任务：
     *        1:申请后，对自己的申请进行确认，然后提交，使用一次complete
     *        2:此时task来到小组审批，由小组审批确认后，使用一次complete，提交到网关
     *        3：如果是Sampler——判断提交的day和网关分支的判别条件，走相应流程，一条分支如第二步继续审批，审批结束后结束。另一条分支直接结束。
     *        如果是Parallel——传递给之后的两个用户task，只有当两个用户task全部审批结束后，使用了complete之后，才可以继续向后
     *        4：若后续无其他task，此时走到end，结束。
     */
    @Test
    public void completeMyPersonalTask() {
        System.out.println("--测试步骤555⑤——");
        System.out.println("########################################################");
        String processDefinitionKey = "Sampler";
        String candidateUser = "salaboy";
//        String taskId = "9c5dd8eb-22f5-11eb-bd31-94c691a0af85";
//        String candidateUser = "leader1";
//        taskService.claim(taskId,candidateUser);

        Map<String, Object> map = new HashMap<>();

//        property.put("hr", "wyy");
//        property.put("day", 2);
        Task task = taskService.createTaskQuery()
                .processDefinitionKey(processDefinitionKey)
                .taskAssignee(candidateUser)
                .singleResult();
        //申请，置入下一任务需要的参数
        if(APPLY.equals(task.getName())){
            map.put("leader","salaboy");
        }
        //组长审批
        if(GROUP_LEADER_APPROVAL.equals(task.getName())){
            map.put("day",2);
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
        String processDefinitionKey = "Sampler";
        String candidateUser = "salaboy";
        String assignee = "leader1";

        //查询
        List<Task> list = taskService.createTaskQuery()
//                .processDefinitionKey(processDefinitionKey)
//                .processDefinitionId(processDefinitionId)//使用流程定义ID查询
//                .processInstanceId(processInstanceId)//使用流程实例ID查询
//                .executionId(executionId)//使用执行对象ID查询
//                .taskCandidateUser(candidateUser)
                .taskAssignee(candidateUser)
                /*返回结果集*/
//                .singleResult()//返回惟一结果集
//                .count()//返回结果集的数量
//                .listPage(firstResult, maxResults);//分页查询
                .list();

        //输出
        if (list != null && list.size() > 0) {
            for(Task task:list){
                System.out.println("########################################################");
                System.out.println("任务ID:"+task.getId());
                System.out.println("任务名称:"+task.getName());
                System.out.println("任务的创建时间:"+task.getCreateTime());
                System.out.println("任务的办理人:"+task.getAssignee());
                System.out.println("流程实例ID："+task.getProcessInstanceId());
                System.out.println("执行对象ID:"+task.getExecutionId());
                System.out.println("流程定义ID:"+task.getProcessDefinitionId());
                System.out.println("########################################################");
            }
        }
    }

    /**
     * 组任务拾取，组成员任意一人拾取即可，拾取后，assignee被设置为这个组成员
     */
    @Test
    public void ctt() {
        System.out.println("--测试步骤444④——");
        System.out.println("########################################################");
        String processDefinitionKey = "Sampler";
        String candidateUser = "salaboy";
        Task task = taskService.createTaskQuery()
                .processDefinitionKey(processDefinitionKey)
                .taskCandidateUser(candidateUser)
                .singleResult();
        if(task!=null){
            taskService.claim(task.getId(),candidateUser);
            System.out.println("任务拾取完毕！");
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
}
