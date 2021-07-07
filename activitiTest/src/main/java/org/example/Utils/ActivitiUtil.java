package org.example.Utils;

import org.activiti.engine.*;

/**
 * 用于获取流程引擎
 */
public class ActivitiUtil {

    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    private static ActivitiUtil activitiUtil;

    RepositoryService repositoryService = processEngine.getRepositoryService();
    TaskService taskService = processEngine.getTaskService();
    RuntimeService runtimeService = processEngine.getRuntimeService();
    HistoryService historyService = processEngine.getHistoryService();

    public static ActivitiUtil instance() {
        if (null == activitiUtil) {
            activitiUtil = new ActivitiUtil();
        }
        return activitiUtil;
    }

    public RepositoryService getRepositoryService() {
        return repositoryService;
    }

    public void setRepositoryService(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    public TaskService getTaskService() {
        return taskService;
    }

    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    public RuntimeService getRuntimeService() {
        return runtimeService;
    }

    public void setRuntimeService(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }

    public HistoryService getHistoryService() {
        return historyService;
    }

    public void setHistoryService(HistoryService historyService) {
        this.historyService = historyService;
    }
}
