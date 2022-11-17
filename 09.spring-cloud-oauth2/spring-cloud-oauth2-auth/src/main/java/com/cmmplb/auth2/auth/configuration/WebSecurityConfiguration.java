package com.cmmplb.auth2.auth.configuration;

import com.cmmplb.core.utils.MD5Util;
import com.cmmplb.redis.service.RedisService;
import com.cmmplb.security.oauth2.starter.provider.granter.mobile.MobileAuthenticationProvider;
import com.cmmplb.security.oauth2.starter.provider.granter.thirdParty.ThirdPartyAuthenticationProvider;
import com.cmmplb.auth2.auth.handler.AuthenticationFailureHandler;
import com.cmmplb.security.oauth2.starter.handler.SsoLogoutSuccessHandler;
import com.cmmplb.security.oauth2.starter.provider.AuthenticationProviderImpl;
import com.cmmplb.security.oauth2.starter.service.impl.UserDetailsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

/**
 * @author penglibo
 * @date 2021-11-10 14:17:33
 * @since jdk 1.8
 * security web安全配置,spring-cloud-starter-oauth2依赖于security
 * 默认情况下SecurityConfigurerAdapter执行比ResourceServerConfig先
 */

@Slf4j
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    private RedisService redisService;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                // 添加自定义登录处理
                .authenticationProvider(thirdPartyAuthenticationProvider())
                // 添加手机号登录处理
                .authenticationProvider(mobileAuthenticationProvider())

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

                .antMatchers("/actuator/**").denyAll()
                .antMatchers("/oauth/**", "/login", "/do/logout", "/error", "/css/**", "/static/**").permitAll()
                .anyRequest().authenticated()
                .and().csrf().disable();
    }

    /**
     * 这里是对认证管理器的添加配置
     * 使用自定义的验证器
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsServiceImpl).passwordEncoder(passwordEncoder());
        auth.authenticationProvider(authenticationProvider());
        auth.authenticationProvider(authenticationProviderImpl());
        auth.authenticationProvider(mobileAuthenticationProvider());
        auth.authenticationProvider(thirdPartyAuthenticationProvider());
    }

    /**
     * 自定义验证
     */
    private AuthenticationProviderImpl authenticationProviderImpl() {
        AuthenticationProviderImpl authenticationProvider = new AuthenticationProviderImpl();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsServiceImpl(userDetailsServiceImpl);
        return authenticationProvider;
    }

    /**
     * 默认的提供者
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsServiceImpl);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    /**
     * 不要直接使用@Bean注入 会导致默认的提供者无法注入（DaoAuthenticationProvider）,↑
     */
    private ThirdPartyAuthenticationProvider thirdPartyAuthenticationProvider() {
        ThirdPartyAuthenticationProvider thirdPartyAuthenticationProvider = new ThirdPartyAuthenticationProvider();
        thirdPartyAuthenticationProvider.setUserDetailsServiceImpl(userDetailsServiceImpl);
        return thirdPartyAuthenticationProvider;
    }

    /**
     * 不要直接使用@Bean注入 会导致默认的提供者无法注入（DaoAuthenticationProvider）
     */
    private MobileAuthenticationProvider mobileAuthenticationProvider() {
        MobileAuthenticationProvider mobileAuthenticationProvider = new MobileAuthenticationProvider();
        mobileAuthenticationProvider.setUserService(userDetailsServiceImpl);
        mobileAuthenticationProvider.setRedisService(redisService);
        return mobileAuthenticationProvider;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * SSO 退出
     */
    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new SsoLogoutSuccessHandler();
    }

    /**
     * https://spring.io/blog/2017/11/01/spring-security-5-0-0-rc1-released#password-storage-updated
     * Encoded password does not look like BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {

        // 自定义MD5加密方式
        return new PasswordEncoder() {

            /**
             * MD5加密
             */
            @Override
            public String encode(CharSequence rawPassword) {
                return MD5Util.encode(String.valueOf(rawPassword));
            }

            /**
             * rawPassword用户输入的，encodedPassword数据库查出来的
             */
            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return encodedPassword.equals(MD5Util.encode(String.valueOf(rawPassword)));
            }
        };
        // return new BCryptPasswordEncoder(10);
        // return new NoOpPasswordEncoder(); // 不使用加密，明文对比
    }

}
