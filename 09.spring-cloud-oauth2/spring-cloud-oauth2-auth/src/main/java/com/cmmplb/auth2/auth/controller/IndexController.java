package com.cmmplb.auth2.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author penglibo
 * @date 2021-09-07 15:50:45
 * @since jdk 1.8
 */

@Controller
public class IndexController {

    @RequestMapping("/")
    public String index() {
        return "index";
    }
}
