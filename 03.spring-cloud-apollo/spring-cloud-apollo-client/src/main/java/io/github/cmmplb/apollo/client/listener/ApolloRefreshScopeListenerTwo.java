package io.github.cmmplb.apollo.client.listener;

import com.ctrip.framework.apollo.model.ConfigChange;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author penglibo
 * @date 2022-12-12 13:52:28
 * @since jdk 1.8
 */

@Slf4j
@Component
public class ApolloRefreshScopeListenerTwo {

    @Autowired
    private ApplicationContext applicationContext;

    // @ApolloConfigChangeListeners 默认监控命名空间是 application.properties， 如果是自己创建的namespace ，一定要明确指定（包含文件扩展名）。
    // @ApolloConfigChangeListener(interestedKeyPrefixes = "apollo")
    @ApolloConfigChangeListener(value = {"client.yml"})
    public void doRefresh(ConfigChangeEvent changeEvent) {
        log.info("changeEvent ApolloRefreshScopeListenerTwo");
        for (String key : changeEvent.changedKeys()) {
            ConfigChange change = changeEvent.getChange(key);
            log.info("Found change - {}", change.toString());
        }
        // 更新相应的bean的属性值，主要是存在@ConfigurationProperties注解的bean
        applicationContext.publishEvent(new EnvironmentChangeEvent(changeEvent.changedKeys()));
    }
}
