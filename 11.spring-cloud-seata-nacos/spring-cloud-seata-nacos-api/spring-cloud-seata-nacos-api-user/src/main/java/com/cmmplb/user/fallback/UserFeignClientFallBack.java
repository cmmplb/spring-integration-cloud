package com.cmmplb.user.fallback;


import com.cmmplb.core.exception.CustomException;
import com.cmmplb.core.result.Result;
import com.cmmplb.core.result.ResultUtil;
import com.cmmplb.user.client.UserFeignClient;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author penglibo
 * @date 2021-05-07 14:15:24
 * @since jdk 1.8
 */

@Component
public class UserFeignClientFallBack implements FallbackFactory<UserFeignClient> {

    /*@Autowired
    private WorkAspect workAspect;*/

    @Override
    public UserFeignClient create(Throwable throwable) {

        /**
         * 手动进行事务回滚
         */
        /*try {
            this.workAspect.doRecoveryActions(throwable);
        } catch (TransactionException e) {
            e.printStackTrace();
        }*/
        return new UserFeignClient() {

            CustomException c = (CustomException) throwable;

            @Override
            public Result<String> decrMoney(String username, int money) {
                return ResultUtil.custom(c.getCode(), c.getMessage());
            }
        };
    }
}
