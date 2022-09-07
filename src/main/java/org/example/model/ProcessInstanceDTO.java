package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessInstanceDTO {

    private String processInstanceId;
    private String processDefinitionName;
    private String processDefinitionId;
    private String startTime;
    private UserDTO initiator;

}
