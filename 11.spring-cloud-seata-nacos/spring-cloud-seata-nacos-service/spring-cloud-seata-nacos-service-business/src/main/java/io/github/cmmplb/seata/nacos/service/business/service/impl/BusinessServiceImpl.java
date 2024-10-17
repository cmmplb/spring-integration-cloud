package io.github.cmmplb.seata.nacos.service.business.service.impl;


import io.github.cmmplb.seata.nacos.api.business.entity.Log;
import io.github.cmmplb.seata.nacos.service.business.service.BusinessService;
import io.github.cmmplb.seata.nacos.service.business.service.LogService;
import io.github.cmmplb.core.result.Result;
import io.github.cmmplb.seata.nacos.api.user.client.UserFeignClient;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author penglibo
 * @date 2021-05-13 15:52:27
 * @since jdk 1.8
 */

@Service
public class BusinessServiceImpl implements BusinessService {

    @Autowired
    private UserFeignClient userFeignClient;

    @Autowired
    private LogService logService;

    /***
     * 下单
     * @param username
     * @param id
     * @param count
     */
    @GlobalTransactional(name = "seata_nacos", rollbackFor = Exception.class)  //调用Service入口开启分布式事务
    @Override
    public void add(String username, int id, int count) {
        //添加订单日志
        Log logInfo = new Log();
        logInfo.setContent("添加订单数据---" + new Date());
        logInfo.setCreateTime(new Date());
        boolean save = logService.save(logInfo);
        System.out.println("是否添加成功：" + save);

        //用户账户余额递减
        Result<String> result = userFeignClient.decrMoney(username, 10);
        System.out.println("结果:----" + result);
    }
}
