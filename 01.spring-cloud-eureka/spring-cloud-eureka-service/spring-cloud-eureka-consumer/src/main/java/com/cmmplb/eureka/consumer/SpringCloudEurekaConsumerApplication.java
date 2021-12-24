package com.cmmplb.eureka.consumer;

import com.cmmplb.core.utils.SpringApplicationUtil;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @author penglibo
 * @date 2021-05-13 15:40:56
 * @since jdk 1.8
 */

@EnableFeignClients("com.cmmplb.eureka.provider")
@SpringBootApplication
public class SpringCloudEurekaConsumerApplication {

    public static void main(String[] args) {
        SpringApplicationUtil.run(SpringCloudEurekaConsumerApplication.class, args);
    }

}