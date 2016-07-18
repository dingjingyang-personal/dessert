package com.dessert.system.service.user.service.impl;

import com.dessert.sys.common.bean.Page;
import com.dessert.sys.common.bean.User;
import com.dessert.sys.common.dao.DaoClient;
import com.dessert.sys.common.tool.SysToolHelper;
import com.dessert.system.service.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

/**
 * Created by ding-Admin on 2016/4/21.
 */

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private DaoClient daoClient;


    @Override
    public Map<String, Object> getUserMap(Map<String, Object> params) {

        return daoClient.selectMap("com.dessert.user.getUser", params);
    }

    @Override
    public boolean updateUser(Map<String, Object> params) {
        if (!SysToolHelper.isExists(params, "userid")) {
            return false;
        }
        return daoClient.update("com.dessert.user.updateuser",params)>0?true : false;
    }

    @Override
    public Set<String> findRoles(String username) {
        return null;
    }

    @Override
    public Set<String> findPermissions(String username) {
        return null;
    }

    @Override
    public User findUser(Map<String, Object> userAccunt) {
        Map<String,Object> userMap = getUserMap(userAccunt);
        if(userMap==null||userMap.isEmpty())
            return null;
        User user = new User();

        user.setUserid        (SysToolHelper.getMapValue(userMap,"userid"));
        user.setUserno        (SysToolHelper.getMapValue(userMap,"userno"));
        user.setUsername      (SysToolHelper.getMapValue(userMap,"username"));
        user.setLoginname     (SysToolHelper.getMapValue(userMap,"loginname"));
        user.setEmail         (SysToolHelper.getMapValue(userMap,"email"));
        user.setUserpwd       (SysToolHelper.getMapValue(userMap,"userpwd"));
        user.setSex           (Integer.parseInt(SysToolHelper.getMapValue(userMap,"sex")));
        user.setTel           (SysToolHelper.getMapValue(userMap,"tel"));
        user.setBirthday      (SysToolHelper.getMapValue(userMap,"birthday"));
        user.setStatus        (Integer.parseInt(SysToolHelper.getMapValue(userMap,"status")));
        user.setCreatedate    (SysToolHelper.getMapValue(userMap,"createdate"));
        user.setUpdate        (SysToolHelper.getMapValue(userMap,"update"));
        user.setIp            (SysToolHelper.getMapValue(userMap,"ip"));
        user.setMac           (SysToolHelper.getMapValue(userMap,"mac"));
        user.setState         (Integer.parseInt(SysToolHelper.getMapValue(userMap,"state")));
        user.setActicode      (SysToolHelper.getMapValue(userMap,"acticode"));
        user.setActivationdate(SysToolHelper.getMapValue(userMap,"activationdate"));

        return user;
    }

    @Override
    public Page findUsersPage(Map<String, Object> params) {
        return daoClient.selectPage("com.dessert.user.getUser",params);
    }


}
