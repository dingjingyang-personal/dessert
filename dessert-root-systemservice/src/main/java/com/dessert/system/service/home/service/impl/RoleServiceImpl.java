package com.dessert.system.service.home.service.impl;

import com.dessert.sys.common.bean.Page;
import com.dessert.sys.common.dao.DaoClient;
import com.dessert.sys.common.tool.SysToolHelper;
import com.dessert.system.service.home.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by admin-ding on 2016/6/22.
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private DaoClient daoClient;

    @Override
    public Page findRolesPage(Map<String, Object> params) {
        return daoClient.selectPage("com.dessert.role.selectRoles",params);
    }

    @Override
    public List<Map<String, Object>> findRoles(Map<String, Object> params) {

        return daoClient.selectList("com.dessert.role.selectRoles",params);
    }


    @Override
    public Map<String, Object> findRole(Map<String, Object> params) {

        return daoClient.selectMap("com.dessert.role.selectRoles",params);
    }

    @Override
    public boolean addRole(Map<String, Object> params) {
        return daoClient.update("",params)>0;
    }

    @Override
    public boolean updateRole(Map<String, Object> params) {
        if(SysToolHelper.isExists(params,"roleid")){
            return false;
        }
        return daoClient.update("",params)>0;
    }

    @Override
    public boolean deleteRole(Map<String, Object> params) {
        if(SysToolHelper.isExists(params,"roleid")){
            return false;
        }
        return daoClient.update("",params)>0;
    }
}
