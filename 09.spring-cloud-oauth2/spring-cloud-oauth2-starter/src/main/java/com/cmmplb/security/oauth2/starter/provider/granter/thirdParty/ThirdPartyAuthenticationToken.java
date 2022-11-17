package com.cmmplb.security.oauth2.starter.provider.granter.thirdParty;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author penglibo
 * @date 2021-10-19 10:15:24
 * @since jdk 1.8
 */

public class ThirdPartyAuthenticationToken extends AbstractAuthenticationToken {

    private static final long serialVersionUID = -3509738245043512999L;

    private final Object principal;

    private Object credentials;

    public ThirdPartyAuthenticationToken(Object principal, Object credentials) {
        super(AuthorityUtils.NO_AUTHORITIES);
        this.principal = principal;
        this.credentials = credentials;
        this.setAuthenticated(false);
    }

    public ThirdPartyAuthenticationToken(UserDetails userDetails) {
        super(userDetails.getAuthorities());
        this.principal = userDetails;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;

    }
}
