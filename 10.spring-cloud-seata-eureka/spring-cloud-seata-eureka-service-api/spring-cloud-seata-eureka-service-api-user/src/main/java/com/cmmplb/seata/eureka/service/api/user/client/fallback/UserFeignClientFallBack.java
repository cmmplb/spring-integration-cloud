package com.cmmplb.seata.eureka.service.api.user.client.fallback;

import com.cmmplb.seata.eureka.service.api.user.client.UserFeignClient;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * @author penglibo
 * @date 2021-05-07 14:15:24
 * @since jdk 1.8
 */

public class UserFeignClientFallBack implements FallbackFactory<UserFeignClient> {

    @Override
    public UserFeignClient create(Throwable throwable) {
        return new UserFeignClient() {

            @Override
            public String decrMoney(String username, int money) {
                return throwable.getMessage();
            }
        };
    }
}
