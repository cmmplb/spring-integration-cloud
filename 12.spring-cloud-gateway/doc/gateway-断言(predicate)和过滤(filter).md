# gateway-断言(predicate)和过滤(filter)

https://www.cnblogs.com/interface-/p/17160967.html

## GateWay路由断言工厂

多个Route Predicate工厂可以进行组合。

官方文档：https://docs.spring.io/spring-cloud-gateway/docs/2.2.9.RELEASE/reference/html/#gateway-request-predicates-factories

抽象父类：org.springframework.cloud.gateway.support.AbstractConfigurable

### After Route Predicate Factory

**在指定时间之后的请求会匹配该路由**

````yaml
routes:
  # ========================服务模块========================
  - id: spring_cloud_gateway_service
    uri: lb://spring-cloud-gateway-service
    # 路由断言
    predicates:
      # 路由断言,在指定时间之后的请求会匹配该路由
      - After=2020-01-01T00:00:00.000+08:00[+08:00]
````

````json
{
  "id": "spring_cloud_gateway_service",
  "uri": "lb://spring-cloud-gateway-service",
  "predicates": [
    {
      "name": "After",
      "args": {
        "datetime": "2020-01-01T00:00:00.000+08:00[+08:00]"
      }
    }
  ]
}
````

### Before Route Predicate Factory

**在指定时间之前的请求会匹配该路由**

````yaml
routes:
  # ========================服务模块========================
  - id: spring_cloud_gateway_service
    uri: lb://spring-cloud-gateway-service
    # 路由断言
    predicates:
      # 路由断言,在指定时间之前的请求会匹配该路由
      - Before=2020-01-01T00:00:00.000+08:00[+08:00]
````

````json
{
  "id": "spring_cloud_gateway_service",
  "uri": "lb://spring-cloud-gateway-service",
  "predicates": [
    {
      "name": "Before",
      "args": {
        "datetime": "2020-01-01T00:00:00.000+08:00[+08:00]"
      }
    }
  ]
}
````

### Between Route Predicate Factory

**在指定时间区间内的请求会匹配该路由**

````yaml
routes:
  # ========================服务模块========================
  - id: spring_cloud_gateway_service
    uri: lb://spring-cloud-gateway-service
    # 路由断言
    predicates:
      # 路由断言,在指定时间区间内的请求会匹配该路由
      - Between=2020-01-01T00:00:00.000+08:00[+08:00],2020-01-01T00:00:00.000+08:00[+08:00]
````

````json
{
  "id": "spring_cloud_gateway_service",
  "uri": "lb://spring-cloud-gateway-service",
  "predicates": [
    {
      "name": "Between",
      "args": {
        "datetime1": "2020-01-01T00:00:00.000+08:00[+08:00]",
        "datetime2": "2020-01-01T00:00:00.000+08:00[+08:00]"
      }
    }
  ]
}
````

### Cookie Route Predicate Factory

**带有指定Cookie的请求会匹配该路由**

````yaml
routes:
  # ========================服务模块========================
  - id: spring_cloud_gateway_service
    uri: lb://spring-cloud-gateway-service
    # 路由断言
    predicates:
      # 路由断言,带有指定Cookie的请求会匹配该路由
      - Cookie=Authorization,Bearer 123456
````

Authorization是cookie的名称，Bearer 123456是cookie的值。

````json
{
  "id": "spring_cloud_gateway_service",
  "uri": "lb://spring-cloud-gateway-service",
  "predicates": [
    {
      "name": "Cookie",
      "args": {
        "name": "Authorization",
        "regexp": "Bearer 123456"
      }
    }
  ]
}
````

### Header Route Predicate Factory

**带有指定Header的请求会匹配该路由**

````yaml
routes:
  # ========================服务模块========================
  - id: spring_cloud_gateway_service
    uri: lb://spring-cloud-gateway-service
    # 路由断言
    predicates:
      # 路由断言,带有指定Header的请求会匹配该路由
      - Header=X-Request-Id,\d+
````

对应的值符合指定正则表达式，才算匹配成功。

````json
{
  "id": "spring_cloud_gateway_service",
  "uri": "lb://spring-cloud-gateway-service",
  "predicates": [
    {
      "name": "Header",
      "args": {
        "header": "X-Request-Id",
        "regexp": "\\d+"
      }
    }
  ]
}
````

### Host Route Predicate Factory

**带有指定Host的请求会匹配该路由**

````yaml
routes:
  # ========================服务模块========================
  - id: spring_cloud_gateway_service
    uri: lb://spring-cloud-gateway-service
    # 路由断言
    predicates:
      # 路由断言,带有指定Host的请求会匹配该路由
      - Host=**.example.org
````

````json
{
  "id": "spring_cloud_gateway_service",
  "uri": "lb://spring-cloud-gateway-service",
  "predicates": [
    {
      "name": "Host",
      "args": {
        "_genkey_0": "**.example.org"
      }
    }
  ]
}
````

### Method Route Predicate Factory

**带有指定Method的请求会匹配该路由**

````yaml
routes:
  # ========================服务模块========================
  - id: spring_cloud_gateway_service
    uri: lb://spring-cloud-gateway-service
    # 路由断言
    predicates:
      # 路由断言,带有指定Method的请求会匹配该路由
      - Method=GET,POST
````

````json
{
  "id": "spring_cloud_gateway_service",
  "uri": "lb://spring-cloud-gateway-service",
  "predicates": [
    {
      "name": "Method",
      "args": {
        "_genkey_0": "GET",
        "_genkey_1": "POST"
      }
    }
  ]
}
````

### Path Route Predicate Factory

**带有指定Path的请求会匹配该路由**

````yaml
routes:
  # ========================服务模块========================
  - id: spring_cloud_gateway_service
    uri: lb://spring-cloud-gateway-service
    # 路由断言
    predicates:
      # 路由断言,带有指定Method的请求会匹配该路由
      - Path=/service/**,/info/{segment}
````

````json
{
  "id": "spring_cloud_gateway_service",
  "uri": "lb://spring-cloud-gateway-service",
  "predicates": [
    {
      "name": "Path",
      "args": {
        "_genkey_0": "/service/**",
        "_genkey_1": "/info/{segment}"
      }
    }
  ]
}
````

````shell
curl http://localhost:20000/info/1
````

### Query Route Predicate Factory

**带指定查询参数的请求可以匹配该路由**

````yaml
routes:
  # ========================服务模块========================
  - id: spring_cloud_gateway_service
    uri: lb://spring-cloud-gateway-service
    # 路由断言
    predicates:
      # 路由断言,带指定查询参数的请求可以匹配该路由
      - Query=name,value
````

name的参数，值为value。

````json
{
  "id": "spring_cloud_gateway_service",
  "uri": "lb://spring-cloud-gateway-service",
  "predicates": [
    {
      "name": "Query",
      "args": {
        "param": "name",
        "regexp": "value"
      }
    }
  ]
}
````

### RemoteAddr Route Predicate Factory

**带有指定RemoteAddr的请求会匹配该路由**

````yaml
routes:
  # ========================服务模块========================
  - id: spring_cloud_gateway_service
    uri: lb://spring-cloud-gateway-service
    # 路由断言
    predicates:
      # 路由断言,带有指定RemoteAddr的请求会匹配该路由
      - RemoteAddr=127.0.0.1
````

````json
{
  "id": "spring_cloud_gateway_service",
  "uri": "lb://spring-cloud-gateway-service",
  "predicates": [
    {
      "name": "RemoteAddr",
      "args": {
        "_genkey_0": "127.0.0.1"
      }
    }
  ]
}
````

### Weight Route Predicate Factory

**带有指定Weight的请求会匹配该路由**

````yaml
routes:
  # ========================服务模块========================
  - id: spring_cloud_gateway_service
    uri: lb://spring-cloud-gateway-service
    # 路由断言
    predicates:
      # 路由断言,带指定查询参数的请求可以匹配该路由
      - Weight=group1,8
````

权重这个一般不用了，在微服务中一般有loadbalancer负载均衡器在，所以请求的分发一般由它来负责。

````json
{
  "id": "spring_cloud_gateway_service",
  "uri": "lb://spring-cloud-gateway-service",
  "predicates": [
    {
      "name": "Weight",
      "args": {
        "weight.group": "group1",
        "weight.weight": "8"
      }
    }
  ]
}
````

## GateWay过滤器工厂

路由过滤器可用于修改进入的HTTP请求和返回的HTTP响应

与Predicates相比，Filters的功能更加全面。它不仅可以在请求到达目标之前进行拦截，还可以对响应进行修改和修饰。

具体来说，Filters可以用于修改响应报文，增加或修改Header或Cookie，甚至可以修改响应的主体内容。这些功能是Predicates所不具备的。

官方文档：https://docs.spring.io/spring-cloud-gateway/docs/2.2.9.RELEASE/reference/html/#gatewayfilter-factories

### AddRequestParameter GatewayFilter

**添加参数过滤器**

````yaml
routes:
  # ========================服务模块========================
  - id: spring_cloud_gateway_service
    uri: lb://spring-cloud-gateway-service
    # 路由断言,带指定查询参数的请求可以匹配该路由
    predicates:
      - Path=/service/**
    # 过滤器配置
    filters:
      # 添加参数过滤器
      - AddRequestParameter=name,张三  
````

````json
{
  "name": "AddRequestParameter",
  "args": {
    "_genkey_0": "name",
    "_genkey_1": "张三"
  }
}
````

````shell
curl http://localhost:20000/service/info
# 等价于
curl http://localhost:20000/service/info?name=张三
````

### The AddRequestHeader GatewayFilter Factory

**添加请求头(键和值)**

````yaml
routes:
  # ========================服务模块========================
  - id: spring_cloud_gateway_service
    uri: lb://spring-cloud-gateway-service
    # 路由断言,带指定查询参数的请求可以匹配该路由
    predicates:
      - Path=/service/**
    # 过滤器配置
    filters:
      # 添加请求头
      - AddRequestHeader=Authorization,Bearer 123456
````

添加一个名为Authorization的请求头参数，值为Bearer 123456。

````json
{
  "name": "AddRequestHeader",
  "args": {
    "_genkey_0": "Authorization",
    "_genkey_1": "Bearer 123456"
  }
}
````

### RewritePath

**路径重写**

````yaml
routes:
  # ========================服务模块========================
  - id: spring_cloud_gateway_service
    uri: lb://spring-cloud-gateway-service
    # 路由断言,带指定查询参数的请求可以匹配该路由
    predicates:
      - Path=/service/**
    # 过滤器配置
    filters:
      - RewritePath = /path/(?<segment>.*), /$\{segment}
````

````json
{
  "name": "RewritePath",
  "args": {
    "_genkey_0": "/path/(?<segment>.*)",
    "_genkey_1": "/$\\{segment}"
  }
}
````

### The AddResponseHeader GatewayFilter Factory

**添加响应头**

````yaml
routes:
  # ========================服务模块========================
  - id: spring_cloud_gateway_service
    uri: lb://spring-cloud-gateway-service
    # 路由断言,带指定查询参数的请求可以匹配该路由
    predicates:
      - Path=/service/**
    # 过滤器配置
    filters:
      # 添加响应头
      - AddResponseHeader=Content-Type,application/json
````

````json
{
  "name": "AddResponseHeader",
  "args": {
    "_genkey_0": "Content-Type",
    "_genkey_1": "application/json"
  }
}
````

### StripPath GatewayFilter

**指定数量的路径前缀进行去除**

````yaml
routes:
  # ========================服务模块========================
  - id: spring_cloud_gateway_service
    uri: lb://spring-cloud-gateway-service
    # 路由断言,带指定查询参数的请求可以匹配该路由
    predicates:
      - Path=/service/**
    # 过滤器配置
    filters:
      # 指定数量的路径前缀进行去除
      - StripPrefix=1
````

````json
{
  "name": "StripPrefix",
  "args": {
    "_genkey_0": "1"
  }
}
````

### RedirectTo

**重定向**

指定响应码和重定向路径

````yaml
routes:
  # ========================服务模块========================
  - id: spring_cloud_gateway_service
    uri: lb://spring-cloud-gateway-service
    # 路由断言,带指定查询参数的请求可以匹配该路由
    predicates:
      - Path=/service/**
    # 过滤器配置
    filters:
      - RedirectTo = 302,https://www.baidu.com
````

````json
{
  "name": "RedirectTo",
  "args": {
    "_genkey_0": "302",
    "_genkey_1": "https://www.baidu.com"
  }
}
````

### RemoveRequestHeader

删除请求头属性

````yaml
routes:
  # ========================服务模块========================
  - id: spring_cloud_gateway_service
    uri: lb://spring-cloud-gateway-service
    # 路由断言,带指定查询参数的请求可以匹配该路由
    predicates:
      - Path=/service/**
    # 过滤器配置
    filters:
      - RemoveRequestHeader = X-Request-Foo
````

````json
{
  "name": "RemoveRequestHeader",
  "args": {
    "_genkey_0": "X-Request-Foo"
  }
}
````

### RemoveResponseHeader

删除响应头属性

````yaml
routes:
  # ========================服务模块========================
  - id: spring_cloud_gateway_service
    uri: lb://spring-cloud-gateway-service
    # 路由断言,带指定查询参数的请求可以匹配该路由
    predicates:
      - Path=/service/**
    # 过滤器配置
    filters:
      - RemoveResponseHeader = X-Request-Foo
````

````json
{
  "name": "RemoveResponseHeader",
  "args": {
    "_genkey_0": "X-Request-Foo"
  }
}
````

### ModifyResponseBodyGatewayFilterFactory

**修改响应主体**

````yaml
routes:
  # ========================服务模块========================
  - id: spring_cloud_gateway_service
    uri: lb://spring-cloud-gateway-service
    # 路由断言
    predicates:
      - Path=/service/**
    # 过滤器配置,修改响应主体
    filters:
      - name: ModifyResponseBody
        args:
          inClass: '#{T(String)}'
          outClass: '#{T(String)}'
          rewriteFunction: '#{@handleEchoResponse}'
````

````java

@Slf4j
@Component
public class HandleEchoResponse implements RewriteFunction<String, String> {

    @Override
    public Publisher<String> apply(ServerWebExchange exchange, String body) {
        if (StrUtil.isNotBlank(body)) {
            log.debug("response body:{}", body);
        }
        return Mono.just(body);
    }
}
````

### PrefixPath GatewayFilter

**对路径进行添加前缀**

````yaml
routes:
  # ========================服务模块========================
  - id: spring_cloud_gateway_service
    uri: lb://spring-cloud-gateway-service
    # 路由断言,带指定查询参数的请求可以匹配该路由
    predicates:
      - Path=/service/**
    # 过滤器配置
    filters:
      # 对路径进行添加前缀
      - PrefixPath=/api
````

````json
{
  "name": "PrefixPath",
  "args": {
    "_genkey_0": "/api"
  }
}
````

## 通过网关提供web路径查看路由详细规则

````yaml
management:
  endpoints:
    web:
      exposure:
        include: "*" #开启所有web端点暴露
````

http://localhost:20000/actuator/gateway/routes

## 自定义网关全局filter

````java
package com.cmmplb.gateway.server.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author penglibo
 * @date 2024-08-14 10:16:56
 * @since jdk 1.8
 */

@Slf4j
@Component
public class GatewayGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 放行filter继续向后执行
        Mono<Void> filter = chain.filter(exchange);
        log.info("响应回来filter的处理......");
        return filter;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
````

