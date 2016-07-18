package com.dessert.system.service.resources.service.impl;

import com.dessert.sys.common.bean.Page;
import com.dessert.sys.common.dao.DaoClient;
import com.dessert.system.service.resources.service.ResourcesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by admin-ding on 2016/6/30.
 */

@Service
public class ResourcesServiceImpl implements ResourcesService {

    @Autowired
    private DaoClient daoClient ;

    @Override
    public Page findResourcesPage(Map<String, Object> params) {
        return daoClient.selectPage("com.dessert.resources.selectResources",params);
    }

    @Override
    public boolean addResources(Map<String, Object> params) {
        return false;
    }

    @Override
    public boolean updateResources(Map<String, Object> params) {
        return false;
    }

    @Override
    public boolean deleteResources(Map<String, Object> params) {
        return false;
    }

    @Override
    public List<Map<String, Object>> findResources(Map<String, Object> params) {
        return daoClient.selectList("com.dessert.resources.selectResources",params);
    }
}
