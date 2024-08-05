package com.cmmplb.security.oauth2.starter.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.cmmplb.core.result.HttpCodeEnum;
import com.cmmplb.core.result.Result;
import com.cmmplb.core.utils.StringUtil;
import com.cmmplb.security.oauth2.starter.constants.Oauth2Constants;
import com.cmmplb.security.oauth2.starter.converter.SimpleGrantedAuthority;
import com.cmmplb.security.oauth2.starter.converter.User;
import com.cmmplb.security.oauth2.starter.converter.UserInfoVO;
import com.cmmplb.security.oauth2.starter.handler.exceptions.MobileNotFoundException;
import com.cmmplb.security.oauth2.starter.service.UserDetailsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @author penglibo
 * @date 2021-10-20 15:23:47
 * @since jdk 1.8
 */

@Slf4j
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) {
        // 直接用http调用
        HttpResponse httpResponse = HttpRequest.get("http://localhost:40000/user/info/" + username).header(Oauth2Constants.SOURCE, Oauth2Constants.INNER).execute();
        Result<UserInfoVO> result = JSON.parseObject(httpResponse.body(), new TypeReference<Result<UserInfoVO>>() {
        });
        return getUserDetails(result);
    }

    @Override
    public UserDetails loadUserByMobile(String mobile) {
        HttpResponse httpResponse = HttpRequest.get("http://localhost:40000/user/info/mobile/" + mobile).header(Oauth2Constants.SOURCE, Oauth2Constants.INNER).execute();
        Result<UserInfoVO> result = JSON.parseObject(httpResponse.body(), new TypeReference<Result<UserInfoVO>>() {
        });
        return getUserDetails(result);
    }

    private UserDetails getUserDetails(Result<UserInfoVO> result) {
        if (result == null || result.getData() == null) {
            throw new MobileNotFoundException(HttpCodeEnum.MOBILE_NOT_FOUND.getMessage());
        }
        UserInfoVO info = result.getData();
        Set<String> dbAuthsSet = new HashSet<String>();
        if (StringUtil.isNotEmpty(info.getRoles())) {
            // 获取角色
            dbAuthsSet.addAll(info.getRoles());
            // 获取权限
            dbAuthsSet.addAll(info.getPermissions());
        }
        Collection<? extends GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(String.valueOf(dbAuthsSet));
        // 转换成自定义的SimpleGrantedAuthority，框架里面的SimpleGrantedAuthority没有无参构造，自定义json反序列化会失败
        Set<SimpleGrantedAuthority> authorityArrayList = new HashSet<>();
        if (!CollectionUtils.isEmpty(authorities)) {
            authorities.forEach(auth -> authorityArrayList.add(new SimpleGrantedAuthority(auth.getAuthority())));
        }
        UserInfoVO.UserVO user = info.getUser();
        return new User(user.getId(), user.getUsername(), user.getPassword(), true,
                true, true, authorityArrayList);
    }
}
