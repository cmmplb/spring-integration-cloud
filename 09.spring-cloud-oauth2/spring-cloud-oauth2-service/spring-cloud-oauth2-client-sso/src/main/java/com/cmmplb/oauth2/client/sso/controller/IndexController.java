package com.cmmplb.oauth2.client.sso.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author penglibo
 * @date 2024-07-29 09:54:02
 * @since jdk 1.8
 */

@Controller
public class IndexController {

    @Value("${server.client-port}")
    private Integer clientPort;

    @RequestMapping("/")
    public ModelAndView index() {
        // sso-two路径
        String ssoTwoUrl = "http://localhost:" + clientPort;
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        modelAndView.addObject("clientUrl", ssoTwoUrl);
        modelAndView.addObject("username", SecurityContextHolder.getContext().getAuthentication());
        return modelAndView;
    }

}
