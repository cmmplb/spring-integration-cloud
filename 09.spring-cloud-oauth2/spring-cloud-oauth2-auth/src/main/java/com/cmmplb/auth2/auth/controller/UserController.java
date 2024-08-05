package com.cmmplb.auth2.auth.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * @author penglibo
 * @date 2021-10-20 15:23:47
 * @since jdk 1.8
 */
@RestController
@RequestMapping("/user")
public class UserController {

    /**
     * 提供user-info-uri端点
     */
    @RequestMapping("/info")
    public Authentication info() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}