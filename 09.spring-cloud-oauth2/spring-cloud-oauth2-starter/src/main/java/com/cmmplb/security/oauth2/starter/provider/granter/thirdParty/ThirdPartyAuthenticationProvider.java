package com.cmmplb.security.oauth2.starter.provider.granter.thirdParty;

import com.cmmplb.security.oauth2.starter.constants.Oauth2Constants;
import com.cmmplb.security.oauth2.starter.common.exceptions.OAuth2ThirdPartyException;
import com.cmmplb.security.oauth2.starter.service.impl.UserDetailsServiceImpl;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author penglibo
 * @date 2021-10-19 11:22:58
 * @since jdk 1.8
 * 自定义一个登陆方式用于后续润工作或者其他登陆对接。
 */

@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public class ThirdPartyAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private UserDetailsServiceImpl userDetailsServiceImpl;

    /**
     * 认证逻辑校验
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        if (authentication.getCredentials() == null) {
            log.debug("Failed to authenticate since no credentials provided");
            throw new OAuth2ThirdPartyException("Bad credentials");
        }
        String username = authentication.getPrincipal().toString();
        // todo:
        String password = authentication.getCredentials().toString();

        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);
        check(userDetails);
        ThirdPartyAuthenticationToken authenticationToken = new ThirdPartyAuthenticationToken(userDetails);
        authenticationToken.setDetails(authenticationToken.getDetails());
        return authenticationToken;
    }

    /**
     * 账号禁用、锁定、超时校验
     */
    private void check(UserDetails user) {
        if (!user.isAccountNonLocked()) {
            throw new LockedException(Oauth2Constants.ACCOUNT_NON_LOCKED);
        } else if (!user.isEnabled()) {
            throw new DisabledException(Oauth2Constants.ACCOUNT_NON_LOCKED);
        } else if (!user.isAccountNonExpired()) {
            throw new AccountExpiredException(Oauth2Constants.ACCOUNT_NON_EXPIRED);
        }
    }

    /**
     * 身份校验
     */
    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        if (authentication.getCredentials() == null) {
            this.logger.debug("Failed to authenticate since no credentials provided");
            throw new BadCredentialsException(this.messages
                    .getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        }
    }

    /**
     * 用户检索
     */
    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        return null;
    }

    /**
     * 指定该认证提供者验证Token对象
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(ThirdPartyAuthenticationToken.class);
    }
}
