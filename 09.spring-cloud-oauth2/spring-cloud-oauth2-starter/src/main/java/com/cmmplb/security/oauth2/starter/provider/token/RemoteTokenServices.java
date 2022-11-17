package com.cmmplb.security.oauth2.starter.provider.token;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.endpoint.CheckTokenEndpoint;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author penglibo
 * @date 2022-11-04 16:58:24
 * @since jdk 1.8
 * 复写RemoteTokenServices
 * {@link org.springframework.security.oauth2.provider.token.RemoteTokenServices}
 */

@Slf4j
@Setter
public class RemoteTokenServices extends org.springframework.security.oauth2.provider.token.RemoteTokenServices {

    private String checkTokenEndpointUrl;

    private String clientId;

    private String clientSecret;

    private String tokenName = "token";

    private AccessTokenConverter tokenConverter = new DefaultAccessTokenConverter();

    @Override
    public OAuth2Authentication loadAuthentication(String accessToken) throws AuthenticationException, InvalidTokenException {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", getAuthorizationHeader(clientId, clientSecret));
        Map<String, Object> map = postForMap(checkTokenEndpointUrl, accessToken, headers);

        if (map.containsKey("error")) {
            if (log.isDebugEnabled()) {
                log.debug("check_token returned error: " + map.get("error"));
            }
            throw new InvalidTokenException(accessToken);
        }

        // gh-838
        if (!Boolean.TRUE.equals(map.get("active"))) {
            log.debug("check_token returned active attribute: " + map.get("active"));
            throw new InvalidTokenException(accessToken);
        }

        return tokenConverter.extractAuthentication(map);
    }

    private String getAuthorizationHeader(String clientId, String clientSecret) {

        if (clientId == null || clientSecret == null) {
            logger.warn("Null Client ID or Client Secret detected. Endpoint that requires authentication will reject request with 401 error.");
        }

        String creds = String.format("%s:%s", clientId, clientSecret);
        return "Basic " + new String(Base64.encode(creds.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * 重写发起请求方式，由restTemplate改成http，测试处理不需要注册中心来服务调用
     */
    private Map<String, Object> postForMap(String path, String accessToken, HttpHeaders headers) {
        if (headers.getContentType() == null) {
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        }
        /**
         * 通过这个接口校验
         * {@link CheckTokenEndpoint#checkToken(java.lang.String)}
         */
        HttpResponse httpResponse = HttpRequest.get(path).header(headers).body("token=" + accessToken).execute();
        return JSON.parseObject(httpResponse.body(), new TypeReference<Map<String, Object>>() {
        });
    }
}
