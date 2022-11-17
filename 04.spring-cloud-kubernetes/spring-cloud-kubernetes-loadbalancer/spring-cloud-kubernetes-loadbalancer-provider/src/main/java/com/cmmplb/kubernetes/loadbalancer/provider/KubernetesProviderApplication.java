package com.cmmplb.kubernetes.loadbalancer.provider;

import com.cmmplb.core.utils.SpringApplicationUtil;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author penglibo
 * @date 2021-05-13 15:40:56
 * @since jdk 1.8
 */

@EnableDiscoveryClient
@SpringBootApplication
public class KubernetesProviderApplication {

    public static void main(String[] args) {
        SpringApplicationUtil.run(KubernetesProviderApplication.class, args);
    }

}