package com.cmmplb.security.oauth2.starter.constants;

/**
 * @author penglibo
 * @date 2021-09-06 17:41:32
 * @since jdk 1.8
 */

public interface Oauth2Constants {
    String INTERNAL = "INTERNAL";
    String IN = "IN";
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
    String AUTH_TOKEN = "/oauth/token";
    String REFRESH_TOKEN = "refresh_token";
    String MOBILE = "mobile";
    String TOKEN_LOGOUT = "/token/logout";
    String DETAILS_USERNAME = "username";
    String DETAILS_NAME = "name";
    String DETAILS_SEX = "sex";
    String DETAILS_MOBILE = "mobile";
    String DETAILS_EMAIL = "email";
    String DETAILS_EMP_NO = "emp_no";
    String DETAILS_TYPE = "type";
    String USERNAME_EMPTY = "账号不能为空";


    String DETAILS_USER_ID = "user_id";
    String DETAILS_DEPT_ID = "dept_id";
    String DETAILS_TENANT_ID = "tenant_id";

    String AUTHORIZATION_CODE_CACHE_PREFIX = "authorization:code:";// 授权码CODE缓存前缀

    /**
     * sys_oauth_client_details 表的字段
     */
    String CLIENT_FIELDS = "client_id, client_secret, resource_ids, scope, "
            + "authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, "
            + "refresh_token_validity, additional_information, auto_approve, create_time, update_time";

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
    String DEFAULT_FIND_STATEMENT = BASE_FIND_STATEMENT + " order by id";


}
