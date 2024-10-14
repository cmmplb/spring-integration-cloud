
package com.cmmplb.gateway.service;

import io.github.cmmplb.core.utils.SpringApplicationUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author penglibo
 * @date 2024-07-02 16:56:28
 * @since jdk 1.8
 */

@SpringBootApplication
public class ServiceApplication {

    public static void main(String[] args) {
        SpringApplicationUtil.run(ServiceApplication.class, args);
    }

}
