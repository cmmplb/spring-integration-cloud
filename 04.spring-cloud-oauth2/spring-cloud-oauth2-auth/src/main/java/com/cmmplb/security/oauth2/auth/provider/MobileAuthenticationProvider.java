package com.cmmplb.security.oauth2.auth.provider;

import com.cmmplb.common.redis.service.RedisService;
import com.cmmplb.security.oauth2.start.constants.Oauth2Constants;
import com.cmmplb.security.oauth2.auth.mobile.MobileCodeException;
import com.cmmplb.security.oauth2.auth.service.UserService;
import com.cmmplb.security.oauth2.start.token.MobileAuthenticationToken;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author penglibo
 * @date 2021-10-19 11:22:58
 * @since jdk 1.8
 * 手机号短信验证码提供则
 */

@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public class MobileAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private RedisService redisService;

    private UserService userService;

    /**
     * 认证逻辑校验
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        if (authentication.getCredentials() == null) {
            log.debug("Failed to authenticate since no credentials provided");
            throw new MobileCodeException("Bad credentials");
        }

        String mobile = authentication.getPrincipal().toString();
        String code = authentication.getCredentials().toString();
        Object cacheCode = redisService.get(Oauth2Constants.SMS_CODE_PREFIX + mobile);
        if (null == cacheCode || !cacheCode.toString().equals(code)) {
            log.error(Oauth2Constants.CODE_ERROR);
            throw new MobileCodeException(Oauth2Constants.CODE_ERROR);
        }
        //清除redis中的短信验证码
        // redisService.del(Oauth2Constants.SMS_CODE_PREFIX + mobile);

        UserDetails userDetails;
        try {
            userDetails = userService.loadUserByMobile(mobile);
        } catch (UsernameNotFoundException var6) {
            log.info("手机号:" + mobile + "未查到用户信息");
            if (this.hideUserNotFoundExceptions) {
                throw new MobileCodeException("手机号:" + mobile + "未查到用户信息");
            }
            throw var6;
        }
        check(userDetails);
        MobileAuthenticationToken authenticationToken = new MobileAuthenticationToken(userDetails);
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
        return authentication.isAssignableFrom(MobileAuthenticationToken.class);
    }
}
