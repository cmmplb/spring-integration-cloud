package com.cmmplb.security.oauth2.starter.converter;

import cn.hutool.core.convert.Convert;
import com.cmmplb.core.utils.ObjectUtil;
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

    private static final String PRINCIPAL = "principal";

    /**
     * org.springframework.security.oauth2.provider.token.AccessTokenConverter#convertAccessToken(org.springframework.security.oauth2.common.OAuth2AccessToken, org.springframework.security.oauth2.provider.OAuth2Authentication)
     * 获取令牌中的用户信息，转换成LinkedHashMap
     * @return
     */
    @Override
    public Map<String, ?> convertUserAuthentication(Authentication authentication) {
        Map<String, Object> authMap = new LinkedHashMap<>();
        authMap.put(USERNAME, authentication.getName());
        Object principal = authentication.getPrincipal();
        if (principal instanceof User) {
            authMap.put(PRINCIPAL, principal);
        }
        if (authentication.getAuthorities() != null && !authentication.getAuthorities().isEmpty()) {
            authMap.put(AUTHORITIES, AuthorityUtils.authorityListToSet(authentication.getAuthorities()));
        }
        return authMap;
    }

    /**
     * 通过上一步提取到的map集合，构建Authentication对象
     * @return
     */
    @Override
    public Authentication extractAuthentication(Map<String, ?> map) {
        if (map.containsKey(USERNAME)) {
            Collection<? extends SimpleGrantedAuthority> authorities = getAuthorities(map);
            Object principal = map.get(USERNAME);
            // 可以从最外层map获取
            // Long userId = Convert.toLong(map.get(Oauth2Constants.DETAILS_USER_ID));
            // String username = map.get(Oauth2Constants.DETAILS_USERNAME).toString();
            if (map.containsKey(PRINCIPAL)) {
                // 也可以从上面封装的principal获取
                LinkedHashMap<String, Object> res = ObjectUtil.cast(map.get(PRINCIPAL));
                Long userId = Convert.toLong(map.get(Oauth2Constants.DETAILS_USER_ID));
                String username = map.get(Oauth2Constants.DETAILS_USERNAME).toString();
                Boolean accountNonExpired = Convert.toBool(res.get(User.COLUMN_ACCOUNT_NON_EXPIRED));
                Boolean credentialsNonExpired = Convert.toBool(res.get(User.COLUMN_CREDENTIALS_NON_EXPIRED));
                Boolean accountNonLocked = Convert.toBool(res.get(User.COLUMN_ACCOUNT_NON_LOCKED));
                User user = new User(userId, username, N_A, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
                return new UsernamePasswordAuthenticationToken(user, N_A, authorities);
            }
            return new UsernamePasswordAuthenticationToken(principal, N_A, authorities);
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
