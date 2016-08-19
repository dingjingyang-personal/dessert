package com.dessert.sys.common.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


/**
 * Created by ding-Admin on 2016/4/22.
 */
public class User implements Serializable{

    private static final long serialVersionUID = -4301845988718737047L;


    private String  userid;//用户id
    private String  userno;//用户号
    private String  username;//用户名
    private String  loginname;//登录名
    private String  email;//邮件
    private String  userpwd;//密码
    private String  salt;//密码
    private int  sex;//性别
    private String  tel;//电话
    private String  birthday;//生日
    private int  status;//状态
    private String  createdate;//创建时间
    private String  update;//更新时间
    private String  ip;//ip
    private String  mac;//mac地址
    private int  state;//激活状态
    private String  acticode;//激活码
    private String  activationdate;//激活到期时间
    private List<Map<String,Object>> roleList;//角色集合

    public List<Map<String, Object>> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Map<String, Object>> roleList) {
        this.roleList = roleList;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUserno() {
        return userno;
    }

    public void setUserno(String userno) {
        this.userno = userno;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserpwd() {
        return userpwd;
    }

    public void setUserpwd(String userpwd) {
        this.userpwd = userpwd;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
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

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getActicode() {
        return acticode;
    }

    public void setActicode(String acticode) {
        this.acticode = acticode;
    }

    public String getActivationdate() {
        return activationdate;
    }

    public void setActivationdate(String activationdate) {
        this.activationdate = activationdate;
    }
}
