package org.example.controller2022;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.example.Utils.function.FunctionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * 功能描述:测试部署
 *
 * @author Tethamo
 */
@Slf4j
@RestController
@RequestMapping("/process")
@RequiredArgsConstructor
public class ProcessController {

    private final RepositoryService repositoryService;

    /**
     * 测试 url：http://localhost:8080/process/deploy?name=A&&resource=A
     *
     * @return 返回成功与否
     */
    @RequestMapping("/deploy")
    public String deploy(@RequestParam String name, @RequestParam String resource) {
        // 重复部署会增加 版本 VERSION_ 的值
        try {
            // 创建部署对象并部署
            Deployment deploy = repositoryService.createDeployment()
                    .name(name)
                    .addClasspathResource("processes/" + resource + ".bpmn20.xml")
                    .deploy();
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                    .latestVersion()
                    .active()
                    .list()
                    .get(0);
            log.info(deploy.getId());
            // 如果没有部署成功，processDefinition.getName()会报错
            log.info(processDefinition.getName());
        } catch (Exception e) {
            log.info("fail");
            FunctionUtils.isWrong(true).throwCustomException("fail");
        }
        log.info("success");
        return "success";
    }


}