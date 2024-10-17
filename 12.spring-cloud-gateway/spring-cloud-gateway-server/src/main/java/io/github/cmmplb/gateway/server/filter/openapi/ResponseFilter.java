package io.github.cmmplb.gateway.server.filter.openapi;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.NettyWriteResponseFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.lang.NonNull;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


@Slf4j
// @Component
public class ResponseFilter implements GlobalFilter, Ordered {

    @Override
    public int getOrder() {
        return NettyWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER - 2;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();
        if (!"OPENAPI".equals(request.getHeaders().getFirst("from-system"))) {
            log.info("非openAPI请求");
            return chain.filter(exchange);
        }
        if (!isSkip4Header(exchange)) {
            log.info("响应类型过滤");
            return chain.filter(exchange);
        }
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
                        log.info("modifiedContent:{}", modifiedContent);
                        byte[] modifiedBytes = JSON.toJSONString(new OpenApiResponse(modifiedContent)).getBytes(StandardCharsets.UTF_8);
                        originalResponse.getHeaders().setContentLength(modifiedBytes.length);
                        return originalResponse.bufferFactory().wrap(modifiedBytes);
                    }));
                }
                // if body is not a flux. never got there.
                return super.writeWith(body);
            }
        };
        return chain.filter(exchange.mutate().response(responseDecorator).build());
    }

    public boolean isSkip4Header(ServerWebExchange exchange) {
        AtomicBoolean isSkip = new AtomicBoolean(false);

        final List<String> contentTypeWhiteList = Arrays.asList("application/x-msdownload"
                , "application/pdf"
                , "application/octet-stream"
                , "image/png"
                , "application/vnd.ms-excel"
                , "application/msword"
                , "application/zip");

        ServerHttpRequest serverHttpRequest = exchange.getRequest();
        ServerHttpResponse serverHttpResponse = exchange.getResponse();
        HttpHeaders httpHeaders = serverHttpResponse.getHeaders();
        String origin = serverHttpRequest.getHeaders().getFirst(HttpHeaders.ORIGIN);
        origin = StringUtils.isBlank(origin) ? "*" : origin;
        httpHeaders.set(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, origin);
        StringBuilder strBuilder = new StringBuilder("当前响应头信息：");
        httpHeaders.forEach((key, value) -> {
            strBuilder.append(String.format("  %s : %s", key, value));
            if (HttpHeaders.CONTENT_TYPE.equals(key) && contentTypeWhiteList.contains(value.get(0))) {
                String requestUriPath = serverHttpRequest.getURI().getPath();
                String requestUriQuery = serverHttpRequest.getURI().getQuery();
                String requestUrl = StringUtils.isBlank(requestUriQuery) ? requestUriPath : String.format("%s?%s", requestUriPath, requestUriQuery);
                strBuilder.append(String.format("(当前请求%s的响应类型中包含%s，故标识为不做OpenAPI数据结构处理)", requestUrl, value.get(0)));
                isSkip.set(true);
            }
        });
        log.info(strBuilder.toString());
        return !isSkip.get();
    }


}
