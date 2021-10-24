DROP DATABASE IF EXISTS `spring_integration`;

CREATE DATABASE `spring_integration` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

USE `spring_integration`;

/*用户信息表*/
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    `id`          bigint     NOT NULL AUTO_INCREMENT COMMENT '主键',
    `tenant_id`   bigint(20) NOT NULL DEFAULT '0' COMMENT '租户id',
    `name`        varchar(32)         DEFAULT NULL COMMENT '用户名',
    `sex`         tinyint             DEFAULT NULL COMMENT '性别:0-女;1-男',
    `mobile`      varchar(16)         DEFAULT NULL COMMENT '手机号',
    `status`      tinyint             DEFAULT '0' COMMENT '用户状态:0-正常;1-禁用',
    `version`     int(11)    NOT NULL DEFAULT '0' COMMENT '乐观锁版本号',
    `create_time` datetime   NOT NULL COMMENT '创建时间',
    `update_time` datetime            DEFAULT NULL COMMENT '更新时间',
    `deleted`     tinyint    NOT NULL DEFAULT '0' COMMENT '逻辑删除:0-正常;1-删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户信息表';

INSERT INTO `user` (`tenant_id`,
                    `name`,
                    `sex`,
                    `mobile`,
                    `status`,
                    `create_time`,
                    `update_time`,
                    `version`,
                    `deleted`)
VALUES (1,
        '小明',
        1,
        '13999999999',
        0,
        NOW(),
        NOW(),
        1,
        0),
       (2,
        '小芳',
        0,
        '14999999999',
        1,
        NOW(),
        NOW(),
        1,
        0);

