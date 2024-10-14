package com.cmmplb.security.oauth2.starter.controller;

import io.github.cmmplb.core.result.Result;
import io.github.cmmplb.core.result.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author penglibo
 * @date 2021-11-10 11:39:19
 * @since jdk 1.8
 * 处理Security filter chain抛出的异常
 */

@Slf4j
public class SecurityExceptionController implements ErrorController {

    @RequestMapping(value = "/error")
    public Result<Object> error(HttpServletRequest request, HttpServletResponse response) {
        // Appropriate HTTP response code (e.g. 404 or 500) is automatically set by Spring.
        // Here we just define response body.
        log.info("security-error");
        Map<String, String[]> errorMap = request.getParameterMap();
        Map<String, Object> paramMap = new HashMap<>();
        // param参数
        errorMap.forEach((key, value) -> {
            if (value.length == 1) {
                paramMap.put(key, value[0]);
            } else {
                paramMap.put(key, Arrays.asList(value));
            }
        });
        // json参数-> 获取request的输入流来获取参数,只能被读取一次,所以后续Controller会传递不了，导致参数异常
        log.info("paramMap{}", paramMap);
        log.info("errorMap{}", errorMap);
        response.setStatus(HttpServletResponse.SC_OK);
        return ResultUtil.custom(Integer.parseInt(Arrays.toString(Objects.requireNonNull(errorMap.get("status")))), Arrays.toString(Objects.requireNonNull(errorMap.get("message"))));
    }
}
