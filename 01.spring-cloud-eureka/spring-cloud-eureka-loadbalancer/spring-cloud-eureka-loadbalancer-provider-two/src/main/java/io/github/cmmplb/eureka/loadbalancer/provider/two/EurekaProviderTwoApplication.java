package io.github.cmmplb.eureka.loadbalancer.provider.two;

import io.github.cmmplb.core.utils.SpringApplicationUtil;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author penglibo
 * @date 2021-05-13 15:40:56
 * @since jdk 1.8
 */

@SpringBootApplication
public class EurekaProviderTwoApplication {

    public static void main(String[] args) {
        SpringApplicationUtil.run(EurekaProviderTwoApplication.class, args);
    }

}