package io.github.cmmplb.seata.eureka.service.user.controller;

import io.github.cmmplb.core.result.Result;
import io.github.cmmplb.core.result.ResultUtil;
import io.github.cmmplb.seata.eureka.service.user.entity.User;
import io.github.cmmplb.seata.eureka.service.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

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

    @PutMapping("/info")
    public Result<Boolean> updateInfo() {
        User user = new User();
        user.setId(1L);
        user.setUpdateTime(new Date());

        userService.updateById(user);

        user.setId(2L);
        user.setUpdateTime(new Date());
        boolean b = userService.updateById(user);

        return ResultUtil.success(b);
    }
}
