package org.example.manager;

import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.example.dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;

public class TaskManager {

    private static final Logger logger = LoggerFactory.getLogger(TaskManager.class);

    TaskService taskService;

    @Autowired
    public TaskManager(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * @param userDTO 用户
     * @return 该用户可以拾取的所有task
     */
    public Collection<Task> listTasksByCandidateUser(UserDTO userDTO) {
        return taskService.createTaskQuery()
                .active()
                .taskCandidateUser(userDTO.getUserName())
                .list();
    }

    public Task getTaskByTaskId(String taskId) {
        return checkSingleTask(
                taskService.createTaskQuery()
                        .taskId(taskId)
                        .active()
                        .list()
        );
    }

    public Task getTaskByProcessInstanceId(String processInstanceId) {
        return checkSingleTask(
                taskService.createTaskQuery()
                        .processInstanceId(processInstanceId)
                        .active()
                        .list()
        );
    }

    public Collection<Task> getTasksByProcessDefinitionName(String processDefinitionName){
        return taskService.createTaskQuery()
                .processDefinitionName(processDefinitionName)
                .active()
                .list();
    }

    @Transactional(rollbackFor = Exception.class)
    public void claimAndCompleteTask(UserDTO userDTO,Task task){
        logger.info("> 用户{}申领待办事项: {}", userDTO.getUserName(), task.getName());
        taskService.claim(task.getId(), userDTO.getUserName());
        logger.info("> 完成待办事项: {}", task.getName());
        taskService.complete(task.getId());
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
