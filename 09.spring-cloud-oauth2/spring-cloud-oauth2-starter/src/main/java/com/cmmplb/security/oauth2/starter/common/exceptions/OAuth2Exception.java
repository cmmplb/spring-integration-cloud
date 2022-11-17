package com.cmmplb.security.oauth2.starter.common.exceptions;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;

/**
 * @author penglibo
 * @date 2021-09-03 12:00:21
 * @since jdk 1.8
 * 认证服务器异常处理类
 */

@JsonSerialize(using = Oauth2StdSerializer.class)
public class OAuth2Exception extends org.springframework.security.oauth2.common.exceptions.OAuth2Exception {

    @Getter
    private String errorCode;

    private static final long serialVersionUID = -7406175446198237250L;

    public OAuth2Exception(String msg, Throwable t) {
        super(msg, t);
    }

    public OAuth2Exception(String msg) {
        super(msg);
    }

    public OAuth2Exception(String msg, String errorCode) {
        super(msg);
        this.errorCode = errorCode;
    }
}
