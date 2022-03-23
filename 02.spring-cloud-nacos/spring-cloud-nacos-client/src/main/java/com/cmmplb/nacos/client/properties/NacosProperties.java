package com.cmmplb.nacos.client.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author penglibo
 * @date 2022-03-23 09:25:03
 * @since jdk 1.8
 */

@Data
@ConfigurationProperties(prefix = "nacos")
public class NacosProperties {

    private String name;
}
