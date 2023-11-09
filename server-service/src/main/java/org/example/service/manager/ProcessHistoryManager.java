package org.example.service.manager;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.example.common.utils.ActivitiUtil;
import org.example.common.utils.VerificationUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class ProcessHistoryManager {

    /**
     * Desc: 通过流程实例ID获取历史流程实例
     *
     * @param processInstanceId 流程实例Id
     * @return 历史流程实例
     */
    public HistoricProcessInstance getHistoricProcessInstance(String processInstanceId) {
        return VerificationUtils.checkHistoricProcessInstanceById(processInstanceId);
    }

    /**
     * Desc: 通过流程实例ID获取流程中已经执行的结点，按照执行先后顺序排序
     *
     * @param processInstanceId 流程实例Id
     * @return 已经执行的节点
     */
    public List<HistoricActivityInstance> getHistoricActivityInstancesAsc(String processInstanceId) {
        return ActivitiUtil.instance().getHistoryService().createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId)
                .orderByHistoricActivityInstanceId()
                .asc()
                .list();
    }

    /**
     * Desc: 通过流程实例ID获取已经完成的历史流程实例
     *
     * @param processInstanceId 流程实例ID
     * @return 已经完成的历史流程实例
     */
    public List<HistoricProcessInstance> getHistoricFinishedProcessInstance(String processInstanceId) {
        return ActivitiUtil.instance().getHistoryService().createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .finished()
                .list();
    }


}
