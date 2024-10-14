package com.cmmplb.oauth2.client.sso.controller;

import io.github.cmmplb.core.result.Result;
import io.github.cmmplb.core.result.ResultUtil;
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

    /**
     * 不需要认证的接口
     * @return
     */
    @GetMapping("/not/authentication")
    public Result<Authentication> notAuthentication() {
        return ResultUtil.success();
    }

    /**
     * 获取authentication
     * @param authentication
     * @return
     */
    @GetMapping("/authentication")
    public Result<Authentication> authentication(Authentication authentication) {
        return ResultUtil.success(authentication);
    }

}
