package com.dessert.test.system.service.home.manager.service.impl;

import com.dessert.sys.common.dao.DaoClient;
import com.dessert.test.system.service.home.manager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by ding-Admin on 2016/4/27.
 */

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private DaoClient daoClient;

    @Override
    public Map<String, Object> getUser(Map<String, Object> params) {
        return daoClient.selectMap("",params);
    }
}

