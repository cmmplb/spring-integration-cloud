package com.cmmplb.oauth2.system;

import io.github.cmmplb.core.utils.SpringApplicationUtil;
import com.cmmplb.security.oauth2.starter.annotation.EnableResourceServer;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author penglibo
 * @date 2021-04-01 21:07:28
 */

@EnableResourceServer
@SpringBootApplication
public class SystemApplication {

    public static void main(String[] args) {
        SpringApplicationUtil.run(SystemApplication.class, args);
    }
}