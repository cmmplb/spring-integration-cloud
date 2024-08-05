package com.cmmplb.security.oauth2.starter.utils;

import com.cmmplb.security.oauth2.starter.converter.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author penglibo
 * @date 2021-11-22 10:14:45
 * @since jdk 1.8
 */
public class SecurityUtil {

    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 获取用户账号
     */
    public static String getUsername() {
        if (null != getUser()) {
            return getUser().getUsername();
        }
        return "";
    }


    /**
     * 获取用户id,设置一个-999为系统用于不需要登陆操作
     */
    public static Long getUserId() {
        if (null != getUser()) {
            return getUser().getId();
        }
        return -999L;
    }

    /**
     * 获取用户
     */
    public static User getUser(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof User) {
            return (User) principal;
        }
        return null;
    }

    /**
     * 获取用户
     */
    public static User getUser() {
        Authentication authentication = getAuthentication();
        if (authentication == null) {
            return null;
        }
        return getUser(authentication);
    }

    /**
     * 是否为管理员
     * @param userId 用户ID
     * @return 结果
     */
    public static boolean isAdmin(Long userId) {
        return userId != null && 1L == userId;
    }

    /**
     * 是否为管理员
     * @return 结果
     */
    public static boolean isAdmin() {
        return getUserId() != null && 1L == getUserId();
    }
}
