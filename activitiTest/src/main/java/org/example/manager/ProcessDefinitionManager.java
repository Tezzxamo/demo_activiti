package org.example.manager;

import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.example.dto.ProcessDefinitionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    public ProcessDefinitionDTO getProcessDefinitionByName(String processDefinitionName) {
        return toProcessDefinitionDTO(checkProcessDefinitionByName(processDefinitionName));
    }

    /**
     * @return 一个不可修改的流程定义列表
     */
    public Collection<ProcessDefinitionDTO> getProcessDefinitions() {
        return Collections.unmodifiableList(repositoryService.createProcessDefinitionQuery()
                .latestVersion()
                .list())
                .stream()
                .map(this::toProcessDefinitionDTO)
                .collect(Collectors.toList());
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
        repositoryService.deleteDeployment(processDefinition.getDeploymentId(), true);
    }

    /**
     * 检查该流程定义是否存在
     *
     * @param processDefinitionName 流程定义name
     * @return 流程定义是否存在(存在 - > processDefinition ； 不存在 - > 抛出异常)
     */
    public ProcessDefinition checkProcessDefinitionByName(String processDefinitionName) {
        List<ProcessDefinition> processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionName(processDefinitionName)
                .latestVersion()
                .list();
        if (CollectionUtils.isEmpty(processDefinition)) {
            throw new ActivitiObjectNotFoundException("流程定义未找到");// 待修改-整合
        }
        if (processDefinition.size() > 1) {
            throw new ArrayIndexOutOfBoundsException("根据给定的流程名称或流程ID[%s], 查找到多个流程定义");
        }
        return processDefinition.get(0);
    }

    public ProcessDefinitionDTO toProcessDefinitionDTO(ProcessDefinition processDefinition) {
        return new ProcessDefinitionDTO(
                processDefinition.getId(),
                processDefinition.getName(),
                !processDefinition.isSuspended());
    }
}
