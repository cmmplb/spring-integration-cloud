package com.cmmplb.security.oauth2.start.autoconfigure;

import com.cmmplb.security.oauth2.start.converter.UserConverter;
import com.cmmplb.security.oauth2.start.converter.UserPrincipalExtractor;
import com.cmmplb.security.oauth2.start.handler.AccessDeniedHandler;
import com.cmmplb.security.oauth2.start.point.ResourceAuthExceptionEntryPoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;

/**
 * @author penglibo
 * @date 2021-10-15 14:59:22
 * @since jdk 1.8
 * 配置资源服务器
 */

@Slf4j
@Order(100)
@EnableResourceServer // 开启资源服务器
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    private static final String RESOURCE_ID = "auth-server";

    @Autowired
    private OAuth2ClientProperties oAuth2ClientProperties;

    @Autowired
    private ResourceServerProperties resourceServerProperties;

    @Autowired
    private ResourceAuthExceptionEntryPoint resourceAuthExceptionEntryPoint;

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    // @Autowired
    // private UserInfoTokenServices userInfoTokenServices;

    /**
     * 配置资源接口安全，http.authorizeRequests()针对的所有url，但是由于登录页面url包含在其中，这里配置会进行token校验，校验不通过返回错误json，
     * 而授权码模式获取code时需要重定向登录页面，重定向过程并不能携带token，所有不能用http.authorizeRequests()，
     * 而是用requestMatchers().antMatchers("")，这里配置的是需要资源接口拦截的url数组
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                // 配置不需要安全拦截url
                .antMatchers("/resources/not/authentication", "/basic/sms/code", "/basic/map").permitAll()
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .anyRequest().authenticated();
    }

    /**
     * 这个是跟服务绑定的，注意要跟client配置一致，如果客户端没有，则不能访问
     * @param resources
     * @throws Exception
     */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId(RESOURCE_ID).stateless(true);
        // 配置了user-info-uri默认使用的就是userInfoTokenServices，这个这么配置只是为了设置principalExtractor
        // userInfoTokenServices.setPrincipalExtractor(principalExtractor());
        // resources.tokenServices(userInfoTokenServices);
        // resources.authenticationEntryPoint(resourceAuthExceptionEntryPoint); // token过期处理
        resources.accessDeniedHandler(accessDeniedHandler); // 权限不足处理
    }

    /**
     * 通过这个Bean，去远程调用认证服务器，验token
     * @return
     */
    @Bean
    @Primary
    public ResourceServerTokenServices tokenServices() {
        DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();
        UserAuthenticationConverter userTokenConverter = new UserConverter();
        accessTokenConverter.setUserTokenConverter(userTokenConverter);

        RemoteTokenServices remoteTokenServices = new RemoteTokenServices();
        remoteTokenServices.setClientId(oAuth2ClientProperties.getClientId());
        remoteTokenServices.setClientSecret(oAuth2ClientProperties.getClientSecret());
        remoteTokenServices.setCheckTokenEndpointUrl(resourceServerProperties.getTokenInfoUri());
        remoteTokenServices.setAccessTokenConverter(accessTokenConverter);
        return remoteTokenServices;
    }

    /**
     * 自定义Principal提取器
     */
    @Bean
    public PrincipalExtractor principalExtractor() {
        return new UserPrincipalExtractor();
    }
}
