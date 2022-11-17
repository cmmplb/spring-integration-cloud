package com.cmmplb.security.oauth2.starter.annotation;

import java.lang.annotation.*;

/**
 * @author penglibo
 * @date 2021-11-12 17:15:14
 * @since jdk 1.8
 * 忽略服务内部调用认证
 */

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthIgnore {

    /**
     * aop权限拦截
     */
    boolean value() default true;
}
