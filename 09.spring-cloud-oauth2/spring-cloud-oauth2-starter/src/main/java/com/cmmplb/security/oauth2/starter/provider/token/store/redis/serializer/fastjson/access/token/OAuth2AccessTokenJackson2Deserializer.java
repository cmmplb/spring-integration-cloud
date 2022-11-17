package com.cmmplb.security.oauth2.starter.provider.token.store.redis.serializer.fastjson.access.token;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.DefaultOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.util.OAuth2Utils;

import java.io.IOException;
import java.util.*;

/**
 * @author penglibo
 * @date 2022-08-30 09:31:40
 * @since jdk 1.8
 * 参照 {@link org.springframework.security.oauth2.common.OAuth2AccessTokenJackson2Deserializer}
 */

@Slf4j
public class OAuth2AccessTokenJackson2Deserializer extends StdDeserializer<OAuth2AccessToken> {

    private static final long serialVersionUID = 1L;

    public OAuth2AccessTokenJackson2Deserializer() {
        super(OAuth2AccessToken.class);
    }

    @Override
    public OAuth2AccessToken deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        JsonNode jsonNode = mapper.readTree(jp);

        String tokenValue = null;
        String tokenType = null;
        String refreshToken = null;
        Long expiresIn = null;
        Set<String> scope = null;
        Map<String, Object> additionalInformation = new LinkedHashMap<String, Object>();

        Iterator<String> stringIterator = jsonNode.fieldNames();
        while (stringIterator.hasNext()) {
            String name = stringIterator.next();
            jp.nextToken();
            if (OAuth2AccessToken.ACCESS_TOKEN.equals(name)) {
                tokenValue = jsonNode.get(OAuth2AccessToken.ACCESS_TOKEN).asText();
            } else if (OAuth2AccessToken.TOKEN_TYPE.equals(name)) {
                tokenType = jsonNode.get(OAuth2AccessToken.TOKEN_TYPE).asText();
            } else if (OAuth2AccessToken.EXPIRES_IN.equals(name)) {
                expiresIn = jsonNode.get(OAuth2AccessToken.EXPIRES_IN).asLong();
            } else if (OAuth2AccessToken.REFRESH_TOKEN.equals(name)) {
                refreshToken = null == jsonNode.get(OAuth2AccessToken.REFRESH_TOKEN) ? null : jsonNode.get(OAuth2AccessToken.REFRESH_TOKEN).asText();
            } else if (OAuth2AccessToken.SCOPE.equals(name)) {
                String scopeText = jsonNode.get(OAuth2AccessToken.SCOPE).asText();
                scope = OAuth2Utils.parseParameterList(scopeText);
            } else {
                additionalInformation.put(name, mapper.convertValue(jsonNode.get(name), Object.class));
            }
        }

        DefaultOAuth2AccessToken accessToken = new DefaultOAuth2AccessToken(tokenValue);
        accessToken.setTokenType(tokenType);
        if (expiresIn != null) {
            accessToken.setExpiration(new Date(System.currentTimeMillis() + (expiresIn * 1000)));
        }
        if (refreshToken != null) {
            accessToken.setRefreshToken(new DefaultOAuth2RefreshToken(refreshToken));
        }
        accessToken.setScope(scope);
        accessToken.setAdditionalInformation(additionalInformation);

        return accessToken;
    }

}
