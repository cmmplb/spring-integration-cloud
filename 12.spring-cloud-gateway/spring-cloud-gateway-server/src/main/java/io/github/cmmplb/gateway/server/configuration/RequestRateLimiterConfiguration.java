package io.github.cmmplb.gateway.server.configuration;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import reactor.core.publisher.Mono;

/**
 * @author penglibo
 * @date 2024-08-15 09:51:39
 * @since jdk 1.8
 * 限流规则配置
 */

@Configuration
public class RequestRateLimiterConfiguration {

    /**
     * 限流规则
     */
    @Primary
    @Bean(name = "keyResolver")
    public KeyResolver keyResolver() {
        // 根据请求参数限流
        return exchange -> Mono.just(exchange.getRequest().getQueryParams().getFirst("name"));
        // 根据请求路径限流
        // return exchange -> Mono.just(exchange.getRequest().getPath().value());
    }

    /**
     * 根据ip限流
     */
    @Bean(name = "hostAddrKeyResolver")
    public KeyResolver hostAddrKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getRemoteAddress().getAddress().getHostAddress());
    }
}
