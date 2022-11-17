package com.cmmplb.auth2.auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * @author penglibo
 * @date 2021-10-20 15:23:47
 * @since jdk 1.8
 */
@RestController
@RequestMapping("/oauth")
public class UserController {
    @RequestMapping("/user")
    public Principal user(Principal user) {
        return user;
    }
}