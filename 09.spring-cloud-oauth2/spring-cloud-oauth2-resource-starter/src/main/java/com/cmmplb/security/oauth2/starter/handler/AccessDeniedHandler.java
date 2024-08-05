package com.cmmplb.security.oauth2.starter.handler;

import com.alibaba.fastjson.JSON;
import com.cmmplb.core.constants.StringConstant;
import com.cmmplb.core.result.HttpCodeEnum;
import com.cmmplb.core.result.Result;
import com.cmmplb.core.result.ResultUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @author penglibo
 * @date 2021-10-19 17:48:00
 * @since jdk 1.8
 * 权限不足处理器，覆盖默认的OAuth2AccessDeniedHandler包装失败信息
 */

@Slf4j
public class AccessDeniedHandler extends OAuth2AccessDeniedHandler {

    @Override
    @SneakyThrows
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException authException) {
        log.info(HttpCodeEnum.FORBIDDEN + " {}", request.getRequestURI());
        response.setCharacterEncoding(StringConstant.UTF8);
        response.setContentType(StringConstant.APPLICATION_JSON);
        Result<Object> fail = ResultUtil.fail();
        fail.setMsg(HttpCodeEnum.FORBIDDEN.getMessage());
        response.setStatus(HttpCodeEnum.FORBIDDEN.getCode());
        PrintWriter printWriter = response.getWriter();
        printWriter.append(JSON.toJSONString(fail));
    }

}
