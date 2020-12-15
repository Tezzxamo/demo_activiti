package org.example.manager;

import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

/**
 * ①获取单个流程定义
 * ②获取所有流程定义
 * ③普通删除流程定义
 * ④级联删除流程定义
 * ⑤检查流程定义是否存在
 */
@Component
public class ProcessDefinitionManager {

    private static final Logger logger = LoggerFactory.getLogger(ProcessDefinitionManager.class);

    RepositoryService repositoryService;

    @Autowired
    public ProcessDefinitionManager(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    /**
     * @param processDefinitionName 流程定义name
     * @return ProcessDefinition
     */
    public ProcessDefinition getProcessDefinitionByName(String processDefinitionName) {
        return checkProcessDefinitionByName(processDefinitionName);
    }

    /**
     * @return 一个不可修改的流程定义列表
     */
    public Collection<ProcessDefinition> getProcessDefinitions() {
        return Collections.unmodifiableList(repositoryService.createProcessDefinitionQuery()
                .latestVersion()
                .list());
    }

    /**
     * 普通删除，如果当前规则下有正在执行的流程，则抛异常
     *
     * @param processDefinitionName 流程定义name
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteProcessDefinitionByName(String processDefinitionName) {
        ProcessDefinition processDefinition = checkProcessDefinitionByName(processDefinitionName);
        repositoryService.deleteDeployment(processDefinition.getDeploymentId());
    }

    /**
     * 级联删除,会删除和当前规则相关的所有信息，包括该流程定义下的所有的流程实例的历史数据
     *
     * @param processDefinitionName 流程定义name
     */
    @Transactional(rollbackFor = Exception.class)
    public void cascadeDeleteProcessDefinitionByName(String processDefinitionName) {
        ProcessDefinition processDefinition = checkProcessDefinitionByName(processDefinitionName);
        repositoryService.deleteDeployment(processDefinition.getDeploymentId(),true);
    }

    /**
     * 检查该流程定义是否存在
     *
     * @param processDefinitionName 流程定义name
     * @return 流程定义是否存在(存在 - > processDefinition ； 不存在 - > 抛出异常)
     */
    public ProcessDefinition checkProcessDefinitionByName(String processDefinitionName) {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionName(processDefinitionName)
                .latestVersion()
                .singleResult();
        if (Objects.isNull(processDefinition)) {
            throw new ActivitiObjectNotFoundException("流程定义未找到");// 待修改-整合
        }
        return processDefinition;
    }

}
