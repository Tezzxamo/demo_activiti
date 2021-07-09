package org.example.service.impl;

import org.example.dto.ProcessDefinitionDTO;
import org.example.manager.ProcessManager;
import org.example.service.ProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

@Service
public class ProcessServiceImpl implements ProcessService {



    ProcessManager processManager;

    @Autowired
    public ProcessServiceImpl(ProcessManager processManager) {
        this.processManager = processManager;
    }

    /**
     * 将该流程定义挂起，则之后创建流程实例时会失败
     *
     * @param processDefinitionName 流程定义name
     * @return 挂起状态 - > false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void suspendProcessDefinitionByName(String processDefinitionName) {
        processManager.suspendProcessDefinitionByName(processDefinitionName);
    }

    /**
     * 将该流程定义挂起，则之后创建流程实例时会失败
     * 若是级联挂起，则之前创建的流程实例也会被挂起
     * 同时可以自定义什么时间开始挂起这个流程定义的时间，若null则立即挂起
     *
     * @param processDefinitionName 流程定义name
     * @param cascade               是否级联挂起
     * @param suspensionDate        多久之后开始挂起
     * @return 挂起状态 - > false
     */
    @Override
    public void cascadeSuspendProcessDefinitionByName(String processDefinitionName, boolean cascade, Date suspensionDate) {
        processManager.cascadeSuspendProcessDefinitionByName(processDefinitionName, cascade, suspensionDate);
    }

    /**
     * 将该流程定义激活，则之后创建流程实例时会成功
     *
     * @param processDefinitionName 流程定义name
     * @return 激活状态 - > true
     */
    @Override
    public void activateProcessDefinitionByName(String processDefinitionName) {
        processManager.activateProcessDefinitionByName(processDefinitionName);
    }

    /**
     * 将该流程定义激活，则之后创建流程实例时会成功
     * 若是级联激活，则有关该流程定义的被挂起的流程实例也会被激活
     * 同时可以自定义什么时间开始激活这个流程定义的时间，若null则立即激活
     *
     * @param processDefinitionName 流程定义name
     * @param cascade               是否级联激活
     * @param activationDate        多久之后开始激活
     * @return 激活状态 - > true
     */
    @Override
    public void cascadeActivateProcessDefinitionByName(String processDefinitionName, boolean cascade, Date activationDate) {
        processManager.cascadeActivateProcessDefinitionByName(processDefinitionName, cascade, activationDate);
    }

    /**
     * @param processDefinitionName 流程定义name
     * @return 激活状态返回 - > true , 挂起状态返回 - > false
     */
    @Override
    public void getProcessDefinitionStatusByName(String processDefinitionName) {
        processManager.getProcessDefinitionStatusByName(processDefinitionName);
    }

    /**
     * @return 返回所有流程定义的状态（挂起或激活）
     */
    @Override
    public Map<String, Boolean> getProcessConfigStatusMap() {
        return processManager.getProcessConfigStatusMap();
    }

    /**
     * 根据流程定义name获取流程定义
     *
     * @param processDefinitionName 流程定义name
     * @return ProcessDefinition
     */
    @Override
    public ProcessDefinitionDTO getProcessDefinitionByName(String processDefinitionName) {
        return processManager.getProcessDefinitionByName(processDefinitionName);
    }

    /**
     * 获取所有的流程定义
     *
     * @return 一个不可修改的流程定义列表
     */
    @Override
    public Collection<ProcessDefinitionDTO> getProcessDefinitions() {
        return processManager.getProcessDefinitions();
    }

    /**
     * 普通删除，如果当前规则下有正在执行的流程，则抛异常
     *
     * @param processDefinitionName 流程定义name
     */
    @Override
    public void deleteProcessDefinitionByName(String processDefinitionName) {
        processManager.deleteProcessDefinitionByName(processDefinitionName);
    }

    /**
     * 级联删除,会删除和当前规则相关的所有信息，包括该流程定义下的所有的流程实例的历史数据
     *
     * @param processDefinitionName 流程定义name
     */
    @Override
    public void cascadeDeleteProcessDefinitionByName(String processDefinitionName) {
        processManager.cascadeDeleteProcessDefinitionByName(processDefinitionName);
    }
}
