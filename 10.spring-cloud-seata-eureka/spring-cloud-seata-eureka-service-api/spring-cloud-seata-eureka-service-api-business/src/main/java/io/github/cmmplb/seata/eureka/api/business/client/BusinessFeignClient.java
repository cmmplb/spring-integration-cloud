package io.github.cmmplb.seata.eureka.api.business.client;

import io.github.cmmplb.seata.eureka.common.constant.ServiceNameConstants;
import io.github.cmmplb.seata.eureka.api.business.client.fallback.BusinessFeignClientFallBack;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author penglibo
 * @date 2021-05-07 14:14:02
 * @since jdk 1.8
 */

@FeignClient(contextId = "seata-business", name = ServiceNameConstants.BUSINESS_SERVICE, fallbackFactory = BusinessFeignClientFallBack.class)
public interface BusinessFeignClient {

}
