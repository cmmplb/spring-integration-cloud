package com.cmmplb.security.oauth2.auth.service.impl;

import com.cmmplb.security.oauth2.auth.service.UserDetailsServiceImpl;
import com.cmmplb.security.oauth2.start.entity.AuthUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserDetailServiceImpl implements UserDetailsServiceImpl {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("密码模式查询用户信息");
        // 模拟一个用户，替代数据库获取逻辑
        AuthUser user = new AuthUser();
        user.setId(1L);
        user.setUsername(username);
        // Spring security 5.0中新增了多种加密方式，使得当进行验证时Spring Security将传输的数据看作是进行了加密后的数据，
        // 在匹配之后发现找不到正确识别序列，就认为id是null，因此要将前端传过来的密码进行某种方式加密。
        user.setPassword(this.passwordEncoder.encode("123456"));
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (StringUtils.equalsIgnoreCase("admin", username)) {
            authorities = AuthorityUtils.commaSeparatedStringToAuthorityList("admin");
        } else {
            authorities = AuthorityUtils.commaSeparatedStringToAuthorityList("test");
        }
        user.setAuthorities(authorities);
        return user;
    }

    @Override
    public UserDetails loadUserByMobile(String mobile) {
        log.debug("手机号模式查询用户信息");
        // 模拟一个用户，替代数据库获取逻辑
        AuthUser user = new AuthUser();
        user.setId(1L);
        user.setUsername(mobile);
        // Spring security 5.0中新增了多种加密方式，使得当进行验证时Spring Security将传输的数据看作是进行了加密后的数据，
        // 在匹配之后发现找不到正确识别序列，就认为id是null，因此要将前端传过来的密码进行某种方式加密。
        user.setPassword(this.passwordEncoder.encode("123456"));
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (StringUtils.equalsIgnoreCase("admin", mobile)) {
            authorities = AuthorityUtils.commaSeparatedStringToAuthorityList("admin");
        } else {
            authorities = AuthorityUtils.commaSeparatedStringToAuthorityList("test");
        }
        user.setAuthorities(authorities);
        return user;
    }
}
