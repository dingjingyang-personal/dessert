package com.dessert.system.service.user.service.impl;

import com.dessert.sys.common.dao.DaoClient;
import com.dessert.sys.common.tool.SysToolHelper;
import com.dessert.system.service.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

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


}
