package com.cmmplb.kubernetes.service.provider.controller;

import com.cmmplb.core.result.Result;
import com.cmmplb.core.result.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author penglibo
 * @date 2022-06-10 09:29:23
 * @since jdk 1.8
 */

@Slf4j
@RestController
public class KubernetesProviderController {

    private final String hostName = System.getenv("HOSTNAME");

    /**
     * 探针检查响应类
     */
    @RequestMapping("/health")
    public Result<String> health() {
        return ResultUtil.success("OK");
    }

    @RequestMapping("/")
    public Result<String> ribbonPing() {
        log.info("ribbonPing of {}", hostName);
        return ResultUtil.success(hostName);
    }

    /**
     * @return 当前应用所在容器的hostname.
     */
    @RequestMapping("/name")
    public Result<String> getName() {
        return ResultUtil.success(this.hostName
                + ", "
                + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }
}
