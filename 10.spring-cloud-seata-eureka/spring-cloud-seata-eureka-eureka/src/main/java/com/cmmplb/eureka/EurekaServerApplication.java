package com.cmmplb.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author penglibo
 * @date 2021-05-13 15:40:56
 * @since jdk 1.8
 */

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class}) //不加载数据库
@EnableEurekaServer //开启Eureka服务
public class EurekaServerApplication {

    public static void main(String[] args) {
        
        SpringApplication.run(EurekaServerApplication.class, args);
    }
}