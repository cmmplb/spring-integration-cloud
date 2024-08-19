package com.cmmplb.gateway.service.controller;

import com.cmmplb.core.result.Result;
import com.cmmplb.core.result.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
