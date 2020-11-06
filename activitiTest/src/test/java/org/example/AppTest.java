package org.example;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
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
import java.util.List;

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

    private Logger logger = LoggerFactory.getLogger(AppTest.class);

    @Autowired
    RepositoryService repositoryService;

    @Autowired
    RuntimeService runtimeService;

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
    public void findProcessDefinition() {
        List<ProcessDefinition> pageList = repositoryService.createProcessDefinitionQuery().listPage(0, 15);
        for (ProcessDefinition pd : pageList) {
//            System.out.println("===> Process definition:"+pd);
            logger.info("\t ===> Process definition: " + pd.getId());
        }
    }

    @Test
    public void deleteProcessDefinition() {
//        String deploymentId="";
//        repositoryService.deleteDeployment(deploymentId);
//        repositoryService.createDeploymentQuery().list()
//                .stream()
//                .map(Deployment::getId)
//                .forEach(System.out::println);
        //deploymentId是一个uuid，这里的deploymentId是一个示例，
        //在使用deploymentProcessDefinition()
        //以后会得到一个部署ID，可以通过这个部署ID来测试删除
        String deploymentId = "ba396d83-2020-11eb-9ece-94c691a0af85";
        // 普通删除，如果当前规则下有正在执行的流程，则抛异常
        repositoryService.deleteDeployment(deploymentId);
        // 级联删除,会删除和当前规则相关的所有信息，包括历史
        repositoryService.deleteDeployment(deploymentId, true);
    }

    /**
     * 部署流程定义
     */
    @Test
    public void deploymentProcessDefinition() {
        Deployment deployment = repositoryService.createDeployment()//添加一个部署对象
                .name("第一个流程")//添加部署的名字
                .addClasspathResource("processes/A.bpmn")//加载资源
                .addClasspathResource("processes/A.png")//加载资源
                .deploy();//完成部署
        System.out.println("部署ID： " + deployment.getId());
        System.out.println("部署名称： " + deployment.getName());
        System.out.println(deployment.getKey());
    }


    /**
     * 启动流程实例
     * 对于一个流程，多次启动流程实例，会生成多个流程实例
     */
    @Test
    public void startProcessesInstance(){
        String processDefinitionKey = "PROCESS_1";
//        System.out.println( repositoryService.getProcessDefinition("f48a5cd9-2021-11eb-a664-94c691a0af85").getKey());
        ProcessInstance pi = runtimeService.startProcessInstanceByKey(processDefinitionKey);

        System.out.println("流程实例ID: " + pi.getId());//流程实例ID
        System.out.println("流程定义ID: " + pi.getProcessDefinitionId());//流程定义ID
    }

}
