package com.cmmplb.gateway.server.handler;

import com.alibaba.fastjson.JSON;
import com.cmmplb.core.exception.BusinessException;
import com.cmmplb.core.result.HttpCodeEnum;
import com.cmmplb.core.result.Result;
import com.cmmplb.core.result.ResultUtil;
import com.cmmplb.core.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;

/**
 * @author penglibo
 * @date 2021-11-15 17:33:21
 * @since jdk 1.8
 * 异常拦截
 */

@Slf4j
@Order(-1)
@Component
public class GatewayErrorWebExceptionHandler implements ErrorWebExceptionHandler {

    @Nonnull
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, @Nonnull Throwable ex) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        // 参考AbstractErrorWebExceptionHandler
        if (response.isCommitted()) {
            return Mono.error(ex);
        }

        // 按照异常类型进行处理
        HttpStatus httpStatus = HttpStatus.OK;
        String message;
        int code;
        if (ex instanceof NotFoundException) {
            message = "服务不可用";
            httpStatus = HttpStatus.SERVICE_UNAVAILABLE;
            code = HttpStatus.SERVICE_UNAVAILABLE.value();
        } else if (ex instanceof ResponseStatusException) {
            ResponseStatusException rse = (ResponseStatusException) ex;
            httpStatus = rse.getStatus();
            message = rse.getReason();
            code = httpStatus.value();
        } else if (ex instanceof BusinessException) {
            // 业务异常,httpStatus响应为200,code为具体业务异常码
            BusinessException be = (BusinessException) ex;
            message = be.getMessage();
            code = be.getCode();
        } else {
            message = "网络繁忙,请稍后再试";
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            code = HttpStatus.INTERNAL_SERVER_ERROR.value();
        }
        if (StringUtil.isBlank(message)) {
            message = HttpCodeEnum.getMessage(code);
        }

        if (ex instanceof BusinessException) {
            log.info("url:{},error:{}", request.getPath(), ex.getMessage());
        } else {
            log.error("url:{}", request.getPath(), ex);
        }


        response.setStatusCode(httpStatus);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        int finalCode = code;
        String finalMessage = message;
        return response.writeWith(Mono.fromSupplier(() -> {
            DataBufferFactory bufferFactory = response.bufferFactory();
            return bufferFactory.wrap(JSON.toJSONBytes(ResultUtil.fail(finalCode, finalMessage)));
        }));
    }

    /**
     * 构建返回的JSON数据格式
     * @param errorMessage 异常信息
     * @return
     */
    public static Object response(ServerHttpRequest request, String errorMessage) {
        Result<Object> fail = ResultUtil.fail(errorMessage);
        String originalBody = "";
        boolean isWarp = false;
        // 如果是openAPI业务，添加openAPI的结构体
        if ("OPENAPI".equals(request.getHeaders().getFirst("from-system"))) {
            originalBody = JSON.toJSONString(fail);
            if (!originalBody.startsWith("{\"RESPONSE")) {
                isWarp = true;
            }
        }
        if (isWarp) {
            // return new OpenApiResponse(originalBody);
        }
        return fail;
    }
}
