package org.example;

import cn.dev33.satoken.SaManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.repository.ProcessDefinition;
import org.example.common.utils.ActivitiUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.List;

/**
 * Activiti启动类
 * 暂时屏蔽SpringSecurity
 *
 * @author Tethamo_zzx
 */
@Slf4j
@RequiredArgsConstructor
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class, ManagementWebSecurityAutoConfiguration.class},
        scanBasePackages = {"org.example"})
@EnableTransactionManagement
@EnableConfigurationProperties
public class ActivitiApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ActivitiApplication.class, args);
    }

    @Override
    public void run(String... args) {
        List<ProcessDefinition> processDefinitionList = ActivitiUtil.instance().getRepositoryService().createProcessDefinitionQuery().active().latestVersion().list();
        log.info("> 处于激活状态的最新版本的流程定义数量: " + processDefinitionList.size());
        for (ProcessDefinition pd : processDefinitionList) {
            log.info("\t ===> Process definition: " + pd.getKey() + " 版本：" + pd.getVersion());
        }
        // sa-token配置
        log.info("启动成功，Sa-Token 配置如下：" + SaManager.getConfig());
    }
}