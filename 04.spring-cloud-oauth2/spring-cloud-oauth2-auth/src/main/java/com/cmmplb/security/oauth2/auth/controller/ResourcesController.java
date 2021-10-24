package com.cmmplb.security.oauth2.auth.controller;

import com.cmmplb.common.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author penglibo
 * @date 2021-09-03 17:10:50
 * @since jdk 1.8
 */

@RestController
@RequestMapping("/resources")
public class ResourcesController {

    @GetMapping("/authentication")
    public Authentication authentication(Authentication authentication) {
        return authentication;
    }

}
