package com.cmmplb.security.oauth2.starter.service;

import org.springframework.stereotype.Service;

/**
 * @author penglibo
 * @date 2022-11-02 15:36:22
 * @since jdk 1.8
 */
@Service("ps")
public interface SecurityPermissionService {

    /**
     * 验证用户是否具备某权限
     * @param permission 权限字符串
     * @return 用户是否具备某权限
     */
    boolean hasPer(String permission);

    /**
     * 验证用户是否不具备某权限，与 hasPer逻辑相反
     * @param permission 权限字符串
     * @return 用户是否不具备某权限
     */
    boolean lacksPer(String permission);

    /**
     * 验证用户是否具有以下任意一个权限
     * @param permissions 以 PERMISSION_NAMES_DELIMETER 为分隔符的权限列表
     * @return 用户是否具有以下任意一个权限
     */
    boolean hasAnyPer(String permissions);

    /**
     * 判断用户是否拥有某个角色
     * @param role 角色字符串
     * @return 用户是否具备某角色
     */
    boolean hasRole(String role);

    /**
     * 验证用户是否不具备某角色，与 isRole逻辑相反。
     * @param role 角色名称
     * @return 用户是否不具备某角色
     */
    boolean lacksRole(String role);

    /**
     * 验证用户是否具有以下任意一个角色
     * @param roles 以 ROLE_NAMES_DELIMETER 为分隔符的角色列表
     * @return 用户是否具有以下任意一个角色
     */
    boolean hasAnyRoles(String roles);

}
