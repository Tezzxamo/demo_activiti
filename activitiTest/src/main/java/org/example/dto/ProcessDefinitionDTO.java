package org.example.dto;

public class ProcessDefinitionDTO {

    // 流程定义id
    private String processDefinitionId;

    // 流程定义name
    private String processDefinitionName;

    // 流程定义激活/挂起的状态
    private Boolean status;

    public ProcessDefinitionDTO(String processDefinitionId, String processDefinitionName, Boolean status) {
        this.processDefinitionId = processDefinitionId;
        this.processDefinitionName = processDefinitionName;
        this.status = status;
    }

    public ProcessDefinitionDTO() {

    }

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
}
