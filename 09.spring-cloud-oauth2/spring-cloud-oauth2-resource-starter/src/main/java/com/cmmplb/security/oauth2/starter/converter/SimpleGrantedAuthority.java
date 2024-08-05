package com.cmmplb.security.oauth2.starter.converter;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

/**
 * @author penglibo
 * @date 2022-08-31 09:35:03
 * @since jdk 1.8
 * 自定义一个，原来的SimpleGrantedAuthority没有无参构造函数，反序列化会失败
 * {@link org.springframework.security.core.authority.SimpleGrantedAuthority}
 */

@Data
@NoArgsConstructor
public class SimpleGrantedAuthority implements GrantedAuthority {

    private static final long serialVersionUID = 550L;

    private String role;

    public SimpleGrantedAuthority(String role) {
        Assert.hasText(role, "A granted authority textual representation is required");
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return this.role;
    }
}
