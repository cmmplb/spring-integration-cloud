package com.cmmplb.security.oauth2.auth.mobile.granter;

import cn.hutool.core.util.StrUtil;
import com.cmmplb.security.oauth2.auth.mobile.MobileCodeException;
import com.cmmplb.security.oauth2.start.token.MobileAuthenticationToken;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author penglibo
 * @date 2021-10-19 10:11:27
 * @since jdk 1.8
 * 手机号短信验证模式
 */

public class MobileTokenGranter extends AbstractTokenGranter {

    private static final String GRANT_TYPE = "mobile";

    private final AuthenticationManager authenticationManager;

    public MobileTokenGranter(AuthenticationManager authenticationManager,
                                               AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService,
                                               OAuth2RequestFactory requestFactory) {
        this(authenticationManager, tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
    }


    protected MobileTokenGranter(AuthenticationManager authenticationManager, AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory, String grantType) {
        super(tokenServices, clientDetailsService, requestFactory, grantType);
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        Map<String, String> parameters = new LinkedHashMap<>(tokenRequest.getRequestParameters());
        // 手机号
        String mobile = parameters.get("mobile");
        // 验证码/密码
        String code = parameters.get("code");

        if (StrUtil.isBlank(mobile) || StrUtil.isBlank(code)) {
            throw new MobileCodeException("Bad credentials [ params must be has phone with code ]");
        }

        // Protect from downstream leaks of code
        parameters.remove("code");

        Authentication userAuth = new MobileAuthenticationToken(mobile, code);
        ((AbstractAuthenticationToken) userAuth).setDetails(parameters);
        try {
            userAuth = authenticationManager.authenticate(userAuth);
        } catch (AccountStatusException | BadCredentialsException ase) {
            // covers expired, locked, disabled cases (mentioned in section 5.2, draft 31)
            throw new MobileCodeException(ase.getMessage());
        }
        // If the phone/code are wrong the spec says we should send 400/invalid grant

        if (userAuth == null || !userAuth.isAuthenticated()) {
            throw new MobileCodeException("Could not authenticate user: " + mobile);
        }

        OAuth2Request storedOAuth2Request = getRequestFactory().createOAuth2Request(client, tokenRequest);
        return new OAuth2Authentication(storedOAuth2Request, userAuth);
    }
}
