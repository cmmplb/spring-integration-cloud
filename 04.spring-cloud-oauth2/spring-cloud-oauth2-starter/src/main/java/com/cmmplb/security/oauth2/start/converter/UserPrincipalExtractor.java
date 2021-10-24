package com.cmmplb.security.oauth2.start.converter;

import com.cmmplb.security.oauth2.start.entity.AuthUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;

import java.util.Map;
import java.util.Optional;

/**
 * @author penglibo
 * @date 2021-10-18 11:37:08
 * @since jdk 1.8
 * 自定义principal提取器
 */

@Slf4j
public class UserPrincipalExtractor implements PrincipalExtractor {

    @Override
    public Object extractPrincipal(Map<String, Object> map) {
        log.debug("extractPrincipal-> principal-> {}", map);
        // 自定义principal属性，对应=》Authentication里面authentication.getPrincipal()
        /*
        * "principal": {
                "id": 1,
                "username": "cmmplb",
                "password": "N/A",
                "accountNonExpired": true,
                "accountNonLocked": true,
                "credentialsNonExpired": true,
                "enabled": true,
                "authorities": [
                    {
                        "authority": "test"
                    }
                ]
            },
        * */
        // 这里可以扩展Principal
        AuthUser authUser = new AuthUser();
        authUser.setId(Long.parseLong(map.get("id").toString()));
        authUser.setUsername(Optional.ofNullable(map.get("username")).toString());
        authUser.setPassword(Optional.ofNullable(map.get("password")).toString());
        // authUser.setAccountNonExpired(Optional.ofNullable(map.get("accountNonExpired")).toString());
        // authUser.setAccountNonLocked(Optional.ofNullable(map.get("username")).toString());
        // authUser.setCredentialsNonExpired(Optional.ofNullable(map.get("credentialsNonExpired")));
        // authUser.setEnabled(Optional.ofNullable(map.get("enabled")));
        // authUser.setAuthorities(map.get("authority"));
        return map;
    }

    /*
    * authentication的json:
    * {
        "authorities": [
            {
                "authority": "test"
            }
        ],
        "details": {
            "remoteAddress": "127.0.0.1",
            "sessionId": null,
            "tokenValue": "99df630e-bf0a-44ec-958c-e1dc2bcab4a6",
            "tokenType": "Bearer",
            "decodedDetails": null
        },
        "authenticated": true,
        "userAuthentication": {
            "authorities": [
                {
                    "authority": "test"
                }
            ],
            "details": null,
            "authenticated": true,
            "principal": {
                "id": 1,
                "username": "cmmplb",
                "password": "N/A",
                "accountNonExpired": true,
                "accountNonLocked": true,
                "credentialsNonExpired": true,
                "enabled": true,
                "authorities": [
                    {
                        "authority": "test"
                    }
                ]
            },
            "credentials": "N/A",
            "name": "cmmplb"
        },
        "credentials": "",
        "principal": {
            "id": 1,
            "username": "cmmplb",
            "password": "N/A",
            "accountNonExpired": true,
            "accountNonLocked": true,
            "credentialsNonExpired": true,
            "enabled": true,
            "authorities": [
                {
                    "authority": "test"
                }
            ]
        },
        "clientOnly": false,
        "oauth2Request": {
            "clientId": "client",
            "scope": [
                "all"
            ],
            "requestParameters": {
                "client_id": "client"
            },
            "resourceIds": [],
            "authorities": [],
            "approved": true,
            "refresh": false,
            "redirectUri": null,
            "responseTypes": [],
            "extensions": {},
            "grantType": null,
            "refreshTokenRequest": null
        },
        "name": "cmmplb"
    }
    *
    *
    * */
}
