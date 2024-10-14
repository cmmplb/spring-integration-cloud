package com.cmmplb.seata.eureka.service.user;


import io.github.cmmplb.core.utils.SpringApplicationUtil;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author penglibo
 * @date 2021-05-13 15:53:29
 * @since jdk 1.8
 */

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.cmmplb.seata.eureka.service.business.client"})
public class UserApplication {

    public static void main(String[] args) {
        SpringApplicationUtil.run(UserApplication.class, args);
    }
}