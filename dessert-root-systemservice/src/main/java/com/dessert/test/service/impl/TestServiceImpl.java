package com.dessert.test.service.impl;

import com.dessert.sys.common.dao.DaoClient;
import com.dessert.test.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ding-Admin on 2016/4/21.
 */

@Service
public class TestServiceImpl implements TestService {

    @Autowired
    private DaoClient daoClient;


    @Override
    public Map<String, Object> getMap() {
        return daoClient.selectMap("com.dessert.test.getmap",new HashMap<String, Object>());
    }
}
