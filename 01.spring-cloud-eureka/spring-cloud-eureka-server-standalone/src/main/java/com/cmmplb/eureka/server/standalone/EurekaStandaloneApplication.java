package com.cmmplb.eureka.server.standalone;


import com.cmmplb.core.utils.SpringApplicationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author penglibo
 * @date 2021-05-13 15:40:56
 * @since jdk 1.8
 */

@Slf4j
@EnableEurekaServer
@SpringBootApplication
public class EurekaStandaloneApplication {

    public static void main(String[] args) {
        SpringApplicationUtil.run(EurekaStandaloneApplication.class, args);
    }

}