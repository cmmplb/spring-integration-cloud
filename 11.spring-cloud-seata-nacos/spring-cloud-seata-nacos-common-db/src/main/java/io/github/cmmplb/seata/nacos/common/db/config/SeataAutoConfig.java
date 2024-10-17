package io.github.cmmplb.seata.nacos.common.db.config;//package com.cmmplb.cmmondb.config;
//
//import com.alibaba.druid.pool.DruidDataSource;
//import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
//import io.seata.rm.datasource.DataSourceProxy;
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
//
//import javax.sql.DataSource;
//
//
//@Configuration
//public class SeataAutoConfig {
//
//    @Autowired
//    private DataSourceProperties dataSourceProperties;
//
//    private final static Logger logger = LoggerFactory.getLogger(SeataAutoConfig.class);
//
//    @Bean(name = "data") // 声明其为Bean实例
//    @Primary // 在同样的DataSource中，首先使用被标注的DataSource
//    public DataSource druidDataSource() {
//        DruidDataSource druidDataSource = new DruidDataSource();
//        logger.info("dataSourceProperties.getUrl():{}", dataSourceProperties.getUrl());
//        druidDataSource.setUrl(dataSourceProperties.getUrl());
//        druidDataSource.setUsername(dataSourceProperties.getUsername());
//        druidDataSource.setPassword(dataSourceProperties.getPassword());
//        druidDataSource.setDriverClassName(dataSourceProperties.getDriverClassName());
//        druidDataSource.setInitialSize(0);
//        druidDataSource.setMaxActive(180);
//        druidDataSource.setMaxWait(60000);
//        druidDataSource.setMinIdle(0);
//        druidDataSource.setValidationQuery("Select 1 from DUAL");
//        druidDataSource.setTestOnBorrow(false);
//        druidDataSource.setTestOnReturn(false);
//        druidDataSource.setTestWhileIdle(true);
//        druidDataSource.setTimeBetweenEvictionRunsMillis(60000);
//        druidDataSource.setMinEvictableIdleTimeMillis(25200000);
//        druidDataSource.setRemoveAbandoned(true);
//        druidDataSource.setRemoveAbandonedTimeout(1800);
//        druidDataSource.setLogAbandoned(true);
//        logger.info("装载dataSource........");
//        return druidDataSource;
//    }
//
//    /**
//     * init datasource proxy
//     * @Param: druidDataSource datasource bean instance
//     * @Return: DataSourceProxy datasource proxy
//     */
//    @Bean(name = "proxyData")
//    @Primary
//    public DataSourceProxy dataSourceProxy(@Qualifier("data") DataSource dataSource) {
//        logger.info("代理dataSource........");
//        return new DataSourceProxy(dataSource);
//    }
//
//    @Bean
//    public SqlSessionFactory sqlSessionFactory(@Qualifier("proxyData") DataSourceProxy dataSourceProxy) throws Exception {
//        MybatisSqlSessionFactoryBean factory = new MybatisSqlSessionFactoryBean();
//        factory.setDataSource(dataSourceProxy);
//        factory.setMapperLocations(new PathMatchingResourcePatternResolver()
//                .getResources("classpath*:mapper/*.xml"));
//        return factory.getObject();
//    }
//
//}