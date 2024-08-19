package com.cmmplb.security.oauth2.starter.utils;

import com.cmmplb.security.oauth2.starter.converter.SimpleGrantedAuthority;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author penglibo
 * @date 2024-08-12 15:55:10
 * @since jdk 1.8
 * 参照
 * {@link org.springframework.security.core.authority.AuthorityUtils}
 */
public class AuthorityUtil {

    public static List<SimpleGrantedAuthority> commaSeparatedStringToAuthorityList(String authorityString) {
        return createAuthorityList(StringUtils.tokenizeToStringArray(authorityString, ","));
    }

    public static List<SimpleGrantedAuthority> createAuthorityList(String... authorities) {
        List<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<>(authorities.length);
        for (String authority : authorities) {
            grantedAuthorities.add(new SimpleGrantedAuthority(authority));
        }
        return grantedAuthorities;
    }
}
