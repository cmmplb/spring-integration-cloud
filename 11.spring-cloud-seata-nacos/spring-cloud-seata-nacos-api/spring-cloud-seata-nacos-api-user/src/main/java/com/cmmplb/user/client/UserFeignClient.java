package com.cmmplb.user.client;


import com.cmmplb.core.result.Result;
import com.cmmplb.user.fallback.UserFeignClientFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author penglibo
 * @date 2021-05-07 14:14:02
 * @since jdk 1.8
 */

@FeignClient(contextId = "seata-user", name = "seata-service-user", path = "/api/user", fallbackFactory = UserFeignClientFallBack.class)
public interface UserFeignClient {


    /***
     * 账户余额递减
     * @param username
     * @param money
     */
    @PostMapping(value = "/user/add")
    Result<String> decrMoney(@RequestParam(value = "username") String username, @RequestParam(value = "money") int money);
}
