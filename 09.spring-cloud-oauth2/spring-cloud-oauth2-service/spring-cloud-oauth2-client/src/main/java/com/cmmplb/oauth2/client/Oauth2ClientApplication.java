package com.cmmplb.oauth2.client;

import com.cmmplb.core.utils.SpringApplicationUtil;
import com.cmmplb.security.oauth2.starter.annotation.EnableResourceServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;

/**
 * @author penglibo
 * @date 2021-04-01 21:07:28
 */

@EnableOAuth2Sso
@SpringBootApplication
public class Oauth2ClientApplication {

    public static void main(String[] args) {
        SpringApplicationUtil.run(Oauth2ClientApplication.class, args);
    }
}
