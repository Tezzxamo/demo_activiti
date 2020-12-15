package org.example.listener;

import org.activiti.api.model.shared.event.VariableCreatedEvent;
import org.activiti.api.process.model.events.SequenceFlowEvent;
import org.activiti.api.process.runtime.events.*;
import org.activiti.api.process.runtime.events.listener.ProcessRuntimeEventListener;
import org.activiti.engine.HistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * ProcessRuntimeEvent监听 ———— 记录log日志
 */
@Component
public class ProcessRuntimeEventListenerImpl {
    private final Logger logger = LoggerFactory.getLogger(ProcessRuntimeEventListenerImpl.class);

    @Autowired
    HistoryService historyService;

    @Bean
    public ProcessRuntimeEventListener<ProcessCreatedEvent> ProcessCreatedListener() {
        return ProcessCreated -> logger.info(">>> Process Created: '"
                + ProcessCreated.getEntity().getInitiator()
                + "' 它的ProcessDefinitionKey是 : " + ProcessCreated.getEntity().getProcessDefinitionKey());
    }

    @Bean
    public ProcessRuntimeEventListener<ProcessStartedEvent> ProcessStartedListener() {
        return ProcessStarted -> logger.info(">>> Process Started: '"
                + ProcessStarted.getEntity().getInitiator()
                + "' 它的ProcessDefinitionKey是 : " + ProcessStarted.getEntity().getProcessDefinitionKey());
    }

    @Bean
    public ProcessRuntimeEventListener<ProcessCompletedEvent> ProcessCompletedListener() {
        return ProcessCompleted -> logger.info(">>> Process Completed: '"
                + ProcessCompleted.getEntity().getProcessDefinitionId()
                + "' 它的Initiator是 : " + ProcessCompleted.getEntity().getInitiator());
    }

    @Bean
    public ProcessRuntimeEventListener<ProcessCancelledEvent> ProcessCancelledListener() {
        return ProcessCancelled -> logger.info(">>> Process Cancelled: '"
                + historyService.createHistoricProcessInstanceQuery().processInstanceId(ProcessCancelled.getEntity().getId()).singleResult().getStartUserId()
                + "' 它的ProcessInstanceId是 : " + ProcessCancelled.getEntity().getId());
    }

    @Bean
    public ProcessRuntimeEventListener<ProcessSuspendedEvent> ProcessSuspendedListener() {
        return ProcessSuspended -> logger.info(">>> Process Suspended: '"
                + ProcessSuspended.getEntity().getName()
                + "' 它的ProcessDefinitionKey是 : " + ProcessSuspended.getEntity().getProcessDefinitionKey());
    }

    @Bean
    public ProcessRuntimeEventListener<ProcessResumedEvent> ProcessResumedListener() {
        return ProcessResumed -> logger.info(">>> Process Resumed: '"
                + ProcessResumed.getEntity().getName()
                + "' 它的ProcessDefinitionKey是 : " + ProcessResumed.getEntity().getProcessDefinitionKey());
    }

    @Bean
    public ProcessRuntimeEventListener<SequenceFlowEvent> SequenceFlowListener() {
        return SequenceFlow -> logger.info(">>> Sequence Flow: '"
                + SequenceFlow.getId()
                + "' 它的ProcessDefinitionKey是 : " + SequenceFlow.getProcessDefinitionKey());
    }

    @Bean
    public ProcessRuntimeEventListener<VariableCreatedEvent> VariableCreatedListener() {
        return VariableCreated -> logger.info(">>> Variable Created: '"
                + VariableCreated.getEntity().getName()
                + "' 它的ProcessDefinitionKey是 : " + VariableCreated.getProcessDefinitionKey());
    }

}
