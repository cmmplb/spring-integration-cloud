package com.cmmplb.security.oauth2.starter.annotation;

import com.cmmplb.security.oauth2.starter.configuration.ResourceAutoConfiguration;
import com.cmmplb.security.oauth2.starter.configuration.SecurityImportBeanDefinitionRegistrar;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

import java.lang.annotation.*;

/**
 * @author penglibo
 * @date 2021-11-04 17:27:36
 * @since jdk 1.8
 * 资源服务注解
 */

@Documented
@Inherited
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@EnableGlobalMethodSecurity(prePostEnabled = true) // 开启注解权限控制
@org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer // 开启资源服务器
@Import({SecurityImportBeanDefinitionRegistrar.class, ResourceAutoConfiguration.class})
public @interface EnableResourceServer {

}
