/*
SQLyog Ultimate v12.5.0 (64 bit)
MySQL - 5.7.29 : Database - nacos
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`nacos` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `nacos`;

/*Table structure for table `config_info` */

CREATE TABLE `config_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `content` longtext COLLATE utf8_bin NOT NULL COMMENT 'content',
  `md5` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '修改时间',
  `src_user` text COLLATE utf8_bin COMMENT 'source user',
  `src_ip` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT 'source ip',
  `app_name` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `tenant_id` varchar(128) COLLATE utf8_bin DEFAULT '' COMMENT '租户字段',
  `c_desc` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  `c_use` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `effect` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `type` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `c_schema` text COLLATE utf8_bin,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfo_datagrouptenant` (`data_id`,`group_id`,`tenant_id`)
) ENGINE=InnoDB AUTO_INCREMENT=91 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_info';

/*Data for the table `config_info` */

insert  into `config_info`(`id`,`data_id`,`group_id`,`content`,`md5`,`gmt_create`,`gmt_modified`,`src_user`,`src_ip`,`app_name`,`tenant_id`,`c_desc`,`c_use`,`effect`,`type`,`c_schema`) values 
(1,'business.yaml','SERVICE_DEV_GROUP','server:\n  port: 10001\n  servlet:\n    context-path: /api/business\nspring:\n  application:\n    name: seata-service-business\n  datasource:\n    type: com.alibaba.druid.pool.DruidDataSource\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    url: jdbc:mysql://127.0.0.1:3306/seata_nacos?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC&useSSL=false\n    username: root\n    password: cmmplb\n    druid:\n      initial-size: 20\n      max-active: 20\n      min-idle: 2\n      max-wait: 60000 #空闲等待超时\n      time-between-eviction-runs-millis: 60000 # 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒\n      min-evictable-idle-time-millis: 300000  # 配置一个连接在池中最小生存的时间，单位是毫秒\n  main:\n    allow-bean-definition-overriding: true #后发现的bean会覆盖之前相同名称的bean\n\n  #seata 配置分布式事务组-seata 服务分组，要与服务端nacos-config.txt中service.vgroup_mapping的后缀对应-在1.4的seata版本中建议设置在seata.tx-service-group\n  cloud:\n    alibaba:\n      seata:\n        tx-service-group: seata_service_business_tx_group\n\nmybatis-plus:\n  mapper-locations: classpath:mapper/*.xml\n  global-config:\n    db-config:\n      id-type: auto\n      table-underline: true\n  configuration:\n    map-underscore-to-camel-case: true #是否开启驼峰命名规则\n    call-setters-on-nulls: true\n    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl\n\nfeign:\n  hystrix:\n    enabled: true\n\nseata:\n  # 1.0新添加的enabled激活自动配置，使得我们可以在yaml/properties文件中配置，\n  # 避免了以前需要客户端引入2个文件：\n  # file.conf.bak 和 registry.conf.bak\n  # 1.0新特性，需要依赖seata-spring-boot-starter,默认为true\n  enabled: true\n  application-id: ${spring.application.name} # seata-service-business\n  tx-service-group: seata_service_business_tx_group    #此处配置自定义的seata事务分组名称 ${spring.application.name}-tx-group\n  enable-auto-data-source-proxy: true    #开启数据库代理\n  ##registry\n  registry:\n    type: nacos\n    nacos:\n      application: seata-server\n      server-addr: ${spring.cloud.nacos.discovery.server-addr} # 127.0.0.1:8848\n      group: SERVICE_SEATA_GROUP\n      namespace: service_seata\n      username: nacos\n      password: nacos\n  ##config\n  config:\n    type: nacos\n    nacos:\n      server-addr: ${spring.cloud.nacos.discovery.server-addr} # 127.0.0.1:8848\n      namespace: service_seata\n      group: SERVICE_SEATA_GROUP\n      username: nacos\n      password: nacos\n  #service  -- 对应file里面的serivce\n  service:\n    vgroup-mapping:\n      seata_service_business_tx_group: default\n    grouplist:\n      business: 127.0.0.1:8091\n    disable-global-transaction: false\n  client:\n    rm:\n      report-success-enable: false\n\n\n\n\n\n','0e0232809de4733b88771db2de56f7ed','2021-05-19 01:27:43','2021-05-19 01:34:05',NULL,'0:0:0:0:0:0:0:1','','service_dev','','','','yaml',''),
(2,'user.yaml','SERVICE_DEV_GROUP','server:\n  port: 10002\n  servlet:\n    context-path: /api/user\nspring:\n  application:\n    name: seata-service-user\n  datasource:\n    type: com.alibaba.druid.pool.DruidDataSource\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    url: jdbc:mysql://127.0.0.1:3306/seata_nacos?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC&useSSL=false\n    username: root\n    password: cmmplb\n    druid:\n      initial-size: 20\n      max-active: 20\n      min-idle: 2\n      max-wait: 60000 #空闲等待超时\n      time-between-eviction-runs-millis: 60000 # 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒\n      min-evictable-idle-time-millis: 300000  # 配置一个连接在池中最小生存的时间，单位是毫秒\n  main:\n    allow-bean-definition-overriding: true #后发现的bean会覆盖之前相同名称的bean\n\n  #seata 配置分布式事务组-seata 服务分组，要与服务端nacos-config.txt中service.vgroup_mapping的后缀对应-在1.4的seata版本中建议设置在seata.tx-service-group\n  cloud:\n    alibaba:\n      seata:\n        tx-service-group: seata_service_user_tx_group\n\nmybatis-plus:\n  mapper-locations: classpath:mapper/*.xml\n  global-config:\n    db-config:\n      id-type: auto\n      table-underline: true\n  configuration:\n    map-underscore-to-camel-case: true #是否开启驼峰命名规则\n    call-setters-on-nulls: true\n    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl\n\nfeign:\n  hystrix:\n    enabled: true\n\nseata:\n  # 1.0新添加的enabled激活自动配置，使得我们可以在yaml/properties文件中配置，\n  # 避免了以前需要客户端引入2个文件：\n  # file.conf.bak 和 registry.conf.bak\n  # 1.0新特性，需要依赖seata-spring-boot-starter,默认为true\n  enabled: true\n  application-id: ${spring.application.name} # seata-service-business\n  tx-service-group: seata_service_user_tx_group    #此处配置自定义的seata事务分组名称 ${spring.application.name}-tx-group\n  enable-auto-data-source-proxy: true    #开启数据库代理\n  ##registry\n  registry:\n    type: nacos\n    nacos:\n      application: seata-server\n      server-addr: ${spring.cloud.nacos.discovery.server-addr} # 127.0.0.1:8848\n      group: SERVICE_SEATA_GROUP\n      namespace: service_seata\n      username: nacos\n      password: nacos\n  ##config\n  config:\n    type: nacos\n    nacos:\n      server-addr: ${spring.cloud.nacos.discovery.server-addr} # 127.0.0.1:8848\n      namespace: service_seata\n      group: SERVICE_SEATA_GROUP\n      username: nacos\n      password: nacos\n  #service  -- 对应file里面的serivce\n  service:\n    vgroup-mapping:\n      seata_service_user_tx_group: default\n    grouplist:\n      business: 127.0.0.1:8091\n    disable-global-transaction: false\n  client:\n    rm:\n      report-success-enable: false\n\n','78386c69b7ec9789cf8bedca54b4bb09','2021-05-19 01:28:20','2021-05-19 01:34:16',NULL,'0:0:0:0:0:0:0:1','','service_dev','','','','yaml',''),
(3,'user.properties','SERVICE_DEV_GROUP','server.port=10002\nserver.servlet.context-path=/api/user\nspring.application.name=seata-service-user\n\n#数据源\nspring.datasource.type=com.alibaba.druid.pool.DruidDataSource\nspring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver\nspring.datasource.url=jdbc:mysql://127.0.0.1:3306/seata_nacos?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC&useSSL=false\nspring.datasource.username=root\nspring.datasource.password=cmmplb\nspring.datasource.druid.initial-size=20\nspring.datasource.druid.max-active=20\nspring.datasource.druid.min-idle=2\nspring.datasource.druid.max-wait=60000\nspring.datasource.druid.time-between-eviction-runs-millis=60000\nspring.datasource.druid.min-evictable-idle-time-millis=300000\n\n#后发现的bean会覆盖之前相同名称的bean\nspring.main.allow-bean-definition-overriding=true\n\n#mybatis-plus\nmybatis-plus.mapper-locations=classpath:mapper/*.xml\nmybatis-plus.global-config.db-config.id-type=auto\nmybatis-plus.global-config.db-config.table-underline=true\nmybatis-plus.configuration.map-underscore-to-camel-case=true\nmybatis-plus.configuration.call-setters-on-nulls=true\nmybatis-plus.configuration.log-impl=org.apache.ibatis.logging.stdout.StdOutImpl\n\n#feign\nfeign.hystrix.enabled=false\n\n#nacos-discovery-在bootstrap里面设置了\n#spring.cloud.nacos.discovery.server-addr=127.0.0.1:8848\n#spring.cloud.nacos.discovery.namespace=service_dev\n#spring.cloud.nacos.discovery.group=SERVICE_DEV_GROUP\n\n#nacos-config-在bootstrap里面设置了\n#spring.cloud.nacos.config.server-addr=${spring.cloud.nacos.discovery.server-addr}\n#spring.cloud.nacos.config.namespace=service_dev\n#spring.cloud.nacos.config.group=SERVICE_DEV_GROUP\n#spring.cloud.nacos.config.file-extension=yaml\n#spring.cloud.nacos.config.prefix=business\n#spring.cloud.nacos.config.refresh-enabled=true\n\n#seata 配置分布式事务组-seata 服务分组，要与服务端nacos-config.txt中service.vgroup_mapping的后缀对应-在1.4的seata版本中建议设置在seata.tx-service-group\nspring.cloud.alibaba.seata.tx-service-group=seata_service_user_tx_group\n# 1.0新添加的enabled激活自动配置，使得我们可以在yaml/properties文件中配置，\n# 避免了以前需要客户端引入2个文件：\n# file.conf.bak 和 registry.conf.bak\n# 1.0新特性，需要依赖seata-spring-boot-starter,默认为true\nseata.enabled=true\n#开启数据库代理\nseata.enable-auto-data-source-proxy=true\n#和下面service中的vgroup-mapping对应\nseata.tx-service-group=seata_service_user_tx_group\n##registry\nseata.registry.type=nacos\nseata.registry.nacos.application=seata-server\nseata.registry.nacos.server-addr=127.0.0.1:8848\nseata.registry.nacos.group=SERVICE_SEATA_GROUP\nseata.registry.nacos.namespace=service_seata\nseata.registry.nacos.cluster=default\nseata.registry.nacos.username=nacos\nseata.registry.nacos.password=nacos\n##config\nseata.config.type=nacos\nseata.config.nacos.server-addr=127.0.0.1:8848\nseata.config.nacos.namespace=service_seata\nseata.config.nacos.group=SERVICE_SEATA_GROUP\nseata.config.nacos.username=nacos\nseata.config.nacos.password=nacos\n#service  -- 对应file里面的serivce\nseata.service.vgroup-mapping.seata_service_user_tx_group=default\nseata.service.disable-global-transaction=false\nseata.client.rm.report-success-enable=false\n','fabf0a644823e98d96e948eb1da92f7f','2021-05-19 01:28:59','2021-05-19 01:34:28',NULL,'0:0:0:0:0:0:0:1','','service_dev','','','','properties',''),
(4,'business.properties','SERVICE_DEV_GROUP','server.port=10001\nserver.servlet.context-path=/api/business\nspring.application.name=seata-service-business\n\n#数据源\nspring.datasource.type=com.alibaba.druid.pool.DruidDataSource\nspring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver\nspring.datasource.url=jdbc:mysql://127.0.0.1:3306/seata_nacos?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC&useSSL=false\nspring.datasource.username=root\nspring.datasource.password=cmmplb\nspring.datasource.druid.initial-size=20\nspring.datasource.druid.max-active=20\nspring.datasource.druid.min-idle=2\nspring.datasource.druid.max-wait=60000\nspring.datasource.druid.time-between-eviction-runs-millis=60000\nspring.datasource.druid.min-evictable-idle-time-millis=300000\n\n#后发现的bean会覆盖之前相同名称的bean\nspring.main.allow-bean-definition-overriding=true\n\n#mybatis-plus\nmybatis-plus.mapper-locations=classpath:mapper/*.xml\nmybatis-plus.global-config.db-config.id-type=auto\nmybatis-plus.global-config.db-config.table-underline=true\nmybatis-plus.configuration.map-underscore-to-camel-case=true\nmybatis-plus.configuration.call-setters-on-nulls=true\nmybatis-plus.configuration.log-impl=org.apache.ibatis.logging.stdout.StdOutImpl\n\n#feign\nfeign.hystrix.enabled=false\n\n#nacos-discovery-在bootstrap里面设置了\n#spring.cloud.nacos.discovery.server-addr=127.0.0.1:8848\n#spring.cloud.nacos.discovery.namespace=service_dev\n#spring.cloud.nacos.discovery.group=SERVICE_DEV_GROUP\n\n#nacos-config-在bootstrap里面设置了\n#spring.cloud.nacos.config.server-addr=${spring.cloud.nacos.discovery.server-addr}\n#spring.cloud.nacos.config.namespace=seata_nacos\n#spring.cloud.nacos.config.group=SERVICE_DEV_GROUP\n#spring.cloud.nacos.config.file-extension=yaml\n#spring.cloud.nacos.config.prefix=business\n#spring.cloud.nacos.config.refresh-enabled=true\n\n#seata 配置分布式事务组-seata 服务分组，要与服务端nacos-config.txt中service.vgroup_mapping的后缀对应-在1.4的seata版本中建议设置在seata.tx-service-group\nspring.cloud.alibaba.seata.tx-service-group=seata_service_business_tx_group\n# 1.0新添加的enabled激活自动配置，使得我们可以在yaml/properties文件中配置，\n# 避免了以前需要客户端引入2个文件：\n# file.conf.bak 和 registry.conf.bak\n# 1.0新特性，需要依赖seata-spring-boot-starter,默认为true\nseata.enabled=true\n#开启数据库代理\nseata.enable-auto-data-source-proxy=true\n#和下面service中的vgroup-mapping对应\nseata.tx-service-group=seata_service_business_tx_group\n##registry\nseata.registry.type=nacos\nseata.registry.nacos.application=seata-server\nseata.registry.nacos.server-addr=127.0.0.1:8848\nseata.registry.nacos.group=SERVICE_SEATA_GROUP\nseata.registry.nacos.namespace=service_seata\nseata.registry.nacos.cluster=default\nseata.registry.nacos.username=nacos\nseata.registry.nacos.password=nacos\n##config\nseata.config.type=nacos\nseata.config.nacos.server-addr=127.0.0.1:8848\nseata.config.nacos.namespace=service_seata\nseata.config.nacos.group=SERVICE_SEATA_GROUP\nseata.config.nacos.username=nacos\nseata.config.nacos.password=nacos\n#service  -- 对应file里面的serivce\nseata.service.vgroup-mapping.seata_service_business_tx_group=default\nseata.service.disable-global-transaction=false\nseata.client.rm.report-success-enable=false\n','30d7bb0719c699cb93a5125a5544065d','2021-05-19 01:29:27','2021-05-19 01:34:39',NULL,'0:0:0:0:0:0:0:1','','service_dev','','','','properties',''),
(5,'transport.type','SERVICE_SEATA_GROUP','TCP','b136ef5f6a01d816991fe3cf7a6ac763','2021-05-19 01:44:15','2021-05-19 01:44:15',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(6,'transport.server','SERVICE_SEATA_GROUP','NIO','b6d9dfc0fb54277321cebc0fff55df2f','2021-05-19 01:44:15','2021-05-19 01:44:15',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(7,'transport.heartbeat','SERVICE_SEATA_GROUP','true','b326b5062b2f0e69046810717534cb09','2021-05-19 01:44:16','2021-05-19 01:44:16',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(8,'transport.enableClientBatchSendRequest','SERVICE_SEATA_GROUP','false','68934a3e9455fa72420237eb05902327','2021-05-19 01:44:16','2021-05-19 01:44:16',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(9,'transport.threadFactory.bossThreadPrefix','SERVICE_SEATA_GROUP','NettyBoss','0f8db59a3b7f2823f38a70c308361836','2021-05-19 01:44:16','2021-05-19 01:44:16',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(10,'transport.threadFactory.workerThreadPrefix','SERVICE_SEATA_GROUP','NettyServerNIOWorker','a78ec7ef5d1631754c4e72ae8a3e9205','2021-05-19 01:44:16','2021-05-19 01:44:16',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(11,'transport.threadFactory.serverExecutorThreadPrefix','SERVICE_SEATA_GROUP','NettyServerBizHandler','11a36309f3d9df84fa8b59cf071fa2da','2021-05-19 01:44:17','2021-05-19 01:44:17',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(12,'transport.threadFactory.shareBossWorker','SERVICE_SEATA_GROUP','false','68934a3e9455fa72420237eb05902327','2021-05-19 01:44:17','2021-05-19 01:44:17',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(13,'transport.threadFactory.clientSelectorThreadPrefix','SERVICE_SEATA_GROUP','NettyClientSelector','cd7ec5a06541e75f5a7913752322c3af','2021-05-19 01:44:17','2021-05-19 01:44:17',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(14,'transport.threadFactory.clientSelectorThreadSize','SERVICE_SEATA_GROUP','1','c4ca4238a0b923820dcc509a6f75849b','2021-05-19 01:44:17','2021-05-19 01:44:17',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(15,'transport.threadFactory.clientWorkerThreadPrefix','SERVICE_SEATA_GROUP','NettyClientWorkerThread','61cf4e69a56354cf72f46dc86414a57e','2021-05-19 01:44:18','2021-05-19 01:44:18',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(16,'transport.threadFactory.bossThreadSize','SERVICE_SEATA_GROUP','1','c4ca4238a0b923820dcc509a6f75849b','2021-05-19 01:44:18','2021-05-19 01:44:18',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(17,'transport.threadFactory.workerThreadSize','SERVICE_SEATA_GROUP','default','c21f969b5f03d33d43e04f8f136e7682','2021-05-19 01:44:18','2021-05-19 01:44:18',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(18,'transport.shutdown.wait','SERVICE_SEATA_GROUP','3','eccbc87e4b5ce2fe28308fd9f2a7baf3','2021-05-19 01:44:18','2021-05-19 01:44:18',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(19,'service.vgroupMapping.seata_service_business_tx_group','SERVICE_SEATA_GROUP','default','c21f969b5f03d33d43e04f8f136e7682','2021-05-19 01:44:19','2021-05-19 01:44:19',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(20,'service.vgroupMapping.seata_service_user_tx_group','SERVICE_SEATA_GROUP','default','c21f969b5f03d33d43e04f8f136e7682','2021-05-19 01:44:19','2021-05-19 01:44:19',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(21,'service.default.grouplist','SERVICE_SEATA_GROUP','127.0.0.1:8091','c32ce0d3e264525dcdada751f98143a3','2021-05-19 01:44:19','2021-05-19 01:44:19',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(22,'service.enableDegrade','SERVICE_SEATA_GROUP','false','68934a3e9455fa72420237eb05902327','2021-05-19 01:44:19','2021-05-19 01:44:19',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(23,'service.disableGlobalTransaction','SERVICE_SEATA_GROUP','false','68934a3e9455fa72420237eb05902327','2021-05-19 01:44:20','2021-05-19 01:44:20',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(24,'client.rm.asyncCommitBufferLimit','SERVICE_SEATA_GROUP','10000','b7a782741f667201b54880c925faec4b','2021-05-19 01:44:20','2021-05-19 01:44:20',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(25,'client.rm.lock.retryInterval','SERVICE_SEATA_GROUP','10','d3d9446802a44259755d38e6d163e820','2021-05-19 01:44:20','2021-05-19 01:44:20',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(26,'client.rm.lock.retryTimes','SERVICE_SEATA_GROUP','30','34173cb38f07f89ddbebc2ac9128303f','2021-05-19 01:44:20','2021-05-19 01:44:20',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(27,'client.rm.lock.retryPolicyBranchRollbackOnConflict','SERVICE_SEATA_GROUP','true','b326b5062b2f0e69046810717534cb09','2021-05-19 01:44:20','2021-05-19 01:44:20',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(28,'client.rm.reportRetryCount','SERVICE_SEATA_GROUP','5','e4da3b7fbbce2345d7772b0674a318d5','2021-05-19 01:44:21','2021-05-19 01:44:21',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(29,'client.rm.tableMetaCheckEnable','SERVICE_SEATA_GROUP','false','68934a3e9455fa72420237eb05902327','2021-05-19 01:44:21','2021-05-19 01:44:21',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(30,'client.rm.tableMetaCheckerInterval','SERVICE_SEATA_GROUP','60000','2b4226dd7ed6eb2d419b881f3ae9c97c','2021-05-19 01:44:21','2021-05-19 01:44:21',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(31,'client.rm.sqlParserType','SERVICE_SEATA_GROUP','druid','3d650fb8a5df01600281d48c47c9fa60','2021-05-19 01:44:21','2021-05-19 01:44:21',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(32,'client.rm.reportSuccessEnable','SERVICE_SEATA_GROUP','false','68934a3e9455fa72420237eb05902327','2021-05-19 01:44:22','2021-05-19 01:44:22',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(33,'client.rm.sagaBranchRegisterEnable','SERVICE_SEATA_GROUP','false','68934a3e9455fa72420237eb05902327','2021-05-19 01:44:22','2021-05-19 01:44:22',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(34,'client.tm.commitRetryCount','SERVICE_SEATA_GROUP','5','e4da3b7fbbce2345d7772b0674a318d5','2021-05-19 01:44:22','2021-05-19 01:44:22',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(35,'client.tm.rollbackRetryCount','SERVICE_SEATA_GROUP','5','e4da3b7fbbce2345d7772b0674a318d5','2021-05-19 01:44:22','2021-05-19 01:44:22',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(36,'client.tm.defaultGlobalTransactionTimeout','SERVICE_SEATA_GROUP','60000','2b4226dd7ed6eb2d419b881f3ae9c97c','2021-05-19 01:44:23','2021-05-19 01:44:23',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(37,'client.tm.degradeCheck','SERVICE_SEATA_GROUP','false','68934a3e9455fa72420237eb05902327','2021-05-19 01:44:23','2021-05-19 01:44:23',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(38,'client.tm.degradeCheckAllowTimes','SERVICE_SEATA_GROUP','10','d3d9446802a44259755d38e6d163e820','2021-05-19 01:44:23','2021-05-19 01:44:23',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(39,'client.tm.degradeCheckPeriod','SERVICE_SEATA_GROUP','2000','08f90c1a417155361a5c4b8d297e0d78','2021-05-19 01:44:23','2021-05-19 01:44:23',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(40,'store.mode','SERVICE_SEATA_GROUP','db','d77d5e503ad1439f585ac494268b351b','2021-05-19 01:44:23','2021-05-19 01:44:23',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(41,'store.file.dir','SERVICE_SEATA_GROUP','file_store/data','6a8dec07c44c33a8a9247cba5710bbb2','2021-05-19 01:44:24','2021-05-19 01:44:24',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(42,'store.file.maxBranchSessionSize','SERVICE_SEATA_GROUP','16384','c76fe1d8e08462434d800487585be217','2021-05-19 01:44:24','2021-05-19 01:44:24',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(43,'store.file.maxGlobalSessionSize','SERVICE_SEATA_GROUP','512','10a7cdd970fe135cf4f7bb55c0e3b59f','2021-05-19 01:44:24','2021-05-19 01:44:24',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(44,'store.file.fileWriteBufferCacheSize','SERVICE_SEATA_GROUP','16384','c76fe1d8e08462434d800487585be217','2021-05-19 01:44:25','2021-05-19 01:44:25',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(45,'store.file.flushDiskMode','SERVICE_SEATA_GROUP','async','0df93e34273b367bb63bad28c94c78d5','2021-05-19 01:44:25','2021-05-19 01:44:25',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(46,'store.file.sessionReloadReadSize','SERVICE_SEATA_GROUP','100','f899139df5e1059396431415e770c6dd','2021-05-19 01:44:25','2021-05-19 01:44:25',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(47,'store.db.datasource','SERVICE_SEATA_GROUP','druid','3d650fb8a5df01600281d48c47c9fa60','2021-05-19 01:44:25','2021-05-19 01:44:25',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(48,'store.db.dbType','SERVICE_SEATA_GROUP','mysql','81c3b080dad537de7e10e0987a4bf52e','2021-05-19 01:44:25','2021-05-19 01:44:25',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(49,'store.db.driverClassName','SERVICE_SEATA_GROUP','com.mysql.jdbc.Driver','683cf0c3a5a56cec94dfac94ca16d760','2021-05-19 01:44:26','2021-05-19 01:44:26',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(50,'store.db.url','SERVICE_SEATA_GROUP','jdbc:mysql://127.0.0.1:3306/seata?useUnicode=true&rewriteBatchedStatements=true','030ea5ff5c2ef043adf9826c70570b0b','2021-05-19 01:44:26','2021-05-19 01:44:26',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(51,'store.db.user','SERVICE_SEATA_GROUP','root','63a9f0ea7bb98050796b649e85481845','2021-05-19 01:44:26','2021-05-19 01:44:26',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(52,'store.db.password','SERVICE_SEATA_GROUP','cmmplb','a63c8f5c97d01ba54853d329f2b2dc0f','2021-05-19 01:44:26','2021-05-19 01:44:26',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(53,'store.db.minConn','SERVICE_SEATA_GROUP','5','e4da3b7fbbce2345d7772b0674a318d5','2021-05-19 01:44:26','2021-05-19 01:44:26',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(54,'store.db.maxConn','SERVICE_SEATA_GROUP','30','34173cb38f07f89ddbebc2ac9128303f','2021-05-19 01:44:27','2021-05-19 01:44:27',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(55,'store.db.globalTable','SERVICE_SEATA_GROUP','global_table','8b28fb6bb4c4f984df2709381f8eba2b','2021-05-19 01:44:27','2021-05-19 01:44:27',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(56,'store.db.branchTable','SERVICE_SEATA_GROUP','branch_table','54bcdac38cf62e103fe115bcf46a660c','2021-05-19 01:44:27','2021-05-19 01:44:27',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(57,'store.db.queryLimit','SERVICE_SEATA_GROUP','100','f899139df5e1059396431415e770c6dd','2021-05-19 01:44:27','2021-05-19 01:44:27',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(58,'store.db.lockTable','SERVICE_SEATA_GROUP','lock_table','55e0cae3b6dc6696b768db90098b8f2f','2021-05-19 01:44:27','2021-05-19 01:44:27',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(59,'store.db.maxWait','SERVICE_SEATA_GROUP','5000','a35fe7f7fe8217b4369a0af4244d1fca','2021-05-19 01:44:28','2021-05-19 01:44:28',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(60,'store.redis.mode','SERVICE_SEATA_GROUP','single','dd5c07036f2975ff4bce568b6511d3bc','2021-05-19 01:44:28','2021-05-19 01:44:28',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(61,'store.redis.single.host','SERVICE_SEATA_GROUP','127.0.0.1','f528764d624db129b32c21fbca0cb8d6','2021-05-19 01:44:28','2021-05-19 01:44:28',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(62,'store.redis.single.port','SERVICE_SEATA_GROUP','6379','92c3b916311a5517d9290576e3ea37ad','2021-05-19 01:44:28','2021-05-19 01:44:28',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(63,'store.redis.maxConn','SERVICE_SEATA_GROUP','10','d3d9446802a44259755d38e6d163e820','2021-05-19 01:44:29','2021-05-19 01:44:29',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(64,'store.redis.minConn','SERVICE_SEATA_GROUP','1','c4ca4238a0b923820dcc509a6f75849b','2021-05-19 01:44:29','2021-05-19 01:44:29',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(65,'store.redis.maxTotal','SERVICE_SEATA_GROUP','100','f899139df5e1059396431415e770c6dd','2021-05-19 01:44:29','2021-05-19 01:44:29',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(66,'store.redis.database','SERVICE_SEATA_GROUP','0','cfcd208495d565ef66e7dff9f98764da','2021-05-19 01:44:30','2021-05-19 01:44:30',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(67,'store.redis.queryLimit','SERVICE_SEATA_GROUP','100','f899139df5e1059396431415e770c6dd','2021-05-19 01:44:30','2021-05-19 01:44:30',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(68,'server.recovery.committingRetryPeriod','SERVICE_SEATA_GROUP','1000','a9b7ba70783b617e9998dc4dd82eb3c5','2021-05-19 01:44:30','2021-05-19 01:44:30',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(69,'server.recovery.asynCommittingRetryPeriod','SERVICE_SEATA_GROUP','1000','a9b7ba70783b617e9998dc4dd82eb3c5','2021-05-19 01:44:31','2021-05-19 01:44:31',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(70,'server.recovery.rollbackingRetryPeriod','SERVICE_SEATA_GROUP','1000','a9b7ba70783b617e9998dc4dd82eb3c5','2021-05-19 01:44:31','2021-05-19 01:44:31',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(71,'server.recovery.timeoutRetryPeriod','SERVICE_SEATA_GROUP','1000','a9b7ba70783b617e9998dc4dd82eb3c5','2021-05-19 01:44:31','2021-05-19 01:44:31',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(72,'server.maxCommitRetryTimeout','SERVICE_SEATA_GROUP','-1','6bb61e3b7bce0931da574d19d1d82c88','2021-05-19 01:44:31','2021-05-19 01:44:31',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(73,'server.maxRollbackRetryTimeout','SERVICE_SEATA_GROUP','-1','6bb61e3b7bce0931da574d19d1d82c88','2021-05-19 01:44:31','2021-05-19 01:44:31',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(74,'server.rollbackRetryTimeoutUnlockEnable','SERVICE_SEATA_GROUP','false','68934a3e9455fa72420237eb05902327','2021-05-19 01:44:32','2021-05-19 01:44:32',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(75,'client.undo.dataValidation','SERVICE_SEATA_GROUP','true','b326b5062b2f0e69046810717534cb09','2021-05-19 01:44:32','2021-05-19 01:44:32',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(76,'client.undo.logSerialization','SERVICE_SEATA_GROUP','jackson','b41779690b83f182acc67d6388c7bac9','2021-05-19 01:44:32','2021-05-19 01:44:32',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(77,'client.undo.onlyCareUpdateColumns','SERVICE_SEATA_GROUP','true','b326b5062b2f0e69046810717534cb09','2021-05-19 01:44:32','2021-05-19 01:44:32',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(78,'server.undo.logSaveDays','SERVICE_SEATA_GROUP','7','8f14e45fceea167a5a36dedd4bea2543','2021-05-19 01:44:33','2021-05-19 01:44:33',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(79,'server.undo.logDeletePeriod','SERVICE_SEATA_GROUP','86400000','f4c122804fe9076cb2710f55c3c6e346','2021-05-19 01:44:33','2021-05-19 01:44:33',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(80,'client.undo.logTable','SERVICE_SEATA_GROUP','undo_log','2842d229c24afe9e61437135e8306614','2021-05-19 01:44:33','2021-05-19 01:44:33',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(81,'client.undo.compress.enable','SERVICE_SEATA_GROUP','true','b326b5062b2f0e69046810717534cb09','2021-05-19 01:44:33','2021-05-19 01:44:33',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(82,'client.undo.compress.type','SERVICE_SEATA_GROUP','zip','adcdbd79a8d84175c229b192aadc02f2','2021-05-19 01:44:33','2021-05-19 01:44:33',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(83,'client.undo.compress.threshold','SERVICE_SEATA_GROUP','64k','bd44a6458bdbff0b5cac721ba361f035','2021-05-19 01:44:34','2021-05-19 01:44:34',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(84,'log.exceptionRate','SERVICE_SEATA_GROUP','100','f899139df5e1059396431415e770c6dd','2021-05-19 01:44:34','2021-05-19 01:44:34',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(85,'transport.serialization','SERVICE_SEATA_GROUP','seata','b943081c423b9a5416a706524ee05d40','2021-05-19 01:44:34','2021-05-19 01:44:34',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(86,'transport.compressor','SERVICE_SEATA_GROUP','none','334c4a4c42fdb79d7ebc3e73b517e6f8','2021-05-19 01:44:34','2021-05-19 01:44:34',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(87,'metrics.enabled','SERVICE_SEATA_GROUP','false','68934a3e9455fa72420237eb05902327','2021-05-19 01:44:35','2021-05-19 01:44:35',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(88,'metrics.registryType','SERVICE_SEATA_GROUP','compact','7cf74ca49c304df8150205fc915cd465','2021-05-19 01:44:35','2021-05-19 01:44:35',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(89,'metrics.exporterList','SERVICE_SEATA_GROUP','prometheus','e4f00638b8a10e6994e67af2f832d51c','2021-05-19 01:44:35','2021-05-19 01:44:35',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL),
(90,'metrics.exporterPrometheusPort','SERVICE_SEATA_GROUP','9898','7b9dc501afe4ee11c56a4831e20cee71','2021-05-19 01:44:35','2021-05-19 01:44:35',NULL,'0:0:0:0:0:0:0:1','','service_seata',NULL,NULL,NULL,'text',NULL);

/*Table structure for table `config_info_aggr` */

CREATE TABLE `config_info_aggr` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `datum_id` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'datum_id',
  `content` longtext COLLATE utf8_bin NOT NULL COMMENT '内容',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `app_name` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `tenant_id` varchar(128) COLLATE utf8_bin DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfoaggr_datagrouptenantdatum` (`data_id`,`group_id`,`tenant_id`,`datum_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='增加租户字段';

/*Data for the table `config_info_aggr` */

/*Table structure for table `config_info_beta` */

CREATE TABLE `config_info_beta` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `app_name` varchar(128) COLLATE utf8_bin DEFAULT NULL COMMENT 'app_name',
  `content` longtext COLLATE utf8_bin NOT NULL COMMENT 'content',
  `beta_ips` varchar(1024) COLLATE utf8_bin DEFAULT NULL COMMENT 'betaIps',
  `md5` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '修改时间',
  `src_user` text COLLATE utf8_bin COMMENT 'source user',
  `src_ip` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT 'source ip',
  `tenant_id` varchar(128) COLLATE utf8_bin DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfobeta_datagrouptenant` (`data_id`,`group_id`,`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_info_beta';

/*Data for the table `config_info_beta` */

/*Table structure for table `config_info_tag` */

CREATE TABLE `config_info_tag` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) COLLATE utf8_bin DEFAULT '' COMMENT 'tenant_id',
  `tag_id` varchar(128) COLLATE utf8_bin NOT NULL COMMENT 'tag_id',
  `app_name` varchar(128) COLLATE utf8_bin DEFAULT NULL COMMENT 'app_name',
  `content` longtext COLLATE utf8_bin NOT NULL COMMENT 'content',
  `md5` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '修改时间',
  `src_user` text COLLATE utf8_bin COMMENT 'source user',
  `src_ip` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT 'source ip',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfotag_datagrouptenanttag` (`data_id`,`group_id`,`tenant_id`,`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_info_tag';

/*Data for the table `config_info_tag` */

/*Table structure for table `config_tags_relation` */

CREATE TABLE `config_tags_relation` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `tag_name` varchar(128) COLLATE utf8_bin NOT NULL COMMENT 'tag_name',
  `tag_type` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'tag_type',
  `data_id` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) COLLATE utf8_bin DEFAULT '' COMMENT 'tenant_id',
  `nid` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`nid`),
  UNIQUE KEY `uk_configtagrelation_configidtag` (`id`,`tag_name`,`tag_type`),
  KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_tag_relation';

/*Data for the table `config_tags_relation` */

/*Table structure for table `group_capacity` */

CREATE TABLE `group_capacity` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `group_id` varchar(128) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT 'Group ID，空字符表示整个集群',
  `quota` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '配额，0表示使用默认值',
  `usage` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '使用量',
  `max_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '聚合子配置最大个数，，0表示使用默认值',
  `max_aggr_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_group_id` (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='集群、各Group容量信息表';

/*Data for the table `group_capacity` */

/*Table structure for table `his_config_info` */

CREATE TABLE `his_config_info` (
  `id` bigint(64) unsigned NOT NULL,
  `nid` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `data_id` varchar(255) COLLATE utf8_bin NOT NULL,
  `group_id` varchar(128) COLLATE utf8_bin NOT NULL,
  `app_name` varchar(128) COLLATE utf8_bin DEFAULT NULL COMMENT 'app_name',
  `content` longtext COLLATE utf8_bin NOT NULL,
  `md5` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `gmt_create` datetime NOT NULL DEFAULT '2010-05-05 00:00:00',
  `gmt_modified` datetime NOT NULL DEFAULT '2010-05-05 00:00:00',
  `src_user` text COLLATE utf8_bin,
  `src_ip` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `op_type` char(10) COLLATE utf8_bin DEFAULT NULL,
  `tenant_id` varchar(128) COLLATE utf8_bin DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`nid`),
  KEY `idx_gmt_create` (`gmt_create`),
  KEY `idx_gmt_modified` (`gmt_modified`),
  KEY `idx_did` (`data_id`)
) ENGINE=InnoDB AUTO_INCREMENT=91 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='多租户改造';

/*Data for the table `his_config_info` */

insert  into `his_config_info`(`id`,`nid`,`data_id`,`group_id`,`app_name`,`content`,`md5`,`gmt_create`,`gmt_modified`,`src_user`,`src_ip`,`op_type`,`tenant_id`) values 
(0,1,'business.yaml','SERVICE_DEV_GROUP','','server:\r\n  port: 10001\r\n  servlet:\r\n    context-path: /api/business\r\nspring:\r\n  application:\r\n    name: seata-service-business\r\n  datasource:\r\n    type: com.alibaba.druid.pool.DruidDataSource\r\n    driver-class-name: com.mysql.cj.jdbc.Driver\r\n    url: jdbc:mysql://127.0.0.1:3306/seata_nacos?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC&useSSL=false\r\n    username: root\r\n    password: cmmplb\r\n    druid:\r\n      initial-size: 20\r\n      max-active: 20\r\n      min-idle: 2\r\n      max-wait: 60000 #空闲等待超时\r\n      time-between-eviction-runs-millis: 60000 # 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒\r\n      min-evictable-idle-time-millis: 300000  # 配置一个连接在池中最小生存的时间，单位是毫秒\r\n  main:\r\n    allow-bean-definition-overriding: true #后发现的bean会覆盖之前相同名称的bean\r\n\r\n  #seata 配置分布式事务组-seata 服务分组，要与服务端nacos-config.txt中service.vgroup_mapping的后缀对应-在1.4的seata版本中建议设置在seata.tx-service-group\r\n  cloud:\r\n    alibaba:\r\n      seata:\r\n        tx-service-group: seata_service_business_tx_group\r\n\r\nmybatis-plus:\r\n  mapper-locations: classpath:mapper/*.xml\r\n  global-config:\r\n    db-config:\r\n      id-type: auto\r\n      table-underline: true\r\n  configuration:\r\n    map-underscore-to-camel-case: true #是否开启驼峰命名规则\r\n    call-setters-on-nulls: true\r\n    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl\r\n\r\nfeign:\r\n  hystrix:\r\n    enabled: true\r\n\r\nseata:\r\n  # 1.0新添加的enabled激活自动配置，使得我们可以在yaml/properties文件中配置，\r\n  # 避免了以前需要客户端引入2个文件：\r\n  # file.conf.bak 和 registry.conf.bak\r\n  # 1.0新特性，需要依赖seata-spring-boot-starter,默认为true\r\n  enabled: true\r\n  application-id: ${spring.application.name} # seata-service-business\r\n  tx-service-group: seata_service_business_tx_group    #此处配置自定义的seata事务分组名称 ${spring.application.name}-tx-group\r\n  enable-auto-data-source-proxy: true    #开启数据库代理\r\n  ##registry\r\n  registry:\r\n    type: nacos\r\n    nacos:\r\n      application: seata-server\r\n      server-addr: ${spring.cloud.nacos.discovery.server-addr} # 127.0.0.1:8848\r\n      group: SERVICE_SEATA_GROUP\r\n      namespace: service_seata\r\n      username: nacos\r\n      password: nacos\r\n  ##config\r\n  config:\r\n    type: nacos\r\n    nacos:\r\n      server-addr: ${spring.cloud.nacos.discovery.server-addr} # 127.0.0.1:8848\r\n      namespace: service_seata\r\n      group: SERVICE_SEATA_GROUP\r\n      username: nacos\r\n      password: nacos\r\n  #service  -- 对应file里面的serivce\r\n  service:\r\n    vgroup-mapping:\r\n      seata_service_business_tx_group: default\r\n    grouplist:\r\n      business: 127.0.0.1:8091\r\n    disable-global-transaction: false\r\n  client:\r\n    rm:\r\n      report-success-enable: false\r\n\r\n\r\n\r\n\r\n\r\n','08115717d0275122e00ba763a0e27923','2010-05-05 00:00:00','2021-05-19 01:27:43',NULL,'0:0:0:0:0:0:0:1','I','service_dev'),
(0,2,'user.yaml','SERVICE_DEV_GROUP','','server:\r\n  port: 10002\r\n  servlet:\r\n    context-path: /api/user\r\nspring:\r\n  application:\r\n    name: seata-service-user\r\n  datasource:\r\n    type: com.alibaba.druid.pool.DruidDataSource\r\n    driver-class-name: com.mysql.cj.jdbc.Driver\r\n    url: jdbc:mysql://127.0.0.1:3306/seata_nacos?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC&useSSL=false\r\n    username: root\r\n    password: cmmplb\r\n    druid:\r\n      initial-size: 20\r\n      max-active: 20\r\n      min-idle: 2\r\n      max-wait: 60000 #空闲等待超时\r\n      time-between-eviction-runs-millis: 60000 # 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒\r\n      min-evictable-idle-time-millis: 300000  # 配置一个连接在池中最小生存的时间，单位是毫秒\r\n  main:\r\n    allow-bean-definition-overriding: true #后发现的bean会覆盖之前相同名称的bean\r\n\r\n  #seata 配置分布式事务组-seata 服务分组，要与服务端nacos-config.txt中service.vgroup_mapping的后缀对应-在1.4的seata版本中建议设置在seata.tx-service-group\r\n  cloud:\r\n    alibaba:\r\n      seata:\r\n        tx-service-group: seata_service_user_tx_group\r\n\r\nmybatis-plus:\r\n  mapper-locations: classpath:mapper/*.xml\r\n  global-config:\r\n    db-config:\r\n      id-type: auto\r\n      table-underline: true\r\n  configuration:\r\n    map-underscore-to-camel-case: true #是否开启驼峰命名规则\r\n    call-setters-on-nulls: true\r\n    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl\r\n\r\nfeign:\r\n  hystrix:\r\n    enabled: true\r\n\r\nseata:\r\n  # 1.0新添加的enabled激活自动配置，使得我们可以在yaml/properties文件中配置，\r\n  # 避免了以前需要客户端引入2个文件：\r\n  # file.conf.bak 和 registry.conf.bak\r\n  # 1.0新特性，需要依赖seata-spring-boot-starter,默认为true\r\n  enabled: true\r\n  application-id: ${spring.application.name} # seata-service-business\r\n  tx-service-group: seata_service_user_tx_group    #此处配置自定义的seata事务分组名称 ${spring.application.name}-tx-group\r\n  enable-auto-data-source-proxy: true    #开启数据库代理\r\n  ##registry\r\n  registry:\r\n    type: nacos\r\n    nacos:\r\n      application: seata-server\r\n      server-addr: ${spring.cloud.nacos.discovery.server-addr} # 127.0.0.1:8848\r\n      group: SEATA_GROUP\r\n      namespace: service_seata\r\n      username: nacos\r\n      password: nacos\r\n  ##config\r\n  config:\r\n    type: nacos\r\n    nacos:\r\n      server-addr: ${spring.cloud.nacos.discovery.server-addr} # 127.0.0.1:8848\r\n      namespace: service_seata\r\n      group: SEATA_GROUP\r\n      username: nacos\r\n      password: nacos\r\n  #service  -- 对应file里面的serivce\r\n  service:\r\n    vgroup-mapping:\r\n      seata_service_user_tx_group: default\r\n    grouplist:\r\n      business: 127.0.0.1:8091\r\n    disable-global-transaction: false\r\n  client:\r\n    rm:\r\n      report-success-enable: false\r\n\r\n','b5dc14b9f9d4a34ffb1f686a09c0070e','2010-05-05 00:00:00','2021-05-19 01:28:20',NULL,'0:0:0:0:0:0:0:1','I','service_dev'),
(0,3,'user.properties','SERVICE_DEV_GROUP','','server.port=10002\r\nserver.servlet.context-path=/api/user\r\nspring.application.name=seata-service-user\r\n\r\n#数据源\r\nspring.datasource.type=com.alibaba.druid.pool.DruidDataSource\r\nspring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver\r\nspring.datasource.url=jdbc:mysql://127.0.0.1:3306/seata_nacos?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC&useSSL=false\r\nspring.datasource.username=root\r\nspring.datasource.password=cmmplb\r\nspring.datasource.druid.initial-size=20\r\nspring.datasource.druid.max-active=20\r\nspring.datasource.druid.min-idle=2\r\nspring.datasource.druid.max-wait=60000\r\nspring.datasource.druid.time-between-eviction-runs-millis=60000\r\nspring.datasource.druid.min-evictable-idle-time-millis=300000\r\n\r\n#后发现的bean会覆盖之前相同名称的bean\r\nspring.main.allow-bean-definition-overriding=true\r\n\r\n#mybatis-plus\r\nmybatis-plus.mapper-locations=classpath:mapper/*.xml\r\nmybatis-plus.global-config.db-config.id-type=auto\r\nmybatis-plus.global-config.db-config.table-underline=true\r\nmybatis-plus.configuration.map-underscore-to-camel-case=true\r\nmybatis-plus.configuration.call-setters-on-nulls=true\r\nmybatis-plus.configuration.log-impl=org.apache.ibatis.logging.stdout.StdOutImpl\r\n\r\n#feign\r\nfeign.hystrix.enabled=false\r\n\r\n#nacos-discovery-在bootstrap里面设置了\r\n#spring.cloud.nacos.discovery.server-addr=127.0.0.1:8848\r\n#spring.cloud.nacos.discovery.namespace=service_dev\r\n#spring.cloud.nacos.discovery.group=SERVICE_DEV_GROUP\r\n\r\n#nacos-config-在bootstrap里面设置了\r\n#spring.cloud.nacos.config.server-addr=${spring.cloud.nacos.discovery.server-addr}\r\n#spring.cloud.nacos.config.namespace=service_dev\r\n#spring.cloud.nacos.config.group=SERVICE_DEV_GROUP\r\n#spring.cloud.nacos.config.file-extension=yaml\r\n#spring.cloud.nacos.config.prefix=business\r\n#spring.cloud.nacos.config.refresh-enabled=true\r\n\r\n#seata 配置分布式事务组-seata 服务分组，要与服务端nacos-config.txt中service.vgroup_mapping的后缀对应-在1.4的seata版本中建议设置在seata.tx-service-group\r\nspring.cloud.alibaba.seata.tx-service-group=seata_service_user_tx_group\r\n# 1.0新添加的enabled激活自动配置，使得我们可以在yaml/properties文件中配置，\r\n# 避免了以前需要客户端引入2个文件：\r\n# file.conf.bak 和 registry.conf.bak\r\n# 1.0新特性，需要依赖seata-spring-boot-starter,默认为true\r\nseata.enabled=true\r\n#开启数据库代理\r\nseata.enable-auto-data-source-proxy=true\r\n#和下面service中的vgroup-mapping对应\r\nseata.tx-service-group=seata_service_user_tx_group\r\n##registry\r\nseata.registry.type=nacos\r\nseata.registry.nacos.application=seata-server\r\nseata.registry.nacos.server-addr=127.0.0.1:8848\r\nseata.registry.nacos.group=SERVICE_SEATA_GROUP\r\nseata.registry.nacos.namespace=service_seata\r\nseata.registry.nacos.cluster=default\r\nseata.registry.nacos.username=nacos\r\nseata.registry.nacos.password=nacos\r\n##config\r\nseata.config.type=nacos\r\nseata.config.nacos.server-addr=127.0.0.1:8848\r\nseata.config.nacos.namespace=service_seata\r\nseata.config.nacos.group=SERVICE_SEATA_GROUP\r\nseata.config.nacos.username=nacos\r\nseata.config.nacos.password=nacos\r\n#service  -- 对应file里面的serivce\r\nseata.service.vgroup-mapping.seata_service_user_tx_group=default\r\nseata.service.disable-global-transaction=false\r\nseata.client.rm.report-success-enable=false\r\n','2b2af9fbe6d1f7c8b57a6b8d8ea9dbb9','2010-05-05 00:00:00','2021-05-19 01:28:59',NULL,'0:0:0:0:0:0:0:1','I','service_dev'),
(0,4,'business.properties','SERVICE_DEV_GROUP','','server.port=10001\r\nserver.servlet.context-path=/api/business\r\nspring.application.name=seata-service-business\r\n\r\n#数据源\r\nspring.datasource.type=com.alibaba.druid.pool.DruidDataSource\r\nspring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver\r\nspring.datasource.url=jdbc:mysql://127.0.0.1:3306/seata_nacos?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC&useSSL=false\r\nspring.datasource.username=root\r\nspring.datasource.password=cmmplb\r\nspring.datasource.druid.initial-size=20\r\nspring.datasource.druid.max-active=20\r\nspring.datasource.druid.min-idle=2\r\nspring.datasource.druid.max-wait=60000\r\nspring.datasource.druid.time-between-eviction-runs-millis=60000\r\nspring.datasource.druid.min-evictable-idle-time-millis=300000\r\n\r\n#后发现的bean会覆盖之前相同名称的bean\r\nspring.main.allow-bean-definition-overriding=true\r\n\r\n#mybatis-plus\r\nmybatis-plus.mapper-locations=classpath:mapper/*.xml\r\nmybatis-plus.global-config.db-config.id-type=auto\r\nmybatis-plus.global-config.db-config.table-underline=true\r\nmybatis-plus.configuration.map-underscore-to-camel-case=true\r\nmybatis-plus.configuration.call-setters-on-nulls=true\r\nmybatis-plus.configuration.log-impl=org.apache.ibatis.logging.stdout.StdOutImpl\r\n\r\n#feign\r\nfeign.hystrix.enabled=false\r\n\r\n#nacos-discovery-在bootstrap里面设置了\r\n#spring.cloud.nacos.discovery.server-addr=127.0.0.1:8848\r\n#spring.cloud.nacos.discovery.namespace=service_dev\r\n#spring.cloud.nacos.discovery.group=SERVICE_DEV_GROUP\r\n\r\n#nacos-config-在bootstrap里面设置了\r\n#spring.cloud.nacos.config.server-addr=${spring.cloud.nacos.discovery.server-addr}\r\n#spring.cloud.nacos.config.namespace=seata_nacos\r\n#spring.cloud.nacos.config.group=SERVICE_DEV_GROUP\r\n#spring.cloud.nacos.config.file-extension=yaml\r\n#spring.cloud.nacos.config.prefix=business\r\n#spring.cloud.nacos.config.refresh-enabled=true\r\n\r\n#seata 配置分布式事务组-seata 服务分组，要与服务端nacos-config.txt中service.vgroup_mapping的后缀对应-在1.4的seata版本中建议设置在seata.tx-service-group\r\nspring.cloud.alibaba.seata.tx-service-group=seata_service_business_tx_group\r\n# 1.0新添加的enabled激活自动配置，使得我们可以在yaml/properties文件中配置，\r\n# 避免了以前需要客户端引入2个文件：\r\n# file.conf.bak 和 registry.conf.bak\r\n# 1.0新特性，需要依赖seata-spring-boot-starter,默认为true\r\nseata.enabled=true\r\n#开启数据库代理\r\nseata.enable-auto-data-source-proxy=true\r\n#和下面service中的vgroup-mapping对应\r\nseata.tx-service-group=seata_service_business_tx_group\r\n##registry\r\nseata.registry.type=nacos\r\nseata.registry.nacos.application=seata-server\r\nseata.registry.nacos.server-addr=127.0.0.1:8848\r\nseata.registry.nacos.group=SERVICE_SEATA_GROUP\r\nseata.registry.nacos.namespace=service_seata\r\nseata.registry.nacos.cluster=default\r\nseata.registry.nacos.username=nacos\r\nseata.registry.nacos.password=nacos\r\n##config\r\nseata.config.type=nacos\r\nseata.config.nacos.server-addr=127.0.0.1:8848\r\nseata.config.nacos.namespace=service_seata\r\nseata.config.nacos.group=SERVICE_SEATA_GROUP\r\nseata.config.nacos.username=nacos\r\nseata.config.nacos.password=nacos\r\n#service  -- 对应file里面的serivce\r\nseata.service.vgroup-mapping.seata_service_business_tx_group=default\r\nseata.service.disable-global-transaction=false\r\nseata.client.rm.report-success-enable=false\r\n','c44fe4f3e47ddc1a5c1e36466215eca0','2010-05-05 00:00:00','2021-05-19 01:29:27',NULL,'0:0:0:0:0:0:0:1','I','service_dev'),
(0,5,'transport.type','SERVICE_SEATA_GROUP','','TCP','b136ef5f6a01d816991fe3cf7a6ac763','2010-05-05 00:00:00','2021-05-19 01:44:15',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,6,'transport.server','SERVICE_SEATA_GROUP','','NIO','b6d9dfc0fb54277321cebc0fff55df2f','2010-05-05 00:00:00','2021-05-19 01:44:15',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,7,'transport.heartbeat','SERVICE_SEATA_GROUP','','true','b326b5062b2f0e69046810717534cb09','2010-05-05 00:00:00','2021-05-19 01:44:16',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,8,'transport.enableClientBatchSendRequest','SERVICE_SEATA_GROUP','','false','68934a3e9455fa72420237eb05902327','2010-05-05 00:00:00','2021-05-19 01:44:16',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,9,'transport.threadFactory.bossThreadPrefix','SERVICE_SEATA_GROUP','','NettyBoss','0f8db59a3b7f2823f38a70c308361836','2010-05-05 00:00:00','2021-05-19 01:44:16',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,10,'transport.threadFactory.workerThreadPrefix','SERVICE_SEATA_GROUP','','NettyServerNIOWorker','a78ec7ef5d1631754c4e72ae8a3e9205','2010-05-05 00:00:00','2021-05-19 01:44:16',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,11,'transport.threadFactory.serverExecutorThreadPrefix','SERVICE_SEATA_GROUP','','NettyServerBizHandler','11a36309f3d9df84fa8b59cf071fa2da','2010-05-05 00:00:00','2021-05-19 01:44:17',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,12,'transport.threadFactory.shareBossWorker','SERVICE_SEATA_GROUP','','false','68934a3e9455fa72420237eb05902327','2010-05-05 00:00:00','2021-05-19 01:44:17',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,13,'transport.threadFactory.clientSelectorThreadPrefix','SERVICE_SEATA_GROUP','','NettyClientSelector','cd7ec5a06541e75f5a7913752322c3af','2010-05-05 00:00:00','2021-05-19 01:44:17',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,14,'transport.threadFactory.clientSelectorThreadSize','SERVICE_SEATA_GROUP','','1','c4ca4238a0b923820dcc509a6f75849b','2010-05-05 00:00:00','2021-05-19 01:44:17',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,15,'transport.threadFactory.clientWorkerThreadPrefix','SERVICE_SEATA_GROUP','','NettyClientWorkerThread','61cf4e69a56354cf72f46dc86414a57e','2010-05-05 00:00:00','2021-05-19 01:44:18',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,16,'transport.threadFactory.bossThreadSize','SERVICE_SEATA_GROUP','','1','c4ca4238a0b923820dcc509a6f75849b','2010-05-05 00:00:00','2021-05-19 01:44:18',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,17,'transport.threadFactory.workerThreadSize','SERVICE_SEATA_GROUP','','default','c21f969b5f03d33d43e04f8f136e7682','2010-05-05 00:00:00','2021-05-19 01:44:18',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,18,'transport.shutdown.wait','SERVICE_SEATA_GROUP','','3','eccbc87e4b5ce2fe28308fd9f2a7baf3','2010-05-05 00:00:00','2021-05-19 01:44:18',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,19,'service.vgroupMapping.seata_service_business_tx_group','SERVICE_SEATA_GROUP','','default','c21f969b5f03d33d43e04f8f136e7682','2010-05-05 00:00:00','2021-05-19 01:44:19',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,20,'service.vgroupMapping.seata_service_user_tx_group','SERVICE_SEATA_GROUP','','default','c21f969b5f03d33d43e04f8f136e7682','2010-05-05 00:00:00','2021-05-19 01:44:19',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,21,'service.default.grouplist','SERVICE_SEATA_GROUP','','127.0.0.1:8091','c32ce0d3e264525dcdada751f98143a3','2010-05-05 00:00:00','2021-05-19 01:44:19',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,22,'service.enableDegrade','SERVICE_SEATA_GROUP','','false','68934a3e9455fa72420237eb05902327','2010-05-05 00:00:00','2021-05-19 01:44:19',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,23,'service.disableGlobalTransaction','SERVICE_SEATA_GROUP','','false','68934a3e9455fa72420237eb05902327','2010-05-05 00:00:00','2021-05-19 01:44:20',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,24,'client.rm.asyncCommitBufferLimit','SERVICE_SEATA_GROUP','','10000','b7a782741f667201b54880c925faec4b','2010-05-05 00:00:00','2021-05-19 01:44:20',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,25,'client.rm.lock.retryInterval','SERVICE_SEATA_GROUP','','10','d3d9446802a44259755d38e6d163e820','2010-05-05 00:00:00','2021-05-19 01:44:20',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,26,'client.rm.lock.retryTimes','SERVICE_SEATA_GROUP','','30','34173cb38f07f89ddbebc2ac9128303f','2010-05-05 00:00:00','2021-05-19 01:44:20',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,27,'client.rm.lock.retryPolicyBranchRollbackOnConflict','SERVICE_SEATA_GROUP','','true','b326b5062b2f0e69046810717534cb09','2010-05-05 00:00:00','2021-05-19 01:44:20',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,28,'client.rm.reportRetryCount','SERVICE_SEATA_GROUP','','5','e4da3b7fbbce2345d7772b0674a318d5','2010-05-05 00:00:00','2021-05-19 01:44:21',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,29,'client.rm.tableMetaCheckEnable','SERVICE_SEATA_GROUP','','false','68934a3e9455fa72420237eb05902327','2010-05-05 00:00:00','2021-05-19 01:44:21',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,30,'client.rm.tableMetaCheckerInterval','SERVICE_SEATA_GROUP','','60000','2b4226dd7ed6eb2d419b881f3ae9c97c','2010-05-05 00:00:00','2021-05-19 01:44:21',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,31,'client.rm.sqlParserType','SERVICE_SEATA_GROUP','','druid','3d650fb8a5df01600281d48c47c9fa60','2010-05-05 00:00:00','2021-05-19 01:44:21',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,32,'client.rm.reportSuccessEnable','SERVICE_SEATA_GROUP','','false','68934a3e9455fa72420237eb05902327','2010-05-05 00:00:00','2021-05-19 01:44:22',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,33,'client.rm.sagaBranchRegisterEnable','SERVICE_SEATA_GROUP','','false','68934a3e9455fa72420237eb05902327','2010-05-05 00:00:00','2021-05-19 01:44:22',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,34,'client.tm.commitRetryCount','SERVICE_SEATA_GROUP','','5','e4da3b7fbbce2345d7772b0674a318d5','2010-05-05 00:00:00','2021-05-19 01:44:22',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,35,'client.tm.rollbackRetryCount','SERVICE_SEATA_GROUP','','5','e4da3b7fbbce2345d7772b0674a318d5','2010-05-05 00:00:00','2021-05-19 01:44:22',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,36,'client.tm.defaultGlobalTransactionTimeout','SERVICE_SEATA_GROUP','','60000','2b4226dd7ed6eb2d419b881f3ae9c97c','2010-05-05 00:00:00','2021-05-19 01:44:23',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,37,'client.tm.degradeCheck','SERVICE_SEATA_GROUP','','false','68934a3e9455fa72420237eb05902327','2010-05-05 00:00:00','2021-05-19 01:44:23',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,38,'client.tm.degradeCheckAllowTimes','SERVICE_SEATA_GROUP','','10','d3d9446802a44259755d38e6d163e820','2010-05-05 00:00:00','2021-05-19 01:44:23',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,39,'client.tm.degradeCheckPeriod','SERVICE_SEATA_GROUP','','2000','08f90c1a417155361a5c4b8d297e0d78','2010-05-05 00:00:00','2021-05-19 01:44:23',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,40,'store.mode','SERVICE_SEATA_GROUP','','db','d77d5e503ad1439f585ac494268b351b','2010-05-05 00:00:00','2021-05-19 01:44:23',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,41,'store.file.dir','SERVICE_SEATA_GROUP','','file_store/data','6a8dec07c44c33a8a9247cba5710bbb2','2010-05-05 00:00:00','2021-05-19 01:44:24',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,42,'store.file.maxBranchSessionSize','SERVICE_SEATA_GROUP','','16384','c76fe1d8e08462434d800487585be217','2010-05-05 00:00:00','2021-05-19 01:44:24',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,43,'store.file.maxGlobalSessionSize','SERVICE_SEATA_GROUP','','512','10a7cdd970fe135cf4f7bb55c0e3b59f','2010-05-05 00:00:00','2021-05-19 01:44:24',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,44,'store.file.fileWriteBufferCacheSize','SERVICE_SEATA_GROUP','','16384','c76fe1d8e08462434d800487585be217','2010-05-05 00:00:00','2021-05-19 01:44:25',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,45,'store.file.flushDiskMode','SERVICE_SEATA_GROUP','','async','0df93e34273b367bb63bad28c94c78d5','2010-05-05 00:00:00','2021-05-19 01:44:25',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,46,'store.file.sessionReloadReadSize','SERVICE_SEATA_GROUP','','100','f899139df5e1059396431415e770c6dd','2010-05-05 00:00:00','2021-05-19 01:44:25',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,47,'store.db.datasource','SERVICE_SEATA_GROUP','','druid','3d650fb8a5df01600281d48c47c9fa60','2010-05-05 00:00:00','2021-05-19 01:44:25',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,48,'store.db.dbType','SERVICE_SEATA_GROUP','','mysql','81c3b080dad537de7e10e0987a4bf52e','2010-05-05 00:00:00','2021-05-19 01:44:25',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,49,'store.db.driverClassName','SERVICE_SEATA_GROUP','','com.mysql.jdbc.Driver','683cf0c3a5a56cec94dfac94ca16d760','2010-05-05 00:00:00','2021-05-19 01:44:26',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,50,'store.db.url','SERVICE_SEATA_GROUP','','jdbc:mysql://127.0.0.1:3306/seata?useUnicode=true&rewriteBatchedStatements=true','030ea5ff5c2ef043adf9826c70570b0b','2010-05-05 00:00:00','2021-05-19 01:44:26',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,51,'store.db.user','SERVICE_SEATA_GROUP','','root','63a9f0ea7bb98050796b649e85481845','2010-05-05 00:00:00','2021-05-19 01:44:26',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,52,'store.db.password','SERVICE_SEATA_GROUP','','cmmplb','a63c8f5c97d01ba54853d329f2b2dc0f','2010-05-05 00:00:00','2021-05-19 01:44:26',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,53,'store.db.minConn','SERVICE_SEATA_GROUP','','5','e4da3b7fbbce2345d7772b0674a318d5','2010-05-05 00:00:00','2021-05-19 01:44:26',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,54,'store.db.maxConn','SERVICE_SEATA_GROUP','','30','34173cb38f07f89ddbebc2ac9128303f','2010-05-05 00:00:00','2021-05-19 01:44:27',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,55,'store.db.globalTable','SERVICE_SEATA_GROUP','','global_table','8b28fb6bb4c4f984df2709381f8eba2b','2010-05-05 00:00:00','2021-05-19 01:44:27',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,56,'store.db.branchTable','SERVICE_SEATA_GROUP','','branch_table','54bcdac38cf62e103fe115bcf46a660c','2010-05-05 00:00:00','2021-05-19 01:44:27',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,57,'store.db.queryLimit','SERVICE_SEATA_GROUP','','100','f899139df5e1059396431415e770c6dd','2010-05-05 00:00:00','2021-05-19 01:44:27',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,58,'store.db.lockTable','SERVICE_SEATA_GROUP','','lock_table','55e0cae3b6dc6696b768db90098b8f2f','2010-05-05 00:00:00','2021-05-19 01:44:27',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,59,'store.db.maxWait','SERVICE_SEATA_GROUP','','5000','a35fe7f7fe8217b4369a0af4244d1fca','2010-05-05 00:00:00','2021-05-19 01:44:28',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,60,'store.redis.mode','SERVICE_SEATA_GROUP','','single','dd5c07036f2975ff4bce568b6511d3bc','2010-05-05 00:00:00','2021-05-19 01:44:28',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,61,'store.redis.single.host','SERVICE_SEATA_GROUP','','127.0.0.1','f528764d624db129b32c21fbca0cb8d6','2010-05-05 00:00:00','2021-05-19 01:44:28',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,62,'store.redis.single.port','SERVICE_SEATA_GROUP','','6379','92c3b916311a5517d9290576e3ea37ad','2010-05-05 00:00:00','2021-05-19 01:44:28',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,63,'store.redis.maxConn','SERVICE_SEATA_GROUP','','10','d3d9446802a44259755d38e6d163e820','2010-05-05 00:00:00','2021-05-19 01:44:29',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,64,'store.redis.minConn','SERVICE_SEATA_GROUP','','1','c4ca4238a0b923820dcc509a6f75849b','2010-05-05 00:00:00','2021-05-19 01:44:29',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,65,'store.redis.maxTotal','SERVICE_SEATA_GROUP','','100','f899139df5e1059396431415e770c6dd','2010-05-05 00:00:00','2021-05-19 01:44:29',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,66,'store.redis.database','SERVICE_SEATA_GROUP','','0','cfcd208495d565ef66e7dff9f98764da','2010-05-05 00:00:00','2021-05-19 01:44:30',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,67,'store.redis.queryLimit','SERVICE_SEATA_GROUP','','100','f899139df5e1059396431415e770c6dd','2010-05-05 00:00:00','2021-05-19 01:44:30',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,68,'server.recovery.committingRetryPeriod','SERVICE_SEATA_GROUP','','1000','a9b7ba70783b617e9998dc4dd82eb3c5','2010-05-05 00:00:00','2021-05-19 01:44:30',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,69,'server.recovery.asynCommittingRetryPeriod','SERVICE_SEATA_GROUP','','1000','a9b7ba70783b617e9998dc4dd82eb3c5','2010-05-05 00:00:00','2021-05-19 01:44:31',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,70,'server.recovery.rollbackingRetryPeriod','SERVICE_SEATA_GROUP','','1000','a9b7ba70783b617e9998dc4dd82eb3c5','2010-05-05 00:00:00','2021-05-19 01:44:31',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,71,'server.recovery.timeoutRetryPeriod','SERVICE_SEATA_GROUP','','1000','a9b7ba70783b617e9998dc4dd82eb3c5','2010-05-05 00:00:00','2021-05-19 01:44:31',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,72,'server.maxCommitRetryTimeout','SERVICE_SEATA_GROUP','','-1','6bb61e3b7bce0931da574d19d1d82c88','2010-05-05 00:00:00','2021-05-19 01:44:31',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,73,'server.maxRollbackRetryTimeout','SERVICE_SEATA_GROUP','','-1','6bb61e3b7bce0931da574d19d1d82c88','2010-05-05 00:00:00','2021-05-19 01:44:31',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,74,'server.rollbackRetryTimeoutUnlockEnable','SERVICE_SEATA_GROUP','','false','68934a3e9455fa72420237eb05902327','2010-05-05 00:00:00','2021-05-19 01:44:32',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,75,'client.undo.dataValidation','SERVICE_SEATA_GROUP','','true','b326b5062b2f0e69046810717534cb09','2010-05-05 00:00:00','2021-05-19 01:44:32',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,76,'client.undo.logSerialization','SERVICE_SEATA_GROUP','','jackson','b41779690b83f182acc67d6388c7bac9','2010-05-05 00:00:00','2021-05-19 01:44:32',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,77,'client.undo.onlyCareUpdateColumns','SERVICE_SEATA_GROUP','','true','b326b5062b2f0e69046810717534cb09','2010-05-05 00:00:00','2021-05-19 01:44:32',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,78,'server.undo.logSaveDays','SERVICE_SEATA_GROUP','','7','8f14e45fceea167a5a36dedd4bea2543','2010-05-05 00:00:00','2021-05-19 01:44:33',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,79,'server.undo.logDeletePeriod','SERVICE_SEATA_GROUP','','86400000','f4c122804fe9076cb2710f55c3c6e346','2010-05-05 00:00:00','2021-05-19 01:44:33',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,80,'client.undo.logTable','SERVICE_SEATA_GROUP','','undo_log','2842d229c24afe9e61437135e8306614','2010-05-05 00:00:00','2021-05-19 01:44:33',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,81,'client.undo.compress.enable','SERVICE_SEATA_GROUP','','true','b326b5062b2f0e69046810717534cb09','2010-05-05 00:00:00','2021-05-19 01:44:33',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,82,'client.undo.compress.type','SERVICE_SEATA_GROUP','','zip','adcdbd79a8d84175c229b192aadc02f2','2010-05-05 00:00:00','2021-05-19 01:44:33',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,83,'client.undo.compress.threshold','SERVICE_SEATA_GROUP','','64k','bd44a6458bdbff0b5cac721ba361f035','2010-05-05 00:00:00','2021-05-19 01:44:34',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,84,'log.exceptionRate','SERVICE_SEATA_GROUP','','100','f899139df5e1059396431415e770c6dd','2010-05-05 00:00:00','2021-05-19 01:44:34',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,85,'transport.serialization','SERVICE_SEATA_GROUP','','seata','b943081c423b9a5416a706524ee05d40','2010-05-05 00:00:00','2021-05-19 01:44:34',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,86,'transport.compressor','SERVICE_SEATA_GROUP','','none','334c4a4c42fdb79d7ebc3e73b517e6f8','2010-05-05 00:00:00','2021-05-19 01:44:34',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,87,'metrics.enabled','SERVICE_SEATA_GROUP','','false','68934a3e9455fa72420237eb05902327','2010-05-05 00:00:00','2021-05-19 01:44:35',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,88,'metrics.registryType','SERVICE_SEATA_GROUP','','compact','7cf74ca49c304df8150205fc915cd465','2010-05-05 00:00:00','2021-05-19 01:44:35',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,89,'metrics.exporterList','SERVICE_SEATA_GROUP','','prometheus','e4f00638b8a10e6994e67af2f832d51c','2010-05-05 00:00:00','2021-05-19 01:44:35',NULL,'0:0:0:0:0:0:0:1','I','service_seata'),
(0,90,'metrics.exporterPrometheusPort','SERVICE_SEATA_GROUP','','9898','7b9dc501afe4ee11c56a4831e20cee71','2010-05-05 00:00:00','2021-05-19 01:44:35',NULL,'0:0:0:0:0:0:0:1','I','service_seata');

/*Table structure for table `permissions` */

CREATE TABLE `permissions` (
  `role` varchar(50) NOT NULL,
  `resource` varchar(512) NOT NULL,
  `action` varchar(8) NOT NULL,
  UNIQUE KEY `uk_role_permission` (`role`,`resource`,`action`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `permissions` */

/*Table structure for table `roles` */

CREATE TABLE `roles` (
  `username` varchar(50) NOT NULL,
  `role` varchar(50) NOT NULL,
  UNIQUE KEY `uk_username_role` (`username`,`role`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `roles` */

insert  into `roles`(`username`,`role`) values 
('nacos','ROLE_ADMIN');

/*Table structure for table `tenant_capacity` */

CREATE TABLE `tenant_capacity` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `tenant_id` varchar(128) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT 'Tenant ID',
  `quota` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '配额，0表示使用默认值',
  `usage` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '使用量',
  `max_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '聚合子配置最大个数',
  `max_aggr_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='租户容量信息表';

/*Data for the table `tenant_capacity` */

/*Table structure for table `tenant_info` */

CREATE TABLE `tenant_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `kp` varchar(128) COLLATE utf8_bin NOT NULL COMMENT 'kp',
  `tenant_id` varchar(128) COLLATE utf8_bin DEFAULT '' COMMENT 'tenant_id',
  `tenant_name` varchar(128) COLLATE utf8_bin DEFAULT '' COMMENT 'tenant_name',
  `tenant_desc` varchar(256) COLLATE utf8_bin DEFAULT NULL COMMENT 'tenant_desc',
  `create_source` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT 'create_source',
  `gmt_create` bigint(20) NOT NULL COMMENT '创建时间',
  `gmt_modified` bigint(20) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tenant_info_kptenantid` (`kp`,`tenant_id`),
  KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='tenant_info';

/*Data for the table `tenant_info` */

insert  into `tenant_info`(`id`,`kp`,`tenant_id`,`tenant_name`,`tenant_desc`,`create_source`,`gmt_create`,`gmt_modified`) values 
(2,'1','service_seata','service_seata','service_seata','nacos',1621234165297,1621234165297),
(3,'1','service_dev','service_dev','service_dev','nacos',1621234188469,1621234188469);

/*Table structure for table `users` */

CREATE TABLE `users` (
  `username` varchar(50) NOT NULL,
  `password` varchar(500) NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `users` */

insert  into `users`(`username`,`password`,`enabled`) values 
('nacos','$2a$10$PukZ8blhX6YGlhbec3MFcOylOR8OUlzLsHw5F73IWKfh/roghgBE6',1);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
