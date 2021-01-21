package org.example.manager;

import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * functions:
 * ①挂起流程定义（普通、级联）
 * ②激活流程定义（普通、级联）
 * ③查询流程定义状态（单个、所有）
 * ④检查单个流程定义是否存在
 */
@Component
public class ProcessConfigManager {

    private static final Logger logger = LoggerFactory.getLogger(ProcessConfigManager.class);

    RepositoryService repositoryService;

    @Autowired
    public ProcessConfigManager(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    /**
     * 将该流程定义挂起，则之后创建流程实例时会失败
     *
     * @param processDefinitionName 流程定义name
     * @return 挂起状态 - > false
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean suspendProcessDefinitionByName(String processDefinitionName) {
        ProcessDefinition processDefinition = checkProcessDefinitionByName(processDefinitionName);
        repositoryService.suspendProcessDefinitionById(processDefinition.getId());
        return false;
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
    @Transactional(rollbackFor = Exception.class)
    public boolean cascadeSuspendProcessDefinitionByName(String processDefinitionName, boolean cascade, Date suspensionDate) {
        ProcessDefinition processDefinition = checkProcessDefinitionByName(processDefinitionName);
        repositoryService.suspendProcessDefinitionById(processDefinition.getId(), cascade, suspensionDate);
        return false;
    }


    /**
     * 将该流程定义激活，则之后创建流程实例时会成功
     *
     * @param processDefinitionName 流程定义name
     * @return 激活状态 - > true
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean activateProcessDefinitionByName(String processDefinitionName) {
        ProcessDefinition processDefinition = checkProcessDefinitionByName(processDefinitionName);
        repositoryService.activateProcessDefinitionById(processDefinition.getId());
        return true;
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
    @Transactional(rollbackFor = Exception.class)
    public boolean cascadeActivateProcessDefinitionByName(String processDefinitionName, boolean cascade, Date activationDate) {
        ProcessDefinition processDefinition = checkProcessDefinitionByName(processDefinitionName);
        repositoryService.activateProcessDefinitionById(processDefinition.getId(), cascade, activationDate);
        return true;
    }

    /**
     * @param processDefinitionName 流程定义name
     * @return 激活状态返回 - > true , 挂起状态返回 - > false
     */
    public boolean getProcessDefinitionStatusByName(String processDefinitionName) {
        // 先检查是否存在，不需要获取返回值
        checkProcessDefinitionByName(processDefinitionName);
        // 获取激活装态下的它，如果处于激活能获取到，如果处于挂起则获取不到，能否获取到直接返回true、false
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionName(processDefinitionName)
                .latestVersion()
                .active()
                .singleResult();
        return Objects.nonNull(processDefinition);
    }

    /**
     * @return 返回所有流程定义的状态（挂起或激活）
     */
    public Map<String, Boolean> getProcessConfigStatusMap() {
        Map<String, Boolean> map = new HashMap<>();
        // 放入所有的激活状态的流程定义
        repositoryService.createProcessDefinitionQuery()
                .active()
                .latestVersion()
                .list()
                .forEach(processDefinition -> map.put(processDefinition.getName(), true));
        // 放入所有的挂起状态的流程定义
        repositoryService.createProcessDefinitionQuery()
                .suspended()
                .latestVersion()
                .list()
                .forEach(processDefinition -> map.put(processDefinition.getName(), false));
        return map;
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
