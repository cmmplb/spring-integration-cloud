package com.cmmplb.auth2.auth.dto;

import lombok.Data;

/**
 * @author penglibo
 * @date 2022-09-29 10:48:20
 * @since jdk 1.8
 */

@Data
public class OauthToken {

    private String username;

    private String password;

    private String grantType;
}
