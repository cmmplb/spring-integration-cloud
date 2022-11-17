package com.cmmplb.security.oauth2.starter.provider.token.store.redis.serializer.fastjson.authentication;

import com.alibaba.fastjson.serializer.JSONSerializable;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import java.io.IOException;
import java.lang.reflect.Type;

;

/**
 * @author penglibo
 * @date 2022-08-28 19:09:16
 * @since jdk 1.8
 * {@link com.alibaba.fastjson.serializer.JSONSerializableSerializer}
 */

public class OAuth2AuthenticationFastJson2Serializer implements ObjectSerializer {

    @Override
    public void write(JSONSerializer jsonSerializer, Object o, Object fieldName, Type fieldType, int features) throws IOException {
        if (o instanceof OAuth2Authentication) {
            SerializeWriter out = jsonSerializer.out;
            OAuth2Authentication oAuth2Authentication = ((OAuth2Authentication) o);
            jsonSerializer.write(oAuth2Authentication);

        } else {
            JSONSerializable jsonSerializable = ((JSONSerializable) o);
            if (jsonSerializable == null) {
                jsonSerializer.writeNull();
                return;
            }
            jsonSerializable.write(jsonSerializer, fieldName, fieldType, features);
        }
    }
}
