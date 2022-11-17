package com.cmmplb.security.oauth2.starter.handler;

import cn.hutool.core.util.StrUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author penglibo
 * @date 2021-09-08 09:37:22
 * @since jdk 1.8
 * sso 退出
 */

public class SsoLogoutSuccessHandler implements LogoutSuccessHandler {

    private static final String REDIRECT_URL = "redirect_url";

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        // 获取请求参数中是否包含 回调地址
        String redirectUrl = httpServletRequest.getParameter(REDIRECT_URL);
        if (StrUtil.isNotBlank(redirectUrl)) {
            httpServletResponse.sendRedirect(redirectUrl);
        } else {
            // 默认跳转referer 地址
            String referer = httpServletRequest.getHeader(HttpHeaders.REFERER);
            httpServletResponse.sendRedirect(referer);
        }
    }
}
