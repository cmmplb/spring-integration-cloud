package io.github.cmmplb.seata.nacos.common.db.asect;//package com.cmmplb.cmmondb.asect;
//
//import io.seata.core.context.RootContext;
//import io.seata.core.exception.TransactionException;
//import io.seata.tm.api.GlobalTransaction;
//import io.seata.tm.api.GlobalTransactionContext;
//import org.apache.commons.lang.StringUtils;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.AfterThrowing;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//import java.lang.reflect.Method;
//
///**
// * @author penglibo
// * @date 2021-05-16 21:03:02
// * @since jdk 1.8
// */
//
//@Aspect
//@Component
//public class WorkAspect {
//
//    // 使用AOP手动开启全局事务并进行回滚
//
//    private final static Logger log = LoggerFactory.getLogger(WorkAspect.class);
//
//    @Before("execution(* com.cmmplb.*.service.*.*(..))")
//    public void before(JoinPoint joinPoint) throws TransactionException {
//        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
//        Method method = signature.getMethod();
//        GlobalTransaction tx = GlobalTransactionContext.getCurrentOrCreate();
//        tx.begin(300000, "tran");
//        log.info("**********创建分布式事务完毕 {}" , tx.getXid());
//    }
//
//    @AfterThrowing(throwing = "e", pointcut = "execution(* com.cmmplb.*.service.*.*(..))")
//    public void doRecoveryActions(Throwable e) throws TransactionException {
//        log.info("方法执行异常:{}", e.getMessage());
//        if (!StringUtils.isBlank(RootContext.getXID())) {
//            GlobalTransactionContext.reload(RootContext.getXID()).rollback();
//        }
//    }
//
//
//}
