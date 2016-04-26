package com.dessert.sys.remote.service.impl;

import com.dessert.sys.remote.service.TestRemoteService;

import java.util.HashMap;
import java.util.Map;

public class TestRemoteServiceImpl implements TestRemoteService {

    @Override
    public Map<String, Object> test(Map<String, Object> params) {
        return new HashMap<String, Object>();
    }

}
