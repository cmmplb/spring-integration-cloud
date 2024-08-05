package com.cmmplb.security.oauth2.starter.converter;

import cn.hutool.core.convert.Convert;
import com.cmmplb.security.oauth2.starter.constants.Oauth2Constants;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @author penglibo
 * @date 2021-10-15 17:29:40
 * @since jdk 1.8
 * 用户身份验证转换器=>仿照org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter
 */

public class UserConverter implements UserAuthenticationConverter {

    private static final String N_A = "N/A";

    /**
     * 通过DefaultAccessTokenConverter.convertAccessToken方法
     * 调用自定义的convertUserAuthentication将授权信息返回到资源服务
     * @return
     */
    @Override
    public Map<String, ?> convertUserAuthentication(Authentication authentication) {
        Map<String, Object> authMap = new LinkedHashMap<>();
        authMap.put(USERNAME, authentication.getName());
        if (authentication.getAuthorities() != null && !authentication.getAuthorities().isEmpty()) {
            authMap.put(AUTHORITIES, AuthorityUtils.authorityListToSet(authentication.getAuthorities()));
        }
        return authMap;
    }

    /**
     * 获取用户认证信息
     * @return
     */
    @Override
    public Authentication extractAuthentication(Map<String, ?> map) {
        if (map.containsKey(USERNAME)) {
            Collection<? extends SimpleGrantedAuthority> authorities = getAuthorities(map);
            Long userId = Convert.toLong(map.get(Oauth2Constants.DETAILS_USER_ID));
            String username = map.get(Oauth2Constants.DETAILS_USERNAME).toString();
            User user = new User(userId, username, N_A, true, true, true, authorities);
            return new UsernamePasswordAuthenticationToken(user, N_A, authorities);
        }
        return null;
    }

    /**
     * 获取权限资源信息
     */
    private Collection<? extends SimpleGrantedAuthority> getAuthorities(Map<String, ?> map) {
        Object authorities = map.get(AUTHORITIES);
        if (authorities instanceof String) {
            return commaSeparatedStringToAuthorityList((String) authorities);
        }
        if (authorities instanceof Collection) {
            return commaSeparatedStringToAuthorityList(
                    StringUtils.collectionToCommaDelimitedString((Collection<?>) authorities));
        }
        throw new IllegalArgumentException("Authorities must be either a String or a Collection");
    }

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
