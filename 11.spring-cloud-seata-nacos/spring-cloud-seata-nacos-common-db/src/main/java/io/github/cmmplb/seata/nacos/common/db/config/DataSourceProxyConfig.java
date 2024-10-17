package io.github.cmmplb.seata.nacos.common.db.config;//package com.cmmplb.cmmondb.config;
//
//import com.alibaba.druid.pool.DruidDataSource;
//import io.seata.rm.datasource.DataSourceProxy;
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.mybatis.spring.SqlSessionFactoryBean;
//import org.mybatis.spring.transaction.SpringManagedTransactionFactory;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
//
//import javax.sql.DataSource;
//
///**
// * @author penglibo
// * @date 2021-05-13 16:05:42
// * @since jdk 1.8
// */
//
//@Configuration
//public class DataSourceProxyConfig {
//
//    /**
//     * 普通数据源-通过yml的spring.datasource配置
//     * @return
//     */
//    @Bean
//    @ConfigurationProperties(prefix = "spring.datasource")
//    public DataSource dataSource() {
//        return new DruidDataSource();
//    }
//
//    /**
//     * 代理数据源-引用普通数据源
//     * @param dataSource
//     * @return
//     */
//    /*@Bean
//    public DataSourceProxy dataSourceProxy(DataSource dataSource) {
//        return new DataSourceProxy(dataSource);
//    }*/
//
//    /**
//     * MyBatis需要手动指定SqlSessionFactory绑定代理数据源
//     * @param dataSourceProxy
//     * @return
//     * @throws Exception
//     */
//    @Bean
//    public SqlSessionFactory sqlSessionFactoryBean(/*DataSourceProxy*/DataSource dataSourceProxy) throws Exception {
//        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
//        //使用代理数据源
//        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
//                .getResources("classpath*:mapper/*.xml"));
//        sqlSessionFactoryBean.setTransactionFactory(new SpringManagedTransactionFactory());
//        sqlSessionFactoryBean.setDataSource(dataSourceProxy);
//        return sqlSessionFactoryBean.getObject();
//    }
//}
