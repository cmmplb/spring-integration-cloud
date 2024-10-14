package com.cmmplb.kubernetes.loadbalancer.consumer.controller;

import io.github.cmmplb.core.result.Result;
import io.github.cmmplb.core.result.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cmmplb.kubernetes.loadbalancer.api.provider.client.RemoteProviderFeignClient;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author penglibo
 * @date 2022-06-10 09:35:12
 * @since jdk 1.8
 */

@Slf4j
@RestController
public class KubernetesConsumerController {

    private final String hostName = System.getenv("HOSTNAME");

    @Resource
    private RemoteProviderFeignClient remoteProviderFeignClient;

    @Autowired
    private DiscoveryClient discoveryClient;

    @RequestMapping("/services")
    public List<String> services() {
        return this.discoveryClient.getServices();
    }

    /**
     * 探针检查响应类
     */
    @RequestMapping("/health")
    public Result<String> health() {
        return ResultUtil.success("OK");
    }

    @RequestMapping("/")
    public Result<String> ribbonPing() {
        log.info("ribbonPing of {}", hostName);
        return ResultUtil.success(hostName);
    }

    /**
     * 远程调用account-service提供的服务
     * @return 多次远程调返回的所有结果.
     */
    @RequestMapping("/account")
    public Result<String> account() {
        StringBuilder sbud = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sbud.append(remoteProviderFeignClient.getName())
                    .append("<br>");
        }
        return ResultUtil.success(sbud.toString());
    }
}