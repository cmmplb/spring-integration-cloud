package com.cmmplb.business.service;

/**
 * @author penglibo
 * @date 2021-05-13 15:52:00
 * @since jdk 1.8
 */

public interface BusinessService {

    /**
     * 下单
     * @param username
     * @param id
     * @param count
     */
    void add(String username, int id, int count);
}
