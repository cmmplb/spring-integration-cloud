package com.cmmplb.eureka.server.two;

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

@Slf4j
@SpringBootApplication
@EnableEurekaServer
public class SpringCloudEurekaServerTwoApplication {

    public static void main(String[] args) throws UnknownHostException {
        ApplicationContext application = SpringApplication.run(SpringCloudEurekaServerTwoApplication.class, args);
        Environment env = application.getEnvironment();
        String port = env.getProperty("server.port");
        log.info("\n----------------------------------------------------------\n\t" +
                        "Application is running! Access URLs:\n\t" +
                        "Local: \t\thttp://localhost:{}\n\t" +
                        "External: \thttp://{}:{}\n\t" +
                        "----------------------------------------------------------",
                port, // Local
                InetAddress.getLocalHost().getHostAddress(), port); // External
    }
}