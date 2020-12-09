package org.example.listener;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.example.DemoApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class AbandonListenerImpl implements ExecutionListener {

    private Logger logger = LoggerFactory.getLogger(DemoApplication.class);

//    @Autowired
//    RuntimeService runtimeService;


    @Override
    public void notify(DelegateExecution delegateExecution) {
        String pid=delegateExecution.getProcessInstanceId();
        logger.info(">>> pid: '" + pid );
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        runtimeService.deleteProcessInstance(pid,"废弃了！");
    }
}
