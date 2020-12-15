package org.example.service.impl;

import org.activiti.engine.repository.ProcessDefinition;
import org.example.dto.ProcessDefinitionDTO;
import org.example.manager.ProcessDefinitionManager;
import org.example.service.ProcessDefinitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * ①获取单个流程定义
 * ②获取所有流程定义
 * ③普通删除流程定义
 * ④级联删除流程定义
 * ⑤检查流程定义是否存在
 */
@Service
public class ProcessDefinitionServiceImpl implements ProcessDefinitionService {

    @Autowired
    ProcessDefinitionManager processDefinitionManager;

    /**
     * @param processDefinitionName 流程定义name
     * @return ProcessDefinition
     */
    @Override
    public ProcessDefinitionDTO getProcessDefinitionByName(String processDefinitionName) {
        return processDefinitionManager.getProcessDefinitionByName(processDefinitionName);
    }

    /**
     * @return 一个不可修改的流程定义列表
     */
    @Override
    public Collection<ProcessDefinitionDTO> getProcessDefinitions() {
        return processDefinitionManager.getProcessDefinitions();
    }

    /**
     * 普通删除，如果当前规则下有正在执行的流程，则抛异常
     *
     * @param processDefinitionName 流程定义name
     */
    @Override
    public void deleteProcessDefinitionByName(String processDefinitionName) {
        processDefinitionManager.deleteProcessDefinitionByName(processDefinitionName);
    }

    /**
     * 级联删除,会删除和当前规则相关的所有信息，包括该流程定义下的所有的流程实例的历史数据
     *
     * @param processDefinitionName 流程定义name
     */
    @Override
    public void cascadeDeleteProcessDefinitionByName(String processDefinitionName) {
        processDefinitionManager.cascadeDeleteProcessDefinitionByName(processDefinitionName);
    }

}
