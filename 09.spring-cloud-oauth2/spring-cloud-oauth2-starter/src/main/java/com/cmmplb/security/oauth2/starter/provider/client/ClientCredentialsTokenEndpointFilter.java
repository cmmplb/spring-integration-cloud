package com.cmmplb.security.oauth2.starter.provider.client;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * @author penglibo
 * @date 2021-11-11 10:40:33
 * @since jdk 1.8
 * 重写ClientCredentialsTokenEndpointFilter实现客户端自定义异常处理
 */

public class ClientCredentialsTokenEndpointFilter extends org.springframework.security.oauth2.provider.client.ClientCredentialsTokenEndpointFilter {

    private final AuthorizationServerSecurityConfigurer configurer;
    private AuthenticationEntryPoint authenticationEntryPoint;


    public ClientCredentialsTokenEndpointFilter(AuthorizationServerSecurityConfigurer configurer, AuthenticationEntryPoint authenticationEntryPoint) {
        // 把父类的干掉
        this.configurer = configurer;
        super.setAuthenticationEntryPoint(null);
        this.authenticationEntryPoint = authenticationEntryPoint;
        afterPropertiesSet();
    }

    /**
     * 替换AuthenticationEntryPoint
     */
    @Override
    public void setAuthenticationEntryPoint(AuthenticationEntryPoint authenticationEntryPoint) {
        super.setAuthenticationEntryPoint(null);
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Override
    protected AuthenticationManager getAuthenticationManager() {
        return configurer.and().getSharedObject(AuthenticationManager.class);
    }

    @Override
    public void afterPropertiesSet() {
        setAuthenticationFailureHandler((request, response, e) -> authenticationEntryPoint.commence(request, response, e));
        setAuthenticationSuccessHandler((request, response, authentication) -> {
            // 无操作-仅允许过滤器链继续到令牌端点
        });
    }
}
