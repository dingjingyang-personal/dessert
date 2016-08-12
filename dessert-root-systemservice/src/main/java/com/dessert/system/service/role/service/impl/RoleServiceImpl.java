package com.dessert.system.service.role.service.impl;

import com.dessert.sys.common.bean.Page;
import com.dessert.sys.common.dao.DaoClient;
import com.dessert.sys.common.tool.SysToolHelper;
import com.dessert.system.service.role.service.RoleService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
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

    @Override
    public List<Map<String, Object>> findResourcessByRole(Map<String, Object> params) {
        if(!SysToolHelper.isExists(params,"roleid")){
            return null;
        }
        return daoClient.selectList("com.dessert.role.findResourcessByRole",params);
    }

    @Override
    public boolean addOrDeletePermissions(Map<String, Object> params) {
        if(SysToolHelper.isExists(params,"roleid","checkNodes")){
            String roleid = SysToolHelper.getMapValue(params,"roleid");
            String checkNodes = SysToolHelper.getMapValue(params,"checkNodes");
            String type = SysToolHelper.getMapValue(params,"typeMode");
            if(checkNodes!=null||checkNodes.equals("")){
                String[] checkNodesArr = checkNodes.split(",");
                List<String> checkNodesList = Arrays.asList(checkNodesArr);
                List<Map<String,Object>> nodesList = Lists.newArrayList();
                for(String resourcesid :checkNodesList){
                    Map<String,Object> map = Maps.newHashMap();
                    if(type.equals("add")){
                        map.put("rmlinkid",SysToolHelper.getUuid());
                    }
                    map.put("roleid",roleid);
                    map.put("menuid",resourcesid);
                    nodesList.add(map);
                }
                if(type.equals("add")){
                    return daoClient.batchUpdate("com.dessert.system_role_resMapper.insert",nodesList);
                }else {
                    return daoClient.batchUpdate("com.dessert.system_role_resMapper.delete",nodesList);
                }
            }
            return false;
        }
        return false;
    }


}
