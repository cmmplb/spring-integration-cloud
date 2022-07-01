package com.cmmplb.security.oauth2.auth.exception.auth;

import com.cmmplb.core.result.HttpCodeEnum;
import com.cmmplb.security.oauth2.auth.mobile.MobileCodeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.DefaultThrowableAnalyzer;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InsufficientScopeException;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.web.util.ThrowableAnalyzer;
import org.springframework.web.HttpRequestMethodNotSupportedException;

/**
 * @author penglibo
 * @date 2021-09-03 11:53:41
 * @since jdk 1.8
 * 认证服务器异常=》自定义异常处理,重写oauth 默认实现
 */

@Slf4j
public class GlobalWebResponseExceptionTranslator implements WebResponseExceptionTranslator<org.springframework.security.oauth2.common.exceptions.OAuth2Exception> {

    private ThrowableAnalyzer throwableAnalyzer = new DefaultThrowableAnalyzer();

    @Override
    public ResponseEntity<org.springframework.security.oauth2.common.exceptions.OAuth2Exception> translate(Exception e) {
        log.error(e.getMessage(), e);
        // 从堆栈中提取SpringSecurityException
        Throwable[] causeChain = throwableAnalyzer.determineCauseChain(e);
        Exception ase;
        // 认证失败
        ase = (AuthenticationException) throwableAnalyzer.getFirstThrowableOfType(AuthenticationException.class,
                causeChain);
        if (ase != null) {
            return handleOAuth2Exception(new UnauthorizedException(e.getMessage(), e));
        }

        // 手机号验证码认证异常
        ase = (MobileCodeException) throwableAnalyzer.getFirstThrowableOfType(MobileCodeException.class,
                causeChain);
        if (null != ase) {
            return handleOAuth2Exception(new MobileCodeException(ase.getMessage(), ase));
        }

        // 权限不足
        ase = (AccessDeniedException) throwableAnalyzer.getFirstThrowableOfType(AccessDeniedException.class,
                causeChain);
        if (null != ase) {
            return handleOAuth2Exception(new ForbiddenException(ase.getMessage(), ase));
        }

        // 无效授权=>账号或密码错误
        ase = (org.springframework.security.oauth2.common.exceptions.InvalidGrantException) throwableAnalyzer.getFirstThrowableOfType(org.springframework.security.oauth2.common.exceptions.InvalidGrantException.class,
                causeChain);
        if (null != ase) {
            return handleOAuth2Exception(new InvalidGrantException(ase.getMessage(), ase));
        }

        // 无效令牌
        ase = (org.springframework.security.oauth2.common.exceptions.InvalidTokenException) throwableAnalyzer.getFirstThrowableOfType(org.springframework.security.oauth2.common.exceptions.InvalidTokenException.class,
                causeChain);
        if (null != ase) {
            return handleOAuth2Exception(new InvalidTokenException(ase.getMessage(), ase));
        }

        // 请求方式异常
        ase = (HttpRequestMethodNotSupportedException) throwableAnalyzer
                .getFirstThrowableOfType(HttpRequestMethodNotSupportedException.class, causeChain);
        if (null != ase) {
            HttpRequestMethodNotSupportedException h = (HttpRequestMethodNotSupportedException) ase;
            StringBuilder sb = new StringBuilder().append("不支持").append(h.getMethod()).append("请求方法，").append("支持");
            String[] methods = h.getSupportedMethods();
            if (methods != null) {
                for (String str : methods) {
                    sb.append(str);
                }
            }
            return handleOAuth2Exception(new MethodNotAllowed(sb.toString(), ase));
        }

        // 异常栈获取 OAuth2Exception 异常
        ase = (org.springframework.security.oauth2.common.exceptions.OAuth2Exception) throwableAnalyzer
                .getFirstThrowableOfType(org.springframework.security.oauth2.common.exceptions.OAuth2Exception.class, causeChain);
        if (ase != null) {
            return handleOAuth2Exception((org.springframework.security.oauth2.common.exceptions.OAuth2Exception) ase);
        }

        // 不包含上述异常则服务器内部错误
        return handleOAuth2Exception(new ServerErrorException(HttpStatus.OK.getReasonPhrase(), e));
    }

    // 使用自定义的异常处理类处理异常
    private ResponseEntity<org.springframework.security.oauth2.common.exceptions.OAuth2Exception> handleOAuth2Exception(org.springframework.security.oauth2.common.exceptions.OAuth2Exception e) {
        int status = e.getHttpErrorCode();
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CACHE_CONTROL, "no-store");
        headers.set(HttpHeaders.PRAGMA, "no-cache");
        if (status == HttpStatus.UNAUTHORIZED.value() || (e instanceof InsufficientScopeException)) {
            headers.set(HttpHeaders.WWW_AUTHENTICATE,
                    String.format("%s %s", OAuth2AccessToken.BEARER_TYPE, e.getSummary()));
        }
        return new ResponseEntity<>(e, headers, HttpStatus.OK);
    }

    private static class UnauthorizedException extends OAuth2Exception {

        private static final long serialVersionUID = 4140857108980837371L;

        public UnauthorizedException(String msg, Throwable t) {
            super(msg, t);
        }

        public String getOAuth2ErrorCode() {
            return HttpCodeEnum.BAD_CREDENTIALS.getMessage();
        }

        public int getHttpErrorCode() {
            return HttpCodeEnum.BAD_CREDENTIALS.getCode();
        }

    }

    private static class ForbiddenException extends OAuth2Exception {

        private static final long serialVersionUID = -938886052318365087L;

        public ForbiddenException(String msg, Throwable t) {
            super(msg, t);
        }
    }

    private static class MethodNotAllowed extends OAuth2Exception {

        private static final long serialVersionUID = 1852837114708577374L;

        public MethodNotAllowed(String msg, Throwable t) {
            super(msg, t);
        }
    }

    private static class InvalidGrantException extends OAuth2Exception {

        private static final long serialVersionUID = 1852837114708577374L;

        public InvalidGrantException(String msg, Throwable t) {
            super(msg, t);
        }

        public String getOAuth2ErrorCode() {
            return HttpCodeEnum.BAD_CREDENTIALS.getMessage();
        }

        public int getHttpErrorCode() {
            return HttpCodeEnum.BAD_CREDENTIALS.getCode();
        }
    }

    private static class InvalidTokenException extends OAuth2Exception {

        private static final long serialVersionUID = 1852837114708577374L;

        public InvalidTokenException(String msg, Throwable t) {
            super(msg, t);
        }
    }

    private static class ServerErrorException extends OAuth2Exception {

        private static final long serialVersionUID = 7503110732193705543L;

        public ServerErrorException(String msg, Throwable t) {
            super(msg, t);
        }

        public String getOAuth2ErrorCode() {
            return HttpCodeEnum.INTERNAL_SERVER_ERROR.getMessage();
        }

        public int getHttpErrorCode() {
            return HttpCodeEnum.INTERNAL_SERVER_ERROR.getCode();
        }
    }
}
