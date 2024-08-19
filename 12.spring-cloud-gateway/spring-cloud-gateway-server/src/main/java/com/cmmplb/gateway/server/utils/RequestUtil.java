package com.cmmplb.gateway.server.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.cmmplb.core.exception.BusinessException;
import com.cmmplb.core.utils.ServletUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.NettyDataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author penglibo
 * @date 2024-08-07 17:18:13
 * @since jdk 1.8
 */

@Slf4j
public class RequestUtil {

    public static Mono<String> modifyRequestBody(String params) {
        log.info("原请求体:{}", params);
        // 修改请求体
        Map<String, Object> map = JSON.parseObject(params, new TypeReference<Map<String, Object>>() {
        });
        if (!map.containsKey("name")) {
            return Mono.error(new BusinessException(HttpStatus.BAD_REQUEST.value(), "参数name不存在"));
        }
        map.put("request", "gateway");
        params = JSON.toJSONString(map);
        log.info("修改后请求体:{}", params);
        return Mono.just(params);
    }

    /**
     * 从requestBody中取出json或者form-data数据
     */
    public static String getRequestBodyFormRequest(ServerHttpRequest request) {
        Flux<DataBuffer> body = request.getBody();
        StringBuilder sb = new StringBuilder();
        body.subscribe(buffer -> {
            byte[] bytes = new byte[buffer.readableByteCount()];
            buffer.read(bytes);
            String bodyString = new String(bytes, StandardCharsets.UTF_8);
            sb.append(bodyString);
        });
        MediaType mediaType = request.getHeaders().getContentType();
        if (null != mediaType && mediaType.isCompatibleWith(MediaType.APPLICATION_FORM_URLENCODED)) {
            return ServletUtil.urlDecode(sb.toString());
        }
        return sb.toString();
    }

    /**
     * 从exchange获取缓存参数
     */
    public static String getRequestBodyFormRequest(ServerWebExchange exchange) {
        Object cachedBody = exchange.getAttribute(ServerWebExchangeUtils.CACHED_REQUEST_BODY_ATTR);
        if (null != cachedBody) {
            NettyDataBuffer buffer = (NettyDataBuffer) cachedBody;
            String params = buffer.toString(StandardCharsets.UTF_8);

            // form-data参数url解码
            ServerHttpRequest request = exchange.getRequest();
            MediaType mediaType = request.getHeaders().getContentType();
            if (null != mediaType && mediaType.isCompatibleWith(MediaType.APPLICATION_FORM_URLENCODED)) {
                return ServletUtil.urlDecode(params);
            }
            return params;
        }
        return null;
    }

    /**
     * 解析form-data数据
     */
    public static String handleFormData(String str, ServerHttpRequest request) {
        MediaType mediaType = request.getHeaders().getContentType();
        if (null == mediaType) {
            return "";
        }
        String contentType = mediaType.toString();
        String sep = "--" + contentType.replace("multipart/form-data;boundary=", "");
        String[] strs = str.split("\r\n");
        // 空行
        boolean bankRow = false;
        // name=xxx行
        boolean keyRow = true;
        // 内容是否拼接换行符
        boolean append = false;
        // 这里保证接收顺序，LinkedHashMap
        Map<String, String> params = new LinkedHashMap<>();
        String s, key = null;
        StringBuilder sb = new StringBuilder();
        for (int i = 1, len = strs.length - 1; i < len; i++) {
            s = strs[i];
            if (keyRow) {
                key = s.replace("Content-Disposition: form-data; name=", "");
                key = key.substring(1, key.length() - 1);
                keyRow = false;
                bankRow = true;
                sb = new StringBuilder();
                continue;
            }
            if (sep.equals(s)) {
                keyRow = true;
                params.put(key, sb.toString());
                append = false;
                continue;
            }
            if (bankRow) {
                bankRow = false;
                continue;
            }
            if (append) {
                sb.append("\r\n");
            }
            sb.append(s);
            append = true;
        }
        if (null != key) {
            params.put(key, sb.toString());
        }
        return JSON.toJSONString(params);
    }
}
