package com.dessert.system.service.resources.service.impl;

import com.dessert.sys.common.bean.Page;
import com.dessert.sys.common.dao.DaoClient;
import com.dessert.sys.common.tool.SysToolHelper;
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
    private DaoClient daoClient;

    @Override
    public Page findResourcesPage(Map<String, Object> params) {
        return daoClient.selectPage("com.dessert.resources.selectResources", params);
    }

    @Override
    public boolean addResources(Map<String, Object> params) {

        String parentidStr = SysToolHelper.getMapValue(params, "parentid");
        if (parentidStr == null || parentidStr.equals("")) {
            parentidStr = "000000000";
        }else {
            int parentid = Integer.parseInt(parentidStr);
            if(parentid<999){
                return false;
            }
        }

        int menuid = getMenuID(parentidStr);

        params.put("menuid", menuid);
        daoClient.update("", params);
        return daoClient.update("", params) > 0 ? true : false;
    }

    /**
     * 获取菜单ID
     *
     * @param parentidStr
     * @return
     */
    private int getMenuID(String parentidStr) {

        String levelA = parentidStr.substring(0,3);
        String levelB = parentidStr.substring(3,6);
        String levelC = parentidStr.substring(6,9);

        int parentid = Integer.parseInt(parentidStr);

        if (999 < parentid & parentid < 0) {
            levelC = SysToolHelper.readSeqBySeqKeyAndOwner("MENU-C-LEVEL", "MENU-C-LEVEL", false);
        } else if (999 < parentid & parentid < 999000) {
            levelB = SysToolHelper.readSeqBySeqKeyAndOwner("MENU-B-LEVEL", "MENU-B-LEVEL", false);
        } else {
            levelA = SysToolHelper.readSeqBySeqKeyAndOwner("MENU-A-LEVEL", "MENU-A-LEVEL", false);
        }

        String menuidStr = levelA + levelB + levelC;

        return Integer.parseInt(menuidStr);
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
        return daoClient.selectList("com.dessert.resources.selectResources", params);
    }

    @Override
    public Map<String, Object> findResource(Map<String, Object> params) {
        return daoClient.selectMap("com.dessert.resources.selectResources",params);
    }
}
