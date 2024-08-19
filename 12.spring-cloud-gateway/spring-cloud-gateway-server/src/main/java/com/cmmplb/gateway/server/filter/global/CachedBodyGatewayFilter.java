package com.cmmplb.gateway.server.filter.global;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.event.EnableBodyCachingEvent;
import org.springframework.cloud.gateway.filter.AdaptCachedBodyGlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.RetryGatewayFilterFactory;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.retry.Repeat;
import reactor.retry.Retry;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR;

/**
 * @author penglibo
 * @date 2024-08-19 15:18:42
 * @since jdk 1.8
 * <a href="https://www.cnblogs.com/robot-100/p/14052105.html">spring gateway 获取RequestBody</a>
 * 如果要使用gateway自带的ServerWebExchangeUtils.CACHED_REQUEST_BODY_ATTR,需要发布一个事件
 * {@link ServerWebExchangeUtils#decorate(ServerWebExchange, DataBuffer, boolean)}
 * exchange.getAttributes().put(CACHED_REQUEST_BODY_ATTR, dataBuffer);
 * 在{@link RetryGatewayFilterFactory#apply(String, Repeat, Retry)}
 * 能找到发布事件的源码:
 * getPublisher().publishEvent(new EnableBodyCachingEvent(this, routeId));
 */

@Component
public class CachedBodyGatewayFilter implements GlobalFilter, Ordered {

    @Autowired
    private AdaptCachedBodyGlobalFilter adaptCachedBodyGlobalFilter;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        Route route = exchange.getAttribute(GATEWAY_ROUTE_ATTR);
        if (route != null) {
            EnableBodyCachingEvent enableBodyCachingEvent = new EnableBodyCachingEvent(new Object(), route.getId());
            // AdaptCachedBodyGlobalFilter类实现了接口ApplicationListener事件监听
            adaptCachedBodyGlobalFilter.onApplicationEvent(enableBodyCachingEvent);
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
