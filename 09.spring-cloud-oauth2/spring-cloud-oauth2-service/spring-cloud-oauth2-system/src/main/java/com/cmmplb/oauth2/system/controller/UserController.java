package com.cmmplb.oauth2.system.controller;

import com.cmmplb.core.result.Result;
import com.cmmplb.core.result.ResultUtil;
import com.cmmplb.oauth2.system.service.UserService;
import com.cmmplb.security.oauth2.starter.annotation.AuthIgnore;
import com.cmmplb.security.oauth2.starter.provider.converter.UserInfoVO;
import com.cmmplb.security.oauth2.starter.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author penglibo
 * @date 2021-11-05 11:22:24
 * @since jdk 1.8
 */

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @AuthIgnore
    // @WithoutLogin
    @GetMapping("/info/{username}")
    public Result<UserInfoVO> getByUsername(@PathVariable String username) {
        return ResultUtil.success(userService.getByUsername(username));
    }

    @AuthIgnore
    @GetMapping("/info/mobile/{mobile}")
    public Result<UserInfoVO> getByMobile(@PathVariable String mobile) {
        return ResultUtil.success(userService.getByMobile(mobile));
    }

    @GetMapping("/info")
    public Result<UserInfoVO> getInfo() {
        return ResultUtil.success(userService.getByUsername(SecurityUtil.getUsername()));
    }
}
