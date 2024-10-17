package io.github.cmmplb.eureka.server.cluster.three;

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
public class EurekaServerThreeApplication {

    public static void main(String[] args) {
        SpringApplicationUtil.run(EurekaServerThreeApplication.class, args);
    }
}