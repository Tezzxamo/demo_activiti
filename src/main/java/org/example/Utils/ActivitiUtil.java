package org.example.Utils;

import lombok.Data;
import org.activiti.engine.*;

/**
 * 用于获取流程引擎
 *
 * @author Tethamo
 */
@Data
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

}
