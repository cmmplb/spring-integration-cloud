package com.cmmplb.user.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.cmmplb.user.entity.User;

/**
 * @author penglibo
 * @date 2021-05-13 15:52:00
 * @since jdk 1.8
 */

public interface UserService extends IService<User> {

    /***
     * 账户金额递减
     * @param username
     * @param money
     */
    void decrMoney(String username, int money);
}
