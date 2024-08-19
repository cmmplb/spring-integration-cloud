package com.cmmplb.gateway.server.configuration;

import com.cmmplb.gateway.server.utils.RequestUtil;
import com.cmmplb.gateway.server.utils.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

/**
 * @author penglibo
 * @date 2024-08-13 16:53:23
 * @since jdk 1.8
 * 通过配置路由规则,修改请求体和响应体
 */

@Slf4j
@Configuration
public class RouteModifyBodyConfiguration {

    private static final String LB_SERVICE_NAME = "lb://spring-cloud-gateway-service";

    /**
     * 路由规则配置,添加请求体和响应体修改逻辑
     */
    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {

        // 添加路由规则
        return builder.routes().route("modify_body_route",
                predicateSpec -> predicateSpec.path("/modify/body/route/**").filters(filter ->
                        // 去除前缀
                        filter.stripPrefix(3)
                                // 修改请求体,对下游服务请求参数添加一个request字段,第一个参数为原请求体类型,第二个参数为修改后请求体类型,第三个参数新Content-Type标头,第四个参数为修改请求体函数
                                .modifyRequestBody(String.class, String.class, MediaType.APPLICATION_JSON_VALUE,
                                        // 参数都为String类型传递json字符串
                                        (serverWebExchange, params) -> RequestUtil.modifyRequestBody(params)
                                        // 修改响应体,对下游服务相应参数添加一个response字段,第一个参数为原响应体类型,第二个参数为修改后响应体类型,第三个参数新Content-Type标头,第四个参数为修改响应体函数
                                ).modifyResponseBody(String.class, String.class, MediaType.APPLICATION_JSON_VALUE,
                                        // 参数都为String类型传递json字符串
                                        (serverWebExchange, body) -> ResponseUtil.modifyResponseBody(body))
                ).uri(LB_SERVICE_NAME)
        ).build();
    }
}
