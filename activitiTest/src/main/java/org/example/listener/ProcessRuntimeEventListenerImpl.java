package org.example.listener;

import lombok.extern.slf4j.Slf4j;
import org.activiti.api.model.shared.event.VariableCreatedEvent;
import org.activiti.api.process.model.events.SequenceFlowEvent;
import org.activiti.api.process.runtime.events.*;
import org.activiti.api.process.runtime.events.listener.ProcessRuntimeEventListener;
import org.activiti.engine.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * ProcessRuntimeEvent监听 ———— 记录log日志
 */
@Component
@Slf4j
public class ProcessRuntimeEventListenerImpl {

    @Autowired
    HistoryService historyService;

    @Bean
    public ProcessRuntimeEventListener<ProcessCreatedEvent> ProcessCreatedListener() {
        return ProcessCreated -> log.info(">>> Process Created: '"
                + ProcessCreated.getEntity().getInitiator()
                + "' 它的ProcessDefinitionKey是 : " + ProcessCreated.getEntity().getProcessDefinitionKey());
    }

    @Bean
    public ProcessRuntimeEventListener<ProcessStartedEvent> ProcessStartedListener() {
        return ProcessStarted -> log.info(">>> Process Started: '"
                + ProcessStarted.getEntity().getInitiator()
                + "' 它的ProcessDefinitionKey是 : " + ProcessStarted.getEntity().getProcessDefinitionKey());
    }

    @Bean
    public ProcessRuntimeEventListener<ProcessCompletedEvent> ProcessCompletedListener() {
        return ProcessCompleted -> log.info(">>> Process Completed: '"
                + ProcessCompleted.getEntity().getProcessDefinitionId()
                + "' 它的Initiator是 : " + ProcessCompleted.getEntity().getInitiator());
    }

    @Bean
    public ProcessRuntimeEventListener<ProcessCancelledEvent> ProcessCancelledListener() {
        return ProcessCancelled -> log.info(">>> Process Cancelled: '"
                + historyService.createHistoricProcessInstanceQuery().processInstanceId(ProcessCancelled.getEntity().getId()).singleResult().getStartUserId()
                + "' 它的ProcessInstanceId是 : " + ProcessCancelled.getEntity().getId());
    }

    @Bean
    public ProcessRuntimeEventListener<ProcessSuspendedEvent> ProcessSuspendedListener() {
        return ProcessSuspended -> log.info(">>> Process Suspended: '"
                + ProcessSuspended.getEntity().getName()
                + "' 它的ProcessDefinitionKey是 : " + ProcessSuspended.getEntity().getProcessDefinitionKey());
    }

    @Bean
    public ProcessRuntimeEventListener<ProcessResumedEvent> ProcessResumedListener() {
        return ProcessResumed -> log.info(">>> Process Resumed: '"
                + ProcessResumed.getEntity().getName()
                + "' 它的ProcessDefinitionKey是 : " + ProcessResumed.getEntity().getProcessDefinitionKey());
    }

    @Bean
    public ProcessRuntimeEventListener<SequenceFlowEvent> SequenceFlowListener() {
        return SequenceFlow -> log.info(">>> Sequence Flow: '"
                + SequenceFlow.getId()
                + "' 它的ProcessDefinitionKey是 : " + SequenceFlow.getProcessDefinitionKey());
    }

    @Bean
    public ProcessRuntimeEventListener<VariableCreatedEvent> VariableCreatedListener() {
        return VariableCreated -> log.info(">>> Variable Created: '"
                + VariableCreated.getEntity().getName()
                + "' 它的ProcessDefinitionKey是 : " + VariableCreated.getProcessDefinitionKey());
    }

}
