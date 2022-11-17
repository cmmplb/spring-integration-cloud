package com.cmmplb.security.oauth2.starter.provider.token.store.redis.serializer.jackson.authentication;

import com.cmmplb.security.oauth2.starter.provider.converter.User;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import java.io.IOException;

/**
 * @author penglibo
 * @date 2022-08-30 09:32:56
 * @since jdk 1.8
 * OAuth2Authentication序列化
 * {@link org.springframework.security.oauth2.common.OAuth2AccessTokenJackson2Serializer}
 */

@Slf4j
public class OAuth2AuthenticationJackson2Serializer extends StdSerializer<OAuth2Authentication> {

    private static final long serialVersionUID = -2085247206851580891L;

    protected OAuth2AuthenticationJackson2Serializer() {
        super(OAuth2Authentication.class);
    }

    @Override
    public void serializeWithType(OAuth2Authentication authentication, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer) throws IOException {
        extracted(authentication, gen, serializers, typeSer);
    }

    @Override
    public void serialize(OAuth2Authentication authentication, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        extracted(authentication, gen, serializers, null);
    }

    private void extracted(OAuth2Authentication authentication, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer) throws IOException {
        gen.writeStartObject();
        if (typeSer != null) {
            gen.writeStringField(typeSer.getPropertyName(), authentication.getClass().getName());
        }
        gen.writeObjectField("authorities", authentication.getAuthorities());
        gen.writeObjectField("details", authentication.getDetails());
        gen.writeBooleanField("authenticated", authentication.isAuthenticated());
        gen.writeObjectField("userAuthentication", authentication.getUserAuthentication());
        gen.writeObjectField("principal", authentication.getPrincipal());
        gen.writeObjectField("oauth2Request", authentication.getOAuth2Request());
        gen.writeObjectField("credentials", authentication.getCredentials());
        gen.writeObjectField("clientOnly", false);
        gen.writeObjectField("name", ((User) authentication.getPrincipal()).getUsername());
        gen.writeEndObject();
    }
}
