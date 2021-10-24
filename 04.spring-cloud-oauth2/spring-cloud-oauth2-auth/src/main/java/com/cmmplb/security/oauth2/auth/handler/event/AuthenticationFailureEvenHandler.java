package com.cmmplb.security.oauth2.auth.handler.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * @author penglibo
 * @date 2021-10-20 15:49:07
 * @since jdk 1.8
 * 登录失败处理
 */

@Slf4j
@Component
public class AuthenticationFailureEvenHandler implements ApplicationListener<AbstractAuthenticationFailureEvent> {

    @Override
    public void onApplicationEvent(AbstractAuthenticationFailureEvent event) {
        Authentication authentication = (Authentication) event.getSource();
        log.info("登录失败处理authentication:{}", authentication);
        log.info("=============保存登录失败日志等操作==================");
    }
}
