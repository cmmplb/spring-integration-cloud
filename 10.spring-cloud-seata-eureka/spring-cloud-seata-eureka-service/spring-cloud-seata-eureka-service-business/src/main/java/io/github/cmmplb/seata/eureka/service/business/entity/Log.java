package io.github.cmmplb.seata.eureka.service.business.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author penglibo
 * @date 2021-05-13 15:48:39
 * @since jdk 1.8
 */

@Data
public class Log implements Serializable {

    private static final long serialVersionUID = 4759305116360365437L;

    private Integer id;

    private Date createTime;

    private String content;
}

