package io.github.cmmplb.gateway.server.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import io.github.cmmplb.core.result.HttpCodeEnum;
import io.github.cmmplb.core.result.Result;
import io.github.cmmplb.core.result.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * @author penglibo
 * @date 2024-08-13 15:10:56
 * @since jdk 1.8
 */

@Slf4j
public class ResponseUtil {

    public static Mono<Void> buildResponse(ServerHttpResponse response, byte[] msgBytes) {
        return response.writeWith(Mono.create(monoSink -> {
            DataBufferFactory bufferFactory = response.bufferFactory();
            DataBuffer dataBuffer = bufferFactory.wrap(msgBytes);
            monoSink.success(dataBuffer);
        }));
    }

    public static Mono<Void> buildResponse(ServerHttpResponse response, HttpCodeEnum httpCodeEnum) {
        response.setStatusCode(HttpStatus.valueOf(httpCodeEnum.getCode()));
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        return response.writeWith(Mono.create(monoSink -> {
            DataBufferFactory bufferFactory = response.bufferFactory();
            DataBuffer dataBuffer = bufferFactory.wrap(JSON.toJSONBytes(ResultUtil.error(httpCodeEnum)));
            monoSink.success(dataBuffer);
        }));
    }

    public static Mono<String> modifyResponseBody(String body) {
        log.info("原响应体:{}", body);
        // 修改响应体
        Result<Map<String, Object>> result = JSON.parseObject(body, new TypeReference<Result<Map<String, Object>>>() {
        });
        Map<String, Object> map = result.getData();
        if (null != map) {
            map.put("response", "gateway");
        }
        body = JSON.toJSONString(result);
        log.info("修改后响应体:{}", body);
        return Mono.just(body);
    }

    public static String modifyResponseBodyJson(String body) {
        log.info("原响应体:{}", body);
        // 修改响应体
        Result<Map<String, Object>> result = JSON.parseObject(body, new TypeReference<Result<Map<String, Object>>>() {
        });
        Map<String, Object> map = result.getData();
        map.put("response", "gateway");
        body = JSON.toJSONString(result);
        log.info("修改后响应体:{}", body);
        return body;
    }

}
