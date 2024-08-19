package com.cmmplb.gateway.server.filter.global;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author penglibo
 * @date 2024-08-19 15:00:13
 * @since jdk 1.8
 * 最先执行的filter
 * 后面的filter可以取到requestBody数据，也不会影响controller层数据的接收
 * <a href="https://blog.csdn.net/z100871519/article/details/130271068">SpringCloudGateway读取requestBody</a>
 * 适用multipart/form-data和application/json
 * 参照AdaptCachedBodyGlobalFilter向exchange.getAttributes()存放请求参数
 */

@Slf4j
@Component
public class CachedBodyGatewayFilterRelease implements GlobalFilter, Ordered {

    public static final String CACHE_REQUEST_BODY_OBJECT_KEY = "cachedRequestBodyObject";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        HttpMethod method = request.getMethod();
        if (!HttpMethod.POST.equals(method)) {
            return chain.filter(exchange);
        } else {
            return DataBufferUtils.join(exchange.getRequest().getBody()).flatMap(dataBuffer -> {
                DataBufferUtils.retain(dataBuffer);
                Flux<DataBuffer> cachedFlux = Flux.defer(() -> Flux.just(dataBuffer.slice(0, dataBuffer.readableByteCount())));
                ServerHttpRequest requestDecorator = new ServerHttpRequestDecorator(exchange.getRequest()) {
                    @NonNull
                    @Override
                    public Flux<DataBuffer> getBody() {
                        return cachedFlux;
                    }
                };
                exchange.getAttributes().put(CACHE_REQUEST_BODY_OBJECT_KEY, cachedFlux);
                return chain.filter(exchange.mutate().request(requestDecorator).build());
            });
        }
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}