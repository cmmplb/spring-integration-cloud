package com.cmmplb.eureka.loadbalancer.provider.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author penglibo
 * @date 2021-12-23 14:24:46
 * @since jdk 1.8
 */

@FeignClient("spring-cloud-eureka-provider")
public interface InfoFeign {

    @GetMapping("/info")
    String getInfo();
}
