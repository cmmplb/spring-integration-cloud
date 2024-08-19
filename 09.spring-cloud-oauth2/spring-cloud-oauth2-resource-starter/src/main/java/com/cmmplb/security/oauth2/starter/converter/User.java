package com.cmmplb.security.oauth2.starter.converter;

import lombok.Getter;

import java.util.Collection;

/**
 * @author penglibo
 * @date 2021-09-03 11:48:38
 * @since jdk 1.8
 */

public class User extends org.springframework.security.core.userdetails.User {

    private static final long serialVersionUID = -1332147059551881240L;

    /**
     * 主键
     */
    @Getter
    private final Long id;

    public User(Long id, String username, String password,
                boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked,
                Collection<? extends SimpleGrantedAuthority> authorities) {
        super(username, password, true, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.id = id;
    }

    public static final String COLUMN_ID = "id";

    public static final String COLUMN_USERNAME = "username";

    public static final String COLUMN_PASSWORD = "password";

    public static final String COLUMN_ACCOUNT_NON_EXPIRED = "accountNonExpired";

    public static final String COLUMN_CREDENTIALS_NON_EXPIRED = "credentialsNonExpired";

    public static final String COLUMN_ACCOUNT_NON_LOCKED = "accountNonLocked";

    public static final String COLUMN_AUTHORITIES = "authorities";

    public static final String COLUMN_ADDITIONAL_INFORMATION = "additionalInformation";


}
