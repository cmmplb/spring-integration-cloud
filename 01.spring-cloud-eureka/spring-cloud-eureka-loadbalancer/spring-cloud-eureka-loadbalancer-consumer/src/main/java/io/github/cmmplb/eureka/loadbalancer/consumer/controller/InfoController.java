package io.github.cmmplb.eureka.loadbalancer.consumer.controller;

import io.github.cmmplb.eureka.loadbalancer.provider.api.RemoteInfoFeign;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author penglibo
 * @date 2021-09-18 15:42:07
 * @since jdk 1.8
 */

@Slf4j
@RestController
public class InfoController {

    @Resource
    private Registration registration;

    @Autowired
    private RestTemplate restTemplate;

    @Resource
    private RemoteInfoFeign remoteInfoFeign;

    @Autowired
    private DiscoveryClient discoveryClient;

    @RequestMapping("/services")
    public List<String> services() {
        return this.discoveryClient.getServices();
    }

    @GetMapping("/")
    public String getInfo() {
        String host = registration.getHost();
        String serviceId = registration.getServiceId();
        String info = "host：" + host + "，service_id：" + serviceId;
        log.info(info);
        return info;
    }

    @GetMapping("/provider")
    public String providerInfo() {
        return this.restTemplate.getForEntity("http://spring-cloud-eureka-provider/info", String.class).getBody();
        // return this.restTemplate.getForEntity("http://localhost:80/info", String.class).getBody();
    }

    @GetMapping("/provider/feign")
    public String feignProviderInfo() {
        return remoteInfoFeign.getInfo();
    }
}
