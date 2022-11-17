package com.cmmplb.auth2.auth.handler.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * @author penglibo
 * @date 2021-10-19 09:22:48
 * @since jdk 1.8
 * 登录成功的处理==>保存登录日志等操作
 */

@Slf4j
@Component
public class AuthenticationSuccessEventHandler implements ApplicationListener<AuthenticationSuccessEvent> {

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        Authentication authentication = (Authentication) event.getSource();
        log.info("登录成功的处理authentication:{}", authentication);
        log.info("=============保存登录成功日志等操作==================");
    }

    /*@Resource
    private RemoteLoginFeignClient remoteLoginFeignClient;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        Authentication authentication = (Authentication) event.getSource();
        if (StringUtil.isNotEmpty(authentication.getAuthorities()) && authentication.getPrincipal() instanceof User) {
            User user = (User) authentication.getPrincipal();
            // 设置登陆时间
            remoteLoginFeignClient.saveLoginInfo(user.getUsername(), SecurityConstants.IN);
        }
    }*/
}
