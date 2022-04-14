package com.cmmplb.security.oauth2.auth.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author penglibo
 * @date 2021-09-08 11:46:41
 * @since jdk 1.8
 */
public interface UserDetailsServiceImpl extends UserDetailsService {

    UserDetails loadUserByMobile(String mobile);
}
