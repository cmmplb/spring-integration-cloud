package com.cmmplb.security.oauth2.auth;

import com.cmmplb.core.utils.SpringApplicationUtil;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author penglibo
 * @date 2021-04-01 21:07:28
 */

@SpringBootApplication
public class Oauth2AuthApplication {

    public static void main(String[] args) {
        SpringApplicationUtil.run(Oauth2AuthApplication.class, args);
    }

}
