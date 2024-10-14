package com.cmmplb.apollo.client;

import io.github.cmmplb.core.utils.SpringApplicationUtil;
import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author penglibo
 * @date 2021-05-13 15:40:56
 * @since jdk 1.8
 */

@EnableApolloConfig // 默认读取的是application.properties 与yml读取冲突后读取了properties
@SpringBootApplication
public class ApolloClientApplication {

    public static void main(String[] args) {
        SpringApplicationUtil.run(ApolloClientApplication.class, args);
    }
}