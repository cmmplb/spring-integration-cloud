package com.cmmplb.apollo.client.config;

import com.cmmplb.core.utils.StringUtils;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Set;

/**
 * @author penglibo
 * @date 2021-09-18 09:53:33
 * @since jdk 1.8
 * 动态配置日志级别
 */

@Slf4j
@Configuration
public class LoggerConfig {

    private static final String LOGGER_TAG = "logging.level.";

    @Resource
    private LoggingSystem loggingSystem;

    @ApolloConfig // 将Apollo服务端的中的配置注入这个类中。
    private Config config;

    /**
     * @param changeEvent 可以获取被修改配置项的key集合，以及被修改配置项的新值、旧值和修改类型等信息。
     */
    @ApolloConfigChangeListener // 监听配置中心配置的更新事件，若该事件发生，则调用refreshLoggingLevels方法，处理该事件。
    private void configChangeListener(ConfigChangeEvent changeEvent) {
        refreshLoggingLevels();
    }

    @PostConstruct // 通过@PostConstruct 项目启动的时候就去获取apollo的日志级别去覆盖application.yml日志级别。
    private void refreshLoggingLevels() {
        // 在apollo配置中心，新增加一条日志配置。logging.level.com.cmmplb=error
        // 把日志级别设置error,那么在项目启动的时候,因为PostConstruct注解的原因，所以会去执行一次refreshLoggingLevels方法，把当前日志级别改成error。
        Set<String> keyNames = config.getPropertyNames();
        for (String key : keyNames) {
            if (StringUtils.containsIgnoreCase(key, LOGGER_TAG)) {
                String strLevel = config.getProperty(key, "info");
                LogLevel level = LogLevel.valueOf(strLevel.toUpperCase());
                loggingSystem.setLogLevel(key.replace(LOGGER_TAG, ""), level);
                log.info("{}:{}", key, strLevel);
            }
        }
    }
}
