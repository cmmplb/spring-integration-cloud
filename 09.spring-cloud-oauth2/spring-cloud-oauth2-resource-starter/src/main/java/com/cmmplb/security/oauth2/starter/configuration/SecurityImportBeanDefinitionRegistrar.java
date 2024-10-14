package com.cmmplb.security.oauth2.starter.configuration;

import io.github.cmmplb.core.utils.StringUtil;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.NonNull;

/**
 * @author penglibo
 * @date 2021-11-04 17:28:45
 * @since jdk 1.8
 */
public class SecurityImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(@NonNull AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        String beanName = StringUtil.uncapitalize(ResourceServerConfiguration.class.getSimpleName());
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(ResourceServerConfiguration.class);
        registry.registerBeanDefinition(beanName, builder.getBeanDefinition());
    }
}
