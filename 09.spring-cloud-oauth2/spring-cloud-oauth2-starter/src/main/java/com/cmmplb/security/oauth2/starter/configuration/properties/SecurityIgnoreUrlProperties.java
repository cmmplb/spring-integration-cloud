package com.cmmplb.security.oauth2.starter.configuration.properties;

import cn.hutool.core.util.ReUtil;
import com.cmmplb.core.constants.StringConstants;
import com.cmmplb.core.utils.StringUtil;
import com.cmmplb.security.oauth2.starter.annotation.AuthIgnore;
import com.cmmplb.security.oauth2.starter.annotation.WithoutLogin;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.NonNull;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author penglibo
 * @date 2021-11-10 10:39:02
 * @since jdk 1.8
 * 微服务之间调用部分Feign接口忽略认证授权
 * https://blog.csdn.net/weixin_42214548/article/details/112936957
 */

@Slf4j
@ConfigurationProperties(prefix = "security.ignore")
public class SecurityIgnoreUrlProperties implements InitializingBean, ApplicationContextAware {

    private static final String ASTERISK = "*";

    private ApplicationContext applicationContext;

    public static final String AUTH_IGNORE = "auth_ignore";

    private static final Pattern PATTERN = Pattern.compile("\\{(.*?)}");

    /**
     * 白名单
     */
    @Getter
    @Setter
    private Map<String, String> whiteList = new HashMap<>();

    @Override
    public void afterPropertiesSet() {
        // 获取所有被注解的类或者方法
        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();
        map.keySet().forEach(mappingInfo -> {
            HandlerMethod handlerMethod = map.get(mappingInfo);
            build(mappingInfo, AnnotationUtils.findAnnotation(handlerMethod.getMethod(), AuthIgnore.class));
            build(mappingInfo, AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), AuthIgnore.class));
            build(mappingInfo, AnnotationUtils.findAnnotation(handlerMethod.getMethod(), WithoutLogin.class));
            build(mappingInfo, AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), WithoutLogin.class));
        });
        log.info("服务忽略的认证的路径：{}", whiteList);
    }

    private void build(RequestMappingInfo mappingInfo, Object ignore) {
        if (null != ignore && null != mappingInfo.getPatternsCondition()) {
            // path variable 为 *
            mappingInfo.getPatternsCondition().getPatterns().forEach(url -> {
                String white = whiteList.get(AUTH_IGNORE);
                if (StringUtil.isEmpty(white)) {
                    white = StringConstants.SLASH;
                }
                white = white + "," + ReUtil.replaceAll(url, PATTERN, StringConstants.ASTERISK);
                whiteList.put(AUTH_IGNORE, white);
            });
        }
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext context) {
        this.applicationContext = context;
    }
}
