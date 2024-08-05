package com.cmmplb.security.oauth2.starter.handler.exceptions;

import com.cmmplb.core.result.HttpCodeEnum;

/**
 * @author penglibo
 * @date 2021-10-20 11:52:25
 * @since jdk 1.8
 */

public class MobileCodeException extends OAuth2Exception {

    private static final long serialVersionUID = 4140857108980837371L;

    public MobileCodeException(String msg, Throwable t) {
        super(msg, t);
    }

    public MobileCodeException(String msg) {
        super(msg);
    }

    @SuppressWarnings("AlibabaLowerCamelCaseVariableNaming")
    @Override
    public String getOAuth2ErrorCode() {
        return HttpCodeEnum.BAD_CREDENTIALS.getMessage();
    }

    @Override
    public int getHttpErrorCode() {
        return HttpCodeEnum.BAD_CREDENTIALS.getCode();
    }
}
