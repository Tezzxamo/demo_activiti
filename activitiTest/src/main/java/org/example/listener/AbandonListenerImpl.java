package org.example.listener;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.example.DemoApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 添加到流程图bpmn文件上的监听器
 */
@Slf4j
public class AbandonListenerImpl implements ExecutionListener {

//    @Autowired
//    RuntimeService runtimeService;


    @Override
    public void notify(DelegateExecution delegateExecution) {
        String pid=delegateExecution.getProcessInstanceId();
        log.info(">>> pid: '" + pid );
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        runtimeService.deleteProcessInstance(pid,"废弃了！");
    }
}
