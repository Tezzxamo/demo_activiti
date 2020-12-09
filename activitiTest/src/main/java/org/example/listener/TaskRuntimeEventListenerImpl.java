package org.example.listener;

import org.activiti.api.task.runtime.events.*;
import org.activiti.api.task.runtime.events.listener.TaskRuntimeEventListener;
import org.example.DemoApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class TaskRuntimeEventListenerImpl {
    private final Logger logger = LoggerFactory.getLogger(TaskRuntimeEventListenerImpl.class);

    /**
     * 监听task的创建
     *
     * @return taskCreated
     */
    @Bean
    public TaskRuntimeEventListener<TaskCreatedEvent> taskCreatedListener() {
        return taskCreated -> logger.info(">>> Task Created: '"
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
        return taskAssigned -> logger.info(">>> Task Assigned: '"
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
        return taskCompleted -> logger.info(">>> Task Completed: '"
                + taskCompleted.getEntity().getName()
                + "' 它的拥有者Owner是 : " + taskCompleted.getEntity().getOwner());
    }

    /**
     * 监听task挂起
     *
     * @return taskSuspended
     */
    @Bean
    public TaskRuntimeEventListener<TaskSuspendedEvent> taskSuspendedListener() {
        return taskSuspended -> logger.info(">>> Task Suspended: '"
                + taskSuspended.getEntity().getName()
                + "' 它的ProcessInstanceId是 : " + taskSuspended.getEntity().getProcessInstanceId());
    }

    /**
     * 监听task取消
     *
     * @return taskCancelled
     */
    @Bean
    public TaskRuntimeEventListener<TaskCancelledEvent> taskCancelledListener() {
        return taskCancelled -> logger.info(">>> Task Cancelled: '"
                + taskCancelled.getEntity().getName()
                + "' 它的ProcessInstanceId是 : " + taskCancelled.getEntity().getProcessInstanceId());
    }

    /**
     * 监听task更新
     *
     * @return taskUpdated
     */
    @Bean
    public TaskRuntimeEventListener<TaskUpdatedEvent> taskUpdatedListener() {
        return taskUpdated -> logger.info(">>> Task Updated: '"
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
        return taskActivated -> logger.info(">>> Task Activated: '"
                + taskActivated.getEntity().getName()
                + "' 它的ProcessInstanceId是 : " + taskActivated.getEntity().getProcessInstanceId());
    }


}
