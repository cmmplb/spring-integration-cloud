// package com.cmmplb.eureka.provider.config;
//
// import com.github.benmanes.caffeine.cache.Caffeine;
// import lombok.Getter;
// import org.springframework.cache.CacheManager;
// import org.springframework.cache.annotation.EnableCaching;
// import org.springframework.cache.caffeine.CaffeineCache;
// import org.springframework.cache.support.SimpleCacheManager;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.context.annotation.Primary;
//
// import java.util.ArrayList;
// import java.util.concurrent.TimeUnit;
//
// /**
//  * @author penglibo
//  * @date 2021-12-22 17:34:50
//  * @since jdk 1.8
//  * Cache配置類，用于缓存数据
//  */
//
// @Configuration
// @EnableCaching
// public class CacheConfig {
//
//     public static final int DEFAULT_MAXSIZE = 50000;
//     public static final int DEFAULT_TTL = 10;
//
//     /**
//      * 定義cache名稱、超時時長（秒）、最大容量
//      * 每个cache缺省：10秒超时、最多缓存50000条数据，需要修改可以在                构造方法的参数中指定。
//      */
//     @Getter
//     public enum Caches {
//
//         /**
//          * 有效期5秒
//          */
//         getPersonById(5),
//
//         /**
//          * 缺省10秒
//          */
//         getSomething,
//
//         /**
//          * 5分钟，最大容量1000
//          */
//         getOtherThing(300, 1000),
//         ;
//
//         /**
//          * 最大數量
//          */
//         private int maxSize = DEFAULT_MAXSIZE;
//
//         /**
//          * 过期时间（秒）
//          */
//         private int ttl = DEFAULT_TTL;
//
//         Caches() {
//         }
//
//         Caches(int ttl) {
//             this.ttl = ttl;
//         }
//
//         Caches(int ttl, int maxSize) {
//             this.ttl = ttl;
//             this.maxSize = maxSize;
//         }
//
//     }
//
//     /**
//      * 创建基于Caffeine的Cache Manager
//      * @return
//      */
//     @Bean
//     @Primary
//     public CacheManager caffeineCacheManager() {
//         SimpleCacheManager cacheManager = new SimpleCacheManager();
//
//         ArrayList<CaffeineCache> caches = new ArrayList<CaffeineCache>();
//         for (Caches c : Caches.values()) {
//             caches.add(new CaffeineCache(c.name(),
//                     Caffeine.newBuilder().recordStats()
//                             .expireAfterWrite(c.getTtl(), TimeUnit.SECONDS)
//                             .maximumSize(c.getMaxSize())
//                             .build())
//             );
//         }
//
//         cacheManager.setCaches(caches);
//
//         return cacheManager;
//     }
// }
