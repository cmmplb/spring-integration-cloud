package com.cmmplb.auth2.auth.controller;

import com.cmmplb.auth2.auth.dto.OauthToken;
import com.cmmplb.auth2.auth.dto.RefreshInfoDTO;
import com.cmmplb.core.constants.SecurityConstants;
import com.cmmplb.core.exception.CustomException;
import com.cmmplb.core.result.Result;
import com.cmmplb.core.result.ResultUtil;
import com.cmmplb.core.utils.ObjectUtil;
import com.cmmplb.core.utils.StringUtil;
import com.cmmplb.redis.service.RedisService;
import com.cmmplb.security.oauth2.starter.annotation.WithoutLogin;
import com.cmmplb.security.oauth2.starter.constants.CacheConstants;
import com.cmmplb.security.oauth2.starter.provider.converter.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.OAuth2ClientProperties;
import org.springframework.core.NamedThreadLocal;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.util.CollectionUtils;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author penglibo
 * @date 2021-10-20 15:23:47
 * @since jdk 1.8
 */
@Slf4j
@RestController
public class LoginController {

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private RedisService redisService;

    @Autowired
    private TokenEndpoint tokenEndpoint;

    @Autowired
    private OAuth2ClientProperties oAuth2ClientProperties;

    @Autowired
    private UserDetailsService userDetailsService;

    @SuppressWarnings("AlibabaThreadLocalShouldRemove")
    private final ThreadLocal<Long> USER_ID = new NamedThreadLocal<Long>("user_id");

    /**
     * 由于原生的登陆表单没有提交json
     * @param token
     * @return
     * @throws HttpRequestMethodNotSupportedException
     */
    @PostMapping("/login")
    @WithoutLogin
    // @Log(type = LogOperationTypeEnum.LOGIN, content = "login")
    public Result<OAuth2AccessToken> getToken(@RequestBody OauthToken token) throws HttpRequestMethodNotSupportedException {
        Map<String, String> params = new HashMap<>();
        params.put("username", token.getUsername());
        params.put("password", token.getPassword());
        params.put("grant_type", token.getGrantType());
        UsernamePasswordAuthenticationToken usernamePasswordAuthentication = new UsernamePasswordAuthenticationToken(oAuth2ClientProperties.getClientId(),
                oAuth2ClientProperties.getClientSecret(), null);
        ResponseEntity<OAuth2AccessToken> oAuth2AccessToken = null;
        try {
            oAuth2AccessToken = tokenEndpoint.postAccessToken(usernamePasswordAuthentication, params);
        } catch (InvalidGrantException e) {
            e.printStackTrace();
            log.error("error:", e);
            throw new CustomException(SecurityConstants.BAD_CREDENTIALS);
        }
        OAuth2AccessToken accessTokenBody = oAuth2AccessToken.getBody();
        if (null == accessTokenBody){
            throw new CustomException(SecurityConstants.BAD_CREDENTIALS);
        }
        Map<String, Object> additionalInformation = accessTokenBody.getAdditionalInformation();
        // 登陆成功,传递user_id，日志切面记录登陆日志
        USER_ID.set(ObjectUtil.cast(additionalInformation.get("user_id")));
        return ResultUtil.success(accessTokenBody);
    }

    @PostMapping("/do/logout")
    // @Log(type = LogOperationTypeEnum.LOGOUT, content = "logout")
    public Result<Boolean> logout(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader) {
        if (StringUtil.isEmpty(authHeader)) {
            return ResultUtil.success();
        }

        String tokenValue = authHeader.replace(OAuth2AccessToken.BEARER_TYPE, StringUtil.EMPTY).trim();
        OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
        if (accessToken == null || StringUtil.isEmpty(accessToken.getValue())) {
            return ResultUtil.success();
        }
        tokenStore.removeAccessToken(accessToken);
        OAuth2RefreshToken refreshToken = accessToken.getRefreshToken();
        tokenStore.removeRefreshToken(refreshToken);
        return ResultUtil.success(true);
    }

    /**
     * 刷新用户信息
     */
    @PostMapping("/refresh/info")
    public Result<Boolean> refreshInfo(@RequestBody RefreshInfoDTO dto) {
        log.info("刷新用户信息:{}", dto.getUsernames());
        List<String> usernames = dto.getUsernames();
        if (!CollectionUtils.isEmpty(usernames)) {
            for (String username : usernames) {
                Object o = redisService.get(CacheConstants.LOGIN_ACCESS_TOKEN + username);
                if (null != o) {
                    String token = o.toString();
                    OAuth2AccessToken accessToken = tokenStore.readAccessToken(token);
                    if (null != accessToken) {
                        // 读取认证信息
                        OAuth2Authentication authentication = tokenStore.readAuthentication(accessToken);
                        Object principal = authentication.getPrincipal();
                        if (principal instanceof User) {
                            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                            // 替换用户信息
                            UsernamePasswordAuthenticationToken usernamePasswordAuthentication = new UsernamePasswordAuthenticationToken(userDetails, "N/A", userDetails.getAuthorities());
                            OAuth2Authentication oauth2Authentication = new OAuth2Authentication(authentication.getOAuth2Request(), usernamePasswordAuthentication);
                            oauth2Authentication.setDetails(userDetails);
                            // 更新缓存信息
                            tokenStore.storeAccessToken(accessToken, oauth2Authentication);
                        }
                    }
                }
            }
        }
        return ResultUtil.success(true);
    }
}
