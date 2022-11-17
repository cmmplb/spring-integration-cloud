package com.cmmplb.security.oauth2.starter.provider.token.store.redis.serializer.fastjson;

import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.util.IOUtils;
import com.alibaba.fastjson.util.TypeUtils;
import com.cmmplb.redis.serializer.FastJson2JsonRedisSerializer;
import com.cmmplb.security.oauth2.starter.provider.converter.SimpleGrantedAuthority;
import com.cmmplb.security.oauth2.starter.provider.converter.User;
import com.cmmplb.security.oauth2.starter.provider.token.store.redis.serializer.fastjson.authentication.OAuth2AuthenticationFastJson2Deserializer;
import com.cmmplb.security.oauth2.starter.provider.token.store.redis.serializer.fastjson.authentication.OAuth2AuthenticationFastJson2Serializer;
import com.cmmplb.security.oauth2.starter.provider.token.store.redis.serializer.fastjson.refresh.token.Oauth2RefreshTokenFastJson2Deserializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.security.oauth2.common.DefaultExpiringOAuth2RefreshToken;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.DefaultOAuth2RefreshToken;
import org.springframework.security.oauth2.common.exceptions.SerializationException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStoreSerializationStrategy;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.nio.charset.StandardCharsets;

/**
 * @author penglibo
 * @date 2022-08-28 16:06:13
 * @since jdk 1.8
 * // todo:序列化规则
 */

@Slf4j
public class FastJsonSerializationStrategy implements RedisTokenStoreSerializationStrategy {

    private static final ParserConfig PARSER_CONFIG = ParserConfig.getGlobalInstance();
    private static final SerializeConfig SERIALIZE_CONFIG = SerializeConfig.getGlobalInstance();



    static {
        // 开启AutoType
        PARSER_CONFIG.setAutoTypeSupport(true);

        // OAuth2Authentication序列化
        SERIALIZE_CONFIG.put(OAuth2Authentication.class, new OAuth2AuthenticationFastJson2Serializer());
        // OAuth2Authentication反序列化
        PARSER_CONFIG.putDeserializer(OAuth2Authentication.class, new OAuth2AuthenticationFastJson2Deserializer());

        // DefaultOAuth2RefreshToken反序列化
        PARSER_CONFIG.putDeserializer(DefaultOAuth2RefreshToken.class, new Oauth2RefreshTokenFastJson2Deserializer());

        // 添加autoType白名单
        PARSER_CONFIG.addAccept("org.springframework.security.oauth2.provider.");
        TypeUtils.addMapping("org.springframework.security.oauth2.provider.OAuth2Authentication", OAuth2Authentication.class);

        PARSER_CONFIG.addAccept("org.springframework.security.oauth2.provider.client");
        TypeUtils.addMapping("org.springframework.security.oauth2.provider.client.BaseClientDetails", BaseClientDetails.class);

        PARSER_CONFIG.addAccept("org.springframework.security.oauth2.common.");
        TypeUtils.addMapping("org.springframework.security.oauth2.common.DefaultOAuth2AccessToken", DefaultOAuth2AccessToken.class);
        TypeUtils.addMapping("org.springframework.security.oauth2.common.DefaultExpiringOAuth2RefreshToken", DefaultExpiringOAuth2RefreshToken.class);

        PARSER_CONFIG.addAccept("com.cmmplb.security.oauth2.starter.provider.converter");
        TypeUtils.addMapping("com.cmmplb.security.oauth2.starter.provider.converter.User", User.class);
        TypeUtils.addMapping("com.cmmplb.security.oauth2.starter.provider.converter.SimpleGrantedAuthority", SimpleGrantedAuthority.class);

        PARSER_CONFIG.addAccept("org.springframework.security.web.authentication.preauth");
        TypeUtils.addMapping("org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken", PreAuthenticatedAuthenticationToken.class);
    }

    public <T> RedisSerializer<T> fastJson2JsonRedisSerializer(Class<T> clazz) {
        return new FastJson2JsonRedisSerializer<>(clazz);
    }


    public RedisSerializer<Object> fastJson2JsonRedisSerializer() {
        return new FastJson2JsonRedisSerializer<>(Object.class);
    }

    @Override
    public byte[] serialize(Object o) {
        if (o == null) {
            return new byte[0];
        }
        try {
            return fastJson2JsonRedisSerializer().serialize(o);
        } catch (Exception ex) {
            throw new SerializationException("Could not serialize: " + ex.getMessage(), ex);
        }
    }

    @Override
    public byte[] serialize(String data) {
        if (data == null || data.length() == 0) {
            return new byte[0];
        }
        return data.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        if (bytes == null || bytes.length <= 0 || null == clazz) {
            return null;
        }
        try {
            return fastJson2JsonRedisSerializer(clazz).deserialize(bytes);
        } catch (Exception ex) {
            throw new SerializationException("Could not serialize: " + ex.getMessage(), ex);
        }
    }

    @Override
    public String deserializeString(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        return new String(bytes, IOUtils.UTF8);
    }


}
