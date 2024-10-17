package io.github.cmmplb.gateway.server.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import io.github.cmmplb.core.utils.ServletUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.NettyDataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

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
        Map<String, Object> map = null;
        try {
            map = JSON.parseObject(params, new TypeReference<Map<String, Object>>() {
            });
            if (!map.containsKey("name")) {
                // return Mono.error(new BusinessException(HttpStatus.BAD_REQUEST.value(), "参数name不存在"));
            }
            map.put("request", "gateway");
        } catch (Exception e) {
            log.info("json转对象异常", e);
        }
        params = JSON.toJSONString(map);
        log.info("修改后请求体:{}", params);
        return Mono.just(params);
    }

    /**
     * 从requestBody中取出json或者form-data数据,需要前面复制一份，否则下游无法获取参数
     */
    public static String getRequestBodyFormExchange(ServerHttpRequest request) {
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
    public static String getRequestBodyFormExchange(ServerWebExchange exchange) {
        Object cachedBody = exchange.getAttribute(ServerWebExchangeUtils.CACHED_REQUEST_BODY_ATTR);
        if (null != cachedBody) {
            NettyDataBuffer buffer = (NettyDataBuffer) cachedBody;
            String params = buffer.toString(StandardCharsets.UTF_8);

            ServerHttpRequest request = exchange.getRequest();
            MediaType mediaType = request.getHeaders().getContentType();

            // application/x-www-form-urlencoded参数url解码
            if (null != mediaType && mediaType.isCompatibleWith(MediaType.APPLICATION_FORM_URLENCODED)) {
                return analysisFormUrlencoded(params);
            }
            // multipart/form-data参数
            if (null != mediaType && mediaType.isCompatibleWith(MediaType.MULTIPART_FORM_DATA)) {
                return analysisFormData(params, request);
            }
            // 把原始换行的格式转换一下
            JSONObject jsonObject = JSONObject.parseObject(params);
            return jsonObject.toJSONString();
        }
        return null;
    }

    /**
     * 解析application/x-www-form-urlencoded参数
     * @param params key=value&key1=value1
     * @return json结构参数
     */
    public static String analysisFormUrlencoded(String params) {
        String data = null;
        try {
            data = ServletUtil.urlDecode(params);
        } catch (Exception e) {
            log.error("解码失败：", e);
        }
        Map<String, Object> map = Arrays.stream(data.split("&"))
                .map(str -> str.split("="))
                .collect(Collectors.toMap(x -> x[0], x -> x.length == 2 ? x[1] : "",
                        (u, v) -> {
                            throw new IllegalStateException(String.format("Duplicate key %s", u));
                        }, LinkedHashMap::new));
        return JSON.toJSONString(map);
    }

    /**
     * 解析form-data数据
     * @param params  待解析的参数
     * @param request 请求
     * @return json结构参数
     */
    public static String analysisFormData(String params, ServerHttpRequest request) {
        MediaType mediaType = request.getHeaders().getContentType();
        if (null == mediaType) {
            return "";
        }
        String contentType = mediaType.toString();
        String sep = "--" + contentType.replace("multipart/form-data;boundary=", "");
        String[] strs = params.split("\r\n");
        // 空行
        boolean bankRow = false;
        // name=xxx行
        boolean keyRow = true;
        // 内容是否拼接换行符
        boolean append = false;
        // 这里保证接收顺序，LinkedHashMap
        Map<String, String> result = new LinkedHashMap<>();
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
                result.put(key, sb.toString());
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
            result.put(key, sb.toString());
        }
        return JSON.toJSONString(result);
    }
}
