package com.cmmplb.eureka.loadbalancer.consumer.config;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.RandomLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

/**
 * @author penglibo
 * @date 2021-12-23 11:44:19
 * @since jdk 1.8
 * 自定义负载均衡算法
 */

public class LoadBalancerConfiguration {

    @Bean
    ReactorLoadBalancer<ServiceInstance> randomLoadBalancer(Environment env, LoadBalancerClientFactory factory) {
        String name = env.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
        // 随机算法
        return new RandomLoadBalancer(factory.getLazyProvider(name, ServiceInstanceListSupplier.class), name);
        // 自定义
        // return new PeachLoadBalancer(factory.getLazyProvider(name, ServiceInstanceListSupplier.class), name);
    }

    // 这是ribbon之前定义方式
    // public IRule myRule() {
    //         return new RandomRule();
    //     }
}
