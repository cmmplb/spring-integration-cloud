package com.cmmplb.eureka.loadbalancer.consumer;

import io.github.cmmplb.core.utils.SpringApplicationUtil;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author penglibo
 * @date 2021-05-13 15:40:56
 * @since jdk 1.8
 */

@EnableFeignClients("com.cmmplb.eureka.loadbalancer.provider")
@SpringBootApplication
public class EurekaConsumerApplication {

    public static void main(String[] args) {
        SpringApplicationUtil.run(EurekaConsumerApplication.class, args);
    }

}