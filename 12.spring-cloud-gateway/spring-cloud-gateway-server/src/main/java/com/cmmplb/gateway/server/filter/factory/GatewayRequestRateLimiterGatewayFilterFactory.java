package com.cmmplb.gateway.server.filter.factory;

import com.alibaba.fastjson.JSON;
import com.cmmplb.core.exception.BusinessException;
import com.cmmplb.core.result.HttpCodeEnum;
import com.cmmplb.core.result.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.RequestRateLimiterGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyResponseBodyGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RateLimiter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author penglibo
 * @date 2024-08-15 15:38:17
 * @since jdk 1.8
 * 全局过滤器GlobalFilter无需配置，默认过滤所有请求。
 * 自定义过滤器AbstractGatewayFilterFactory需在yaml文件中进行配置，否则不生效。
 * spring.cloud.gateway.routes.[].filters.[GatewayRequestRateLimiterGateway]
 */

@Slf4j
@Component
public class GatewayRequestRateLimiterGatewayFilterFactory extends RequestRateLimiterGatewayFilterFactory {

    @Autowired
    private ModifyResponseBodyGatewayFilterFactory modifyResponseBodyGatewayFilterFactory;

    private final RateLimiter<?> defaultRateLimiter;

    private final KeyResolver defaultKeyResolver;

    public GatewayRequestRateLimiterGatewayFilterFactory(RateLimiter<?> defaultRateLimiter, KeyResolver defaultKeyResolver) {
        super(defaultRateLimiter, defaultKeyResolver);
        this.defaultRateLimiter = defaultRateLimiter;
        this.defaultKeyResolver = defaultKeyResolver;
    }

    @Override
    public GatewayFilter apply(Config config) {
        KeyResolver resolver = getOrDefault(config.getKeyResolver(), defaultKeyResolver);
        RateLimiter<Object> limiter = getOrDefault(config.getRateLimiter(), defaultRateLimiter);
        return (exchange, chain) -> resolver.resolve(exchange).flatMap(key -> {
            String routeId = config.getRouteId();
            if (routeId == null) {
                Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
                routeId = route.getId();
            }
            String finalRouteId = routeId;
            return limiter.isAllowed(routeId, key).flatMap(response -> {
                for (Map.Entry<String, String> header : response.getHeaders().entrySet()) {
                    exchange.getResponse().getHeaders().add(header.getKey(), header.getValue());
                }
                if (response.isAllowed()) {
                    return chain.filter(exchange);
                }
                log.warn("已限流: {}", finalRouteId);

                // 这个是当错误抛出
                return Mono.error(new BusinessException(HttpCodeEnum.TOO_MANY_REQUESTS));

                // 这个不会路由到下游服务，但是后续的响应过滤器还会执行
                // 参照org.springframework.cloud.gateway.filter.factory.rewrite.ModifyRequestBodyGatewayFilterFactory
                // Mono modifiedBody = ResponseUtil.buildResponse(exchange.getResponse(), HttpCodeEnum.TOO_MANY_REQUESTS);
                // BodyInserter<Mono, ReactiveHttpOutputMessage> bodyInserter = BodyInserters.fromPublisher(modifiedBody, Object.class);
                // HttpHeaders headers = new HttpHeaders();
                // headers.putAll(exchange.getRequest().getHeaders());
                // headers.remove(HttpHeaders.CONTENT_LENGTH);
                //
                // headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
                //
                // CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(exchange, headers);
                // return bodyInserter.insert(outputMessage, new BodyInserterContext())
                //         .then(Mono.defer(() -> {
                //             // 报文转换
                //             ServerHttpRequest decorator = decorate(exchange, headers, outputMessage);
                //             return chain.filter(exchange.mutate().request(decorator).build());
                //         }));
            });
        });
    }

    private static ServerHttpRequestDecorator decorate(ServerWebExchange exchange, HttpHeaders headers, CachedBodyOutputMessage outputMessage) {
        return new ServerHttpRequestDecorator(exchange.getRequest()) {
            @Override
            public HttpHeaders getHeaders() {
                long contentLength = headers.getContentLength();
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.putAll(super.getHeaders());
                if (contentLength > 0) {
                    httpHeaders.setContentLength(contentLength);
                } else {
                    httpHeaders.set(HttpHeaders.TRANSFER_ENCODING, "chunked");
                }
                return httpHeaders;
            }

            @Override
            public Flux<DataBuffer> getBody() {
                return outputMessage.getBody();
            }
        };
    }

    private <T> T getOrDefault(T configValue, T defaultValue) {
        return (configValue != null) ? configValue : defaultValue;
    }
}
