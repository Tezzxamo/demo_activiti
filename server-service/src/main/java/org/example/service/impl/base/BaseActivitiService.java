//package org.example.service.impl.base;
//
//import lombok.RequiredArgsConstructor;
//import org.activiti.api.process.runtime.ProcessAdminRuntime;
//import org.activiti.api.process.runtime.ProcessRuntime;
//import org.activiti.api.task.runtime.TaskAdminRuntime;
//import org.activiti.api.task.runtime.TaskRuntime;
//import org.activiti.bpmn.model.FormProperty;
//import org.activiti.bpmn.model.UserTask;
//import org.activiti.engine.HistoryService;
//import org.activiti.engine.RepositoryService;
//import org.activiti.engine.RuntimeService;
//import org.activiti.engine.TaskService;
//import org.springframework.stereotype.Controller;
//
//import java.util.List;
//
//
///**
// * 该类旨在描述activiti7中可以使用的service
// *
// * @author Tethamo
// */
//@Controller
//@RequiredArgsConstructor
//public class BaseActivitiService {
//    private final TaskService taskService;
//    private final RuntimeService runtimeService;
//    private final HistoryService historyService;
//    private final RepositoryService repositoryService;
//    /**
//     * ProcessRuntime类内部最终调用repositoryService和runtimeService相关API。 需要ACTIVITI_USER权限
//     */
//    private final ProcessRuntime processRuntime;
//    /**
//     * ProcessRuntime类内部最终调用repositoryService和runtimeService相关API。
//     * 需要ACTIVITI_ADMIN权限
//     */
//    private final ProcessAdminRuntime processAdminRuntime;
//    /**
//     * 类内部调用taskService 需要ACTIVITI_USER权限
//     */
//    private final TaskRuntime taskRuntime;
//    /**
//     * 类内部调用taskService 需要ACTIVITI_ADMIN权限
//     */
//    private final TaskAdminRuntime taskAdminRuntime;
//
//    /**
//     * 功能描述:Activiti7 formService替代方法
//     *
//     * @see [相关类/方法](可选)
//     * @since [产品/模块版本](可选)
//     */
//    List<FormProperty> getFormPropertiesOfOldFormService(String processDefinitionId, String taskDefinitionKey) {
//        // 在Activiti7中，删除了FormService接口，可用以下方法代替
//        UserTask userTask = (UserTask) repositoryService.getBpmnModel(processDefinitionId).getFlowElement(taskDefinitionKey);
//        return userTask.getFormProperties();
//    }
//}