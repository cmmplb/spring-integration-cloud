package com.cmmplb.nacos.client;

import io.github.cmmplb.core.utils.SpringApplicationUtil;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author penglibo
 * @date 2021-05-13 15:40:56
 * @since jdk 1.8
 */

@SpringBootApplication
// @EnableDiscoveryClient // Dalston.SR4版本之前主函数上需要添加对应的注解；而Edgware.RELEASE之后的版本可以不添加
public class NacosClientApplication {

    public static void main(String[] args) {
        SpringApplicationUtil.run(NacosClientApplication.class, args);
    }

}