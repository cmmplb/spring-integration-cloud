package com.cmmplb.security.oauth2.starter.converter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;

import java.util.Collection;
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

    private static final String N_A = "N/A";

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
        map.put("cmmplb", "1.0.0");
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
