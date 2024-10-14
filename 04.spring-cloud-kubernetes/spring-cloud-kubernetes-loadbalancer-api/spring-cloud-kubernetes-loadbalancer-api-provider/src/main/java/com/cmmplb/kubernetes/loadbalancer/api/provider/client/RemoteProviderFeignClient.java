package com.cmmplb.kubernetes.loadbalancer.api.provider.client;

import io.github.cmmplb.core.result.Result;
import com.cmmplb.kubernetes.loadbalancer.api.provider.client.fallback.RemoteProviderFeignClientFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author penglibo
 * @date 2021-11-05 11:46:08
 * @since jdk 1.8
 */

@FeignClient(contextId = "user-feign", name = "spring-cloud-kubernetes-loadbalancer-provider", fallbackFactory = RemoteProviderFeignClientFallBack.class)
public interface RemoteProviderFeignClient {

    @GetMapping("/name")
    Result<String> getName();
}
