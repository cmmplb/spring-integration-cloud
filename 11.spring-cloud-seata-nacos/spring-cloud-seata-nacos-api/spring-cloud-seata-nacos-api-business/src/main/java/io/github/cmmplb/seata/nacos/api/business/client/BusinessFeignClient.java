package io.github.cmmplb.seata.nacos.api.business.client;


import io.github.cmmplb.seata.nacos.api.business.fallback.BusinessFeignClientFallBack;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author penglibo
 * @date 2021-05-07 14:14:02
 * @since jdk 1.8
 */

@FeignClient(contextId = "seata-business", name = "seata-service-business", path = "/api/business", fallbackFactory = BusinessFeignClientFallBack.class)
public interface BusinessFeignClient {

}
