package com.cmmplb.security.oauth2.starter.handler;

import com.cmmplb.core.result.HttpCodeEnum;
import com.cmmplb.core.result.Result;
import com.cmmplb.core.result.ResultUtil;
import com.cmmplb.security.oauth2.starter.handler.exceptions.MobileNotFoundException;
import com.cmmplb.security.oauth2.starter.handler.exceptions.OAuth2MobileCodeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.DefaultThrowableAnalyzer;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.web.util.ThrowableAnalyzer;

/**
 * @author penglibo
 * @date 2021-09-03 11:53:41
 * @since jdk 1.8
 * 认证服务器异常
 */

@Slf4j
public class GlobalWebResponseExceptionTranslator implements WebResponseExceptionTranslator<OAuth2Exception> {

    private final ThrowableAnalyzer throwableAnalyzer = new DefaultThrowableAnalyzer();

    // 这里添加SuppressWarnings忽略泛型提示，如果添加泛型，下面的Result就限制不能返回了
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public ResponseEntity translate(Exception e) {
        Throwable[] causeChain = throwableAnalyzer.determineCauseChain(e);
        Exception ase;

        Result<String> result = null;
        // 账号密码认证失败=>账号或密码错误
        ase = (org.springframework.security.core.userdetails.UsernameNotFoundException) throwableAnalyzer.getFirstThrowableOfType(UsernameNotFoundException.class,
                causeChain);
        if (null != ase) {
            result = ResultUtil.custom(HttpCodeEnum.BAD_CREDENTIALS);
        }

        // 手机号认证失败=>手机号或验证码错误
        ase = (MobileNotFoundException) throwableAnalyzer.getFirstThrowableOfType(MobileNotFoundException.class,
                causeChain);
        if (ase != null) {
            result = ResultUtil.custom(HttpCodeEnum.MOBILE_NOT_FOUND);
        }

        // 手机号认证失败
        ase = (OAuth2MobileCodeException) throwableAnalyzer.getFirstThrowableOfType(OAuth2MobileCodeException.class,
                causeChain);
        if (ase != null) {
            result = ResultUtil.custom(HttpCodeEnum.MOBILE_NOT_FOUND.getCode(), ase.getMessage());
        }

        // 无效授权=>账号或密码错误
        ase = (InvalidGrantException) throwableAnalyzer.getFirstThrowableOfType(InvalidGrantException.class,
                causeChain);
        if (null != ase) {
            result = ResultUtil.custom(HttpCodeEnum.BAD_CREDENTIALS);
        }

        // 无效token
        ase = (InvalidTokenException) throwableAnalyzer.getFirstThrowableOfType(InvalidTokenException.class,
                causeChain);
        if (null != ase) {
            result = ResultUtil.custom(HttpCodeEnum.UNAUTHORIZED);
        }

        if (null == result) {
            result = ResultUtil.custom(e.getMessage());
            // 不包含上述异常则服务器内部错误
            log.error("认证服务器异常：", e);
        } else {
            log.info("认证服务器异常:{}", e.getMessage());
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
