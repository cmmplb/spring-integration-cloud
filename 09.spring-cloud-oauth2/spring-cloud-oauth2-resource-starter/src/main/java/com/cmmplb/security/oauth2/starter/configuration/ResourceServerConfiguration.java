package com.cmmplb.security.oauth2.starter.configuration;

import com.cmmplb.security.oauth2.starter.configuration.properties.SecurityProperties;
import com.cmmplb.security.oauth2.starter.handler.AccessDeniedHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * @author penglibo
 * @date 2021-10-20 15:23:47
 * @since jdk 1.8
 */

@Slf4j
@AllArgsConstructor
@EnableConfigurationProperties(SecurityProperties.class)
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    private final TokenStore tokenStore;

    private final SecurityProperties securityProperties;

    private final AccessDeniedHandler accessDeniedHandler;

    private final AuthenticationEntryPoint authenticationEntryPoint;

    private final ResourceServerTokenServices resourceServerTokenServices;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        // token存取验证, 如果使用了令牌服务ResourceServerTokenServices/RemoteTokenServices或者UserInfoTokenServices, 会向认证服务发起请求验证
        resources.tokenStore(tokenStore);
        // 校验token
        resources.tokenServices(resourceServerTokenServices);
        // 权限不足处理
        resources.accessDeniedHandler(accessDeniedHandler);
        // 异常端点处理
        resources.authenticationEntryPoint(authenticationEntryPoint);
        // 资源id
        // resources.resourceId(RESOURCE_ID).stateless(true);
    }

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
            securityProperties.getWhiteList().values().forEach(url -> registry.antMatchers(url.split(",")).permitAll());
            // 所匹配的 URL 都不允许被访问。denyAll
            registry.anyRequest().authenticated().and().csrf().disable();
        }
    }

}
