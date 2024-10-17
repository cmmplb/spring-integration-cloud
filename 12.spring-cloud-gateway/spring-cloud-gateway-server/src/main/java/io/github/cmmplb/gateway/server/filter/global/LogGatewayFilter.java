package io.github.cmmplb.gateway.server.filter.global;

import io.github.cmmplb.core.utils.IpUtil;
import io.github.cmmplb.gateway.server.utils.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.NettyWriteResponseFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * @author penglibo
 * @date 2022-08-26 10:30:07
 * @since jdk 1.8
 * 全局过滤器GlobalFilter无需配置，默认过滤所有请求。
 * 自定义过滤器AbstractGatewayFilterFactory需在yaml文件中进行配置，否则不生效。
 */
@Slf4j
@Component
public class LogGatewayFilter implements GlobalFilter, Ordered {

    private static final String START_TIME = "startTime";

    private static final String BODY = "body";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        exchange.getAttributes().put(START_TIME, System.currentTimeMillis());
        return chain.filter(exchange.mutate().response(getResponseDecorator(exchange)).build()).then(log(exchange));
    }

    private static ServerHttpResponseDecorator getResponseDecorator(ServerWebExchange exchange) {
        return new ServerHttpResponseDecorator(exchange.getResponse()) {
            @NonNull
            @Override
            public Mono<Void> writeWith(@NonNull Publisher<? extends DataBuffer> body) {
                if (body instanceof Flux) {
                    // MediaType mediaType = headers.getContentType();
                    // 过滤上传附件请求
                    // if ((mediaType != null && mediaType.equals(MediaType.MULTIPART_FORM_DATA))
                    //         || (mediaType != null && mediaType.equals(MediaType.APPLICATION_FORM_URLENCODED))) {
                    //     return super.writeWith(body);
                    // }
                    // 如果响应类型为json，则读取响应体打印, ObjectUtil.equal(this.getStatusCode(), HttpStatus.OK) &&
                    // if (StringUtil.isNotBlank(originalResponseContentType) && originalResponseContentType.contains("application/json")) {
                    Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                    return super.writeWith(fluxBody.buffer().map(dataBuffers -> {

                        // 合并多个流集合，解决返回体分段传输
                        // DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
                        DataBufferFactory dataBufferFactory = this.getDelegate().bufferFactory();
                        DataBuffer join = dataBufferFactory.join(dataBuffers);
                        byte[] content = new byte[join.readableByteCount()];
                        join.read(content);

                        // 释放掉内存
                        DataBufferUtils.release(join);
                        String responseResult = new String(content, StandardCharsets.UTF_8);

                        // 传递响应参数
                        exchange.getAttributes().put(BODY, responseResult);
                        // 如果修改了请求体
                        // this.getDelegate().getHeaders().setContentLength(responseResult.getBytes().length);
                        return dataBufferFactory.wrap(content);
                    }));
                    // }
                }
                // if body is not a flux. never got there.
                return super.writeWith(body);
            }
        };
    }

    private Mono<Void> log(ServerWebExchange exchange) {
        return Mono.fromRunnable(() -> {
            Long startTime = exchange.getAttribute(START_TIME);
            if (startTime != null) {
                Long executeTime = (System.currentTimeMillis() - startTime);
                int statusCode = 500;
                if (exchange.getResponse().getStatusCode() != null) {
                    statusCode = exchange.getResponse().getStatusCode().value();
                }
                Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
                log.info("clientIp:{}, routeId:{}, uri:{}, method:{}, mediaType:{}, params:{}, status:{}, cost:{}ms",
                        IpUtil.getHostIp(),
                        Optional.ofNullable(route).map(Route::getId).orElse(""),
                        exchange.getRequest().getURI().getRawPath(),
                        exchange.getRequest().getMethod(),
                        exchange.getRequest().getHeaders().getContentType(),
                        RequestUtil.getRequestBodyFormExchange(exchange),
                        statusCode,
                        executeTime
                );
                Object attribute = exchange.getAttribute(BODY);
                if (attribute != null) {
                    log.info("response:{}", attribute);
                }
            }
        });
    }

    @Override
    public int getOrder() {
        // 在CachedBodyGatewayFilter之后，确保缓存请求参数
        // 同时确保在NettyWriteResponseFilter之前,否则过滤器在被调用之前响应。
        return NettyWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER - 2;
    }
}