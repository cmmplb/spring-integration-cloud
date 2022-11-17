package com.cmmplb.security.oauth2.starter.provider.token.store.redis.serializer.jackson.refresh.token;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @author penglibo
 * @date 2022-08-30 09:30:22
 * @since jdk 1.8
 * JsonTypeInfo:
 *      元素“ include”和“ property”都是可选的，不写将使用默认值。 默认的“ include”也是JsonTypeInfo.As.PROPERTY，默认的“ property”是@class。
 *      序列化会生成："@class":"com.xxx"，解决多态类型进行JSON序列化后，Jackson无法在反序列化期间找出正确的类型，如果不使用就要手动反序列化设置字段了。
 */

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@JsonSerialize(
        using = OAuth2RefreshTokenJackson2Serializer.class
)
@JsonDeserialize(
        using = OAuth2RefreshTokenJackson2Deserializer.class
)
public class OAuth2RefreshTokenMixIn {

}