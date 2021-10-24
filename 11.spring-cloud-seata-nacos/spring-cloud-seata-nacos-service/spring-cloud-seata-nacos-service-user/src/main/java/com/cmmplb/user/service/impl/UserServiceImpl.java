package com.cmmplb.user.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cmmplb.user.dao.UserMapper;
import com.cmmplb.user.entity.User;
import com.cmmplb.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author penglibo
 * @date 2021-05-13 15:52:27
 * @since jdk 1.8
 */

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    /***
     * 账户金额递减
     * @param username
     * @param money
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void decrMoney(String username, int money) {
        User user = baseMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getAccount, username));
        user.setMoney(user.getMoney() - money);
        int count = baseMapper.updateById(user);
        System.out.println("添加用户受影响行数：" + count);
        int q = 10 / 0;
    }
}
