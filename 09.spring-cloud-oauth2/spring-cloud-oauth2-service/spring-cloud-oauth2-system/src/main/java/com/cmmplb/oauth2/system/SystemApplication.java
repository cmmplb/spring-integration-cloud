package com.cmmplb.oauth2.system;

import com.cmmplb.core.utils.SpringApplicationUtil;
import com.cmmplb.security.oauth2.starter.annotation.EnableResourceServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;

/**
 * @author penglibo
 * @date 2021-04-01 21:07:28
 */

@Slf4j
@EnableOAuth2Sso
@EnableResourceServer
@SpringBootApplication
public class SystemApplication {

    public static void main(String[] args) {
        SpringApplicationUtil.run(SystemApplication.class, args);
    }
}