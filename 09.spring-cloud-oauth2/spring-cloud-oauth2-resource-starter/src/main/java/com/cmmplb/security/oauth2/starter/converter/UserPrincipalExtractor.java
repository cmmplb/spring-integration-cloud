package com.cmmplb.security.oauth2.starter.converter;

import cn.hutool.core.convert.Convert;
import io.github.cmmplb.core.utils.ObjectUtil;
import com.cmmplb.security.oauth2.starter.utils.AuthorityUtil;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @author penglibo
 * @date 2024-08-12 13:32:50
 * @since jdk 1.8
 * 自定义用户信息提取, 参照
 * {@link org.springframework.boot.autoconfigure.security.oauth2.resource.FixedPrincipalExtractor}
 * 实现获取的principal信息为User对象
 */
public class UserPrincipalExtractor implements PrincipalExtractor {

    private static final String[] PRINCIPAL_KEYS = new String[]{"user", "username",
            "userid", "user_id", "login", "id", "name"};

    private static final String[] AUTHORITY_KEYS = {"authority", "role", "value"};

    private static final String N_A = "N/A";


    private static final String PRINCIPAL = "principal";

    @Override
    public Object extractPrincipal(Map<String, Object> map) {
        // 重写转换principal信息为User对象
        if (map.containsKey(PRINCIPAL)) {
            LinkedHashMap<String, Object> res = ObjectUtil.cast(map.get(PRINCIPAL));
            Long id = Convert.toLong(res.get(User.COLUMN_ID));
            String username = Convert.toStr(res.get(User.COLUMN_USERNAME));
            Boolean accountNonExpired = Convert.toBool(res.get(User.COLUMN_ACCOUNT_NON_EXPIRED));
            Boolean credentialsNonExpired = Convert.toBool(res.get(User.COLUMN_CREDENTIALS_NON_EXPIRED));
            Boolean accountNonLocked = Convert.toBool(res.get(User.COLUMN_ACCOUNT_NON_LOCKED));
            // 这里获取权限信息，
            String authorities = asAuthorities(map.get(User.COLUMN_AUTHORITIES));
            return new User(id, username, N_A, accountNonExpired, credentialsNonExpired, accountNonLocked,
                    AuthorityUtil.commaSeparatedStringToAuthorityList(authorities));
        }
        for (String key : PRINCIPAL_KEYS) {
            if (map.containsKey(key)) {
                return map.get(key);
            }
        }
        return null;
    }

    /**
     * {@link org.springframework.boot.autoconfigure.security.oauth2.resource.FixedAuthoritiesExtractor}
     */
    private String asAuthorities(Object object) {
        List<Object> authorities = new ArrayList<>();
        if (object instanceof Collection) {
            Collection<?> collection = (Collection<?>) object;
            object = collection.toArray(new Object[0]);
        }
        if (ObjectUtils.isArray(object)) {
            Object[] array = (Object[]) object;
            for (Object value : array) {
                if (value instanceof String) {
                    authorities.add(value);
                } else if (value instanceof Map) {
                    authorities.add(asAuthority((Map<?, ?>) value));
                } else {
                    authorities.add(value);
                }
            }
            return StringUtils.collectionToCommaDelimitedString(authorities);
        }
        return object.toString();
    }

    private Object asAuthority(Map<?, ?> map) {
        if (map.size() == 1) {
            return map.values().iterator().next();
        }
        for (String key : AUTHORITY_KEYS) {
            if (map.containsKey(key)) {
                return map.get(key);
            }
        }
        return map;
    }
}
