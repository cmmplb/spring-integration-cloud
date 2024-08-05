package com.cmmplb.security.oauth2.starter.annotation;

import java.lang.annotation.*;

/**
 * @author penglibo
 * @date 2022-08-18 14:11:57
 * @since jdk 1.8
 * 标记此注解的资源不需要登录
 */

@Target(value = {ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface WithoutLogin {

    /**
     * 开启内部服务aop权限拦截
     */
    boolean value() default false;
}