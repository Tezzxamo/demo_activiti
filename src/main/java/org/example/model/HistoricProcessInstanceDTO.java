package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoricProcessInstanceDTO {
    // id
    private String historicProcessInstanceId;

    // name
    private String historicProcessInstanceName;

}
