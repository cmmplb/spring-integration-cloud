package com.cmmplb.security.oauth2.starter.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author penglibo
 * @date 2021-12-27 10:44:31
 * @since jdk 1.8
 */

@Data
@ConfigurationProperties(prefix = "rest.template")
public class RestTemplateProperties {

    /**
     * rest读取配置
     */
    private int readTimeout = -1;

    /**
     * rest连接配置
     */
    private int connectionTimeout = -1;

    /**
     * 代理配置
     */
    private Proxy proxy = new Proxy();

    @Data
    public static class Proxy {

        /**
         * 是否启用代理
         */
        private Boolean enabled = false;

        /**
         * 代理主机地址
         */
        private String host;

        /**
         * 代理端口
         */
        private Integer port;
    }
}
