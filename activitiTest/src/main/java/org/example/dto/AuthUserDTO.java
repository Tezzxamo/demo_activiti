package org.example.dto;

public class AuthUserDTO {
    private String id;

    private String username;

    private String password;

    // 是否锁定
    private Boolean locked;

    // 是否过期
    private Boolean expired;

    public AuthUserDTO(String id, String username, String password, Boolean locked, Boolean expired) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.locked = locked;
        this.expired = expired;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public Boolean getExpired() {
        return expired;
    }

    public void setExpired(Boolean expired) {
        this.expired = expired;
    }
}
