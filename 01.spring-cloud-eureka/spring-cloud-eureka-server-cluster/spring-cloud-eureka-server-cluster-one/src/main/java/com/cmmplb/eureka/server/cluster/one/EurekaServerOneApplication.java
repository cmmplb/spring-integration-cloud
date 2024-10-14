package com.cmmplb.eureka.server.cluster.one;

import io.github.cmmplb.core.utils.SpringApplicationUtil;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author penglibo
 * @date 2021-05-13 15:40:56
 * @since jdk 1.8
 */

@EnableEurekaServer
@SpringBootApplication
public class EurekaServerOneApplication {

    public static void main(String[] args) {
        SpringApplicationUtil.run(EurekaServerOneApplication.class, args);
    }
}