package com.cmmplb.security.oauth2.start.converter;

import com.cmmplb.security.oauth2.start.entity.AuthUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @author penglibo
 * @date 2021-10-15 17:29:40
 * @since jdk 1.8
 * 用户身份验证转换器
 */

public class UserConverter implements UserAuthenticationConverter {

    private static final String N_A = "N/A";

    /**
     * 将授权信息返回到资源服务
     * @param authentication
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
     * @param map
     * @return
     */
    @Override
    public Authentication extractAuthentication(Map<String, ?> map) {
        if (map.containsKey(USERNAME)) {
            Collection<? extends GrantedAuthority> authorities = getAuthorities(map);
            List<GrantedAuthority> list = new ArrayList<>(authorities);
            Long userId = Long.parseLong(map.get("id").toString());
            String username = map.get("username").toString();
            AuthUser user = new AuthUser(userId, username, N_A, true, true, true, true, list);
            return new UsernamePasswordAuthenticationToken(user, N_A, authorities);
        }
        return null;
    }

    /**
     * 获取权限资源信息
     */
    private Collection<? extends GrantedAuthority> getAuthorities(Map<String, ?> map) {
        Object authorities = map.get(AUTHORITIES);
        if (authorities instanceof String) {
            return AuthorityUtils.commaSeparatedStringToAuthorityList((String) authorities);
        }
        if (authorities instanceof Collection) {
            return AuthorityUtils.commaSeparatedStringToAuthorityList(
                    StringUtils.collectionToCommaDelimitedString((Collection<?>) authorities));
        }
        throw new IllegalArgumentException("Authorities must be either a String or a Collection");
    }
}
