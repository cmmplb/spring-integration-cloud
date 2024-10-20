package io.github.cmmplb.seata.eureka.service.business;


import io.github.cmmplb.core.utils.SpringApplicationUtil;
import io.github.cmmplb.seata.eureka.service.business.config.TestConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author penglibo
 * @date 2021-05-13 15:53:29
 * @since jdk 1.8
 */

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.cmmplb.seata.eureka.service.user.client"})
@RibbonClient(name = "microservice-provider-user", configuration = TestConfiguration.class)
public class BusinessApplication {

    public static void main(String[] args) {
        SpringApplicationUtil.run(BusinessApplication.class, args);
    }
}