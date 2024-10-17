package io.github.cmmplb.seata.eureka.service.api.user.client;


import io.github.cmmplb.seata.eureka.common.constant.ServiceNameConstants;
import io.github.cmmplb.seata.eureka.service.api.user.client.fallback.UserFeignClientFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author penglibo
 * @date 2021-05-07 14:14:02
 * @since jdk 1.8
 */

@FeignClient(contextId = "seata-user", name = ServiceNameConstants.USER_SERVICE, fallbackFactory = UserFeignClientFallBack.class)
public interface UserFeignClient {


    /***
     * 账户余额递减
     * @param username
     * @param money
     */
    @PostMapping(value = "/user/add")
    String decrMoney(@RequestParam(value = "username") String username, @RequestParam(value = "money") int money);
}
