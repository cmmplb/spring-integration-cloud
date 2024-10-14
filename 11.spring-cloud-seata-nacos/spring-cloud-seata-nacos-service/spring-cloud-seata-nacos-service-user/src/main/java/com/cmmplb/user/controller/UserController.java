package com.cmmplb.user.controller;

import io.github.cmmplb.core.result.Result;
import io.github.cmmplb.core.result.ResultUtil;
import com.cmmplb.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author penglibo
 * @date 2021-05-13 15:53:15
 * @since jdk 1.8
 */

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /***
     * 账户余额递减
     * @param username
     * @param money
     */
    @PostMapping(value = "/add")
    public Result<String> decrMoney(@RequestParam(value = "username") String username, @RequestParam(value = "money") int money) {
        userService.decrMoney(username, money);
        return ResultUtil.success("success");
    }

}