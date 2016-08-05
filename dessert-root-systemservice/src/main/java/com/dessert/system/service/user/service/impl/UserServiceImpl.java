package com.dessert.system.service.user.service.impl;

import com.dessert.sys.common.bean.Page;
import com.dessert.sys.common.bean.User;
import com.dessert.sys.common.dao.DaoClient;
import com.dessert.sys.common.enm.DateStyle;
import com.dessert.sys.common.tool.DateUtil;
import com.dessert.sys.common.tool.MD5Util;
import com.dessert.sys.common.tool.SysToolHelper;
import com.dessert.system.service.user.service.UserService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by ding-Admin on 2016/4/21.
 */

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private DaoClient daoClient;


    @Override
    public Map<String, Object> findUserMap(Map<String, Object> params) {

        return daoClient.selectMap("com.dessert.user.getUser", params);
    }


    @Override
    public Set<String> findPermissions(String username) {
        return null;
    }

    @Override
    public User findUser(Map<String, Object> userAccunt) {
        Map<String, Object> userMap = findUserMap(userAccunt);
        if (userMap == null || userMap.isEmpty())
            return null;
        User user = new User();

        user.setUserid(SysToolHelper.getMapValue(userMap, "userid"));
        user.setUserno(SysToolHelper.getMapValue(userMap, "userno"));
        user.setUsername(SysToolHelper.getMapValue(userMap, "username"));
        user.setLoginname(SysToolHelper.getMapValue(userMap, "loginname"));
        user.setEmail(SysToolHelper.getMapValue(userMap, "email"));
        user.setUserpwd(SysToolHelper.getMapValue(userMap, "userpwd"));
        user.setSex(Integer.parseInt(SysToolHelper.getMapValue(userMap, "sex")));
        user.setTel(SysToolHelper.getMapValue(userMap, "tel"));
        user.setBirthday(SysToolHelper.getMapValue(userMap, "birthday"));
        user.setStatus(Integer.parseInt(SysToolHelper.getMapValue(userMap, "status")));
        user.setCreatedate(SysToolHelper.getMapValue(userMap, "createdate"));
        user.setUpdate(SysToolHelper.getMapValue(userMap, "update"));
        user.setIp(SysToolHelper.getMapValue(userMap, "ip"));
        user.setMac(SysToolHelper.getMapValue(userMap, "mac"));
        user.setState(Integer.parseInt(SysToolHelper.getMapValue(userMap, "state")));
        user.setActicode(SysToolHelper.getMapValue(userMap, "acticode"));
        user.setActivationdate(SysToolHelper.getMapValue(userMap, "activationdate"));

        return user;
    }

    @Override
    public Page findUsersPage(Map<String, Object> params) {
        return daoClient.selectPage("com.dessert.user.getUser", params);
    }

    @Override
    public boolean addUser(Map<String, Object> params) {
        if (!SysToolHelper.isExists(params, "username", "userpwd", "ip", "mac", "email")) {
            return false;
        }
        String userid = SysToolHelper.readSeqBySeqKeyAndOwner("USER", "USER", true);
        params.put("userid", DateUtil.getDateForFormat(new Date(), DateStyle.YYYY_MM_DD_HH_MM_SS_SSS.getValue()) + userid);


        String userpwd = MD5Util.encode2hex(SysToolHelper.getMapValue(params, "userpwd"));
        params.put("userpwd", userpwd);
        params.put("loginname", "未定义");


        return daoClient.update("com.dessert.user.adduser", params) > 0;
    }


    @Override
    public boolean updateUser(Map<String, Object> params) {
        if (!SysToolHelper.isExists(params, "userid")) {
            return false;
        }
        return daoClient.update("com.dessert.user.updateuser", params) > 0;
    }

    @Override
    public boolean deleteUser(Map<String, Object> params) {
        if (!SysToolHelper.isExists(params, "userid")) {
            return false;
        }
        return daoClient.update("com.dessert.user.deleteByPrimaryKey", params) > 0;
    }

    @Override
    public List<Map<String, Object>> findRoles(Map<String, Object> params) {
        if (!SysToolHelper.isExists(params, "userid")) {
            return null;
        }

        return daoClient.selectList("com.dessert.user.getUserRoles", params);
    }

    @Override
    public Set<String> findRoles(String username) {
        return null;
    }

    @Override
    public boolean addAssigningRoles(Map<String, Object> params) {
        if (SysToolHelper.isExists(params, "userid")) {
            String userid = SysToolHelper.getMapValue(params, "userid");
            String roleidsStr = (String) params.get("roleids");
            if(roleidsStr==null||roleidsStr.equals("")){
                return daoClient.update("com.dessert.system_user_role.deleteUsermenus", params)>0;
            }else {

                String[] roleidsArr = roleidsStr.split(",");
                List<String> roleidlist = Arrays.asList(roleidsArr);
                List<Map<String, Object>> roleidMapList = Lists.newArrayList();

                for (String roleid : roleidlist) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("userroleid", SysToolHelper.getUuid());
                    map.put("userid", userid);
                    map.put("roleid", roleid);
                    roleidMapList.add(map);
                }
                daoClient.update("com.dessert.system_user_role.deleteUsermenus", params);
                return daoClient.batchUpdate("com.dessert.system_user_role.insert", roleidMapList);
            }
        }
        return false;
    }


}
