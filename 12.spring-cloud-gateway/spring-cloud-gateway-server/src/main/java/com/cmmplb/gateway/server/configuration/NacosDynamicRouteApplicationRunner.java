package com.cmmplb.gateway.server.configuration;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * @author penglibo
 * @date 2024-08-14 11:45:06
 * @since jdk 1.8
 * 监听nacos配置动态更新路由
 */

@Slf4j
@Component
public class NacosDynamicRouteApplicationRunner implements ApplicationEventPublisherAware, ApplicationRunner {

    // nacos上配置的dataId,存放的是json路由规则
    private static final String DATA_ID = "gateway-json-routes.json";

    // group名称
    private static final String GROUP = "DEFAULT_GROUP";

    @Value("${spring.cloud.nacos.config.server-addr}")
    private String serverAddr;

    @Autowired
    private RouteDefinitionWriter routeDefinitionWriter;

    private ApplicationEventPublisher applicationEventPublisher;

    private static final List<String> ROUTE_LIST = new ArrayList<>();

    @Override
    public void run(ApplicationArguments args) {
        try {
            // 创建nacos配置服务
            ConfigService configService = NacosFactory.createConfigService(serverAddr);

            // 获取指定的路由配置
            String config = configService.getConfig(DATA_ID, GROUP, 5000);
            log.info("初始化动态路由配置 : {}", config);
            // 项目初始化的时候加载一次动态路由
            publishRoute(config);

            // 添加监听，nacos上的配置变更后会执行
            configService.addListener(DATA_ID, GROUP, new Listener() {

                @Override
                public Executor getExecutor() {
                    return null;
                }

                @Override
                public void receiveConfigInfo(String configStr) {
                    log.info("监听路由配置 : {}", configStr);
                    // 无效字符串不处理
                    if (!StringUtils.hasText(configStr)) {
                        log.error("无效字符串");
                        return;
                    }

                    publishRoute(configStr);

                    log.info("路由配置更新完成");
                }
            });
        } catch (Exception e) {
            log.error("error:", e);
        }
    }

    private void publishRoute(String configStr) {
        List<RouteDefinition> routeDefinitions = JSON.parseObject(configStr, new TypeReference<List<RouteDefinition>>() {
        });

        // 如果等于null，表示反序列化失败，立即返回
        if (null == routeDefinitions) {
            return;
        }

        // 删除路由
        delRoute();

        // 添加路由
        add(routeDefinitions);

        // 发布刷新路由事件
        publishEvent();
    }

    private void publishEvent() {
        applicationEventPublisher.publishEvent(new RefreshRoutesEvent(routeDefinitionWriter));
    }

    /**
     * 添加路由
     */
    private void add(List<RouteDefinition> routeDefinitions) {
        routeDefinitions.forEach(routeDefinition -> {
            routeDefinitionWriter.save(Mono.just(routeDefinition)).subscribe();
            ROUTE_LIST.add(routeDefinition.getId());
        });
    }

    /**
     * 删除路由
     */
    private void delRoute() {
        // 清理掉当前所有路由
        ROUTE_LIST.forEach(id -> routeDefinitionWriter.delete(Mono.just(id)).subscribe());
        // 清空集合
        ROUTE_LIST.clear();
    }

    @Override
    public void setApplicationEventPublisher(@NonNull ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
