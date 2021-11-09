package com.cmmplb.security.oauth2.auth.config;

import com.cmmplb.common.redis.service.RedisService;
import com.cmmplb.core.utils.ObjectUtil;
import com.cmmplb.security.oauth2.auth.exception.auth.GlobalWebResponseExceptionTranslator;
import com.cmmplb.security.oauth2.auth.mobile.granter.MobileTokenGranter;
import com.cmmplb.security.oauth2.auth.service.RedisAuthorizationCodeServices;
import com.cmmplb.security.oauth2.auth.service.UserService;
import com.cmmplb.security.oauth2.start.entity.AuthUser;
import com.cmmplb.security.oauth2.start.service.impl.ClientDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author penglibo
 * @date 2021-09-08 14:24:27
 * @since jdk 1.8
 * 认证服务器
 */

@Order
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfigurer extends AuthorizationServerConfigurerAdapter {

    // @Autowired
    // private DataSource dataSource;

    @Autowired
    private UserService userService;

    @Resource
    private RedisConnectionFactory redisConnectionFactory;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ClientDetailsServiceImpl clientDetailsService;

    @Autowired
    private RedisService redisService;

    /**
     * 配置授权服务器端点的非安全功能，如令牌存储、令牌自定义、用户批准和授权类型。
     * 默认情况下你不需要做任何事情，除非你需要密码授权，在这种情况下你需要提供一个 {@link AuthenticationManager}。 *
     * @param endpoints 端点配置器
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST) // 允许令牌的请求方式
                .tokenGranter(tokenGranter(endpoints)) //配置grant_type模式
                .tokenStore(redisTokenStore()) // 配置token存储，一般配置redis存储
                .tokenEnhancer(tokenEnhancer()) // 拓展token信息
                .userDetailsService(userService)// 配置用户详情server，密码模式必须
                .authenticationManager(authenticationManager) // 配置认证管理器
                .authorizationCodeServices(authorizationCodeServices()) // 配置授权码模式授权码服务,不配置默认为内存模式-->org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices
                .reuseRefreshTokens(false) // 重复使用reuseRefreshToken
                .pathMapping("/oauth/confirm_access", "/oauth/confirm_access") // 替换地址
                .exceptionTranslator(new GlobalWebResponseExceptionTranslator()); // 自定义异常处理
    }

    /**
     * 配置授权码模式授权码服务,不配置默认为内存模式
     * @return
     */
    @Primary
    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        // 配置redis授权码模式
        return new RedisAuthorizationCodeServices(redisService);
    }

    /**
     * 创建grant_type列表
     * 配置grant_type模式，如果不配置则默认使用密码模式、简化模式、验证码模式以及刷新token模式，如果配置了只使用配置中，默认配置失效
     * 具体可以查询AuthorizationServerEndpointsConfigurer中的getDefaultTokenGranters方法
     * org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer.getDefaultTokenGranters
     * @param endpoints 端点配置器
     * @return TokenGranter
     */
    private TokenGranter tokenGranter(AuthorizationServerEndpointsConfigurer endpoints) {
        TokenGranter tokenGranter = endpoints.getTokenGranter();
        ArrayList<TokenGranter> tokenGranters = new ArrayList<>(Collections.singletonList(tokenGranter));
        // 添加一个自定义手机号验证码模式
        tokenGranters.add(new MobileTokenGranter(authenticationManager, endpoints.getTokenServices(), endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory()));
        return new CompositeTokenGranter(tokenGranters);
    }

    /**
     * 令牌存储
     * @return TokenStore
     */
    @Bean
    public TokenStore redisTokenStore() {
        RedisTokenStore redisTokenStore = new RedisTokenStore(redisConnectionFactory);
        redisTokenStore.setPrefix("oauth:access:");
        return redisTokenStore;
    }

    /**
     * 使用JWT替换默认令牌
     */
    /*@Bean
    public TokenStore jwtTokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }*/

    /*@Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
        accessTokenConverter.setSigningKey("test_key"); // 签名密钥
        return accessTokenConverter;
    }*/

    /**
     * 拓展token信息
     * @return TokenEnhancer
     */
    @Bean
    public TokenEnhancer tokenEnhancer() {
        return (accessToken, authentication) -> {
            final Map<String, Object> additionalInfo = new HashMap<>(2);
            if (null != authentication.getUserAuthentication()) {
                AuthUser authUser = ObjectUtil.cast(authentication.getUserAuthentication().getPrincipal());
                additionalInfo.put("id", authUser.getId());
                additionalInfo.put("username", authUser.getUsername());
            }
            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo); // 看源码能发现这是颁发令牌的类
            return accessToken;
        };
    }

    /**
     * 配置令牌端点(Token Endpoint)的安全约束
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
        oauthServer
                // real 值可自定义
                .realm("spring-oauth-server")
                // 支持 client_credentials 的配置
                .allowFormAuthenticationForClients().checkTokenAccess("permitAll()");
    }

    /**
     * 配置客户端详情
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 可以使自带JdbcClientDetailsService
        // clients.withClientDetails(new RedisClientDetailsService(dataSource));
        // 这里配置查询客户端
        clients.withClientDetails(clientDetailsService);
    }

    /**
     * 配置客户端，注意不会启用密码授予（即使某些客户端被允许），除非将AuthenticationManager提供给 {@link #configure(AuthorizationServerEndpointsConfigurer)}。
     * 必须至少声明一个客户端，或完全形成的自定义ClientDetailsService，否则服务器将无法启动。
     * @param clients 客户端详细信息配置器
     */
    /*@Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 定义两个客户端client_id，及客户端可以通过不同的client_id来获取不同的令牌；
        clients.inMemory()
                .withClient("client")
                // 控制台输出了 Encoded password does not look like BCrypt 的告警。新版本的spring-cloud-starter-oauth2指定client_secret的时候需要进行加密处理
                .secret(new BCryptPasswordEncoder().encode("oauth2-client"))
                .accessTokenValiditySeconds(3600) // 令牌有效时间
                .refreshTokenValiditySeconds(864000)
                .scopes("all", "a")
                .authorizedGrantTypes("password", "authorization_code", "client_credentials", "refresh_token") // "authorization_code", "refresh_token" 返回添加refresh_token
                .autoApprove(true)
//                .redirectUris("http://localhost:8081/api/spring-cloud-oauth2-client/login") // 重定向 Uris
                .redirectUris("http://localhost/api/spring-cloud-oauth2-auth") // 重定向 Uris
                .and()
                .withClient("client-sso")
                .secret(new BCryptPasswordEncoder().encode("oauth2-client-sso"))
                .accessTokenValiditySeconds(7200)
                .scopes("all")
                //.autoApprove(true)
                .redirectUris("http://localhost:8082/api/spring-cloud-oauth2-client/sso/login")
        ;
    }*/
}
