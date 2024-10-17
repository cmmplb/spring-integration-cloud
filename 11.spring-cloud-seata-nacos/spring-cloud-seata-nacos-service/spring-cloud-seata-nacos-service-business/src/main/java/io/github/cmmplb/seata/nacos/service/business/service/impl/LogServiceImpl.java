package io.github.cmmplb.seata.nacos.service.business.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.cmmplb.seata.nacos.service.business.dao.LogMapper;
import io.github.cmmplb.seata.nacos.api.business.entity.Log;
import io.github.cmmplb.seata.nacos.service.business.service.LogService;
import org.springframework.stereotype.Service;

/**
 * @author penglibo
 * @date 2021-05-14 09:52:11
 * @since jdk 1.8
 */

@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements LogService {


}
