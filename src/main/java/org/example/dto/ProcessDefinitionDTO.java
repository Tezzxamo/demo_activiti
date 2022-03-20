package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * 描述：流程定义DTO
 *
 * @author Tethamo_zzx
 */
@NoArgsConstructor
@AllArgsConstructor
public class ProcessDefinitionDTO {

    /**
     * 流程定义id
     */
    private String processDefinitionId;

    /**
     * 流程定义name
     */
    private String processDefinitionName;

    /**
     * 流程定义激活/挂起的状态
     */
    private Boolean status;

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    public String getProcessDefinitionName() {
        return processDefinitionName;
    }

    public void setProcessDefinitionName(String processDefinitionName) {
        this.processDefinitionName = processDefinitionName;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ProcessDefinitionDTO{" +
                "processDefinitionId='" + processDefinitionId + '\'' +
                ", processDefinitionName='" + processDefinitionName + '\'' +
                ", status=" + status +
                '}';
    }
}
