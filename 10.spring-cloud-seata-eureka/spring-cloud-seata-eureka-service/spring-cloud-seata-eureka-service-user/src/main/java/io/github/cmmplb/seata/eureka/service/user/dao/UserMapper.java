package io.github.cmmplb.seata.eureka.service.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.cmmplb.seata.eureka.service.user.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author penglibo
 * @date 2021-11-05 15:52:45
 * @since jdk 1.8
 */

@Mapper
public interface UserMapper extends BaseMapper<User> {
}