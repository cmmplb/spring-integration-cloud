package com.cmmplb.security.oauth2.starter.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.cmmplb.core.constants.StringConstants;
import com.cmmplb.core.result.Result;
import com.cmmplb.core.utils.StringUtil;
import com.cmmplb.security.oauth2.starter.constants.Oauth2Constants;
import com.cmmplb.security.oauth2.starter.provider.converter.SimpleGrantedAuthority;
import com.cmmplb.security.oauth2.starter.provider.converter.User;
import com.cmmplb.security.oauth2.starter.provider.converter.UserInfoVO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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

    public static final String HTTP_CESTBON_SMS_MODULES_SYSTEM_USER_INFO = StringConstants.HTTP /*+ ServiceNameConstants.SYSTEM_SERVICE*/ + "/user/info/";

    // private final PasswordEncoder passwordEncoder;

    // private final RestTemplate restTemplate;

    /*@Override
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
    }*/

    /*public UserDetails loadUserByMobile(String mobile) {
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
    }*/

    @Override
    public UserDetails loadUserByUsername(String username) {
        /*
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add(Oauth2Constants.INTERNAL, Oauth2Constants.IN);
        if (headers.getContentType() == null) {
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        }
        JSONObject res = restTemplate.exchange(HTTP_CESTBON_SMS_MODULES_SYSTEM_USER_INFO + username,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                JSONObject.class).getBody();
        if (null == res) {
            log.error("服务调用失败");
            throw new CustomException(Oauth2Constants.BAD_CREDENTIALS);
        }
        Result<UserInfoVO> result = res.toJavaObject(new TypeReference<Result<UserInfoVO>>() {
        });
        if (result == null || result.getCode() != 200 || result.getData() == null) {
            throw new CustomException(Oauth2Constants.BAD_CREDENTIALS);
        }
        Result<UserInfoVO> result = res.toJavaObject(new TypeReference<Result<UserInfoVO>>() {
        });
        */
        HttpResponse httpResponse = HttpRequest.get("http://localhost:18083/user/info/" + username).header(Oauth2Constants.INTERNAL, Oauth2Constants.IN).execute();
        Result<UserInfoVO> result = JSON.parseObject(httpResponse.body(), new TypeReference<Result<UserInfoVO>>() {
        });
        return getUserDetails(result);
    }

    public UserDetails loadUserByMobile(String mobile) {
        HttpResponse httpResponse = HttpRequest.get("http://localhost:18083/user/info/mobile/" + mobile).header(Oauth2Constants.INTERNAL, Oauth2Constants.IN).execute();
        Result<UserInfoVO> result = JSON.parseObject(httpResponse.body(), new TypeReference<Result<UserInfoVO>>() {
        });
        return getUserDetails(result);
    }

    private UserDetails getUserDetails(Result<UserInfoVO> result) {
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

    /*private JSONObject forMap(String path, HttpHeaders headers) {
        if (headers.getContentType() == null) {
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        }
        return restTemplate.exchange(path, HttpMethod.GET, new HttpEntity<>(headers), JSONObject.class).getBody();
    }*/
}
