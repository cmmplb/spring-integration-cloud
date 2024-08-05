package com.cmmplb.security.oauth2.starter.constants;

/**
 * @author penglibo
 * @date 2021-10-20 15:23:47
 * @since jdk 1.8
 */
public interface CacheConstants {

    /**
     * oauth令牌存储key
     */
    String OAUTH_TOKEN_STORE = "oauth:token:store:";
    
    /**
     * oauth 缓存前缀
     */
     String OAUTH_ACCESS = "oauth:access:";

    /**
     * oauth 客户端信息
     */
     String CLIENT_DETAILS_KEY = "oauth:client:details";

    /**
     * 登陆accessToken信息
     */
     String LOGIN_ACCESS_TOKEN = "login:access:";

    /**
     * 默认令牌存储key
     */
    String OAUTH_TOKEN_STORE_DEFAULT = "oauth:token:store:default:";

    /**
     * FAST_JSON令牌存储key
     */
    String OAUTH_TOKEN_STORE_FAST_JSON = "oauth:token:store:fastjson:";

    /**
     * JACKSON令牌存储key
     */
    String OAUTH_TOKEN_STORE_JACKSON = "oauth:token:store:jackson:";

    /**
     * 登陆请求头key
     */
     String TOKEN = "token";

    /**
     * app登陆token信息
     * key: 前缀+ token
     * value: 用户id
     */
    final public static String APP_AUTH_TOKEN_UID = "app:auth:token:uid:";

    /**
     * key: 用户id
     * value: 用户信息
     */
    final public static String APP_AUTH_UID_UINFO = "app:auth:uid:uinfo:";

    /**
     * key: 用户id
     * value: token列表（多端）
     */
    final public static String APP_AUTH_UID_TOKENS = "app:auth:uid:tokens:";

    /**
     * 移动端登录token过期时间：30天/秒
     */
     int APP_TOKEN_EXPIRE_SECONDS = 60 * 60 * 24 * 30;
    /**
     * 登陆验证码前缀
     */
    String LOGIN_CODE_CACHE_PREFIX = "login_code:";

    /**
     * 登陆验证码有效期,默认 60秒
     */
    long LOGIN_CODE_TIME = 60;

    /**
     * 验证码登录code前缀
     */
    String SMS_CODE_PREFIX = "sms:code:";
}
