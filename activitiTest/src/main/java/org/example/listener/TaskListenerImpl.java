package org.example.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.example.DemoApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 */
public class TaskListenerImpl implements TaskListener {
    private static final long serialVersionUID = 1L;

    private Logger logger = LoggerFactory.getLogger(DemoApplication.class);


    String EVENTNAME_CREATE = "create";
    String EVENTNAME_ASSIGNMENT = "assignment";
    String EVENTNAME_COMPLETE = "complete";
    String EVENTNAME_DELETE = "delete";


    @Override
    public void notify(DelegateTask delegateTask) {
        String eventName = delegateTask.getEventName();

        if (EVENTNAME_CREATE.endsWith(eventName)) {
            logger.info("create===任务创建");
        }
        if (EVENTNAME_ASSIGNMENT.endsWith(eventName)) {
            logger.info("assignment===任务分配");
        }
        if (EVENTNAME_COMPLETE.endsWith(eventName)) {
            logger.info("complete===任务完成");
        }
        if (EVENTNAME_DELETE.endsWith(eventName)) {
            logger.info("delete===任务删除");
        }
    }
}
