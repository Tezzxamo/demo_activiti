package org.example.config;

import org.example.dto.AuthUserDTO;
import org.example.dto.UserDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collection;

//@Configuration
public class UserAuthConfig {

    // FIXME: 2020/12/16 ： 需要添加获取用户的manager层的注入，以及获取用户的角色列表的manager层的注
    //  入,需要依据所处系统的自己的实现来对此处进行实现，即：留待扩展

    //    @Bean
    //    @Primary
    public UserDetailsService authUserDetailsService() {
        return username -> {
            // 从数据库中查询 fixme
            AuthUserDTO authUserDTO = null;
            return new UserDetails() {
                // 返回分配给用户的角色列表 fixme ：如   "GROUP_" + roleName  [示例：GROUP_activitiTeam]
                // FIXME: 2020/12/16 :
                //      具体实现方法举例：获取用户所在的审批组，通过审批组获取
                @Override
                public Collection<? extends GrantedAuthority> getAuthorities() {
                    return null;
                }

                @Override
                public String getPassword() {
                    return authUserDTO.getPassword();
                }

                @Override
                public String getUsername() {
                    return authUserDTO.getUsername();
                }

                // 账户是否未过期
                @Override
                public boolean isAccountNonExpired() {
                    return authUserDTO.getExpired();
                }

                // 账户是否未锁定
                @Override
                public boolean isAccountNonLocked() {
                    return authUserDTO.getLocked();
                }

                // 密码是否未过期
                @Override
                public boolean isCredentialsNonExpired() {
                    return authUserDTO.getExpired();
                }

                // 账户是否激活
                @Override
                public boolean isEnabled() {
                    return authUserDTO.getLocked();
                }
            };
        };
    }
}
