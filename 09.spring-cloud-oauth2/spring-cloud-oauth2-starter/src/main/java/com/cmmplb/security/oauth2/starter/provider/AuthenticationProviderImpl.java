package com.cmmplb.security.oauth2.starter.provider;

import com.cmmplb.security.oauth2.starter.service.impl.UserDetailsServiceImpl;
import com.cmmplb.security.oauth2.starter.constants.Oauth2Constants;
import com.cmmplb.security.oauth2.starter.provider.converter.User;
import lombok.Data;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author penglibo
 * @date 2021-10-20 11:27:10
 * @since jdk 1.8
 * 抛出账号密码登录自定义中文错误信息
 */

@Data
public class AuthenticationProviderImpl implements AuthenticationProvider {

    private UserDetailsServiceImpl userDetailsServiceImpl;

    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        User user = (User) userDetailsServiceImpl.loadUserByUsername(username);
        if (!user.isAccountNonLocked()) {
            throw new BadCredentialsException(Oauth2Constants.ACCOUNT_NON_LOCKED);
        }
        if (!user.isAccountNonExpired()) {
            throw new BadCredentialsException(Oauth2Constants.ACCOUNT_NON_EXPIRED);
        }
        if (!password.equals(user.getPassword())) {
            throw new BadCredentialsException(Oauth2Constants.BAD_CREDENTIALS);
        }
        // 指定该认证提供者验证Token对象
        return new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        // 指定该认证提供者验证Token对象
        return UsernamePasswordAuthenticationToken.class.equals(authentication);
    }
}
