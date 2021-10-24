package com.cmmplb.security.oauth2.auth;

import com.cmmplb.core.utils.SpringApplicationUtil;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author penglibo
 * @date 2021-04-01 21:07:28
 */

@SpringBootApplication
//@Import({WebSecurityConfigurer.class, AuthorizationServerConfigurer.class}) // 执行顺序AuthorizationServerConfigurerAdapter->WebSecurityConfigurerAdapter导致AuthenticationManager依赖注入空指针(oauth2)
public class SpringCloudOauth2AuthApplication {

    public static void main(String[] args) {
        SpringApplicationUtil.run(SpringCloudOauth2AuthApplication.class, args);
    }

}
