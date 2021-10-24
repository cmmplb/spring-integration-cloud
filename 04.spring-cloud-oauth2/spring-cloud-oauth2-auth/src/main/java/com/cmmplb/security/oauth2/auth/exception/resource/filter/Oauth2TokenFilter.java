package com.cmmplb.security.oauth2.auth.exception.resource.filter;

import com.alibaba.fastjson.JSONObject;
import com.cmmplb.security.oauth2.start.constants.Oauth2Constants;
import com.cmmplb.core.constants.StringConstants;
import com.cmmplb.core.result.HttpCodeEnum;
import com.cmmplb.core.result.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author penglibo
 * @date 2021-10-20 14:16:52
 * @since jdk 1.8
 * 资源服务器异常处理无token异常
 */

@Slf4j
public class Oauth2TokenFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response,@NonNull FilterChain filterChain) throws ServletException, IOException {
        log.debug("===========doFilterInternal===========");
        String uri = request.getRequestURI();
        if (uri.equals(Oauth2Constants.OAUTH_TOKEN) || uri.equals(Oauth2Constants.OAUTH_LOGIN)) {
            filterChain.doFilter(request, response);
        } else {
            boolean access_token = false;
            boolean authorization = false;
            if (request.getParameter(Oauth2Constants.ACCESS_TOKEN) == null) {
                access_token = true;
            }
            if (request.getHeader(Oauth2Constants.AUTHORIZATION) == null) {
                authorization = true;
            } else {
                if (!request.getHeader(Oauth2Constants.AUTHORIZATION).startsWith(Oauth2Constants.BEARER)) {
                    authorization = true;
                }
            }
            if (access_token && authorization) {
                response.setContentType(StringConstants.APPLICATION_JSON);
                response.getWriter().write(JSONObject.toJSONString(ResultUtil.custom(HttpCodeEnum.UNAUTHORIZED)));
            } else {
                filterChain.doFilter(request, response);
            }
        }
    }
}
