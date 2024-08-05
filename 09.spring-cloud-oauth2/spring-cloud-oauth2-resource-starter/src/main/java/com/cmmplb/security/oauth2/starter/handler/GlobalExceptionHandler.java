package com.cmmplb.security.oauth2.starter.handler;

import com.cmmplb.core.result.HttpCodeEnum;
import com.cmmplb.core.result.Result;
import com.cmmplb.core.result.ResultUtil;
import io.lettuce.core.RedisCommandExecutionException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.security.oauth2.provider.NoSuchClientException;

import java.net.UnknownHostException;

/**
 * @author penglibo
 * @date 2022-11-03 11:00:59
 * @since jdk 1.8
 */

@Slf4j
public class GlobalExceptionHandler<T> extends com.cmmplb.core.handler.GlobalExceptionHandler<T> {

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
