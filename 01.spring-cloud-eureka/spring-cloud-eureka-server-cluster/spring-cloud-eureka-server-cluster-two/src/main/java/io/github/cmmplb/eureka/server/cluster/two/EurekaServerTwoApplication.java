package io.github.cmmplb.eureka.server.cluster.two;

import io.github.cmmplb.core.utils.SpringApplicationUtil;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author penglibo
 * @date 2021-05-13 15:40:56
 * @since jdk 1.8
 */

@SpringBootApplication
@EnableEurekaServer
public class EurekaServerTwoApplication {

    public static void main(String[] args) {
        SpringApplicationUtil.run(EurekaServerTwoApplication.class, args);
    }
}