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
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InclusiveTest {
    @Autowired
    RepositoryService repositoryService;

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    TaskService taskService;
    /**
     * 11111
     * ①部署流程定义
     */
    @Test
    public void deploymentProcessDefinition() {
        System.out.println("--测试步骤111①——");
        System.out.println("————————————————————————————————————————————————————");
        Deployment deployment = repositoryService.createDeployment()//添加一个部署对象
                .name("医生")//添加部署的名字
                .addClasspathResource("processes/Inclusive.bpmn20.xml")
                .deploy();//完成部署
        System.out.println("部署ID： " + deployment.getId());
        System.out.println("部署名称： " + deployment.getName());
        System.out.println("————————————————————————————————————————————————————");
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
        //流程定义processDefinitionKey
        //可以在创建bpmn的时候自定义
        String processDefinitionKey = "Inclusive";
        ProcessInstance pi = runtimeService.startProcessInstanceByKey(processDefinitionKey, "1023", property);

        //输出
        System.out.println("流程实例的ProcessInstanceId: " + pi.getId());
        System.out.println("流程实例的ProcessDefinitionKey: " + pi.getProcessDefinitionKey());
        System.out.println("流程实例的ProcessDefinitionId: " + pi.getProcessDefinitionId());
        System.out.println("流程实例的ProcessDefinitionName: " + pi.getProcessDefinitionName());
        System.out.println("流程实例的ProcessDefinitionVersion: " + pi.getProcessDefinitionVersion());
        System.out.println("流程实例的BusinessKey:" + pi.getBusinessKey());
        System.out.println("————————————————————————————————————————————————————");
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
        String processDefinitionKey = "Inclusive";
        String candidateUser = "salaboy";
        String assignee="zzx";

        Map<String, Object> map = new HashMap<>();
        //进入包含网关
        map.put("day", 1);
        map.put("time",2);
        map.put("doctor","salaboy,erdemedeiros,other");
        //
//        map.put("doctor","salaboy");
//        map.put("doctor","erdemedeiros");
//        map.put("doctor","other");

        Task task = taskService.createTaskQuery()
                .processDefinitionKey(processDefinitionKey)
                .taskAssignee(assignee)
                .singleResult();

        taskService.complete(task.getId(), map);
//        taskService.complete("c9827570-232e-11eb-9749-94c691a0af85");
        System.out.println("任务完成");
        System.out.println("########################################################");
    }

    /**
     * 组任务拾取，组成员任意一人拾取即可，拾取后，assignee被设置为这个组成员
     */
    @Test
    public void ctt() {
        System.out.println("--测试步骤444④——");
        System.out.println("########################################################");
        String processDefinitionKey = "Inclusive";
        String candidateUser = "salaboy";
        Task task = taskService.createTaskQuery()
                .processDefinitionKey(processDefinitionKey)
                .taskCandidateUser(candidateUser)
                .taskName("星期一")
                .singleResult();

        if(task!=null){
            taskService.claim(task.getId(),candidateUser);
            System.out.println("任务拾取完毕！");
        }
        System.out.println("########################################################");
    }

}
