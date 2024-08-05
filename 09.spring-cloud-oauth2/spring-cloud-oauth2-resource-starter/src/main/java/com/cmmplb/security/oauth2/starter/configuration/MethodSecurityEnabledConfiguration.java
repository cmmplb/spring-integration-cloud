package com.cmmplb.security.oauth2.starter.configuration;

import com.cmmplb.security.oauth2.starter.configuration.properties.SecurityProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

/**
 * @author penglibo
 * @date 2024-07-30 16:12:27
 * @since jdk 1.8
 * 开启/关闭注解权限控制
 */

@EnableGlobalMethodSecurity(prePostEnabled = true)
@ConditionalOnProperty(prefix = SecurityProperties.PREFIX, name = SecurityProperties.ENABLED, havingValue = "true")
public class MethodSecurityEnabledConfiguration {

}