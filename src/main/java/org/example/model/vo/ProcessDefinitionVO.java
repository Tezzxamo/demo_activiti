package org.example.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 描述：流程定义DTO
 *
 * @author Tethamo_zzx
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ProcessDefinitionVO {

    /**
     * 流程定义id
     */
    private String processDefinitionId;

    /**
     * 流程定义name
     */
    private String processDefinitionName;

    /**
     * 流程定义激活->true/挂起->false的状态
     */
    private Boolean status;


}
