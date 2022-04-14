package com.cmmplb.seata.eureka.service.business.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author penglibo
 * @date 2021-05-13 15:48:39
 * @since jdk 1.8
 */

@Data
@Table(name="business_log")
public class Log implements Serializable {

    private static final long serialVersionUID = 4759305116360365437L;

    @Id
    @Column(name = "id")
    private Integer id;//

    @Column(name = "create_time")
    private Date createTime;//

    @Column(name = "content")
    private String content;//

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

