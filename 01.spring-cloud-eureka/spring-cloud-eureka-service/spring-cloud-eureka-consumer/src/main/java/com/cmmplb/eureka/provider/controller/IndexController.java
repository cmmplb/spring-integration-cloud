package com.cmmplb.eureka.provider.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author penglibo
 * @date 2021-09-18 15:42:07
 * @since jdk 1.8
 */

@Slf4j
@RestController
public class IndexController {

    @Resource
    private Registration registration;

    @RequestMapping("/")
    public String index() {
        String host = registration.getHost();
        String serviceId = registration.getServiceId();
        String info = "host：" + host + "，service_id：" + serviceId;
        log.info(info);
        return "hello spring boot";
    }
}
