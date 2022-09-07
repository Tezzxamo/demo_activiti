package org.example.service.manager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.example.common.utils.VerificationUtils;
import org.example.model.vo.ProcessDefinitionVO;
import org.example.enumeration.CodeEnum;
import org.example.exceptions.BusinessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * functions:
 * ①挂起流程定义（普通、级联）
 * ②激活流程定义（普通、级联）
 * ③查询流程定义状态（单个、所有）
 * ④检查单个流程定义是否存在
 *
 * @author Tethamo_zzx
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ProcessManager {

    private final RepositoryService repositoryService;

    /**
     * 将该流程定义挂起，则之后创建流程实例时会失败
     *
     * @param processDefinitionName 流程定义name
     */
    @Transactional(rollbackFor = Exception.class)
    public void suspendProcessDefinitionByName(String processDefinitionName) {
        ProcessDefinition processDefinition = VerificationUtils.checkProcessDefinitionByName(processDefinitionName);
        repositoryService.suspendProcessDefinitionById(processDefinition.getId());
    }

    /**
     * 将该流程定义挂起，则之后创建流程实例时会失败
     * 若是级联挂起，则之前创建的流程实例也会被挂起
     * 同时可以自定义什么时间开始挂起这个流程定义的时间，若null则立即挂起
     *
     * @param processDefinitionName 流程定义name
     * @param cascade               是否级联挂起
     * @param suspensionDate        多久之后开始挂起
     */
    @Transactional(rollbackFor = Exception.class)
    public void cascadeSuspendProcessDefinitionByName(String processDefinitionName, boolean cascade, Date suspensionDate) {
        ProcessDefinition processDefinition = VerificationUtils.checkProcessDefinitionByName(processDefinitionName);
        repositoryService.suspendProcessDefinitionById(processDefinition.getId(), cascade, suspensionDate);
    }


    /**
     * 将该流程定义激活，则之后创建流程实例时会成功
     *
     * @param processDefinitionName 流程定义name
     */
    @Transactional(rollbackFor = Exception.class)
    public void activateProcessDefinitionByName(String processDefinitionName) {
        ProcessDefinition processDefinition = VerificationUtils.checkProcessDefinitionByName(processDefinitionName);
        repositoryService.activateProcessDefinitionById(processDefinition.getId());
    }

    /**
     * 将该流程定义激活，则之后创建流程实例时会成功
     * 若是级联激活，则有关该流程定义的流程实例(被挂起的流程实例),也会被激活
     * 同时可以自定义什么时间开始激活这个流程定义的时间，若null则立即激活
     *
     * @param processDefinitionName 流程定义name
     * @param cascade               是否级联激活
     * @param activationDate        多久之后开始激活
     */
    @Transactional(rollbackFor = Exception.class)
    public void cascadeActivateProcessDefinitionByName(String processDefinitionName, boolean cascade, Date activationDate) {
        ProcessDefinition processDefinition = VerificationUtils.checkProcessDefinitionByName(processDefinitionName);
        repositoryService.activateProcessDefinitionById(processDefinition.getId(), cascade, activationDate);
    }

    /**
     * 通过流程定义name获取流程定义状态
     *
     * @param processDefinitionName 流程定义name
     * @return ProcessDefinition 流程定义
     */
    public Boolean getProcessDefinitionStatusByName(String processDefinitionName) {
        // 先检查是否存在，不需要获取返回值
        VerificationUtils.checkProcessDefinitionByName(processDefinitionName);
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
        Map<String, Boolean> map = new HashMap<>(16);
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
     * @param processDefinitionName 流程定义name
     * @return ProcessDefinition
     */
    public ProcessDefinitionVO getProcessDefinitionByName(String processDefinitionName) {
        return toProcessDefinitionDTO(VerificationUtils.checkProcessDefinitionByName(processDefinitionName));
    }

    /**
     * 获取所有的流程定义DTO
     *
     * @return 一个不可修改的流程定义列表
     */
    public Collection<ProcessDefinitionVO> getProcessDefinitions() {
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
        ProcessDefinition processDefinition = VerificationUtils.checkProcessDefinitionByName(processDefinitionName);
        repositoryService.deleteDeployment(processDefinition.getDeploymentId());
    }

    /**
     * 级联删除,会删除和当前规则相关的所有信息，包括该流程定义下的所有的流程实例的历史数据
     *
     * @param processDefinitionName 流程定义name
     */
    @Transactional(rollbackFor = Exception.class)
    public void cascadeDeleteProcessDefinitionByName(String processDefinitionName) {
        ProcessDefinition processDefinition = VerificationUtils.checkProcessDefinitionByName(processDefinitionName);
        repositoryService.deleteDeployment(processDefinition.getDeploymentId(), true);
    }

    @Transactional(rollbackFor = Exception.class)
    public ProcessDefinitionVO deployProcessDefinition(String processDefinitionName, String bpmnPath) {
        // 重复部署会增加 版本 VERSION_ 的值
        try {
            // 创建部署对象并部署
            Deployment deploy = repositoryService.createDeployment()
                    .name(processDefinitionName)
                    .addClasspathResource(bpmnPath)
                    .deploy();
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                    .latestVersion()
                    .active()
                    .list()
                    .get(0);
            log.info(deploy.getId());
            // 如果没有部署成功，processDefinition.getName()会报错
            log.info(processDefinition.getName());
            // processDefinition.isSuspended()返回结果：如果挂起是true，所以需要反一下
            return toProcessDefinitionDTO(processDefinition);
        } catch (Exception e) {
            log.error("[部署流程图]:[失败]，错误原因:[{}]", e.getMessage());
            throw new BusinessException(CodeEnum.DEPLOY_PROCESS_ERROR, e.getMessage());
        }
    }

    /**
     * ProcessDefinition转换为ProcessDefinitionDTO
     *
     * @param processDefinition 流程定义
     * @return ProcessDefinitionDTO
     */
    public ProcessDefinitionVO toProcessDefinitionDTO(ProcessDefinition processDefinition) {
        return new ProcessDefinitionVO()
                .setProcessDefinitionId(processDefinition.getId())
                .setProcessDefinitionName(processDefinition.getName())
                .setStatus(!processDefinition.isSuspended());
    }

}
