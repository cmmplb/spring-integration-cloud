package com.cmmplb.security.oauth2.starter.handler;

import cn.hutool.http.HttpStatus;
import com.alibaba.fastjson.JSON;
import io.github.cmmplb.core.constants.StringConstant;
import io.github.cmmplb.core.result.HttpCodeEnum;
import io.github.cmmplb.core.result.Result;
import io.github.cmmplb.core.result.ResultUtil;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author penglibo
 * @date 2021-10-19 14:12:51
 * @since jdk 1.8
 * 资源异常细节处理,认证失败处理类
 */

public class ResourceAuthExceptionEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setCharacterEncoding(StringConstant.UTF8);
        response.setContentType(StringConstant.APPLICATION_JSON);
        response.setStatus(HttpStatus.HTTP_UNAUTHORIZED);
        Result<Object> result = ResultUtil.custom(HttpCodeEnum.UNAUTHORIZED.getMessage());
        if (authException != null) {
            result.setData(authException.getMessage());
        }
        // 针对令牌过期
        if (authException instanceof InsufficientAuthenticationException) {
            response.setStatus(HttpCodeEnum.UNAUTHORIZED.getCode());
            result.setMsg(HttpCodeEnum.UNAUTHORIZED.getMessage());
        }
        PrintWriter printWriter = response.getWriter();
        printWriter.append(JSON.toJSONString(result));
    }
}
