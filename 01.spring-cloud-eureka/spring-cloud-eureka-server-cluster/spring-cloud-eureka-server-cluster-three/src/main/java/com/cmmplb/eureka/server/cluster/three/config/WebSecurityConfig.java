package com.cmmplb.eureka.server.cluster.three.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author penglibo
 * @date 2021-12-21 17:39:51
 * @since jdk 1.8
 * 新版本的spring-cloud2.0中： Spring Security默认开启了CSRF攻击防御
 * CSRF会将微服务的注册也给过滤了，虽然不会影响注册中心，但是其他客户端是注册不了的。
 */

@EnableWebSecurity
public class WebSecurityConfig  extends WebSecurityConfigurerAdapter{

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().authenticated().and().httpBasic().and().csrf().disable();
    }
}
