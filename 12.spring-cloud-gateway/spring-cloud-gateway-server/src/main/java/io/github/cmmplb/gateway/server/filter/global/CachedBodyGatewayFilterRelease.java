package io.github.cmmplb.gateway.server.filter.global;

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
// @Component
public class CachedBodyGatewayFilterRelease implements GlobalFilter, Ordered {

    public static final String CACHE_REQUEST_BODY_OBJECT_KEY = "cachedRequestBodyObject";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("CachedBodyGatewayFilterRelease-缓存请求参数过滤器");
        ServerHttpRequest request = exchange.getRequest();
        HttpMethod method = request.getMethod();
        if (!HttpMethod.POST.equals(method)) {
            return chain.filter(exchange);
        } else {
            // 使用 DataBufferUtils.join 将DataBuffer数据流聚合为一个Mono
            // 聚合后的 DataBuffer 为一个完整报文的 DataBuffer
            return DataBufferUtils.join(exchange.getRequest().getBody()).flatMap(dataBuffer -> {
                // 获取报文大小
                int size = dataBuffer.readableByteCount();
                if (size == 0) {
                    // 如果报文内容为空，需要主动释放创建的空的DataBuffer。
                    DataBufferUtils.release(dataBuffer);
                    return chain.filter(exchange);
                }
                DataBufferUtils.retain(dataBuffer);
                // 复制一份DataBuffer，slice方法不会retain DataBuffer，只是复制的指针坐标。
                Flux<DataBuffer> cachedFlux = Flux.defer(() -> Flux.just(dataBuffer.slice(0, dataBuffer.readableByteCount())));
                // 构建新的 Request，重写 getBody 返回复制的报文
                ServerHttpRequest requestDecorator = new ServerHttpRequestDecorator(exchange.getRequest()) {
                    @NonNull
                    @Override
                    public Flux<DataBuffer> getBody() {
                        return cachedFlux;
                    }
                };
                exchange.getAttributes().put(CACHE_REQUEST_BODY_OBJECT_KEY, cachedFlux);
                // 继续执行filter
                return chain.filter(exchange.mutate().request(requestDecorator).build());
            });
        }
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}