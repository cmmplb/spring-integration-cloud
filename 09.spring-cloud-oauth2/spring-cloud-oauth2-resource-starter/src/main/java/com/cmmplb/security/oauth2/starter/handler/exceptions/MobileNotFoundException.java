package com.cmmplb.security.oauth2.starter.handler.exceptions;

import org.springframework.security.core.AuthenticationException;

/**
 * @author penglibo
 * @date 2022-08-28 15:01:47
 * @since jdk 1.8
 */

public class MobileNotFoundException extends AuthenticationException {

    private static final long serialVersionUID = 4765960517144116985L;

    /**
     * Constructs a <code>UsernameNotFoundException</code> with the specified message.
     * @param msg the detail message.
     */
    public MobileNotFoundException(String msg) {
        super(msg);
    }

    /**
     * Constructs a {@code UsernameNotFoundException} with the specified message and root
     * cause.
     * @param msg the detail message.
     * @param cause root cause
     */
    public MobileNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }

}