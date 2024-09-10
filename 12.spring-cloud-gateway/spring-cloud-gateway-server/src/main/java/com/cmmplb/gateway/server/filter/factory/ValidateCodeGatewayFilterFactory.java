package com.cmmplb.gateway.server.filter.factory;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.cmmplb.core.constants.StringConstant;
import com.cmmplb.core.result.HttpCodeEnum;
import com.cmmplb.core.result.ResultUtil;
import com.cmmplb.core.utils.StringUtil;
import com.cmmplb.gateway.server.utils.RequestUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author penglibo
 * @date 2022-08-26 10:33:20
 * @since jdk 1.8
 * 验证码校验
 */

@SuppressWarnings({"ReactiveStreamsUnusedPublisher", "NullableProblems", "rawtypes", "unchecked"})
@Slf4j
@RequiredArgsConstructor
public class ValidateCodeGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {

    private static final List<HttpMessageReader<?>> MESSAGE_READERS = HandlerStrategies.withDefaults().messageReaders();

    private static final String GRANT_TYPE = "grantType";

    private static final String CODE = "code";

    private static final String RANDOM_STR = "randomStr";

    private static final String MOBILE = "mobile";

    private static final String PASSWORD = "password";

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();
            Class inClass = String.class;
            Class outClass = String.class;
            ServerRequest serverRequest = ServerRequest.create(exchange, MESSAGE_READERS);
            Mono<?> modifiedBody = serverRequest.bodyToMono(inClass).flatMap(reaDecrypt(request, response));
            BodyInserter<?, CachedBodyOutputMessage> bodyInserter = BodyInserters.fromPublisher(modifiedBody, outClass);
            HttpHeaders headers = new HttpHeaders();
            headers.putAll(exchange.getRequest().getHeaders());
            headers.remove(HttpHeaders.CONTENT_LENGTH);

            headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
            CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(exchange, headers);
            return bodyInserter.insert(outputMessage, new BodyInserterContext()).then(Mono.defer(() -> {
                // 报文转换
                ServerHttpRequest decorator = new ServerHttpRequestDecorator(exchange.getRequest()) {
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
                return chain.filter(exchange.mutate().request(decorator).build());
            }));
        };
    }

    /**
     * 密码解密
     */
    private Function<?, ?> reaDecrypt(ServerHttpRequest request, ServerHttpResponse response) {
        return s -> {
            String contentType = request.getHeaders().getFirst(StringConstant.CONTENT_TYPE);
            String params = s.toString();
            if (StringUtil.isNotBlank(contentType) && contentType.startsWith(StringConstant.MULTIPART_FORM_DATA)) {
                params = RequestUtil.analysisFormData(s.toString(), request);
            }
            Map<String, String> inParamsMap = JSON.parseObject(params, new TypeReference<Map<String, String>>() {
            });
            if (inParamsMap.get(GRANT_TYPE).equals(PASSWORD)) {
                Mono<Void> voidMono = checkCode(inParamsMap, response);
                if (null != voidMono) {
                    return voidMono;
                }
            } else if (inParamsMap.get(GRANT_TYPE).equals(PASSWORD)) {
                log.info("账号密码登陆:{}", s);
            } else {
                log.error("非法请求数据:{}", s);
            }
            return Mono.just(JSON.toJSONString(inParamsMap));
        };
    }

    private Mono<Void> checkCode(Map<String, String> inParamsMap, ServerHttpResponse response) {
        String code = inParamsMap.get(CODE);
        if (CharSequenceUtil.isBlank(code)) {
            return buildMsg(response, JSON.toJSONBytes(ResultUtil.custom(HttpCodeEnum.INVALID_REQUEST.getCode(), "验证码不能为空")));
        }
        String randomStr = inParamsMap.get(RANDOM_STR);
        if (CharSequenceUtil.isBlank(randomStr)) {
            randomStr = inParamsMap.get(MOBILE);
        }
        // 从缓存获取
        Object codeObj = "123456";
        if (ObjectUtil.isEmpty(codeObj) || !code.equals(codeObj)) {
            return buildMsg(response, JSON.toJSONBytes(ResultUtil.custom(HttpCodeEnum.INVALID_REQUEST.getCode(), "验证码错误")));
        }
        return null;
    }

    private static Mono<Void> buildMsg(ServerHttpResponse response, byte[] msgBytes) {
        response.setStatusCode(HttpStatus.BAD_REQUEST);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        return response.writeWith(Mono.create(monoSink -> {
            DataBufferFactory bufferFactory = response.bufferFactory();
            DataBuffer dataBuffer = bufferFactory.wrap(msgBytes);
            monoSink.success(dataBuffer);
        }));
    }
}
