package io.github.cmmplb.gateway.service.controller;

import io.github.cmmplb.core.result.Result;
import io.github.cmmplb.core.result.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author penglibo
 * @date 2024-08-13 16:34:15
 * @since jdk 1.8
 */

@Slf4j
@RestController
public class IndexController {

    @RequestMapping("/info")
    public Result<Map<String, Object>> index(@RequestBody Map<String, Object> params) {
        log.info("params:{}", params);
        params.put("info", "Hello World!");
        return ResultUtil.success(params);
    }

    @RequestMapping("/info/params")
    public Result<Map<String, Object>> params(@RequestBody Map<String, Object> params) {
        log.info("params:{}", params);
        params = new HashMap<>();
        params.put("info", "Hello World!");
        return ResultUtil.success(params);
    }

    @RequestMapping("/info/string")
    public String string(@RequestBody Map<String, Object> params) {
        log.info("params:{}", params);
        return "Hello World!";
    }

    @RequestMapping("/test")
    public String test() {
        return "Hello World!";
    }
}
