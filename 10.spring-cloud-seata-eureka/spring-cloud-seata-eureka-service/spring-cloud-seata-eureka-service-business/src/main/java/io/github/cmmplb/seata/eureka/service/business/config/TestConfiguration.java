package io.github.cmmplb.seata.eureka.service.business.config;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfiguration {

    @Bean
    public IRule ribbonRule() {
        return new RandomRule();
    }    
}