package com.cmmplb.gateway.server.filter.global;

import com.cmmplb.gateway.server.utils.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.NettyWriteResponseFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyResponseBodyGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author penglibo
 * @date 2024-08-14 10:16:56
 * @since jdk 1.8
 * {@link org.springframework.cloud.gateway.filter.factory.rewrite.ModifyResponseBodyGatewayFilterFactory}
 * 使用ModifyResponseBodyGatewayFilterFactory修改响应体
 * ps: 当响应返回体数据量大的情况报错Exceeded limit on max bytes to buffer : 262144
 * 需要配置spring.codec.max-in-memory-size: 20MB
 * 报错Exceeded limit on max bytes to buffer : 262144
 * {@link org.springframework.core.io.buffer.LimitedDataBufferList#raiseLimitException()}
 * 默认的最大值为26214, 256 * 1024, 256k
 * {@link org.springframework.core.codec.AbstractDataBufferDecoder#maxInMemorySize = 256 * 1024}
 */

@Slf4j
@Component
public class ModifyResponseBodyGatewayFilter implements GlobalFilter, Ordered {

    @Autowired
    private ModifyResponseBodyGatewayFilterFactory modifyResponseBodyGatewayFilterFactory;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("------------------------------------------");
        log.info("响应处理......");
        // 修改响应体,对下游服务相应参数添加一个response字段,第一个参数为原响应体类型,第二个参数为修改后响应体类型,第三个参数为修改响应体函数
        return modifyRequestBodyOne(exchange, chain);
        // return modifyResponseBody(exchange, chain);
    }

    /**
     * 写法一
     */
    private Mono<Void> modifyRequestBodyOne(ServerWebExchange exchange, GatewayFilterChain chain) {
        ModifyResponseBodyGatewayFilterFactory.Config config = modifyResponseBodyGatewayFilterFactory.newConfig();
        config.setNewContentType(MediaType.APPLICATION_JSON_VALUE);
        config.setRewriteFunction(String.class, String.class, (serverWebExchange, body) -> ResponseUtil.modifyResponseBody(body));
        return modifyResponseBodyGatewayFilterFactory.apply(config).filter(exchange, chain);
    }

    /**
     * 写法二
     */
    private Mono<Void> modifyResponseBody(ServerWebExchange exchange, GatewayFilterChain chain) {
        return modifyResponseBodyGatewayFilterFactory.apply(c -> c.setRewriteFunction(
                        String.class,
                        String.class,
                        (serverWebExchange, body) -> ResponseUtil.modifyResponseBody(body)
                ).setNewContentType(MediaType.APPLICATION_JSON_VALUE)
        ).filter(exchange, chain);
    }

    @Override
    public int getOrder() {
        // 自定义的GlobalFilter的order必须小于NettyWriteResponseFilter,否则过滤器在被调用之前发送响应。
        return NettyWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER - 1;
    }
}
