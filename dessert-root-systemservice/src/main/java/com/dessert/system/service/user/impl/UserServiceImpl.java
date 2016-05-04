package com.dessert.system.service.user.impl;

import com.dessert.system.service.user.UserService;
import com.dessert.sys.common.dao.DaoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ding-Admin on 2016/4/21.
 */

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private DaoClient daoClient;


    @Override
    public Map<String, Object> getUserMap() {

        return daoClient.selectMap("com.dessert.user.getUser",new HashMap<String, Object>());
    }
}
