package com.cmmplb.eureka.server.two;

import com.cmmplb.core.utils.SpringApplicationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author penglibo
 * @date 2021-05-13 15:40:56
 * @since jdk 1.8
 */

@SpringBootApplication
@EnableEurekaServer
public class SpringCloudEurekaServerTwoApplication {

    public static void main(String[] args) {
        SpringApplicationUtil.run(SpringCloudEurekaServerTwoApplication.class, args);
    }
}