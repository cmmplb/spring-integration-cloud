package com.cmmplb.security.oauth2.auth.config;

import com.cmmplb.common.redis.service.RedisService;
import com.cmmplb.security.oauth2.auth.exception.resource.filter.Oauth2TokenFilter;
import com.cmmplb.security.oauth2.auth.handler.BaseAuthenticationFailureHandler;
import com.cmmplb.security.oauth2.auth.handler.SsoLogoutSuccessHandler;
import com.cmmplb.security.oauth2.auth.provider.MobileAuthenticationProvider;
import com.cmmplb.security.oauth2.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 * @author penglibo
 * @date 2021-09-08 14:25:39
 * @since jdk 1.8
 * security web安全配置,spring-cloud-starter-oauth2依赖于security
 * 默认情况下SecurityConfigurerAdapter执行比ResourceServerConfig先
 */

@Configuration
@EnableWebSecurity
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

    //@Autowired
    //private BaseAuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    private BaseAuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;

    @Autowired
    @Qualifier("authenticationProviderImpl")
    private AuthenticationProvider authenticationProvider;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .authenticationProvider(mobileAuthenticationProvider()) // 添加手机号登录处理
                // .addFilterBefore(new Oauth2TokenFilter(), BasicAuthenticationFilter.class) // 添加token拦截
                .formLogin() // 表单登录
                .loginPage("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                // .loginProcessingUrl("/login") // 处理表单登录 URL
                // .successHandler(authenticationSuccessHandler) // 处理登录成功
                .failureHandler(authenticationFailureHandler) // 处理登录失败
                //.and()
                //.logout() // 处理sso退出
                //.logoutUrl("/signout")
                //.logoutSuccessUrl("/")
                //.logoutSuccessHandler(logoutSuccessHandler()).deleteCookies("JSESSIONID").invalidateHttpSession(true)
                .and()
                .authorizeRequests()
                .antMatchers("/user/**").hasAnyRole("ADMIN") // /user/ 开头的URL需要 ADMIN 权限
                // 拦截地址
                //.antMatchers("/actuator/**", "/swagger-ui.html").denyAll()
                // 放行地址
                .antMatchers("/basic/sms/code", "/basic/map","/login/**", "/logout").permitAll()
                .anyRequest().authenticated()
                .and()
                .csrf().disable();
    }

    /**
     * 这里是对认证管理器的添加配置
     * 使用自定义的验证器
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
        auth.authenticationProvider(authenticationProvider);
        auth.authenticationProvider(authenticationProvider());
        auth.authenticationProvider(mobileAuthenticationProvider());
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    /**
     * 不要直接使用@Bean注入 会导致默认的提供者无法注入（DaoAuthenticationProvider）
     */
    private MobileAuthenticationProvider mobileAuthenticationProvider() {
        MobileAuthenticationProvider mobileAuthenticationProvider = new MobileAuthenticationProvider();
        mobileAuthenticationProvider.setUserService(userService);
        mobileAuthenticationProvider.setRedisService(redisService);
        return mobileAuthenticationProvider;
    }

    /**
     * SSO 退出
     */
    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new SsoLogoutSuccessHandler();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * https://spring.io/blog/2017/11/01/spring-security-5-0-0-rc1-released#password-storage-updated
     * Encoded password does not look like BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
