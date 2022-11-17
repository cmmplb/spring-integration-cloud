package com.cmmplb.security.oauth2.starter.configuration;

import com.cmmplb.security.oauth2.starter.configuration.properties.SecurityIgnoreUrlProperties;
import com.cmmplb.security.oauth2.starter.configuration.properties.SecurityProperties;
import com.cmmplb.security.oauth2.starter.provider.converter.UserConverter;
import com.cmmplb.security.oauth2.starter.provider.converter.UserPrincipalExtractor;
import com.cmmplb.security.oauth2.starter.provider.error.AccessDeniedHandler;
import com.cmmplb.security.oauth2.starter.provider.token.RemoteTokenServices;
import com.cmmplb.security.oauth2.starter.web.ResourceAuthExceptionEntryPoint;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.oauth2.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;
import org.springframework.web.client.RestTemplate;

/**
 * @author penglibo
 * @date 2021-10-20 15:23:47
 * @since jdk 1.8
 */

@Slf4j
@AllArgsConstructor
@EnableConfigurationProperties({SecurityIgnoreUrlProperties.class, SecurityProperties.class})
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    private final RestTemplate restTemplate;

    private final SecurityProperties securityProperties;

    private final SecurityIgnoreUrlProperties securityIgnoreUrlProperties;

    private final AccessDeniedHandler accessDeniedHandler;

    private final RemoteTokenServices remoteTokenServices;

    private final OAuth2ClientProperties oAuth2ClientProperties;

    private final ResourceServerProperties resourceServerProperties;

    // private final UserInfoTokenServices userInfoTokenServices;

    private final ResourceAuthExceptionEntryPoint resourceAuthExceptionEntryPoint;

    /**
     * 配置资源接口安全，http.authorizeRequests()针对的所有url，但是由于登录页面url包含在其中，这里配置会进行token校验，校验不通过返回错误json，
     * 而授权码模式获取code时需要重定向登录页面，重定向过程并不能携带token，所有不能用http.authorizeRequests()，
     * 而是用requestMatchers().antMatchers("")，这里配置的是***需要资源接口拦截的url数组***
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        // ===========================================================
        // ecurity.sessions策略如下：
        //   -always：保存session状态（每次会话都保存，可能会导致内存溢出【Always create an {@link HttpSession}】）
        //   -never：不会创建HttpSession，但是会使用已经存在的HttpSession[Spring Security will never create an {@link HttpSession}]
        //   -if_required：仅在需要HttpSession创建【Spring Security will only create an {@link HttpSession} if required】
        //   -stateless：不会保存session状态【 Spring Security will never create an {@link HttpSession} and it will never use it
        // 注意：stateless策略推荐使用，也是默认配置
        // ===========================================================
        http.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = http.authorizeRequests();
        // 不登录可以访问
        // registry.antMatchers("/actuator/**").denyAll();
        // 关闭资源验证
        if (!securityProperties.getEnabled()) {
            log.info("关闭资源验证");
            registry.anyRequest().permitAll();
        } else {
            // 配置不需要安全拦截url
            securityIgnoreUrlProperties.getWhiteList().values().forEach(url -> registry.antMatchers(url.split(",")).permitAll());
            // 所匹配的 URL 都不允许被访问。denyAll
            registry.anyRequest().authenticated().and().csrf().disable();
        }
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        // Oauth2自定义check_token返回解析参数（自定义UserAuthenticationConverter）
        DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();
        UserAuthenticationConverter userTokenConverter = new UserConverter();
        accessTokenConverter.setUserTokenConverter(userTokenConverter);
        remoteTokenServices.setRestTemplate(restTemplate);
        remoteTokenServices.setClientId(oAuth2ClientProperties.getClientId());
        remoteTokenServices.setClientSecret(oAuth2ClientProperties.getClientSecret());
        remoteTokenServices.setCheckTokenEndpointUrl(resourceServerProperties.getTokenInfoUri());
        remoteTokenServices.setAccessTokenConverter(accessTokenConverter);
        // 通过这个Bean，去远程调用认证服务器，验token
        // todo:这个后期优化成先从本地验证，不存在再调用远程
        // resources.tokenServices(remoteTokenServices);
        // 权限不足处理
        resources.accessDeniedHandler(accessDeniedHandler);
        // todo:动态配置token验证规则
        // 配置了user-info-uri默认使用的就是userInfoTokenServices，这个这么配置只是为了设置principalExtractor
        // userInfoTokenServices.setPrincipalExtractor(principalExtractor());
        // resources.tokenServices(userInfoTokenServices);

        // token过期处理
        resources.authenticationEntryPoint(resourceAuthExceptionEntryPoint);
        resources.resourceId("auth-server").stateless(true);
    }

    /**
     * 自定义Principal提取器
     */
    @Bean
    public PrincipalExtractor principalExtractor() {
        return new UserPrincipalExtractor();
    }
}
