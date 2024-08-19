package com.cmmplb.oauth2.client.sso;

import com.cmmplb.core.utils.SpringApplicationUtil;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;

/**
 * @author penglibo
 * @date 2021-04-01 21:07:28
 */

@EnableOAuth2Sso
@SpringBootApplication
public class Oauth2ClientSsoApplication {

    public static void main(String[] args) {
        SpringApplicationUtil.run(Oauth2ClientSsoApplication.class, args);
    }
}
