package com.cmmplb.security.oauth2.start.provider;

import com.cmmplb.security.oauth2.start.constants.Oauth2Constants;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;

import javax.sql.DataSource;

/**
 * @author penglibo
 * @date 2021-10-18 16:58:44
 * @since jdk 1.8
 */

public class RedisClientDetailsService extends JdbcClientDetailsService {

    public RedisClientDetailsService(DataSource dataSource) {
        super(dataSource);
        super.setSelectClientDetailsSql(Oauth2Constants.DEFAULT_SELECT_STATEMENT);
        super.setFindClientDetailsSql(Oauth2Constants.DEFAULT_FIND_STATEMENT);
    }

    @Override
    @Cacheable(value = Oauth2Constants.CLIENT_DETAILS_KEY, key = "#clientId", unless = "#result == null")
    public ClientDetails loadClientByClientId(String clientId) {
        return super.loadClientByClientId(clientId);
    }
}
