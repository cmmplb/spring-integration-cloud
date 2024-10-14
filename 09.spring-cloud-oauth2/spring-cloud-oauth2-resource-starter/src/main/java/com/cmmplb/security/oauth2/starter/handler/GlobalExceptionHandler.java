package com.cmmplb.security.oauth2.starter.handler;

import io.github.cmmplb.core.result.HttpCodeEnum;
import io.github.cmmplb.core.result.Result;
import io.github.cmmplb.core.result.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.provider.NoSuchClientException;

/**
 * @author penglibo
 * @date 2022-11-03 11:00:59
 * @since jdk 1.8
 */

@Slf4j
public class GlobalExceptionHandler<T> extends io.github.cmmplb.core.handler.GlobalExceptionHandler<T> {

    @Override
    public Result<?> exceptionHandler(Exception e) {
        if (e instanceof NoSuchClientException) {
            log.error(e.getMessage(), e);
            return ResultUtil.custom(HttpCodeEnum.INTERNAL_SERVER_ERROR.getCode(), e.getMessage());
        } else {
            return super.exceptionHandler(e);
        }
    }

}
