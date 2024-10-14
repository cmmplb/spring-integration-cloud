package com.cmmplb.auth2.auth;

import io.github.cmmplb.core.utils.SpringApplicationUtil;
import com.cmmplb.security.oauth2.starter.annotation.EnableResourceServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author penglibo
 * @date 2021-04-01 21:07:28
 */

@EnableResourceServer
@SpringBootApplication
public class AuthApplication {

    public static void main(String[] args) {
        SpringApplicationUtil.run(AuthApplication.class, args);
    }
}
