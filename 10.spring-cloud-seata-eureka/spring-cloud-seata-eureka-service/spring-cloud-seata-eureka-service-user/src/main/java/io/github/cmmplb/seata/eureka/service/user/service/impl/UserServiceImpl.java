package io.github.cmmplb.seata.eureka.service.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.cmmplb.seata.eureka.service.user.dao.UserMapper;
import io.github.cmmplb.seata.eureka.service.user.entity.User;
import io.github.cmmplb.seata.eureka.service.user.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author penglibo
 * @date 2021-11-05 11:29:36
 * @since jdk 1.8
 */

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}

