package com.cmmplb.oauth2.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cmmplb.oauth2.system.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author penglibo
 * @date 2022-11-02 16:01:22
 * @since jdk 1.8
 */

@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据id获取关联的角色编码
     * @param id 用户id
     * @return 角色编码集合
     */
    List<String> selectRoleCodesById(@Param("id") Long id);

    /**
     * 根据角色编码集合获取关联的权限编码集合
     * @param roleCodesStr 角色编码集合字符串 admin,user
     * @return 权限编码集合
     */
    List<String> selectPermissionCodesByRoleCodes(@Param("roleCodesStr") String roleCodesStr);
}
