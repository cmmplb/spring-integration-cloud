package com.cmmplb.seata.eureka.service.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author penglibo
 * @date 2021-05-13 15:49:15
 * @since jdk 1.8
 */

/**
 * 系统用户表
 */
@Data
@TableName(value = "sys_user")
public class User implements Serializable {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    @TableField(value = "username")
    private String username;

    /**
     * 金额
     */
    @TableField(value = "money")
    private Integer money;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 删除标识:0-否;1-是
     */
    @TableField(value = "deleted")
    private Byte deleted;

    private static final long serialVersionUID = 1L;

    public static final String COL_ID = "id";

    public static final String COL_USERNAME = "username";

    public static final String COL_MONEY = "money";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_TIME = "update_time";

    public static final String COL_DELETED = "deleted";
}
