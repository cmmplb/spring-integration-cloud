package com.cmmplb.auth2.auth.controller;

import io.github.cmmplb.core.result.Result;
import io.github.cmmplb.core.result.ResultUtil;
import com.cmmplb.security.oauth2.starter.utils.SmsCodeUtil;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author penglibo
 * @date 2021-09-20 00:08:57
 * @since jdk 1.8
 */

@RestController
@RequestMapping("/basic")
public class BasicController {

    /**
     * 获取短信验证码
     */
    @GetMapping("/sms/code")
    public Result<String> getSmsCode(String mobile) {
        // 发送短信验证码...
        return ResultUtil.success(SmsCodeUtil.create(mobile)); // 这里难得看控制台了，直接返回用来测试看验证码了
    }

    @PostMapping("/map")
    public Result<String> getSmsCode(@RequestBody Map<String, Object> map) {
        System.out.println(map);
        return ResultUtil.success();
    }

}
