package com.cmmplb.security.oauth2.starter.service;

/**
 * @author penglibo
 * @date 2022-11-02 15:36:22
 * @since jdk 1.8
 */
public interface SecurityPermissionService {

    /**
     * 判断是否有权限
     *
     * @param permission 权限
     * @return 是否
     */
    boolean hasPermission(String permission);

    /**
     * 判断是否有权限，任一一个即可
     *
     * @param permissions 权限
     * @return 是否
     */
    boolean hasAnyPermissions(String... permissions);

    /**
     * 判断是否有角色
     *
     * 注意，角色使用的是 SysRoleDO 的 code 标识
     *
     * @param role 角色
     * @return 是否
     */
    boolean hasRole(String role);

    /**
     * 判断是否有角色，任一一个即可
     *
     * @param roles 角色数组
     * @return 是否
     */
    boolean hasAnyRoles(String... roles);

    /**
     * 判断是否有授权
     *
     * @param scope 授权
     * @return 是否
     */
    boolean hasScope(String scope);

    /**
     * 判断是否有授权范围，任一一个即可
     *
     * @param scope 授权范围数组
     * @return 是否
     */
    boolean hasAnyScopes(String... scope);
}
