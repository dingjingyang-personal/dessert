package com.dessert.system.service.role.service.impl;

import com.dessert.sys.common.bean.Page;
import com.dessert.sys.common.dao.DaoClient;
import com.dessert.sys.common.tool.SysToolHelper;
import com.dessert.system.service.role.service.RoleService;
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
        return daoClient.selectPage("com.dessert.role.selectRoles", params);
    }

    @Override
    public List<Map<String, Object>> findRoles(Map<String, Object> params) {

        return daoClient.selectList("com.dessert.role.selectRoles", params);
    }


    @Override
    public Map<String, Object> findRole(Map<String, Object> params) {

        return daoClient.selectMap("com.dessert.role.selectRoles", params);
    }

    @Override
    public boolean addRole(Map<String, Object> params) {
        if (!SysToolHelper.isExists(params, "rolename", "roletype", "status")) {
            return false;
        }

        String roleid = SysToolHelper.getUuid();
        params.put("roleid", roleid);
        return daoClient.update("com.dessert.role.insert", params) > 0;
    }

    @Override
    public boolean updateRole(Map<String, Object> params) {
        if (!SysToolHelper.isExists(params, "roleid", "rolename", "roletype", "status")) {
            return false;
        }
        return daoClient.update("com.dessert.role.update", params) > 0;
    }

    @Override
    public boolean deleteRole(Map<String, Object> params) {
        if (!SysToolHelper.isExists(params, "roleid")) {
            return false;
        }
        return daoClient.update("com.dessert.role.deleteByPrimaryKey", params) > 0;
    }
}
