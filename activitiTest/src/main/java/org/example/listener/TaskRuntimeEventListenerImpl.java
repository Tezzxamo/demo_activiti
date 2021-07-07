package org.example.listener;

import lombok.extern.slf4j.Slf4j;
import org.activiti.api.task.runtime.events.*;
import org.activiti.api.task.runtime.events.listener.TaskRuntimeEventListener;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * task事件监听，随着task的创建、拾取、完成、更新、删除、挂起、激活各类状态——记录log日志
 */
@Component
@Slf4j
public class TaskRuntimeEventListenerImpl {

    /**
     * 监听task的创建
     *
     * @return taskCreated
     */
    @Bean
    public TaskRuntimeEventListener<TaskCreatedEvent> taskCreatedListener() {
        return taskCreated -> log.info(">>> Task Created: '"
                + taskCreated.getEntity().getName()
                + "' 它的ProcessInstanceId是 : " + taskCreated.getEntity().getProcessInstanceId());
    }

    /**
     * 监听task审批人
     *
     * @return taskAssigned
     */
    @Bean
    public TaskRuntimeEventListener<TaskAssignedEvent> taskAssignedListener() {
        return taskAssigned -> log.info(">>> Task Assigned: '"
                + taskAssigned.getEntity().getName()
                + "' 它的审批人Assignee是 : " + taskAssigned.getEntity().getAssignee());
    }

    /**
     * 监听task完成
     *
     * @return taskCompleted
     */
    @Bean
    public TaskRuntimeEventListener<TaskCompletedEvent> taskCompletedListener() {
        return taskCompleted -> log.info(">>> Task Completed: '"
                + taskCompleted.getEntity().getName()
                + "' 它的审批人Assignee是 : " + taskCompleted.getEntity().getAssignee());
    }

    /**
     * 监听task挂起
     *
     * @return taskSuspended
     */
    @Bean
    public TaskRuntimeEventListener<TaskSuspendedEvent> taskSuspendedListener() {
        return taskSuspended -> log.info(">>> Task Suspended: '"
                + taskSuspended.getEntity().getName()
                + "' 它的ProcessInstanceId是 : " + taskSuspended.getEntity().getProcessInstanceId());
    }

    /**
     * 监听task取消
     *
     * 例：如果使用runtimeService.deleteProcessInstance(taskId,DeleteReason),就会触发task取消事件
     *
     * @return taskCancelled
     */
    @Bean
    public TaskRuntimeEventListener<TaskCancelledEvent> taskCancelledListener() {
        return taskCancelled -> log.info(">>> Task Cancelled: '"
                + taskCancelled.getEntity().getName()
                + "' 它的ProcessInstanceId是 : " + taskCancelled.getEntity().getProcessInstanceId());
    }

    /**
     * 监听task更新
     *
     * 例：如果更改审批人，就会触发task更新事件
     *
     * @return taskUpdated
     */
    @Bean
    public TaskRuntimeEventListener<TaskUpdatedEvent> taskUpdatedListener() {
        return taskUpdated -> log.info(">>> Task Updated: '"
                + taskUpdated.getEntity().getName()
                + "' 它的ProcessInstanceId是 : " + taskUpdated.getEntity().getProcessInstanceId());
    }

    /**
     * 监听task激活
     *
     * @return taskActivated
     */
    @Bean
    public TaskRuntimeEventListener<TaskActivatedEvent> taskActivatedListener() {
        return taskActivated -> log.info(">>> Task Activated: '"
                + taskActivated.getEntity().getName()
                + "' 它的ProcessInstanceId是 : " + taskActivated.getEntity().getProcessInstanceId());
    }

}
