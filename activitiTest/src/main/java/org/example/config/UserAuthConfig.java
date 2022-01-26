package org.example.config;

import lombok.RequiredArgsConstructor;
import org.example.dto.AuthUserDTO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collection;
import java.util.Collections;

/**
 * 这个才是实际上activiti用户权限控制所需要的配置类，但是具体实现需要根据自己的系统去实现
 *
 * @author Tethamo
 */
//@Configuration
@RequiredArgsConstructor
public class UserAuthConfig {

    // FIXME: 2020/12/16 ：
    //  注入service/manager，如：private final UserService userService;
    //  userService中需要有获取到实际用户的方法，参考DTO为：AuthUserDTO
    //  即：留待扩展

    //    @Bean
    //    @Primary
    public UserDetailsService authUserDetailsService() {
        return username -> {
            // fixme：使用实际查询出来的用户
            AuthUserDTO authUserDTO = null;
            return new UserDetails() {
                // 返回分配给用户的角色列表 fixme ：如   "GROUP_" + roleName  [示例：GROUP_activitiTeam]
                // FIXME: 2020/12/16 :
                //      具体实现方法举例：获取用户所在的审批组，通过审批组获取，参考DemoApplicationConfiguration
                @Override
                public Collection<? extends GrantedAuthority> getAuthorities() {
                    return Collections.emptyList();
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
