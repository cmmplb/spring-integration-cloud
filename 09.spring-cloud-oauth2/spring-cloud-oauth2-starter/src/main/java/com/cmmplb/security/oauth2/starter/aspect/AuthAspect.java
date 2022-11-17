package com.cmmplb.security.oauth2.starter.aspect;

import com.cmmplb.core.exception.CustomException;
import com.cmmplb.core.result.HttpCodeEnum;
import com.cmmplb.core.utils.ServletUtil;
import com.cmmplb.security.oauth2.starter.annotation.AuthIgnore;
import com.cmmplb.security.oauth2.starter.constants.Oauth2Constants;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * @author penglibo
 * @date 2021-11-12 21:03:21
 * @since jdk 1.8
 * 内部服务调用切面拦截
 */

@Slf4j
@Aspect
public class AuthAspect implements Ordered {

    @Pointcut("@annotation(com.cmmplb.security.oauth2.starter.annotation.AuthIgnore)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        AuthIgnore authIgnore = signature.getMethod().getAnnotation(AuthIgnore.class);
        if (null == authIgnore) {
            authIgnore = AnnotationUtils.findAnnotation(joinPoint.getTarget().getClass(), AuthIgnore.class);
        }
        if (null != authIgnore
                && authIgnore.value()
                && !Oauth2Constants.IN.equals(ServletUtil.getRequest().getHeader(Oauth2Constants.INTERNAL))) {
            throw new CustomException("内部服务,外部" + HttpCodeEnum.FORBIDDEN.getMessage());
        }
        return joinPoint.proceed();
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }
}
