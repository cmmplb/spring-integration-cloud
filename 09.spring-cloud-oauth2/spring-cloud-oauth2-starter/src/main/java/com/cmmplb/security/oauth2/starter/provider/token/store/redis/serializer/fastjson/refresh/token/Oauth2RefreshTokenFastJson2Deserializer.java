package com.cmmplb.security.oauth2.starter.provider.token.store.redis.serializer.fastjson.refresh.token;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.cmmplb.core.utils.ObjectUtil;
import org.springframework.security.oauth2.common.DefaultExpiringOAuth2RefreshToken;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * @author penglibo
 * @date 2022-08-28 19:08:44
 * @since jdk 1.8
 * DefaultOAuth2RefreshToken 没有setValue方法，会导致JSON反序列化为null
 */

public class Oauth2RefreshTokenFastJson2Deserializer implements ObjectDeserializer {

    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        if (type == DefaultExpiringOAuth2RefreshToken.class) {
            JSONObject jsonObject = parser.parseObject();
            String value = jsonObject.getString("value");
            long expiration = jsonObject.getLong("expiration");
            return ObjectUtil.cast(new DefaultExpiringOAuth2RefreshToken(value, new Date(expiration)));
        }
        return null;
    }

    @Override
    public int getFastMatchToken() {
        return 0;
    }

}
