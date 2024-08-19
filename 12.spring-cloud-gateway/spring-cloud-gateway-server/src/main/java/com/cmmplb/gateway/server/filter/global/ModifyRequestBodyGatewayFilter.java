package com.cmmplb.gateway.server.filter.global;

import com.cmmplb.gateway.server.utils.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyRequestBodyGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author penglibo
 * @date 2024-08-14 10:16:56
 * @since jdk 1.8
 * {@link org.springframework.cloud.gateway.filter.factory.rewrite.ModifyRequestBodyGatewayFilterFactory}
 * 使用ModifyRequestBodyGatewayFilterFactory修改请求体
 * ps: 当请求体数据量大的情况报错Exceeded limit on max bytes to buffer : 262144
 * 需要配置spring.codec.max-in-memory-size: 20MB
 * 报错Exceeded limit on max bytes to buffer : 262144
 * {@link org.springframework.core.io.buffer.LimitedDataBufferList#raiseLimitException()}
 * 默认的最大值为26214, 256 * 1024, 256k
 * {@link org.springframework.core.codec.AbstractDataBufferDecoder#maxInMemorySize = 256 * 1024}
 */

@Slf4j
@Component
public class ModifyRequestBodyGatewayFilter implements GlobalFilter, Ordered {

    @Autowired
    private ModifyRequestBodyGatewayFilterFactory modifyRequestBodyGatewayFilterFactory;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("------------------------------------------");
        log.info("请求处理......");
        ServerHttpRequest request = exchange.getRequest();
        String params = RequestUtil.getRequestBodyFormRequest(exchange);
        log.info("params:{}", params);
        String requestBodyFormRequest = RequestUtil.getRequestBodyFormRequest(request);
        log.info("requestBodyFormRequest:{}", requestBodyFormRequest);

        // 修改请求体,对下游服务请求参数添加一个request字段,第一个参数为原请求体类型,第二个参数为修改后请求体类型,第三个参数为修改请求体函数
        return modifyRequestBodyOne(exchange, chain);
        // return modifyRequestBody(exchange, chain);
    }

    /**
     * 写法一
     */
    private Mono<Void> modifyRequestBodyOne(ServerWebExchange exchange, GatewayFilterChain chain) {
        ModifyRequestBodyGatewayFilterFactory.Config config = modifyRequestBodyGatewayFilterFactory.newConfig();
        config.setContentType(MediaType.APPLICATION_JSON_VALUE);
        config.setRewriteFunction(String.class, String.class, (serverWebExchange, params) -> RequestUtil.modifyRequestBody(params));
        return modifyRequestBodyGatewayFilterFactory.apply(config).filter(exchange, chain);
    }

    /**
     * 写法二
     */
    private Mono<Void> modifyRequestBody(ServerWebExchange exchange, GatewayFilterChain chain) {
        return modifyRequestBodyGatewayFilterFactory.apply(c -> c.setRewriteFunction(
                        String.class,
                        String.class,
                        (serverWebExchange, params) -> RequestUtil.modifyRequestBody(params)
                ).setContentType(MediaType.APPLICATION_JSON_VALUE)
        ).filter(exchange, chain);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
