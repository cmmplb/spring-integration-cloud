DROP DATABASE IF EXISTS `seata_nacos`;

CREATE DATABASE `seata_nacos` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

USE `seata_nacos`;

DROP TABLE IF EXISTS `business_log`;
CREATE TABLE `business_log`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `content`     varchar(255) DEFAULT NULL COMMENT '日志内容',
    `create_time` datetime     DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8 COMMENT ='业务日志表';

DROP TABLE IF EXISTS `user_user`;
CREATE TABLE `user_user`
(
    `id`      bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `account` varchar(255) NOT NULL COMMENT '账号',
    `money`   int(11)      DEFAULT NULL COMMENT '余额',
    `name`    varchar(255) DEFAULT NULL COMMENT '姓名',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8 COMMENT ='用户表';
INSERT INTO `seata_nacos`.`user_user` (`account`, `money`, `name`)
VALUES ('zhangsan', 500, '张三');

/*Table structure for table `undo_log` */
DROP TABLE IF EXISTS `undo_log`;
CREATE TABLE `undo_log`
(
    `id`            bigint(20)   NOT NULL AUTO_INCREMENT,
    `branch_id`     bigint(20)   NOT NULL,
    `xid`           varchar(100) NOT NULL,
    `context`       varchar(128) NOT NULL,
    `rollback_info` longblob     NOT NULL,
    `log_status`    int(11)      NOT NULL,
    `log_created`   datetime     NOT NULL,
    `log_modified`  datetime     NOT NULL,
    `ext`           varchar(100) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `ux_undo_log` (`xid`, `branch_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8 COMMENT ='seata事务回滚';




