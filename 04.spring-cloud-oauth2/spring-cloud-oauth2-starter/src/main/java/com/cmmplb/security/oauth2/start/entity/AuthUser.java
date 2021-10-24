package com.cmmplb.security.oauth2.start.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author penglibo
 * @date 2021-09-03 11:48:38
 * @since jdk 1.8
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthUser implements UserDetails {

    private static final long serialVersionUID = -1332147059551881240L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 用户的帐号是否已过期,过期的帐户无法*进行身份验证
     */
    private boolean accountNonExpired = true;

    /**
     * 指示用户是被锁定还是未锁定,无法对锁定的用户进行身份验证。
     */
    private boolean accountNonLocked = true;

    /**
     * 指示用户的凭据（密码）是否已过期。过期的 * 凭据会阻止身份验证。
     */
    private boolean credentialsNonExpired = true;

    /**
     * 指示用户是启用还是禁用。禁用的用户无法验证
     */
    private boolean enabled = true;

    // 授予用户的权限
    private List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

}
