package org.example.service.impl;

import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.example.dto.ProcessDefinitionDTO;
import org.example.service.ProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ProcessServiceImpl implements ProcessService {

    RepositoryService repositoryService;

    @Autowired
    public ProcessServiceImpl(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    @Override
    public boolean suspendProcessDefinitionByName(String processDefinitionName) {
        ProcessDefinition processDefinition = checkProcessDefinitionByName(processDefinitionName);
        repositoryService.suspendProcessDefinitionById(processDefinition.getId());
        return false;
    }

    @Override
    public boolean cascadeSuspendProcessDefinitionByName(String processDefinitionName, boolean cascade, Date suspensionDate) {
        return false;
    }

    @Override
    public boolean activateProcessDefinitionByName(String processDefinitionName) {
        return false;
    }

    @Override
    public boolean cascadeActivateProcessDefinitionByName(String processDefinitionName, boolean cascade, Date activationDate) {
        return false;
    }

    @Override
    public boolean getProcessDefinitionStatusByName(String processDefinitionName) {
        return false;
    }

    @Override
    public Map<String, Boolean> getProcessConfigStatusMap() {
        return null;
    }

    @Override
    public ProcessDefinitionDTO getProcessDefinitionByName(String processDefinitionName) {
        return null;
    }

    @Override
    public Collection<ProcessDefinitionDTO> getProcessDefinitions() {
        return null;
    }

    @Override
    public void deleteProcessDefinitionByName(String processDefinitionName) {

    }

    @Override
    public void cascadeDeleteProcessDefinitionByName(String processDefinitionName) {

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
}
