package io.github.cmmplb.gateway.server.filter.global;

import io.github.cmmplb.gateway.server.utils.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.NettyWriteResponseFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.filter.factory.rewrite.GzipMessageBodyResolver;
import org.springframework.cloud.gateway.filter.factory.rewrite.MessageBodyDecoder;
import org.springframework.cloud.gateway.filter.factory.rewrite.MessageBodyEncoder;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.lang.NonNull;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.ORIGINAL_RESPONSE_CONTENT_TYPE_ATTR;

/**
 * @author penglibo
 * @date 2024-08-14 10:16:56
 * @since jdk 1.8
 * {@link org.springframework.cloud.gateway.filter.factory.rewrite.ModifyResponseBodyGatewayFilterFactory}
 * 参照ModifyResponseBodyGatewayFilterFactory修改响应体
 * ps: 当响应返回体数据量大的情况报错Exceeded limit on max bytes to buffer : 262144
 * 需要配置spring.codec.max-in-memory-size: 20MB
 * 报错Exceeded limit on max bytes to buffer : 262144
 * {@link org.springframework.core.io.buffer.LimitedDataBufferList#raiseLimitException()}
 * 默认的最大值为26214, 256 * 1024, 256k
 * {@link org.springframework.core.codec.AbstractDataBufferDecoder#maxInMemorySize = 256 * 1024}
 */

@Slf4j
// @Component
public class ModifyResponseBodyGatewayFilterImitation implements GlobalFilter, Ordered {

    @Autowired
    private ServerCodecConfigurer codecConfigurer;

    private static final Map<String, MessageBodyDecoder> STRING_MESSAGE_BODY_DECODER_MAP;

    private static final Map<String, MessageBodyEncoder> STRING_MESSAGE_BODY_ENCODER_MAP;

    static {
        Set<MessageBodyDecoder> messageBodyDecodersSet = new HashSet<>();
        Set<MessageBodyEncoder> messageBodyEncodersSet = new HashSet<>();
        MessageBodyDecoder decoder = new GzipMessageBodyResolver();
        MessageBodyEncoder encoder = new GzipMessageBodyResolver();
        messageBodyDecodersSet.add(decoder);
        messageBodyEncodersSet.add(encoder);
        STRING_MESSAGE_BODY_DECODER_MAP = messageBodyDecodersSet.stream().collect(Collectors.toMap(MessageBodyDecoder::encodingType, identity()));
        STRING_MESSAGE_BODY_ENCODER_MAP = messageBodyEncodersSet.stream().collect(Collectors.toMap(MessageBodyEncoder::encodingType, identity()));
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("------------------------------------------");
        log.info("响应处理......");

        ServerHttpResponse originalResponse = exchange.getResponse();
        ServerHttpResponseDecorator responseDecorator = new ServerHttpResponseDecorator(originalResponse) {
            @NonNull
            @Override
            public Mono<Void> writeWith(@NonNull Publisher<? extends DataBuffer> body) {
                String originalResponseContentType = exchange.getAttribute(ORIGINAL_RESPONSE_CONTENT_TYPE_ATTR);
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.add(HttpHeaders.CONTENT_TYPE, originalResponseContentType);

                ClientResponse clientResponse = prepareClientResponse(exchange, body, httpHeaders);

                Class<String> inClass = String.class;
                Class<String> outClass = String.class;

                // 这里是修改响应体的地方
                Mono<String> modifiedBody = extractBody(exchange, clientResponse, inClass)
                        .flatMap(originalBody -> ResponseUtil.modifyResponseBody(originalBody)
                                .switchIfEmpty(Mono.defer(() -> ResponseUtil.modifyResponseBody(null))));

                BodyInserter<Mono<String>, ReactiveHttpOutputMessage> bodyInserter = BodyInserters.fromPublisher(modifiedBody, outClass);
                CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(exchange, exchange.getResponse().getHeaders());
                return bodyInserter.insert(outputMessage, new BodyInserterContext()).then(Mono.defer(() -> {
                    Mono<DataBuffer> messageBody = writeBody(getDelegate(), outputMessage, outClass);
                    HttpHeaders headers = getDelegate().getHeaders();
                    if (!headers.containsKey(HttpHeaders.TRANSFER_ENCODING) || headers.containsKey(HttpHeaders.CONTENT_LENGTH)) {
                        messageBody = messageBody.doOnNext(data -> headers.setContentLength(data.readableByteCount()));
                    }
                    return getDelegate().writeWith(messageBody);
                }));
            }
        };
        return chain.filter(exchange.mutate().response(responseDecorator).build());
    }

    private Mono<DataBuffer> writeBody(ServerHttpResponse httpResponse, CachedBodyOutputMessage message,
                                       Class<?> outClass) {
        Mono<DataBuffer> response = DataBufferUtils.join(message.getBody());
        if (byte[].class.isAssignableFrom(outClass)) {
            return response;
        }

        List<String> encodingHeaders = httpResponse.getHeaders().getOrEmpty(HttpHeaders.CONTENT_ENCODING);
        for (String encoding : encodingHeaders) {
            MessageBodyEncoder encoder = STRING_MESSAGE_BODY_ENCODER_MAP.get(encoding);
            if (encoder != null) {
                DataBufferFactory dataBufferFactory = httpResponse.bufferFactory();
                response = response.publishOn(Schedulers.parallel()).map(buffer -> {
                    byte[] encodedResponse = encoder.encode(buffer);
                    DataBufferUtils.release(buffer);
                    return encodedResponse;
                }).map(dataBufferFactory::wrap);
                break;
            }
        }

        return response;
    }

    private ClientResponse prepareClientResponse(ServerWebExchange exchange, Publisher<? extends DataBuffer> body, HttpHeaders httpHeaders) {
        ClientResponse.Builder builder;
        // 默认的最大值为26214, 256 * 1024, 256k, 这里取配置文件的spring.codec.max-in-memory-size: 20MB
        builder = ClientResponse.create(Objects.requireNonNull(exchange.getResponse().getStatusCode()), codecConfigurer.getReaders());
        return builder.headers(headers -> headers.putAll(httpHeaders)).body(Flux.from(body)).build();
    }

    private Mono<String> extractBody(ServerWebExchange exchange, ClientResponse clientResponse, Class<String> inClass) {
        // if inClass is byte[] then just return body, otherwise check if
        // decoding required
        if (byte[].class.isAssignableFrom(inClass)) {
            return clientResponse.bodyToMono(inClass);
        }

        List<String> encodingHeaders = exchange.getResponse().getHeaders().getOrEmpty(HttpHeaders.CONTENT_ENCODING);
        for (String encoding : encodingHeaders) {
            MessageBodyDecoder decoder = STRING_MESSAGE_BODY_DECODER_MAP.get(encoding);
            if (decoder != null) {
                return clientResponse.bodyToMono(byte[].class).publishOn(Schedulers.parallel()).map(decoder::decode)
                        .map(bytes -> exchange.getResponse().bufferFactory().wrap(bytes))
                        .map(buffer -> prepareClientResponse(exchange, Mono.just(buffer),
                                exchange.getResponse().getHeaders()))
                        .flatMap(response -> response.bodyToMono(inClass));
            }
        }

        return clientResponse.bodyToMono(inClass);
    }

    @Override
    public int getOrder() {
        // 自定义的GlobalFilter的order必须小于NettyWriteResponseFilter,否则过滤器在被调用之前发送响应。
        return NettyWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER - 1;
    }
}
