package org.example.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户DTO示例
 *
 * @author Tethamo
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthUserDTO {

    private String id;

    private String username;

    private String password;

    /**
     * 是否锁定
     */
    private Boolean locked;

    /**
     * 是否过期
     */
    private Boolean expired;

}
