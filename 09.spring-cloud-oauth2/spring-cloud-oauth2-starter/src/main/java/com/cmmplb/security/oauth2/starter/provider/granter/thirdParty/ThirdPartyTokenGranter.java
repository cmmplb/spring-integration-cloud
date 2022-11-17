package com.cmmplb.security.oauth2.starter.provider.granter.thirdParty;

import cn.hutool.core.util.StrUtil;
import com.cmmplb.security.oauth2.starter.constants.Oauth2Constants;
import com.cmmplb.security.oauth2.starter.common.exceptions.OAuth2ThirdPartyException;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author penglibo
 * @date 2021-10-19 10:11:27
 * @since jdk 1.8
 * 自定义其他登陆模式
 */

public class ThirdPartyTokenGranter extends AbstractTokenGranter {

    private static final String GRANT_TYPE = "third_party";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    private final AuthenticationManager authenticationManager;

    public ThirdPartyTokenGranter(AuthenticationManager authenticationManager,
                                  AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService,
                                  OAuth2RequestFactory requestFactory) {
        this(authenticationManager, tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
    }


    protected ThirdPartyTokenGranter(AuthenticationManager authenticationManager, AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory, String grantType) {
        super(tokenServices, clientDetailsService, requestFactory, grantType);
        this.authenticationManager = authenticationManager;
    }

    @SuppressWarnings("AlibabaLowerCamelCaseVariableNaming")
    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        Map<String, String> parameters = new LinkedHashMap<>(tokenRequest.getRequestParameters());
        // 手机号
        String username = parameters.get(USERNAME);
        // 其他字段 todo:
        String password = parameters.get(PASSWORD);

        if (StrUtil.isBlank(username)) {
            throw new OAuth2ThirdPartyException(Oauth2Constants.USERNAME_EMPTY);
        }

        // Protect from downstream leaks of code
        parameters.remove("code");

        Authentication userAuth = new ThirdPartyAuthenticationToken(username, password);
        ((AbstractAuthenticationToken) userAuth).setDetails(parameters);
        try {
            userAuth = authenticationManager.authenticate(userAuth);
        } catch (AccountStatusException | BadCredentialsException ase) {
            // covers expired, locked, disabled cases (mentioned in section 5.2, draft 31)
            throw new OAuth2ThirdPartyException(ase.getMessage());
        }
        // If the phone/code are wrong the spec says we should send 400/invalid grant

        if (userAuth == null || !userAuth.isAuthenticated()) {
            throw new OAuth2ThirdPartyException("Could not authenticate user: " + username);
        }

        return new OAuth2Authentication(getRequestFactory().createOAuth2Request(client, tokenRequest), userAuth);
    }
}
