package com.cmmplb.auth2.auth.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author penglibo
 * @date 2021-09-03 17:33:19
 * @since jdk 1.8
 * 登录失败的处理
 */

@Slf4j
@Component
public class AuthenticationFailureHandler implements org.springframework.security.web.authentication.AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        // response.sendRedirect("/token/login");
        log.error("==========" + exception.getMessage() + "==========", exception);
    }
}
