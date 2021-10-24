package com.cmmplb.security.oauth2.auth.mobile;

import com.cmmplb.core.result.HttpCodeEnum;
import com.cmmplb.security.oauth2.auth.exception.auth.OAuth2Exception;

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

    public String getOAuth2ErrorCode() {
        return HttpCodeEnum.BAD_CREDENTIALS.getMessage();
    }

    public int getHttpErrorCode() {
        return HttpCodeEnum.BAD_CREDENTIALS.getCode();
    }
}
