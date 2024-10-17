package io.github.cmmplb.nacos.client.controller;

import io.github.cmmplb.nacos.client.properties.NacosProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author penglibo
 * @date 2021-09-26 09:52:54
 * @since jdk 1.8
 */

@RefreshScope
@RestController
@EnableConfigurationProperties(NacosProperties.class)
public class IndexController {

    @Autowired
    private NacosProperties nacosProperties; // 这个动态刷新需要EnableConfigurationProperties,自动支持

    @Value("${nacos.name}")  // 这个动态刷新需要注解@RefreshScope
    private String name;

    @RequestMapping("/")
    public String index() {
        return nacosProperties.getName() + "=" + name;
    }
}
