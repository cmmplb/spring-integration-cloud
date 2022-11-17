package com.cmmplb.security.oauth2.starter.service.impl;

import com.cmmplb.core.utils.StringUtil;
import com.cmmplb.security.oauth2.starter.provider.converter.User;
import com.cmmplb.security.oauth2.starter.service.SecurityPermissionService;
import com.cmmplb.security.oauth2.starter.utils.SecurityUtil;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.CollectionUtils;
import org.springframework.util.PatternMatchUtils;

import java.util.Collection;

/**
 * @author penglibo
 * @date 2021-10-20 11:27:10
 * @since jdk 1.8
 * 使用 Spring Security 的缩写，方便使用
 */

public class SecurityPermissionServiceImpl implements SecurityPermissionService {

    /**
     * 所有权限标识
     */
    private static final String ALL_PERMISSION = "*:*:*";

    /**
     * 管理员角色权限标识
     */
    private static final String SUPER_ADMIN = "admin";

    private static final String ROLE_DELIMETER = ",";

    private static final String PERMISSION_DELIMETER = ",";

    /**
     * 验证用户是否具备某权限
     * @param permission 权限字符串
     * @return 用户是否具备某权限
     */
    @Override
    public boolean hasPermission(String permission) {
        if (StringUtil.isEmpty(permission)) {
            return false;
        }
        User user = SecurityUtil.getUser();
        if (StringUtil.isEmpty(user) || CollectionUtils.isEmpty(user.getAuthorities())) {
            return false;
        }
        return hasPermissions(user.getAuthorities(), permission);
    }

    /**
     * 验证用户是否不具备某权限，与 hasPermi逻辑相反
     * @param permission 权限字符串
     * @return 用户是否不具备某权限
     */
    public boolean lacksPer(String permission) {
        return !hasPermission(permission);
    }

    /**
     * 验证用户是否具有以下任意一个权限
     * @param permissions 以 PERMISSION_NAMES_DELIMETER 为分隔符的权限列表
     * @return 用户是否具有以下任意一个权限
     */
    @Override
    public boolean hasAnyPermissions(String... permissions) {
        // 如果为空，说明已经有权限
        if (StringUtil.isEmpty(permissions)) {
            return false;
        }
        User user = SecurityUtil.getUser();
        if (StringUtil.isEmpty(user) || CollectionUtils.isEmpty(user.getAuthorities())) {
            return false;
        }
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        // 遍历权限，判断是否有一个满足
        for (String permission : permissions) {
            if (permission != null && hasPermissions(authorities, permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断用户是否拥有某个角色
     * @param role 角色字符串
     * @return 用户是否具备某角色
     */
    @Override
    public boolean hasRole(String role) {
        if (StringUtil.isEmpty(role)) {
            return false;
        }
        User user = SecurityUtil.getUser();
        if (StringUtil.isEmpty(user) || CollectionUtils.isEmpty(user.getAuthorities())) {
            return false;
        }
        for (GrantedAuthority authorities : user.getAuthorities()) {
            String roleKey = authorities.getAuthority();
            if (SUPER_ADMIN.contains(roleKey) || roleKey.contains(role)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasAnyRoles(String... roles) {
        return false;
    }

    @Override
    public boolean hasScope(String scope) {
        return false;
    }

    @Override
    public boolean hasAnyScopes(String... scope) {
        return false;
    }

    /**
     * 验证用户是否不具备某角色，与 isRole逻辑相反。
     * @param role 角色名称
     * @return 用户是否不具备某角色
     */
    public boolean lacksRole(String role) {
        return !hasRole(role);
    }

    /**
     * 验证用户是否具有以下任意一个角色
     * @param roles 以 ROLE_NAMES_DELIMETER 为分隔符的角色列表
     * @return 用户是否具有以下任意一个角色
     */
    public boolean hasAnyRoles(String roles) {
        if (StringUtil.isEmpty(roles)) {
            return false;
        }
        User user = SecurityUtil.getUser();
        if (StringUtil.isEmpty(user) || CollectionUtils.isEmpty(user.getAuthorities())) {
            return false;
        }
        for (String role : roles.split(ROLE_DELIMETER)) {
            if (hasRole(role)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否包含权限
     * @param authorities 权限列表
     * @param permission  权限字符串
     * @return 用户是否具备某权限
     */
    private boolean hasPermissions(Collection<? extends GrantedAuthority> authorities, String permission) {
        return authorities.stream().map(GrantedAuthority::getAuthority).filter(StringUtil::hasText)
                .anyMatch(x -> ALL_PERMISSION.contains(x) || PatternMatchUtils.simpleMatch(permission, x));
    }
}
