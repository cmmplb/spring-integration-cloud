package com.cmmplb.security.oauth2.starter.configuration;

import com.cmmplb.security.oauth2.starter.configuration.properties.RestTemplateProperties;
import com.cmmplb.security.oauth2.starter.provider.error.AccessDeniedHandler;
import com.cmmplb.security.oauth2.starter.provider.token.RemoteTokenServices;
import com.cmmplb.security.oauth2.starter.service.SecurityPermissionService;
import com.cmmplb.security.oauth2.starter.service.impl.SecurityPermissionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextListener;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;

/**
 * @author penglibo
 * @date 2022-09-01 10:13:43
 * @since jdk 1.8
 */

@EnableConfigurationProperties(RestTemplateProperties.class)
public class ResourceAutoConfiguration {

    @Autowired
    private RestTemplateProperties restTemplateProperties;

    /**
     * 权限不足处理器
     */
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new AccessDeniedHandler();
    }

    /**
     * 使用 Spring Security 的缩写，方便使用
     */
    @Bean("ss")
    public SecurityPermissionService securityPermissionService() {
        return new SecurityPermissionServiceImpl();
    }


    /**
     * 远程调用认证服务器，不配置的话将使用UserInfoTokenServices
     * user-info-uri和token-info-uri二选择即可
     * 如果配置了user-info-uri，该资源服务器使用userInfoTokenServices远程调用认证中心接口，
     * 通过认证中心的OAuth2AuthenticationProcessingFilter完成验证工作，一般设置user-info-uri即可
     * 该资源服务器使用RemoteTokenServices远程调用认证中心接口，注意一点就是如果使用token-info-uri那么就必须设置上clientId和clientSecret，
     * 远程服务调用需要使用"Basic " + new String(Base64.encode(String.format("%s:%s", clientId, clientSecret).getBytes(StandardCharsets.UTF_8)));
     * 通过CheckTokenEndpoint完成验证工作
     */
    @Bean
    @Primary
    public RemoteTokenServices remoteTokenServices() {
        return new RemoteTokenServices();
    }

    /**
     * 绑定上下文
     */
    @Bean
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }

    /**
     * 服务调用
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler());
        restTemplate.setRequestFactory(getSimpleClientFactory());
        return restTemplate;
    }

    /**
     * SimpleClientFactory
     * @return SimpleClientHttpRequestFactory
     */
    private SimpleClientHttpRequestFactory getSimpleClientFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(restTemplateProperties.getReadTimeout());
        factory.setConnectTimeout(restTemplateProperties.getConnectionTimeout());
        RestTemplateProperties.Proxy proxy = restTemplateProperties.getProxy();
        if (proxy.getEnabled()) {
            SocketAddress address = new InetSocketAddress(proxy.getHost(), proxy.getPort());
            factory.setProxy(new Proxy(Proxy.Type.HTTP, address));
        }
        return factory;
    }
}
