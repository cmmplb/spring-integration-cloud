package com.cmmplb.apollo.client;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author penglibo
 * @date 2021-05-13 15:40:56
 * @since jdk 1.8
 */

@Slf4j
@RestController
@EnableApolloConfig // 默认读取的是application.properties 与yml读取冲突后读取了properties
@SpringBootApplication
@EnableDiscoveryClient
public class SpringCloudApolloClientApplication {

    public static void main(String[] args) throws UnknownHostException {
        ApplicationContext application = SpringApplication.run(SpringCloudApolloClientApplication.class, args);
        Environment env = application.getEnvironment();
        String port = env.getProperty("server.port");
        String path = env.getProperty("server.servlet.context-path");
        log.info("\n----------------------------------------------------------\n\t" +
                        "Application is running! Access URLs:\n\t" +
                        "Local: \t\thttp://localhost:{}{}\n\t" +
                        "External: \thttp://{}:{}{}\n\t" +
                        "----------------------------------------------------------",
                port, path, // Local
                InetAddress.getLocalHost().getHostAddress(), port, path); // External
    }

    @Autowired
    private Environment env;

    @RequestMapping("/")
    public String index() {
        log.debug("debug 打印出的日志");
        log.info("info 打印出的日志");
        log.error("error 打印出的日志");
        return "hello spring boot " + env.getProperty("name");
    }
}