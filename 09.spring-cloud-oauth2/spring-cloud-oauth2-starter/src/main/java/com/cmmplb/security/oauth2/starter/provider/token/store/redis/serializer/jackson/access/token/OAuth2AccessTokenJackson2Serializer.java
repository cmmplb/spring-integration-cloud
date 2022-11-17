package com.cmmplb.security.oauth2.starter.provider.token.store.redis.serializer.jackson.access.token;

import com.cmmplb.security.oauth2.starter.provider.token.store.redis.serializer.properties.TokenSerializerProperties;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * @author penglibo
 * @date 2022-08-30 09:30:44
 * @since jdk 1.8
 * 参照 {@link org.springframework.security.oauth2.common.OAuth2AccessTokenJackson2Serializer}
 */

@EnableConfigurationProperties(TokenSerializerProperties.class)
public class OAuth2AccessTokenJackson2Serializer extends StdSerializer<OAuth2AccessToken> {

    private static final long serialVersionUID = 1L;


    public OAuth2AccessTokenJackson2Serializer() {
        super(OAuth2AccessToken.class);
    }

    @Override
    public void serializeWithType(OAuth2AccessToken value, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer) throws IOException {
        extracted(value, gen, serializers, typeSer);
    }

    @Override
    public void serialize(OAuth2AccessToken token, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonGenerationException {
        extracted(token, gen, serializers, null);
    }

    private void extracted(OAuth2AccessToken token, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer) throws IOException {
        gen.writeStartObject();
        if (typeSer != null) {
            gen.writeStringField(typeSer.getPropertyName(), token.getClass().getName());
        }
        gen.writeStringField(OAuth2AccessToken.ACCESS_TOKEN, token.getValue());
        gen.writeStringField(OAuth2AccessToken.TOKEN_TYPE, token.getTokenType());
        OAuth2RefreshToken refreshToken = token.getRefreshToken();
        if (refreshToken != null) {
            gen.writeStringField(OAuth2AccessToken.REFRESH_TOKEN, refreshToken.getValue());
        }
        Date expiration = token.getExpiration();
        if (expiration != null) {
            long now = System.currentTimeMillis();
            gen.writeNumberField(OAuth2AccessToken.EXPIRES_IN, (expiration.getTime() - now) / 1000);
        }
        Set<String> scope = token.getScope();
        if (scope != null && !scope.isEmpty()) {
            StringBuilder scopes = new StringBuilder();
            for (String s : scope) {
                Assert.hasLength(s, "Scopes cannot be null or empty. Got " + scope + "");
                scopes.append(s);
                scopes.append(" ");
            }
            gen.writeStringField(OAuth2AccessToken.SCOPE, scopes.substring(0, scopes.length() - 1));
        }
        Map<String, Object> additionalInformation = token.getAdditionalInformation();
        for (String key : additionalInformation.keySet()) {
            gen.writeObjectField(key, additionalInformation.get(key));
        }
        gen.writeEndObject();
    }

}
