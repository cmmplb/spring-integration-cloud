package com.cmmplb.security.oauth2.starter.provider.token.store.redis.serializer.jackson;

import com.alibaba.fastjson.util.IOUtils;
import com.cmmplb.security.oauth2.starter.provider.token.store.redis.serializer.jackson.access.token.OAuth2AccessTokenMixIn;
import com.cmmplb.security.oauth2.starter.provider.token.store.redis.serializer.jackson.authentication.OAuth2AuthenticationMixin;
import com.cmmplb.security.oauth2.starter.provider.token.store.redis.serializer.jackson.refresh.token.OAuth2RefreshTokenMixIn;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.common.exceptions.SerializationException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStoreSerializationStrategy;

import java.nio.charset.StandardCharsets;

/**
 * @author penglibo
 * @date 2022-08-30 09:28:17
 * @since jdk 1.8
 */

public class JacksonSerializationStrategy implements RedisTokenStoreSerializationStrategy {

    public static ObjectMapper mapper = new ObjectMapper();

    static {
        // 反序列化时候遇到不匹配的属性并不抛出异常 忽略json字符串中不识别的属性
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // 序列化时候遇到空对象不抛出异常 忽略无法转换的对象 “No serializer found for class com.xxx.xxx”
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        // PrettyPrinter 格式化输出，打印json会格式化
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);

        // OAuth2AccessToken序列化和反序列化
        mapper.addMixIn(OAuth2AccessToken.class, OAuth2AccessTokenMixIn.class);
        // OAuth2Authentication序列化和反序列化
        mapper.addMixIn(OAuth2Authentication.class, OAuth2AuthenticationMixin.class);
        // OAuth2RefreshToken序列化和反序列化
        mapper.addMixIn(OAuth2RefreshToken.class, OAuth2RefreshTokenMixIn.class);
    }

    public JacksonSerializationStrategy(boolean typeEnabled) {
        // 序列化包含字段属性
        if (typeEnabled) {
            // 序列化时添加类型，否则自动反序列化时会报错，无法转换
            mapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        }
    }


    public <T> RedisSerializer<T> jackson2JsonRedisSerializer(Class<T> clazz) {
        Jackson2JsonRedisSerializer<T> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(clazz);
        jackson2JsonRedisSerializer.setObjectMapper(mapper);
        return jackson2JsonRedisSerializer;
    }

    public RedisSerializer<Object> jackson2JsonRedisSerializer() {
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        jackson2JsonRedisSerializer.setObjectMapper(mapper);
        return jackson2JsonRedisSerializer;
    }

    @Override
    public byte[] serialize(Object o) {
        if (o == null) {
            return new byte[0];
        }
        try {
            return jackson2JsonRedisSerializer().serialize(o);
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

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        if (null == clazz || bytes == null || bytes.length == 0) {
            return null;
        }
        try {
            return jackson2JsonRedisSerializer(clazz).deserialize(bytes);
        } catch (Exception ex) {
            throw new SerializationException("Could not serialize: " + ex.getMessage(), ex);
        }
    }

    @Override
    public byte[] serialize(String s) {
        if (s == null || s.length() == 0) {
            return new byte[0];
        }
        return s.getBytes(StandardCharsets.UTF_8);
    }


}
