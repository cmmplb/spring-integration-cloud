package com.cmmplb.security.oauth2.starter.configuration;

import io.github.cmmplb.core.exception.BusinessException;
import com.cmmplb.security.oauth2.starter.configuration.properties.Oauth2ConfigProperties;
import com.cmmplb.security.oauth2.starter.configuration.properties.RestTemplateProperties;
import com.cmmplb.security.oauth2.starter.converter.UserConverter;
import com.cmmplb.security.oauth2.starter.converter.UserPrincipalExtractor;
import com.cmmplb.security.oauth2.starter.handler.AccessDeniedHandler;
import com.cmmplb.security.oauth2.starter.impl.RemoteTokenServicesImpl;
import com.cmmplb.security.oauth2.starter.impl.SecurityPermissionServiceImpl;
import com.cmmplb.security.oauth2.starter.impl.TokenEnhancerImpl;
import com.cmmplb.security.oauth2.starter.service.SecurityPermissionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerTokenServicesConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.BaseOAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.JdbcClientTokenServices;
import org.springframework.security.oauth2.provider.expression.OAuth2MethodSecurityExpressionHandler;
import org.springframework.security.oauth2.provider.token.*;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.util.Base64;
import java.util.Map;
import java.util.Objects;

/**
 * @author penglibo
 * @date 2022-09-01 10:13:43
 * @since jdk 1.8
 */

@Slf4j
@SuppressWarnings("JavadocReference")
@EnableGlobalAuthentication
@EnableConfigurationProperties({RestTemplateProperties.class, Oauth2ConfigProperties.class})
public class ResourceAutoConfiguration {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Autowired
    private RestTemplateProperties restTemplateProperties;

    @Autowired
    private Oauth2ConfigProperties oauth2ConfigProperties;

    @Autowired
    private ResourceServerProperties resourceServerProperties;

    /**
     * oauth2.scope权限表达式解析
     */
    @Bean
    public OAuth2MethodSecurityExpressionHandler oAuth2MethodSecurityExpressionHandler() {
        return new OAuth2MethodSecurityExpressionHandler();
    }

    /**
     * 方法权限安全配置
     */
    @Bean
    public MethodSecurityEnabledConfiguration methodSecurityEnabledConfiguration() {
        return new MethodSecurityEnabledConfiguration();
    }

    /**
     * 权限校验
     */
    @Bean
    public SecurityPermissionService securityPermissionService() {
        return new SecurityPermissionServiceImpl();
    }

    /**
     * 权限不足处理器
     */
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new AccessDeniedHandler();
    }

    /**
     * 基于数据库存储客户端令牌
     */
    @Bean
    public JdbcClientTokenServices clientTokenServices() {
        return new JdbcClientTokenServices(dataSource);
    }

    /**
     * 远程令牌服务
     */
    @Bean
    public ResourceServerTokenServices resourceServerTokenServices() {
        // 默认令牌服务，基于tokenStore验证
        if (oauth2ConfigProperties.isDefaultTokenServices()) {
            DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
            defaultTokenServices.setTokenStore(tokenStore());
            defaultTokenServices.setSupportRefreshToken(true);
            defaultTokenServices.setReuseRefreshToken(true);
            defaultTokenServices.setTokenEnhancer(tokenEnhancer());
            return defaultTokenServices;
        }
        // 使用代码配置，会覆盖配置文件中实例，org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerTokenServicesConfiguration.RemoteTokenServicesConfiguration.UserInfoTokenServicesConfiguration.userInfoTokenServices
        if (resourceServerProperties.isPreferTokenInfo()) {
            RemoteTokenServices remoteTokenServices = new RemoteTokenServicesImpl();
            remoteTokenServices.setRestTemplate(restTemplate());
            // 通过org.springframework.boot.autoconfigure.security.oauth2.OAuth2AutoConfiguration
            // 从OAuth2ClientProperties把clientId和clientSecret设置到ResourceServerProperties
            remoteTokenServices.setClientId(resourceServerProperties.getClientId());
            remoteTokenServices.setClientSecret(resourceServerProperties.getClientSecret());
            remoteTokenServices.setCheckTokenEndpointUrl(resourceServerProperties.getTokenInfoUri());
            // 自定义用户信息解析器
            remoteTokenServices.setAccessTokenConverter(accessTokenConverter());
            return remoteTokenServices;
        } else {
            UserInfoTokenServices userInfoTokenServices = new UserInfoTokenServices(
                    resourceServerProperties.getUserInfoUri(), resourceServerProperties.getClientId());
            userInfoTokenServices.setPrincipalExtractor(principalExtractor());
            userInfoTokenServices.setRestTemplate(oAuth2RestTemplate());
            return userInfoTokenServices;
        }
    }

    /**
     * 服务调用
     */
    @Bean
    @LoadBalanced
    public OAuth2RestTemplate oAuth2RestTemplate() {
        BaseOAuth2ProtectedResourceDetails resource = new BaseOAuth2ProtectedResourceDetails();
        resource.setClientId(resourceServerProperties.getClientId());
        return new OAuth2RestTemplate(resource);
    }

    /**
     * 服务调用
     */
    @Bean
    @Primary
    @LoadBalanced
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler());
        restTemplate.setRequestFactory(getSimpleClientFactory());
        return restTemplate;
    }

    /**
     * 令牌存储
     */
    @Bean
    @Primary
    public TokenStore tokenStore() {
        TokenStore tokenStore;
        if (oauth2ConfigProperties.getTokenStoreType().equals(Oauth2ConfigProperties.TokenStoreType.JDBC)) {
            tokenStore = new JdbcTokenStore(dataSource);
        } else if (oauth2ConfigProperties.getTokenStoreType().equals(Oauth2ConfigProperties.TokenStoreType.REDIS)) {
            tokenStore = new RedisTokenStore(redisConnectionFactory);
        } else if (oauth2ConfigProperties.getTokenStoreType().equals(Oauth2ConfigProperties.TokenStoreType.JWT)) {
            tokenStore = new JwtTokenStore(jwtAccessTokenConverter());
        } else {
            tokenStore = new InMemoryTokenStore();
        }
        return tokenStore;
    }

    /**
     * 拓展token信息
     */
    @Bean
    public TokenEnhancer tokenEnhancer() {
        if (oauth2ConfigProperties.getTokenStoreType().equals(Oauth2ConfigProperties.TokenStoreType.JWT)) {
            return jwtAccessTokenConverter();
        } else {
            return new TokenEnhancerImpl();
        }
    }

    /**
     * 令牌转换器
     */
    @Bean
    public AccessTokenConverter accessTokenConverter() {
        if (oauth2ConfigProperties.getTokenStoreType().equals(Oauth2ConfigProperties.TokenStoreType.JWT)) {
            return jwtAccessTokenConverter();
        } else {
            DefaultAccessTokenConverter defaultAccessTokenConverter = new DefaultAccessTokenConverter();
            defaultAccessTokenConverter.setUserTokenConverter(new UserConverter());
            return defaultAccessTokenConverter;
        }
    }

    /**
     * 认证服务端JwtAccessTokenConverter配置
     * JWT令牌转换器，将令牌转换为JWT
     * 如果使用了JWT，需要添加@Bean，不然在org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter#decode(java.lang.String)中
     * Jwt jwt = JwtHelper.decodeAndVerify(token, verifier);verifier为空从而解码失败Cannot convert access token to JSON
     * 原理是JwtAccessTokenConverter实现了InitializingBean，afterPropertiesSet中对verifier = new MacSigner(verifierKey);进行赋值
     * 如果不添加bean，则InitializingBean不会加载afterPropertiesSet。
     * ConditionalOnMissingBean=》当指定类型的Bean不存在时才创建一个新的Bean
     * 参照源码手动设置密钥：{@link ResourceServerTokenServicesConfiguration.JwtTokenServicesConfiguration#jwtTokenEnhancer()}
     */
    @Bean
    @ConditionalOnMissingBean(DefaultAccessTokenConverter.class)
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        // 对称加密签名令牌，资源服务器也需要配置，用于验签，这里在application-security_oauth2_resource.yml配置文件中配置了
        Oauth2ConfigProperties.JwtKey jwtKey = oauth2ConfigProperties.getJwtKey();
        if (jwtKey.isAuthorized()) {
            Oauth2ConfigProperties.JwtKey.Authorization authorization = jwtKey.getAuthorization();
            if (null == authorization) {
                throw new BusinessException("Jwt密钥配置有误，若开启jwt功能需要配置加密签名或者加密密钥");
            }
            if (authorization.isSymmetric()) {
                converter.setSigningKey(authorization.getSigningKey());
            } else {
                // 生成密钥：alias别名，keypass密码
                // keytool -genkeypair -alias oauth2 -keyalg RSA -keypass oauth2 -keystore oauth2.jks -storepass oauth2
                // 第一个参数是密钥文件，第二个参数是密钥密码
                KeyStoreKeyFactory factory = new KeyStoreKeyFactory(new ClassPathResource(
                        authorization.getKeyPath()), authorization.getPass().toCharArray());
                // 别名
                converter.setKeyPair(factory.getKeyPair(authorization.getAlias()));
            }
        } else {
            // 注意，客户端密钥测试的话，需要客户端使用DefaultTokenServices对应tokenStore验签。
            Oauth2ConfigProperties.JwtKey.Client client = jwtKey.getClient();
            if (null != client) {
                if (client.isSymmetric()) {
                    converter.setSigningKey(client.getSigningKey());
                } else {
                    ClassPathResource resource = new ClassPathResource(client.getPublicKeyFilePath());
                    String publicKey = null;
                    try {
                        publicKey = IOUtils.toString(resource.getInputStream(), "UTF-8");
                    } catch (IOException e) {
                        log.error("读取公钥失败", e);
                    }
                    converter.setVerifierKey(publicKey);
                }
            } else {
                // 客户端没配置公钥信息则调用服务端接口获取
                String keyFromServer = getKeyFromServer();
                log.info("从服务端获取公钥:{}", keyFromServer);
                converter.setVerifierKey(keyFromServer);
            }
        }
        return converter;
    }

    /**
     * 从服务端获取公钥，参考:
     * {@link ResourceServerTokenServicesConfiguration.JwtTokenServicesConfiguration#getKeyFromServer()}
     */
    private String getKeyFromServer() {
        HttpHeaders headers = new HttpHeaders();
        String username = resourceServerProperties.getClientId();
        String password = resourceServerProperties.getClientSecret();
        if (username != null && password != null) {
            byte[] token = Base64.getEncoder()
                    .encode((username + ":" + password).getBytes());
            headers.add("Authorization", "Basic " + new String(token));
        }
        HttpEntity<Void> request = new HttpEntity<>(headers);
        String url = resourceServerProperties.getJwt().getKeyUri();
        return (String) Objects.requireNonNull(restTemplate()
                        .exchange(url, HttpMethod.GET, request, Map.class).getBody())
                .get("value");
    }

    /**
     * 自定义Principal提取器
     */
    public PrincipalExtractor principalExtractor() {
        return new UserPrincipalExtractor();
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
