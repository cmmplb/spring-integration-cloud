package io.github.cmmplb.seata.nacos.api.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * @author penglibo
 * @date 2021-05-13 15:48:39
 * @since jdk 1.8
 * 业务日志表
 */

@TableName("business_log")
public class Log implements Serializable {

    private static final long serialVersionUID = 4759305116360365437L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 日志内容
     */
    private Date createTime;

    /**
     * 创建时间
     */
    private String content;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

