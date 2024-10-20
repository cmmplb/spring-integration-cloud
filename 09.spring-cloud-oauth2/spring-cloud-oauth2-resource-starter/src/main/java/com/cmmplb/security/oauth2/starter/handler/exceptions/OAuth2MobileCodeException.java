package com.cmmplb.security.oauth2.starter.handler.exceptions;

import io.github.cmmplb.core.result.HttpCodeEnum;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

/**
 * @author penglibo
 * @date 2021-10-20 11:52:25
 * @since jdk 1.8
 */

public class OAuth2MobileCodeException extends OAuth2Exception {

    private static final long serialVersionUID = 4140857108980837371L;

    public OAuth2MobileCodeException(String msg, Throwable t) {
        super(msg, t);
    }

    public OAuth2MobileCodeException(String msg) {
        super(msg);
    }

    @Override
    public String getOAuth2ErrorCode() {
        return HttpCodeEnum.BAD_CREDENTIALS.getMessage();
    }

    @Override
    public int getHttpErrorCode() {
        return HttpCodeEnum.BAD_CREDENTIALS.getCode();
    }
}
