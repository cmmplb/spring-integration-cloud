package com.cmmplb.security.oauth2.auth.provider;

import com.cmmplb.security.oauth2.start.constants.Oauth2Constants;
import com.cmmplb.security.oauth2.auth.service.UserService;
import com.cmmplb.security.oauth2.start.entity.AuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author penglibo
 * @date 2021-10-20 11:27:10
 * @since jdk 1.8
 * 抛出账号密码登录自定义中文错误信息
 */

@Service
public class AuthenticationProviderImpl implements AuthenticationProvider {

    @Autowired
    private UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        AuthUser account = (AuthUser) userService.loadUserByUsername(username);
        if (!account.isAccountNonLocked()) {
            throw new BadCredentialsException(Oauth2Constants.ACCOUNT_NON_LOCKED);
        }
        if (!account.isAccountNonExpired()) {
            throw new BadCredentialsException(Oauth2Constants.ACCOUNT_NON_EXPIRED);
        }
        if (!new BCryptPasswordEncoder().matches(password, account.getPassword())) {
            throw new BadCredentialsException(Oauth2Constants.BAD_CREDENTIALS);
        }
        // 指定该认证提供者验证Token对象
        return new UsernamePasswordAuthenticationToken(account, account.getPassword(), account.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        // 指定该认证提供者验证Token对象
        return UsernamePasswordAuthenticationToken.class.equals(authentication);
    }
}
