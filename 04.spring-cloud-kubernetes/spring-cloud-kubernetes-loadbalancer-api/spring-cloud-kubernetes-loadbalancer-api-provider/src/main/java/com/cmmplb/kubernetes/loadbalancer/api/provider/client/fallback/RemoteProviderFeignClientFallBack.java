package com.cmmplb.kubernetes.loadbalancer.api.provider.client.fallback;

import com.cmmplb.core.exception.CustomException;
import com.cmmplb.core.result.Result;
import com.cmmplb.core.result.ResultUtil;
import com.cmmplb.core.utils.ObjectUtil;
import com.cmmplb.kubernetes.loadbalancer.api.provider.client.RemoteProviderFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * @author penglibo
 * @date 2021-11-05 11:53:16
 * @since jdk 1.8
 * 服务熔断降级处理
 */

@Slf4j
public class RemoteProviderFeignClientFallBack implements FallbackFactory<RemoteProviderFeignClient> {

    @Override
    public RemoteProviderFeignClient create(Throwable throwable) {

        return new RemoteProviderFeignClient() {
            CustomException c = ObjectUtil.cast(throwable);

            @Override
            public Result<String> getName() {
                return ResultUtil.custom(c.getCode(), c.getMessage());
            }
        };
    }
}
