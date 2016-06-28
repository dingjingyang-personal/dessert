package com.dessert.sys.remote.service.impl;

import com.dessert.sys.remote.service.TestRemoteService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class TestRemoteServiceImpl implements TestRemoteService {

    @Override
    public Map<String, Object> test(Map<String, Object> params) {
        return new HashMap<String, Object>();
    }

}
