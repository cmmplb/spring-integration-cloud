package com.cmmplb.security.oauth2.starter.aspect;

import com.cmmplb.core.constants.SecurityConstant;
import com.cmmplb.core.exception.BusinessException;
import com.cmmplb.core.result.HttpCodeEnum;
import com.cmmplb.core.utils.ServletUtil;
import com.cmmplb.security.oauth2.starter.annotation.WithoutLogin;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * @author penglibo
 * @date 2021-11-12 21:03:21
 * @within自定义注解标注的类下所有的方法都会进入切面的方法
 * @annotation自定义注解标注的方法会进入切面
 * @since jdk 1.8
 * 内部服务调用切面拦截
 */

@Slf4j
@Aspect
public class SecurityInnerAspect {

    @SneakyThrows
    @Around("@within(com.cmmplb.security.oauth2.starter.annotation.WithoutLogin)")
    public Object around(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 获取方法上的注解
        WithoutLogin withoutLogin = signature.getMethod().getAnnotation(WithoutLogin.class);
        if (null == withoutLogin) {
            // 获取类上的注解
            withoutLogin = AnnotationUtils.findAnnotation(joinPoint.getTarget().getClass(), WithoutLogin.class);
        }
        if (null != withoutLogin && withoutLogin.value() && !SecurityConstant.INNER.equals(ServletUtil.getRequest().getHeader(SecurityConstant.SOURCE))) {
            throw new BusinessException(HttpCodeEnum.FORBIDDEN.getCode(), HttpCodeEnum.FORBIDDEN.getCode(), "内部服务,外部禁止访问-未授权");
        }
        return joinPoint.proceed();
    }
}
