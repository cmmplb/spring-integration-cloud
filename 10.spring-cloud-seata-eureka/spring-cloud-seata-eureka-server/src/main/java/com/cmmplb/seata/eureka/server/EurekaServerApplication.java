package com.cmmplb.seata.eureka.server;

import com.cmmplb.core.utils.SpringApplicationUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author penglibo
 * @date 2021-05-13 15:40:56
 * @since jdk 1.8
 */

@EnableEurekaServer
@SpringBootApplication
public class EurekaServerApplication {

    public static void main(String[] args) {
        SpringApplicationUtil.run(EurekaServerApplication.class, args);
    }
}