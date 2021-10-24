package com.cmmplb.business.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cmmplb.business.dao.LogMapper;
import com.cmmplb.business.entity.Log;
import com.cmmplb.business.service.LogService;
import org.springframework.stereotype.Service;

/**
 * @author penglibo
 * @date 2021-05-14 09:52:11
 * @since jdk 1.8
 */

@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements LogService {


}
