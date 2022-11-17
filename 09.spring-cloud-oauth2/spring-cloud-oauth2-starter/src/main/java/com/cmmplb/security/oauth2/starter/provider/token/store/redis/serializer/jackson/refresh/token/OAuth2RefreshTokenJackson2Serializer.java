package com.cmmplb.security.oauth2.starter.provider.token.store.redis.serializer.jackson.refresh.token;

import com.cmmplb.security.oauth2.starter.provider.token.store.redis.serializer.properties.TokenSerializerProperties;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.oauth2.common.DefaultExpiringOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;

import java.io.IOException;

/**
 * @author penglibo
 * @date 2022-08-30 09:30:44
 * @since jdk 1.8
 * 参照 {@link org.springframework.security.oauth2.common.OAuth2AccessTokenJackson2Serializer}
 */

@EnableConfigurationProperties(TokenSerializerProperties.class)
public class OAuth2RefreshTokenJackson2Serializer extends StdSerializer<OAuth2RefreshToken> {

    private static final long serialVersionUID = 1L;


    public OAuth2RefreshTokenJackson2Serializer() {
        super(OAuth2RefreshToken.class);
    }

    @Override
    public void serializeWithType(OAuth2RefreshToken value, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer) throws IOException {
        extracted(value, gen, serializers, typeSer);
    }

    @Override
    public void serialize(OAuth2RefreshToken token, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonGenerationException {
        extracted(token, gen, serializers, null);
    }

    private void extracted(OAuth2RefreshToken token, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer) throws IOException {
        gen.writeStartObject();
        if (typeSer != null) {
            gen.writeStringField(typeSer.getPropertyName(), token.getClass().getName());
        }
        gen.writeStringField("value", token.getValue());
        if (token instanceof DefaultExpiringOAuth2RefreshToken) {
            gen.writeStringField("expiration", String.valueOf(((DefaultExpiringOAuth2RefreshToken) token).getExpiration().getTime()));
        }
        gen.writeEndObject();
    }

}
