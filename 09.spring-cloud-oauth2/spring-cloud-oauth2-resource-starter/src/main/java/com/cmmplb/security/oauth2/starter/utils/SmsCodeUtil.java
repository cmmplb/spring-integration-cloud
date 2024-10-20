package com.cmmplb.security.oauth2.starter.utils;

import io.github.cmmplb.core.utils.RandomUtil;
import io.github.cmmplb.core.utils.SpringUtil;
import com.cmmplb.security.oauth2.starter.constants.CacheConstant;
import io.github.cmmplb.redis.service.RedisService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author penglibo
 * @date 2021-09-26 14:42:23
 * @since jdk 1.8
 * 短信验证码工具类
 */

@Slf4j
public class SmsCodeUtil {

    /**
     * 创建手机验证码
     * @param mobile 手机号
     */
    public static String create(String mobile) {
        //生成验证码
        String code = RandomUtil.getRandomNumString(4);
        // 这里写死一个1234难得每次改勒。
        code = "1234";

        log.info("短信验证码：" + code);

        //保存到缓存
        setCache(mobile, code);
        return code;
    }

    /**
     * 校验验证码
     * @param mobile 手机号
     * @param code   验证码
     * @return boolean
     */
    public static boolean validate(String mobile, String code) {
        return code.equalsIgnoreCase(getCache(mobile));
    }

    private static void setCache(String mobile, String value) {
        getRedisService().set(getCaptchaKey(mobile), value, 60 * 5); // 5分钟
    }

    /**
     * 获取验证码的缓存Key
     */
    public static String getCaptchaKey(String mobile) {
        return CacheConstant.SMS_CODE_PREFIX + mobile;
    }

    /**
     * 获取验证码
     * @param mobile mobile
     * @return code
     */
    private static String getCache(String mobile) {
        return (String) getRedisService().get(getCaptchaKey(mobile));
    }

    /**
     * 删除验证码
     * @param mobile mobile
     */
    public static void delete(String mobile) {
        getRedisService().del(getCaptchaKey(mobile));
    }

    /**
     * 从容器获取redisService
     * @return RedisService
     */
    public static RedisService getRedisService() {
        return SpringUtil.getBean(RedisService.class);
    }
}
