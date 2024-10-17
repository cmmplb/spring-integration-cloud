package io.github.cmmplb.gateway.server.filter.global;

import io.github.cmmplb.gateway.server.utils.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.NettyWriteResponseFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.lang.NonNull;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * @author penglibo
 * @date 2024-08-14 10:16:56
 * @since jdk 1.8
 * 修改响应体,解决响应体数据量大报错问题
 */

@Slf4j
// @Component
public class ModifyResponseBodyGatewayFilterRelease implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpResponse originalResponse = exchange.getResponse();
        ServerHttpResponseDecorator responseDecorator = new ServerHttpResponseDecorator(originalResponse) {
            @NonNull
            @Override
            public Mono<Void> writeWith(@NonNull Publisher<? extends DataBuffer> body) {
                if (body instanceof Flux) {
                    Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>) body;
                    return super.writeWith(fluxBody.buffer().map(dataBuffers -> {
                        // 解决返回体分段传输
                        DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
                        DataBuffer join = dataBufferFactory.join(dataBuffers);
                        byte[] content = new byte[join.readableByteCount()];
                        join.read(content);
                        // 释放掉内存
                        DataBufferUtils.release(join);
                        String result = new String(content, StandardCharsets.UTF_8);
                        // 这里可以对content进行修改操作
                        String modifiedContent = new String(result.getBytes(), StandardCharsets.UTF_8);
                        byte[] modifiedBytes = ResponseUtil.modifyResponseBodyJson(modifiedContent).getBytes(StandardCharsets.UTF_8);
                        this.getDelegate().getHeaders().setContentLength(modifiedBytes.length);
                        return this.getDelegate().bufferFactory().wrap(modifiedBytes);
                    }));
                }
                return super.writeWith(body);
            }
        };
        return chain.filter(exchange.mutate().response(responseDecorator).build());
    }

    @Override
    public int getOrder() {
        // 自定义的GlobalFilter的order必须小于NettyWriteResponseFilter,否则过滤器在被调用之前发送响应。
        return NettyWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER - 1;
    }
}
