package com.cmmplb.kubernetes.service.api.provider.client;

import com.cmmplb.core.result.Result;
import com.cmmplb.kubernetes.service.api.provider.client.fallback.RemoteProviderFeignClientFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author penglibo
 * @date 2021-11-05 11:46:08
 * @since jdk 1.8
 */

@FeignClient(contextId = "user-feign", name = "spring-cloud-kubernetes-service-provider", fallbackFactory = RemoteProviderFeignClientFallBack.class)
public interface RemoteProviderFeignClient {

    @GetMapping("/name")
    Result<String> getName();
}
