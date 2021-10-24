package com.cmmplb.security.oauth2.client;

import com.cmmplb.core.utils.SpringApplicationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;

/**
 * @author penglibo
 * @date 2021-04-01 21:07:28
 */

@Slf4j
@EnableOAuth2Sso
@SpringBootApplication
public class SpringCloudOauth2ClientApplication {

    public static void main(String[] args) {
        SpringApplicationUtil.run(SpringCloudOauth2ClientApplication.class, args);
    }
}
