package org.example.dto;

import java.util.Collection;

public class UserDTO {

    // 用户名称
    private String userName;

    // 用户角色列表
    private Collection<String> roles;

    public UserDTO(String userName, Collection<String> roles) {
        this.userName = userName;
        this.roles = roles;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Collection<String> getRoles() {
        return roles;
    }

    public void setRoles(Collection<String> roles) {
        this.roles = roles;
    }
}
