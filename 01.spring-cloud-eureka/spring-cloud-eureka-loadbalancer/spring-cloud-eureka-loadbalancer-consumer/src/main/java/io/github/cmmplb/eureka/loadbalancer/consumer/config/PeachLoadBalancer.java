package io.github.cmmplb.eureka.loadbalancer.consumer.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.DefaultResponse;
import org.springframework.cloud.client.loadbalancer.EmptyResponse;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.loadbalancer.core.NoopServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.SelectedInstanceCallback;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author penglibo
 * @date 2021-12-23 11:47:10
 * @since jdk 1.8
 * https://blog.csdn.net/qq_35799668/article/details/114534023
 * copy的org.springframework.cloud.loadbalancer.core.RoundRobinLoadBalancer
 */

@Slf4j
public class PeachLoadBalancer implements ReactorServiceInstanceLoadBalancer {

    // 请求的次数
    final AtomicInteger position;

    // 服务名称 用于提示报错信息的
    final String serviceId;

    // 自己定义的计数器
    private int flag = 0;

    // 两个参数的构造方法 需要服务名称和实例提供者 这个在方法中传递进来
    public PeachLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider,
                             String serviceId) {
        // 如果不传人请求次数就自己初始化 反正每次都+1
        this(new Random().nextInt(1000), serviceId, serviceInstanceListSupplierProvider);
    }

    public PeachLoadBalancer(int seedPosition, String serviceId, ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider) {
        this.position = new AtomicInteger(seedPosition);
        this.serviceId = serviceId;
        this.serviceInstanceListSupplierProvider = serviceInstanceListSupplierProvider;
    }

    ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;

    @Override
    public Mono<Response<ServiceInstance>> choose(Request request) {
        // 从服务提供者中获取到当前request请求中的serviceInstances并且遍历
        ServiceInstanceListSupplier supplier = serviceInstanceListSupplierProvider
                .getIfAvailable(NoopServiceInstanceListSupplier::new);
        return supplier.get(request).next()
                .map(serviceInstances -> processInstanceResponse(supplier, serviceInstances));
    }

    private Response<ServiceInstance> processInstanceResponse(ServiceInstanceListSupplier supplier,
                                                              List<ServiceInstance> serviceInstances) {
        Response<ServiceInstance> serviceInstanceResponse = getInstanceResponse(serviceInstances);
        if (supplier instanceof SelectedInstanceCallback && serviceInstanceResponse.hasServer()) {
            ((SelectedInstanceCallback) supplier).selectedServiceInstance(serviceInstanceResponse.getServer());
        }
        return serviceInstanceResponse;
    }

    private Response<ServiceInstance> getInstanceResponse(List<ServiceInstance> instances) {
        if (instances.isEmpty()) {
            if (log.isWarnEnabled()) {
                log.warn("No servers available for service: " + serviceId);
            }
            return new EmptyResponse();
        }
        // pos是当前请求的次数 这样可以自定义负载均衡的切换  这个每次+1的操作是复制的 最好是不删
        int pos = Math.abs(this.position.incrementAndGet());

        if (pos % 4 == 0) {
            // 是4的倍数就切换
            flag += 1;
        }
        if (flag >= instances.size()) {
            flag = 0;
        }
        // 主要的就是这句代码设置负载均衡切换
        ServiceInstance instance = instances.get(flag);
        return new DefaultResponse(instance);
    }
}
