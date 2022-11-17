package com.cmmplb.security.oauth2.starter.provider.token.store.redis.serializer.jackson.refresh.token;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.common.DefaultExpiringOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;

import java.io.IOException;
import java.util.Date;

/**
 * @author penglibo
 * @date 2022-08-30 09:31:40
 * @since jdk 1.8
 * 参照 {@link org.springframework.security.oauth2.common.OAuth2AccessTokenJackson2Deserializer}
 */

@Slf4j
public class OAuth2RefreshTokenJackson2Deserializer extends StdDeserializer<OAuth2RefreshToken> {

    private static final long serialVersionUID = 1L;

    public OAuth2RefreshTokenJackson2Deserializer() {
        super(OAuth2RefreshToken.class);
    }

    @Override
    public OAuth2RefreshToken deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        JsonNode jsonNode = mapper.readTree(jp);
        String value = jsonNode.get("value").asText();
        long expiration = null == jsonNode.get("expiration") ? 0 : jsonNode.get("expiration").asLong();
        return new DefaultExpiringOAuth2RefreshToken(value, new Date(expiration));
    }
}
