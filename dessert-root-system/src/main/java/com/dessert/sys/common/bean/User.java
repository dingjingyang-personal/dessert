package com.dessert.sys.common.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ding-Admin on 2016/4/22.
 */
public class User implements Serializable{

    private String userno;
    private String userName;
    private String loginName;
    private String status;
    private String birthday;
    private int sex;
    private String tel;
    private List<String> userMenus;

    public String getUserno() {
        return userno;
    }

    public void setUserno(String userno) {
        this.userno = userno;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public List<String> getUserMenus() {
        return userMenus;
    }

    public void setUserMenus(List<String> userMenus) {
        this.userMenus = userMenus;
    }
}
