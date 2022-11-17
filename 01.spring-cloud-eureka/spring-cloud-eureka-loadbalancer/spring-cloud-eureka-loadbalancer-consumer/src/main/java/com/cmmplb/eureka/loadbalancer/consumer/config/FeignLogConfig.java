package com.cmmplb.eureka.loadbalancer.consumer.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author penglibo
 * @date 2021-12-23 14:50:22
 * @since jdk 1.8
 * openfeign=Logger有四种类型：NONE(默认)、BASIC、HEADERS、FULL，通过注册Bean来设置日志记录级别
 */

@Configuration
public class FeignLogConfig {

    /**
     * 注：这里的logger是feign包里的
     */
    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
