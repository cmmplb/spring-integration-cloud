package com.cmmplb.auth2.auth.handler;

import com.cmmplb.core.constants.StringConstants;
import com.cmmplb.security.oauth2.starter.constants.Oauth2Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;

/**
 * @author penglibo
 * @date 2021-09-03 17:35:05
 * @since jdk 1.8
 * 登录成功的处理-自定义响应内容
 */

@Slf4j
@Component
public class AuthenticationSuccessHandler implements org.springframework.security.web.authentication.AuthenticationSuccessHandler {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    private AuthorizationServerTokenServices tokenServices;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 1. 从请求头中获取 ClientId
        String header = request.getHeader(Oauth2Constants.AUTHORIZATION);
        if (header == null || !header.startsWith(Oauth2Constants.BASIC)) {
            throw new UnapprovedClientAuthenticationException("请求头中无client信息");
        }

        String[] tokens = this.extractAndDecodeHeader(header);
        String clientId = tokens[0];
        String clientSecret = tokens[1];

        TokenRequest tokenRequest = null;

        // 2. 通过 ClientDetailsService 获取 ClientDetails
        ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);

        // 3. 校验 ClientId和 ClientSecret的正确性
        if (clientDetails == null) {
            throw new UnapprovedClientAuthenticationException("clientId:" + clientId + "对应的信息不存在");
            // } else if (!StringUtils.equals(clientDetails.getClientSecret(), clientSecret)) {
        } else if (!passwordEncoder.matches(clientSecret, clientDetails.getClientSecret())) {
            throw new UnapprovedClientAuthenticationException("clientSecret不正确");
        } else {
            // 4. 通过 TokenRequest构造器生成 TokenRequest
            tokenRequest = new TokenRequest(new HashMap<>(), clientId, clientDetails.getScope(), "custom");
        }

        // 5. 通过 TokenRequest的 createOAuth2Request方法获取 OAuth2Request
        OAuth2Request oAuth2Request = tokenRequest.createOAuth2Request(clientDetails);
        // 6. 通过 Authentication和 OAuth2Request构造出 OAuth2Authentication
        OAuth2Authentication auth2Authentication = new OAuth2Authentication(oAuth2Request, authentication);

        // 7. 通过 AuthorizationServerTokenServices 生成 OAuth2AccessToken
        OAuth2AccessToken token = tokenServices.createAccessToken(auth2Authentication);

        // 8. 返回 Token
        log.info("登录成功");
        response.setContentType(StringConstants.APPLICATION_JSON);
        response.getWriter().write(new ObjectMapper().writeValueAsString(token));
    }

    private String[] extractAndDecodeHeader(String header) {
        byte[] base64Token = header.substring(6).getBytes(StandardCharsets.UTF_8);

        byte[] decoded;
        try {
            decoded = Base64.getDecoder().decode(base64Token);
        } catch (IllegalArgumentException var7) {
            throw new BadCredentialsException("Failed to decode basic authentication token");
        }

        String token = new String(decoded, StandardCharsets.UTF_8);
        int delim = token.indexOf(":");
        if (delim == -1) {
            throw new BadCredentialsException("Invalid basic authentication token");
        } else {
            return new String[]{token.substring(0, delim), token.substring(delim + 1)};
        }
    }
}
