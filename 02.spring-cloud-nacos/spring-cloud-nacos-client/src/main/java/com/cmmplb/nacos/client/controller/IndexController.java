package com.cmmplb.nacos.client.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author penglibo
 * @date 2021-09-26 09:52:54
 * @since jdk 1.8
 */

@RestController
public class IndexController {

    @RequestMapping("/")
    public String index() {
        return "hello spring boot ";
    }
}
