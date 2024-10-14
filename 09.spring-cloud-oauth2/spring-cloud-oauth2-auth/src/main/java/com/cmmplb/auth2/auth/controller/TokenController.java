package com.cmmplb.auth2.auth.controller;

import io.github.cmmplb.core.result.Result;
import io.github.cmmplb.core.result.ResultUtil;
import io.github.cmmplb.core.utils.SpringUtil;
import io.github.cmmplb.core.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author penglibo
 * @date 2021-10-20 15:23:47
 * @since jdk 1.8
 */

@Slf4j
@RestController
@RequestMapping("/token")
public class TokenController {

    @Autowired
    private TokenStore tokenStore;

    /**
     * 退出登录
     */
    @GetMapping("/logout")
    public Result<Boolean> logout(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorization) {
        if (StringUtils.isEmpty(authorization)) {
            return ResultUtil.success();
        }
        String token = authorization.replace(OAuth2AccessToken.BEARER_TYPE, StringUtils.EMPTY).trim();
        // 小写的bearer也处理一遍
        token = token.replace(OAuth2AccessToken.BEARER_TYPE.toLowerCase(), StringUtils.EMPTY).trim();
        OAuth2AccessToken accessToken = tokenStore.readAccessToken(token);
        if (accessToken == null || StringUtils.isEmpty(accessToken.getValue())) {
            return ResultUtil.success();
        }
        // 认证信息
        OAuth2Authentication auth2Authentication = tokenStore.readAuthentication(accessToken);
        // 发布退出登录事件
        SpringUtil.publishEvent(new LogoutSuccessEvent(auth2Authentication));

        // 删除accessToken和refreshToken相关信息
        tokenStore.removeAccessToken(accessToken);
        tokenStore.removeRefreshToken(accessToken.getRefreshToken());
        return ResultUtil.success();
    }
}
