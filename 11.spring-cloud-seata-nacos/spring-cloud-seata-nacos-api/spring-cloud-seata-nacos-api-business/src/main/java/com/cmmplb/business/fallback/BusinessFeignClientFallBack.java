package com.cmmplb.business.fallback;


import com.cmmplb.business.client.BusinessFeignClient;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author penglibo
 * @date 2021-05-07 14:15:24
 * @since jdk 1.8
 */

@Component
public class BusinessFeignClientFallBack implements FallbackFactory<BusinessFeignClient> {

    @Override
    public BusinessFeignClient create(Throwable throwable) {
        return new BusinessFeignClient() {

        };
    }
}
