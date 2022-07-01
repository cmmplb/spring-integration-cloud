package com.cmmplb.seata.eureka.api.business.client.fallback;


import com.cmmplb.seata.eureka.api.business.client.BusinessFeignClient;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * @author penglibo
 * @date 2021-05-07 14:15:24
 * @since jdk 1.8
 */

public class BusinessFeignClientFallBack implements FallbackFactory<BusinessFeignClient> {

    @Override
    public BusinessFeignClient create(Throwable throwable) {
        return new BusinessFeignClient() {

        };
    }
}
