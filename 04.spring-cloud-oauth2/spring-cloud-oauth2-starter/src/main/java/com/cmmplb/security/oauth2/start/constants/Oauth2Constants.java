package com.cmmplb.security.oauth2.start.constants;

/**
 * @author penglibo
 * @date 2021-09-06 17:41:32
 * @since jdk 1.8
 */

public interface Oauth2Constants {

    String AUTHORIZATION = "Authorization";
    String BEARER = "Bearer";
    String BASIC = "Basic ";
    String ACCESS_TOKEN = "access_token";
    String OAUTH_TOKEN = "/oauth/token";
    String OAUTH_LOGIN = "/oauth/login";
    String BAD_CREDENTIALS = "用户名或密码错误";
    String ACCOUNT_NON_EXPIRED = "帐号已过期";
    String ACCOUNT_NON_LOCKED = "帐号已锁定";
    String CODE_ERROR = "短信验证码错误";


    String AUTHORIZATION_CODE_CACHE_PREFIX = "authorization:code:";// 授权码CODE缓存前缀

    /**
     * sys_oauth_client_details 表的字段，不包括client_id、client_secret
     */
    String CLIENT_FIELDS = "client_id, client_secret, resource_ids, scope, "
            + "authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, "
            + "refresh_token_validity, additional_information, autoapprove";

    /**
     * JdbcClientDetailsService 查询语句
     */
    String BASE_FIND_STATEMENT = "select " + CLIENT_FIELDS + " from sys_oauth_client_details";

    /**
     * 按条件client_id 查询
     */
    String DEFAULT_SELECT_STATEMENT = BASE_FIND_STATEMENT + " where client_id = ?";

    /**
     * 默认的查询语句
     */
    String DEFAULT_FIND_STATEMENT = BASE_FIND_STATEMENT + " order by client_id";

    /**
     * oauth 客户端信息
     */
    String CLIENT_DETAILS_KEY = "oauth2:client:details";

    /**
     * 验证码登录code前缀
     */
    String SMS_CODE_PREFIX = "sms:code:";

}
