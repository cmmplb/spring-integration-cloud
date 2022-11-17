package com.cmmplb.security.oauth2.starter.annotation;

import java.lang.annotation.*;

/**
 * @author penglibo
 * @date 2022-08-18 14:11:57
 * @since jdk 1.8
 * 标记此注解的接口不需要登录权限
 */

@Target(value = {ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface WithoutLogin {

}
