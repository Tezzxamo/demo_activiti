package org.example;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * 需要将这个监听类与task连接起来
 * 即在“xxx.bpmn20.xml”中的Listeners中的task listeners进行设置
 * 然后启动这个实例即可
 */
public class TaskListenerImpl implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {
        //指定个人任务的办理人，也可以指定组任务的办理人
        //通过类去查询数据库，将下一个任务的办理人查询获取，然后通过setAssignee()的方法指定任务办理人
        delegateTask.setAssignee("user1");
    }
}
