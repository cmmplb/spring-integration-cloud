package com.cmmplb.security.oauth2.starter.service.impl;

import com.cmmplb.core.utils.ObjectUtil;
import com.cmmplb.redis.service.RedisService;
import com.cmmplb.security.oauth2.starter.constants.Oauth2Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.code.RandomValueAuthorizationCodeServices;

/**
 * @author penglibo
 * @date 2021-10-16 16:51:20
 * @since jdk 1.8
 * 仿照org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices内存存储模式
 * 授权码地址：http://localhost/api/spring-cloud-oauth2-auth/oauth/authorize?response_type=code&client_id=client&redirect_uri=http://localhost/api/spring-cloud-oauth2-auth&scope=all&state=hello
 * 在同时定义了认证服务器和资源服务器后，再去使用授权码模式获取令牌可能会遇到 Full authentication is required to access this resource 的问题，
 */

@Slf4j
public class RedisAuthorizationCodeServicesImpl extends RandomValueAuthorizationCodeServices {

    private final RedisService redisService;

    public RedisAuthorizationCodeServicesImpl(RedisService redisService) {
        this.redisService = redisService;
    }

    @Override
    protected void store(String code, OAuth2Authentication authentication) {
        log.debug("store authorization code :{}", code);
        redisService.set(get(code), authentication);
    }

    @Override
    protected OAuth2Authentication remove(String code) {
        log.debug("remove authorization code :{}", code);
        Object o = redisService.get(get(code));
        if (null != o) {
            redisService.del(get(code));
            return ObjectUtil.cast(o);
        }
        return null;
    }

    private String get(String code) {
        return Oauth2Constants.AUTHORIZATION_CODE_CACHE_PREFIX + code;
    }
}
