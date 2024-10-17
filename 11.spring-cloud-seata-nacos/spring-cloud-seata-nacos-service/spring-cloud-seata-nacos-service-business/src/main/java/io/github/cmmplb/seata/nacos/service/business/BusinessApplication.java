package io.github.cmmplb.seata.nacos.service.business;


import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author penglibo
 * @date 2021-05-13 15:53:29
 * @since jdk 1.8
 */


@SpringBootApplication(scanBasePackages = "com.cmmplb", exclude = DataSourceAutoConfiguration.class)
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.cmmplb.*.client"})
@MapperScan(basePackages = {"io.github.cmmplb.seata.nacos.service.business.dao"})
public class BusinessApplication {

    private final static Logger log = LoggerFactory.getLogger(BusinessApplication.class);

    public static void main(String[] args) throws UnknownHostException {
        ApplicationContext application = SpringApplication.run(BusinessApplication.class, args);
        Environment env = application.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port");
        String path = env.getProperty("server.servlet.context-path");
        log.info("\n----------------------------------------------------------\n\t"
                + "Application is running! Access URLs:\n\t"
                + "Local: \t\thttp://localhost:" + port + path + "/\n\t"
                + "External: \thttp://" + ip + ":" + port + path + "/\n\t");
    }
}