package com.cmmplb.auth2.auth.handler.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * @author penglibo
 * @date 2021-10-20 15:50:54
 * @since jdk 1.8
 * 退出登录处理
 */

@Slf4j
@Component
public class LogoutSuccessEventHandler implements ApplicationListener<LogoutSuccessEvent> {

    @Override
    public void onApplicationEvent(LogoutSuccessEvent event) {
        Authentication authentication = (Authentication) event.getSource();
        log.info("退出登录处理authentication:{}", authentication);
        log.info("=============保存退出登录处理日志等操作==================");
    }
}
