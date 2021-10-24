package com.cmmplb.security.oauth2.auth.exception.auth;

import com.cmmplb.core.result.HttpCodeEnum;
import com.cmmplb.core.result.Result;
import com.cmmplb.core.result.ResultUtil;
import com.cmmplb.core.utils.StringUtils;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author penglibo
 * @date 2021-09-03 12:02:26
 * @since jdk 1.8
 * 认证服务器序列化异常处理类
 */

@Slf4j
public class Oauth2StdSerializer extends StdSerializer<OAuth2Exception> {

    private static final long serialVersionUID = 6270447052454565618L;

    public static final String BAD_CREDENTIALS = "Bad credentials";

    public Oauth2StdSerializer() {
        super(OAuth2Exception.class);
    }

    @Override
    @SneakyThrows
    public void serialize(OAuth2Exception e, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) {
        Result<String> fail = ResultUtil.fail();
        fail.setCode(e.getHttpErrorCode());
        if (StringUtils.equals(e.getMessage(), BAD_CREDENTIALS)) {
            fail.setMsg(HttpCodeEnum.BAD_CREDENTIALS.getMessage());
        } else {
            log.warn("oauth2认证异常:", e);
            fail.setMsg(e.getMessage());
        }
        jsonGenerator.writeObject(fail);
    }
}
