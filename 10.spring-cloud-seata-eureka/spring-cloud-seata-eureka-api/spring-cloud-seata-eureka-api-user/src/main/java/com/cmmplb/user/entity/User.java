package com.cmmplb.user.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author penglibo
 * @date 2021-05-13 15:49:15
 * @since jdk 1.8
 */

@Table(name="user_user")
public class User implements Serializable {

    private static final long serialVersionUID = -5386197839302824675L;
    @Id
    @Column(name = "account")
    private String account;//

    @Column(name = "money")
    private Integer money;//

    @Column(name = "name")
    private String name;//


    //get方法
    public String getAccount() {
        return account;
    }

    //set方法
    public void setAccount(String account) {
        this.account = account;
    }
    //get方法
    public Integer getMoney() {
        return money;
    }

    //set方法
    public void setMoney(Integer money) {
        this.money = money;
    }
    //get方法
    public String getName() {
        return name;
    }

    //set方法
    public void setName(String name) {
        this.name = name;
    }


}
