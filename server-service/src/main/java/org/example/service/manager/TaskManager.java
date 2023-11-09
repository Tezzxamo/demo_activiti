package org.example.service.manager;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.task.Task;
import org.example.common.utils.ActivitiUtil;
import org.example.model.dto.UserDTO;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collection;

@Slf4j
@Component
public class TaskManager {

    /**
     * 该用户所有可以进行拾取审批的任务
     *
     * @param userDTO 用户
     * @return 该用户可以拾取的所有task
     */
    public Collection<Task> listTasksByCandidateUser(UserDTO userDTO) {
        return ActivitiUtil.instance().getTaskService().createTaskQuery()
                .active()
                .taskCandidateUser(userDTO.getUserName())
                .list();
    }

    /**
     * 通过id获取task
     *
     * @param taskId taskId
     * @return task
     */
    public Task getTaskByTaskId(String taskId) {
        return checkSingleTask(
                ActivitiUtil.instance().getTaskService().createTaskQuery()
                        .taskId(taskId)
                        .active()
                        .list()
        );
    }

    /**
     * 通过流程实例获取当前任务
     *
     * @param processInstanceId 流程实例id
     * @return task
     */
    public Task getTaskByProcessInstanceId(String processInstanceId) {
        return checkSingleTask(
                ActivitiUtil.instance().getTaskService().createTaskQuery()
                        .processInstanceId(processInstanceId)
                        .active()
                        .list()
        );
    }

    /**
     * 通过流程定义获取所有它的task
     *
     * @param processDefinitionName 流程定义Name
     * @return 任务集合
     */
    public Collection<Task> getTasksByProcessDefinitionName(String processDefinitionName) {
        return ActivitiUtil.instance().getTaskService().createTaskQuery()
                .processDefinitionName(processDefinitionName)
                .active()
                .list();
    }

    /**
     * 用户拾取任务并完成该待办事项
     *
     * @param userDTO 用户
     * @param task    任务
     */
    @Transactional(rollbackFor = Exception.class)
    public void claimAndCompleteTask(UserDTO userDTO, Task task) {
        log.info("> 用户{}申领待办事项: {}", userDTO.getUserName(), task.getName());
        ActivitiUtil.instance().getTaskService().claim(task.getId(), userDTO.getUserName());
        log.info("> 完成待办事项: {}", task.getName());
        ActivitiUtil.instance().getTaskService().complete(task.getId());
    }

    /**
     * 检查该task是否存在
     *
     * @param list taskList
     * @return task是否存在(存在 - > task ； 不存在 / 存在多个 - > 抛出异常)
     */
    public Task checkSingleTask(Collection<Task> list) {
        if (CollectionUtils.isEmpty(list)) {
            throw new ActivitiObjectNotFoundException("流程实例未找到");
        }
        if (list.size() > 1) {
            throw new ArrayIndexOutOfBoundsException("同一taskId找到多个task!");
        }
        return (Task) list.toArray()[0];
    }

}
