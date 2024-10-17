package io.github.cmmplb.apollo.client.controller;

import io.github.cmmplb.apollo.client.config.properties.ApolloProperties;
import io.github.cmmplb.core.result.Result;
import io.github.cmmplb.core.result.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author penglibo
 * @date 2022-03-23 10:09:58
 * @since jdk 1.8
 */

@Slf4j
@RestController
@EnableConfigurationProperties(ApolloProperties.class)
public class IndexController {

    @Autowired
    private Environment env; // 这个可以动态刷新

    // apollo中的配置修改后，应用中不会动态刷新，需要手动编写程序实现动态刷新。因为spring启动时，已读取配置值且生成了对象。之后再修改配置值后，对象中的属性值并不会改变。
    // 1）基于RefreshScope结合@ApolloConfigChangeListener实现
    // 2）基于EnvironmentChangeEvent结合@ApolloConfigChangeListener实现
    // properties添加@RefreshScope
    @Autowired
    private ApolloProperties apolloProperties; // 这个修改没生效

    @Value("${apollo.name}")
    private String name; // 这个可以动态刷新

    @Value("${test.name}")
    private String testName; // 这个可以动态刷新


    @RequestMapping("/")
    public Result<?> index() {
        log.debug("debug 打印出的日志");
        log.info("info 打印出的日志");
        log.error("error 打印出的日志");

        System.out.println("env:" + env.getProperty("apollo.name"));
        System.out.println("value:" + name);
        System.out.println("testName:" + testName);
        System.out.println("properties:" + apolloProperties.getName());
        return ResultUtil.success();
    }
}
