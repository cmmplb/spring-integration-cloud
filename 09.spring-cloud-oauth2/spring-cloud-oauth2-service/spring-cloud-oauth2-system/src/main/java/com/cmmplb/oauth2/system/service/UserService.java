package com.cmmplb.oauth2.system.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.cmmplb.oauth2.system.entity.User;
import com.cmmplb.security.oauth2.starter.provider.converter.UserInfoVO;

/**
 * @author penglibo
 * @date 2021-11-05 11:29:36
 * @since jdk 1.8
 */

public interface UserService extends IService<User> {

    /**
     * 根据用户名获取用户信息
     * @param username 用户名
     * @return 用户信息
     */
    UserInfoVO getByUsername(String username);

    /**
     * 根据手机号获取用户信息
     * @param mobile 手机号
     * @return 用户信息
     */
    UserInfoVO getByMobile(String mobile);
}

