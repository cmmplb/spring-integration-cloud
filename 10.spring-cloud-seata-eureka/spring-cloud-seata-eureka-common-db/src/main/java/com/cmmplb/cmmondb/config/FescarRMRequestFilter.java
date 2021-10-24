package com.cmmplb.cmmondb.config;

import io.seata.core.context.RootContext;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author penglibo
 * @date 2021-05-13 16:09:30
 * @since jdk 1.8
 */

public class FescarRMRequestFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(FescarRMRequestFilter.class);

    /**
     * 给每次线程请求绑定一个XID
     * @param request
     * @param response
     * @param filterChain
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String currentXID = request.getHeader(FescarAutoConfiguration.FESCAR_XID);
        if(!StringUtils.isEmpty(currentXID)){
            RootContext.bind(currentXID);
            LOGGER.info("当前线程绑定的XID :" + currentXID);
        }
        try{
            filterChain.doFilter(request, response);
        } finally {
            String unbindXID = RootContext.unbind();
            if(unbindXID != null){
                LOGGER.info("当前线程从指定XID中解绑 XID :" + unbindXID);
                if(!currentXID.equals(unbindXID)){
                    LOGGER.info("当前线程的XID发生变更");
                }
            }
            if(currentXID != null){
                LOGGER.info("当前线程的XID发生变更");
            }
        }
    }
}
