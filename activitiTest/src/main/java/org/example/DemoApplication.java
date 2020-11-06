package org.example;


import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

/**
 * Hello world!
 */

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

    private Logger logger = LoggerFactory.getLogger(DemoApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Autowired
    RepositoryService repositoryService;


    /**
     * 读取application.yml中的配置，然后自动建表（或更新表）
     * @param args
     */

    @Override
    public void run(String... args) {
        List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery().active().list();
        logger.info("> 处于激活状态的流程数量: " + processDefinitionList.size());
        for (ProcessDefinition pd : processDefinitionList) {
            logger.info("\t ===> Process definition: " + pd.getKey());
        }
        System.out.println();
        System.out.println();


//        List<ProcessDefinition> pageList = repositoryService.createProcessDefinitionQuery().listPage(0,15);
//        for (ProcessDefinition pd : pageList){
//            System.out.println(pd);
//        }
    }
}