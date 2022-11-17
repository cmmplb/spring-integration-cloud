package com.cmmplb.security.oauth2.starter.provider.token.store.redis;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;

import java.util.Date;

/**
 * @author penglibo
 * @date 2022-08-30 09:25:50
 * @since jdk 1.8
 */

public class RedisTokenStore extends org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore {

    private final ClientDetailsService clientDetailsService;

    public RedisTokenStore(ClientDetailsService clientDetailsService, RedisConnectionFactory connectionFactory) {
        super(connectionFactory);
        this.clientDetailsService = clientDetailsService;
    }

    @Override
    public OAuth2Authentication readAuthentication(OAuth2AccessToken token) {
        OAuth2Authentication result = readAuthentication(token.getValue());
        if (result != null) {
            // 如果token没有失效 更新AccessToken过期时间
            DefaultOAuth2AccessToken oAuth2AccessToken = (DefaultOAuth2AccessToken) token;

            // 重新设置过期时间
            int validitySeconds = getAccessTokenValiditySeconds(result.getOAuth2Request());
            if (validitySeconds > 0) {
                oAuth2AccessToken.setExpiration(new Date(System.currentTimeMillis() + (validitySeconds * 1000L)));
            }
            // super.removeAccessToken(token);
            // 将重新设置过的过期时间重新存入redis, 此时会覆盖redis中原本的过期时间
            storeAccessToken(token, result);
        }
        return result;
    }

    protected int getAccessTokenValiditySeconds(OAuth2Request clientAuth) {
        if (clientDetailsService != null) {
            ClientDetails client = clientDetailsService.loadClientByClientId(clientAuth.getClientId());
            Integer validity = client.getAccessTokenValiditySeconds();
            if (validity != null) {
                return validity;
            }
        }
        // default 12 hours. 432000
        return 60 * 60 * 12;
    }
}
