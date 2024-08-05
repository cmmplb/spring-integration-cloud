/**
  系统认证客户端信息表
 */
CREATE TABLE `sys_oauth_client_details`
(
    `id`                      bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `client_id`               varchar(32)   DEFAULT NULL COMMENT '客户端ID',
    `resource_ids`            varchar(256)  DEFAULT NULL COMMENT '资源列表',
    `client_secret`           varchar(256)  DEFAULT NULL COMMENT '客户端密钥',
    `scope`                   varchar(256)  DEFAULT NULL COMMENT '域(server,all)',
    `authorized_grant_types`  varchar(256)  DEFAULT NULL COMMENT '认证类型(password,refresh_token,mobile,client_credentials)',
    `web_server_redirect_uri` varchar(256)  DEFAULT NULL COMMENT '重定向地址',
    `authorities`             varchar(256)  DEFAULT NULL COMMENT '角色列表',
    `access_token_validity`   int(11)       DEFAULT NULL COMMENT 'token有效期',
    `refresh_token_validity`  int(11)       DEFAULT NULL COMMENT '刷新令牌有效期',
    `additional_information`  varchar(4096) DEFAULT NULL COMMENT '令牌扩展字段JSON',
    `auto_approve`            varchar(4)    DEFAULT NULL COMMENT '是否自动放行(true)',
    PRIMARY KEY (`id`),
    UNIQUE KEY `UQ_CLIENT_ID` (`client_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = DYNAMIC COMMENT ='系统认证客户端信息表';

/**
  用户信息表
 */
DROP TABLE IF EXISTS `user`;
CREATE TABLE `sys_user`
(
    `id`          bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键',
    `username`    varchar(32) NOT NULL COMMENT '用户名',
    `password`    varchar(32) NOT NULL COMMENT '密码(MD5加密)',
    `mobile`      varchar(32) null comment '手机号',
    `create_time` datetime    NOT NULL COMMENT '创建时间',
    `update_time` datetime    NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='系统用户表';

CREATE TABLE `sys_user_role`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id`     bigint(20) NOT NULL COMMENT '用户id',
    `role_id`     bigint(20) NOT NULL COMMENT '角色id',
    `create_time` datetime   NOT NULL COMMENT '创建时间',
    `create_by`   bigint(20) NOT NULL COMMENT '创建人',
    PRIMARY KEY (`id`),
    UNIQUE KEY `UQ_USER_ID_ROLE_ID` (`user_id`, `role_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='系统用户角色关联表';

CREATE TABLE `sys_role`
(
    `id`          bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name`        varchar(32) NOT NULL COMMENT '名称',
    `code`        varchar(32) NOT NULL COMMENT '编码',
    `create_time` datetime    NOT NULL COMMENT '创建时间',
    `update_time` datetime    NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='系统角色表';

CREATE TABLE `sys_role_permission`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `role_id`       bigint(20) NOT NULL COMMENT '角色id',
    `permission_id` bigint(20) NOT NULL COMMENT '权限id',
    `create_time`   datetime   NOT NULL COMMENT '创建时间',
    `create_by`     bigint(20) NOT NULL COMMENT '创建人',
    PRIMARY KEY (`id`),
    UNIQUE KEY `UQ_MENU_ID_ROLE_ID` (`permission_id`, `role_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='系统角色跟菜单关联表';

CREATE TABLE `sys_permission`
(
    `id`          bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '主键',
    `code`        varchar(128) NOT NULL COMMENT '编码',
    `create_time` datetime     NOT NULL COMMENT '创建时间',
    `update_time` datetime     NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='权限表';