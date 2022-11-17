package com.cmmplb.auth2.auth.configuration;

import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import com.cmmplb.core.constants.StringConstants;
import com.cmmplb.core.result.HttpCodeEnum;
import com.cmmplb.core.result.Result;
import com.cmmplb.core.result.ResultUtil;
import com.cmmplb.core.utils.ObjectUtil;
import com.cmmplb.redis.service.RedisService;
import com.cmmplb.security.oauth2.starter.constants.CacheConstants;
import com.cmmplb.security.oauth2.starter.constants.Oauth2Constants;
import com.cmmplb.security.oauth2.starter.provider.converter.User;
import com.cmmplb.security.oauth2.starter.provider.error.GlobalWebResponseExceptionTranslator;
import com.cmmplb.security.oauth2.starter.provider.granter.mobile.MobileTokenGranter;
import com.cmmplb.security.oauth2.starter.provider.granter.thirdParty.ThirdPartyTokenGranter;
import com.cmmplb.security.oauth2.starter.provider.token.store.redis.RedisTokenStore;
import com.cmmplb.security.oauth2.starter.provider.token.store.redis.serializer.fastjson.FastJsonSerializationStrategy;
import com.cmmplb.security.oauth2.starter.provider.token.store.redis.serializer.jackson.JacksonSerializationStrategy;
import com.cmmplb.security.oauth2.starter.provider.token.store.redis.serializer.properties.TokenSerializerProperties;
import com.cmmplb.security.oauth2.starter.service.impl.RedisAuthorizationCodeServicesImpl;
import com.cmmplb.security.oauth2.starter.service.impl.RedisClientDetailsServiceImpl;
import com.cmmplb.security.oauth2.starter.service.impl.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author penglibo
 * @date 2021-11-04 17:43:53
 * @since jdk 1.8
 * 认证服务器
 */

@Slf4j
@Configuration
@AllArgsConstructor
@EnableAuthorizationServer
@EnableConfigurationProperties(TokenSerializerProperties.class)
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    private final DataSource dataSource;

    private final RedisService redisService;

    private final UserDetailsServiceImpl userDetailsService;

    private final AuthenticationManager authenticationManager;

    private final RedisConnectionFactory redisConnectionFactory;

    private final TokenSerializerProperties tokenSerializerProperties;

    private final GlobalWebResponseExceptionTranslator globalWebResponseExceptionTranslator;

    /**
     * 配置授权服务器端点的非安全功能，如令牌存储、令牌自定义、用户批准和授权类型。
     * 默认情况下你不需要做任何事情，除非你需要密码授权，在这种情况下你需要提供一个 {@link AuthenticationManager}。 *
     * @param endpoints 端点配置器
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
                // 允许令牌的请求方式
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
                // 配置grant_type模式
                .tokenGranter(tokenGranter(endpoints))
                // 配置token存储，一般配置redis存储
                .tokenStore(redisTokenStore())
                // 拓展token信息
                .tokenEnhancer(tokenEnhancer())
                // 配置用户详情server，密码模式必须
                .userDetailsService(userDetailsService)
                // 配置认证管理器
                .authenticationManager(authenticationManager)
                // 重复使用reuseRefreshToken
                .reuseRefreshTokens(false)
                // 自定义TokenServices，从数据库获取token存储时长
                .tokenServices(tokenServices())
                // 自定义异常处理 /oauth/token 异常处理
                .exceptionTranslator(globalWebResponseExceptionTranslator);
    }

    /**
     * 创建grant_type列表，如果不配置则默认使用密码模式、简化模式、验证码模式以及刷新token模式，如果配置了只使用配置中，默认配置失效
     * 具体可以查询AuthorizationServerEndpointsConfigurer中的getDefaultTokenGranters方法
     * org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer.getDefaultTokenGranters
     * @param endpoints 端点配置器
     * @return TokenGranter
     */
    private TokenGranter tokenGranter(AuthorizationServerEndpointsConfigurer endpoints) {
        TokenGranter tokenGranter = endpoints.getTokenGranter();
        ArrayList<TokenGranter> tokenGranters = new ArrayList<>(Collections.singletonList(tokenGranter));
        // 添加一个自定义登陆模式
        tokenGranters.add(new ThirdPartyTokenGranter(authenticationManager, endpoints.getTokenServices(), endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory()));
        // 添加一个自定义手机号验证码模式
        tokenGranters.add(new MobileTokenGranter(authenticationManager, endpoints.getTokenServices(), endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory()));
        return new CompositeTokenGranter(tokenGranters);
    }

    /**
     * <p>
     * 注意，自定义TokenServices的时候，注意这个认证可能会和资源冲突=》org.springframework.security.oauth2.provider.token.ResourceServerTokenServices
     * 需要设置@Primary，否则报错，
     * 定义该bean之后，token令牌存储时间就会从数据库获取，对应字段=》access_token_validity，refresh_token_validity
     * </p>
     * @return
     */
    @Bean
    public AuthorizationServerTokenServices tokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(redisTokenStore());
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setTokenEnhancer(tokenEnhancer());
        tokenServices.setClientDetailsService(clientDetailsService());
        return tokenServices;
    }

    /**
     * 令牌存储
     * @return TokenStore
     */
    @Bean
    public TokenStore redisTokenStore() {
        // 使用自定义RedisTokenStore，配置token续签规则
        RedisTokenStore redisTokenStore = new RedisTokenStore(clientDetailsService(), redisConnectionFactory);
        // 开启缓存json序列化，默认jdk序列化，不方便查看
        if (tokenSerializerProperties.getEnabled()) {
            // 使用fastjson
            if (tokenSerializerProperties.getType().equals(TokenSerializerProperties.TypeEnums.FAST_JSON)) {
                redisTokenStore.setPrefix(CacheConstants.OAUTH_TOKEN_STORE_FAST_JSON);
                redisTokenStore.setSerializationStrategy(new FastJsonSerializationStrategy());
            }
            // 使用jackson
            else {
                redisTokenStore.setPrefix(CacheConstants.OAUTH_TOKEN_STORE_JACKSON);
                redisTokenStore.setSerializationStrategy(new JacksonSerializationStrategy(tokenSerializerProperties.getTypeEnabled()));
            }
        } else {
            // 配置默认缓存前缀
            redisTokenStore.setPrefix(CacheConstants.OAUTH_TOKEN_STORE_DEFAULT);
        }

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
     * 配置客户端详情
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 可以使自带JdbcClientDetailsService
        // clients.withClientDetails(new RedisClientDetailsService(dataSource));
        // 这里配置查询客户端
        clients.withClientDetails(clientDetailsService());
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

    /**
     * 声明 ClientDetails实现
     */
    public ClientDetailsService clientDetailsService() {
        return new RedisClientDetailsServiceImpl(dataSource);
    }

    /**
     * 拓展token信息
     * @return TokenEnhancer
     */
    @Bean
    public TokenEnhancer tokenEnhancer() {
        return (accessToken, authentication) -> {
            final Map<String, Object> additionalInfo = new HashMap<>(2);
            if (null != authentication.getUserAuthentication()) {
                User user = ObjectUtil.cast(authentication.getUserAuthentication().getPrincipal());
                additionalInfo.put(Oauth2Constants.DETAILS_USER_ID, user.getId());
                additionalInfo.put(Oauth2Constants.DETAILS_USERNAME, user.getUsername());
            }
            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo); // 看源码发现这是颁发令牌的类
            return accessToken;
        };
    }

    /**
     * 配置令牌端点(Token Endpoint)的安全约束
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security.allowFormAuthenticationForClients().checkTokenAccess("permitAll()");
    }

    /**
     * 配置授权码模式授权码服务,不配置默认为内存模式
     */
    @Primary
    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        // 配置redis授权码模式
        return new RedisAuthorizationCodeServicesImpl(redisService);
    }

    /**
     * 自定义配置身份认证入口端点，自定义令牌端异常，也可以 implements AuthenticationEntryPoint 注入bean
     * 或者extends OAuth2AuthenticationEntryPoint 重写commence
     * @return
     */
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, e) -> {
            response.setStatus(HttpStatus.HTTP_OK);
            response.setHeader(HttpHeaders.CONTENT_TYPE, StringConstants.APPLICATION_JSON);
            response.setHeader(StringConstants.ACCESS_CONTROL_ALLOW_ORIGIN, StringConstants.ASTERISK);
            response.setHeader(StringConstants.CACHE_CONTROL, StringConstants.NO_CACHE);
            Result<Object> result = ResultUtil.custom(HttpCodeEnum.INTERNAL_SERVER_ERROR.getCode(), e.getMessage());
            response.getWriter().print(JSONUtil.toJsonStr(result));
            response.getWriter().flush();
        };
    }
}
