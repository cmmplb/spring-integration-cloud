package com.cmmplb.security.oauth2.starter.impl;

import com.cmmplb.core.constants.StringConstant;
import com.cmmplb.core.utils.StringUtil;
import com.cmmplb.security.oauth2.starter.converter.User;
import com.cmmplb.security.oauth2.starter.service.SecurityPermissionService;
import com.cmmplb.security.oauth2.starter.utils.SecurityUtil;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.PatternMatchUtils;

import java.util.Collection;

/**
 * @author penglibo
 * @date 2021-10-20 11:27:10
 * @since jdk 1.8
 * 使用 Spring Security 的缩写，方便使用
 */

@Service("ps")
public class SecurityPermissionServiceImpl implements SecurityPermissionService {

    /**
     * 所有权限标识
     */
    private static final String ALL_PERMISSION = "*:*:*";

    /**
     * 管理员角色权限标识
     */
    private static final String SUPER_ADMIN = "admin";

    /**
     * 验证用户是否具备某权限
     * @param permission 权限字符串
     * @return 用户是否具备某权限
     */
    @Override
    public boolean hasPer(String permission) {
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
     * 验证用户是否不具备某权限，与 hasPer逻辑相反
     * @param permission 权限字符串
     * @return 用户是否不具备某权限
     */
    @Override
    public boolean lacksPer(String permission) {
        return !hasPer(permission);
    }

    /**
     * 验证用户是否具有以下任意一个权限
     * @param permissions 以 PERMISSION_NAMES_DELIMETER 为分隔符的权限列表
     * @return 用户是否具有以下任意一个权限
     */
    @Override
    public boolean hasAnyPer(String permissions) {
        if (StringUtil.isEmpty(permissions)) {
            return false;
        }
        User user = SecurityUtil.getUser();
        if (StringUtil.isEmpty(user) || CollectionUtils.isEmpty(user.getAuthorities())) {
            return false;
        }
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        for (String permission : permissions.split(StringConstant.COMMA)) {
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

    /**
     * 验证用户是否不具备某角色，与 isRole逻辑相反。
     * @param role 角色名称
     * @return 用户是否不具备某角色
     */
    @Override
    public boolean lacksRole(String role) {
        return !hasRole(role);
    }

    /**
     * 验证用户是否具有以下任意一个角色
     * @param roles 以 ROLE_NAMES_DELIMETER 为分隔符的角色列表
     * @return 用户是否具有以下任意一个角色
     */
    @Override
    public boolean hasAnyRoles(String roles) {
        if (StringUtil.isEmpty(roles)) {
            return false;
        }
        User user = SecurityUtil.getUser();
        if (StringUtil.isEmpty(user) || CollectionUtils.isEmpty(user.getAuthorities())) {
            return false;
        }
        for (String role : roles.split(StringConstant.COMMA)) {
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
