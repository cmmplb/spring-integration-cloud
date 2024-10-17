package io.github.cmmplb.gateway.server.filter.openapi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import io.github.cmmplb.core.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.lang.NonNull;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Function;

@Slf4j
// @Component
public class RequestFilter implements GlobalFilter, Ordered {

    private static final List<HttpMessageReader<?>> MESSAGE_READERS = HandlerStrategies.withDefaults().messageReaders();

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 10001;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 获取ServerHttpRequest对象
        ServerHttpRequest request = exchange.getRequest();

        // openAPI请求为Post请求
        if (!HttpMethod.POST.name().equals(request.getMethod().name())) {
            log.info("非openAPI请求");
            return chain.filter(exchange);
        }
        // 请求头
        if (!"OPENAPI".equals(request.getHeaders().getFirst("from-system"))) {
            log.info("非openAPI请求");
            return chain.filter(exchange);
        }
        return requestDecorator(exchange, chain);
    }

    private Mono<Void> requestDecorator(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerRequest serverRequest = ServerRequest.create(exchange, MESSAGE_READERS);
        // 读取请求体
        Mono<String> modifiedBody = serverRequest.bodyToMono(String.class).flatMap(reaDecrypt(exchange));
        BodyInserter<Mono<String>, ReactiveHttpOutputMessage> bodyInserter = BodyInserters.fromPublisher(modifiedBody, String.class);
        HttpHeaders headers = new HttpHeaders();
        headers.putAll(exchange.getRequest().getHeaders());
        headers.remove(HttpHeaders.CONTENT_LENGTH);
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(exchange, headers);
        return bodyInserter.insert(outputMessage, new BodyInserterContext()).then(Mono.defer(() -> {
            ServerHttpRequestDecorator decorator = new ServerHttpRequestDecorator(exchange.getRequest()) {
                @NonNull
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

                @NonNull
                @Override
                public Flux<DataBuffer> getBody() {
                    return outputMessage.getBody();
                }
            };
            return chain.filter(exchange.mutate().request(decorator).build());
        }));
    }

    /**
     * 参数解析
     */
    private Function<String, Mono<String>> reaDecrypt(ServerWebExchange exchange) {
        return body -> {
            String params = "";
            try {
                log.info("请求体:{}", body);
                // {
                //     "REQUEST": {
                //         "API_ATTRS": {
                //             "Api_ID": "Cestbon.crbpet.wmsuat.ldapLogin",
                //             "Api_Version": "1.0",
                //             "App_Sub_ID": "0009000B0006",
                //             "App_Token": "3384c44c4dad4aefa43e02cc11e75b6d",
                //             "ENV": "uat",
                //             "Partner_ID": "00090000",
                //             "Sys_ID": "0009000B",
                //             "Time_Stamp": "2024-06-11 13:36:35:542",
                //             "User_Token": "",
                //             "Sign": "BCD00BC6C93D6603A4B2D691DFEE2E67"
                //         },
                //         "REQUEST_DATA": {
                //             "ldap": "test"
                //         }
                //     }
                // }
                if (StringUtil.isBlank(body)) {
                    throw new RuntimeException();
                }
                JSONObject jsonObject = JSON.parseObject(body);
                if (null == jsonObject) {
                    throw new RuntimeException();
                }
                JSONObject request = jsonObject.getJSONObject("REQUEST");
                if (null == request) {
                    throw new RuntimeException();
                }
                JSONObject data = request.getJSONObject("REQUEST_DATA");
                if (null != data) {
                    params = data.toJSONString();
                }
            } catch (Exception e) {
                // 处理请求体[1,2,3]
                List<Object> list = null;
                try {
                    list = JSON.parseObject(body, new TypeReference<List<Object>>() {
                    });
                } catch (Exception ex) {
                    log.error("请求参数异常:{}", body);
                    throw new RuntimeException(ex);
                }
                params = JSON.toJSONString(list);
            }
            log.info("params:{}", params);
            return Mono.just(params);
        };
    }
}
