package com.cmmplb.apollo.client.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author penglibo
 * @date 2022-03-23 15:09:23
 * @since jdk 1.8
 */

@Data
@ConfigurationProperties("apollo")
public class ApolloProperties {

    private String name;
}
