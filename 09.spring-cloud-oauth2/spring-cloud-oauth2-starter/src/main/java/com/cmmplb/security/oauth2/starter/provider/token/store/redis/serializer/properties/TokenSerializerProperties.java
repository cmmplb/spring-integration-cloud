package com.cmmplb.security.oauth2.starter.provider.token.store.redis.serializer.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author penglibo
 * @date 2022-08-30 11:37:06
 * @since jdk 1.8
 * uAuth2缓存使用json序列化配置类
 */

@Data
@ConfigurationProperties(prefix = "security.oauth2.token.serializer.json")
public class TokenSerializerProperties {

    /**
     * 开启缓存json序列化
     */
    private Boolean enabled = false;

    /**
     * 序列化包含字段属性=>@class:com.xxx.xxx
     */
    private Boolean typeEnabled = false;

    /**
     * 序列化类型:fastJson,jackson
     */
    private TypeEnums type = TypeEnums.JACKSON;

    @AllArgsConstructor
    @Getter
    public enum TypeEnums {
        /**
         * FAST_JSON
         */
        FAST_JSON,
        JACKSON
    }

}
