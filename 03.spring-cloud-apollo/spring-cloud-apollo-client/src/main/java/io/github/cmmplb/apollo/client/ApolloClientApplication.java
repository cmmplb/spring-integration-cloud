package io.github.cmmplb.apollo.client;

import io.github.cmmplb.core.utils.SpringApplicationUtil;
import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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