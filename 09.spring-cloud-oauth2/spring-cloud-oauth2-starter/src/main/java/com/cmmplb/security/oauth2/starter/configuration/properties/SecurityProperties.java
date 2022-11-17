package com.cmmplb.security.oauth2.starter.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author penglibo
 * @date 2021-11-10 10:39:02
 * @since jdk 1.8
 */

@ConfigurationProperties(prefix = "security")
public class SecurityProperties {

    /**
     * 开启资源拦截
     */
    @Getter
    @Setter
    private Boolean enabled = true;
}
