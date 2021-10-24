package com.cmmplb.security.oauth2.start.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author penglibo
 * @date 2021-10-18 16:51:46
 * @since jdk 1.8
 **/

@Slf4j
public class ClientDetailsServiceImpl implements ClientDetailsService {

    /**
     * 按客户端id加载客户端。此方法不能返回null
     */
    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        log.debug("客户端查询:" + clientId);
        BaseClientDetails baseClientDetails = selectById(clientId);
        if (baseClientDetails == null) {
            throw new NoSuchClientException("not found clientId:" + clientId);
        }
        return baseClientDetails;
    }


    /**
     * 根据客户端id查询==>>这边模拟数据库根据id查询
     * @return org.springframework.security.oauth2.provider.client.BaseClientDetails
     */
    public BaseClientDetails selectById(String clientId) {
        return build().get(clientId);
    }

    private Map<String, BaseClientDetails> build() {
        Map<String, BaseClientDetails> map = new HashMap<>();
        // 配置客户端client
        BaseClientDetails clientDetails = new BaseClientDetails();
        clientDetails.setClientId("client");
        clientDetails.setClientSecret(new BCryptPasswordEncoder().encode("oauth2-client"));
        // 自动批准作用于，授权码模式时使用，登录验证后直接返回code，不再需要下一步点击授权
        clientDetails.setAutoApproveScopes(Collections.singletonList("sever"));
        clientDetails.setScope(Collections.singletonList("all"));
        clientDetails.setAuthorizedGrantTypes(Arrays.asList("password", "authorization_code", "client_credentials", "refresh_token", "implicit", "mobile"));
        Set<String> sets = new HashSet<>(2);
        sets.add("http://localhost:8081/api/spring-cloud-oauth2-client/login");
        clientDetails.setRegisteredRedirectUri(sets);
        //设置accessToken和refreshToken的时效，如果不设置则使tokenServices的配置的
        clientDetails.setAccessTokenValiditySeconds((int) TimeUnit.HOURS.toSeconds(2)); // 令牌有效时间
        clientDetails.setRefreshTokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(30));
        map.put("client", clientDetails);

        // 配置客户端client-sso
        clientDetails = new BaseClientDetails();
        clientDetails.setClientId("client-sso");
        clientDetails.setClientSecret(new BCryptPasswordEncoder().encode("oauth2-client-sso"));
        // 自动批准作用于，授权码模式时使用，登录验证后直接返回code，不再需要下一步点击授权
        clientDetails.setAutoApproveScopes(Collections.singletonList("sever"));
        clientDetails.setScope(Arrays.asList("all", "a"));
        clientDetails.setAuthorizedGrantTypes(Arrays.asList("password", "authorization_code", "client_credentials", "refresh_token", "implicit", "mobile"));
        sets = new HashSet<>(2);
        sets.add("http://localhost:8082/api/spring-cloud-oauth2-client/sso/login");
        clientDetails.setRegisteredRedirectUri(sets);
        //设置accessToken和refreshToken的时效，如果不设置则使tokenServices的配置的
        clientDetails.setAccessTokenValiditySeconds((int) TimeUnit.HOURS.toSeconds(2)); // 令牌有效时间
        clientDetails.setRefreshTokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(30));
        map.put("client-sso", clientDetails);
        return map;
    }


}
