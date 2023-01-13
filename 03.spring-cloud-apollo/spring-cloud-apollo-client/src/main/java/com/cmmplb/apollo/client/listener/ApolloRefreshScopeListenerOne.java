// package com.cmmplb.apollo.client.listener;
//
// import com.ctrip.framework.apollo.model.ConfigChange;
// import com.ctrip.framework.apollo.model.ConfigChangeEvent;
// import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.cloud.context.scope.refresh.RefreshScope;
// import org.springframework.stereotype.Component;
//
// /**
//  * @author penglibo
//  * @date 2022-12-12 13:52:28
//  * @since jdk 1.8
//  */
//
// @Slf4j
// @Component
// public class ApolloRefreshScopeListenerOne {
//
//     @Autowired
//     private RefreshScope refreshScope;
//
//     // @ApolloConfigChangeListeners 默认监控命名空间是 application.properties， 如果是自己创建的namespace ，一定要明确指定（包含文件扩展名）。
//     // @ApolloConfigChangeListener(interestedKeyPrefixes = "apollo")
//     @ApolloConfigChangeListener(value = {"client.yml"})
//     public void refresh(ConfigChangeEvent changeEvent) {
//         log.info("changeEvent ApolloRefreshScopeListenerOne");
//         for (String key : changeEvent.changedKeys()) {
//             ConfigChange change = changeEvent.getChange(key);
//             log.info("Found change - {}", change.toString());
//         }
//         // 指定要刷新的bean
//         refreshScope.refresh("apolloProperties");
//     }
// }
