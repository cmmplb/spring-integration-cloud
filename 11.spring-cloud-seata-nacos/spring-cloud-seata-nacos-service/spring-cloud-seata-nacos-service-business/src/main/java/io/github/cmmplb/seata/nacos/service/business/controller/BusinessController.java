package io.github.cmmplb.seata.nacos.service.business.controller;

import io.github.cmmplb.seata.nacos.service.business.service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author penglibo
 * @date 2021-05-13 15:53:15
 * @since jdk 1.8
 */
@RestController
@RequestMapping(value = "/business")
public class BusinessController {

    @Autowired
    private BusinessService businessService;

    /***
     * 购买商品分布式事务测试
     * @return
     */
    @RequestMapping(value = "/addorder")
    public String order() {
        String username = "zhangsan";
        int id = 1;
        int count = 5;
        //下单
        businessService.add(username, id, count);
        return "success";
    }
}