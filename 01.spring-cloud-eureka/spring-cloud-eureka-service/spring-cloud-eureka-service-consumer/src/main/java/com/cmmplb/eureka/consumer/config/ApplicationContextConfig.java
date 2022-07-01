package com.cmmplb.eureka.consumer.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author penglibo
 * @date 2021-12-23 11:38:58
 * @since jdk 1.8
 * LoadBalancerClients：配置自定义LoadBalancer策略 如果扩展算法 需要实现ReactorServiceInstanceLoadBalancer接口
 * LoadBalancerClient：name属性需要和eureka页面中的服务提供者名字,大写
 */

@Configuration
// @LoadBalancerClients
@LoadBalancerClient(name = "SPRING-CLOUD-EUREKA-PROVIDER", configuration = LoadBalancerConfiguration.class)
public class ApplicationContextConfig {

    @Bean
    @LoadBalanced // 使用这个注解给restTemplate赋予了负载均衡的能力
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
