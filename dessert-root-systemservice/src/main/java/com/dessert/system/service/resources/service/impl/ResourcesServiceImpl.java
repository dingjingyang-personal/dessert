package com.dessert.system.service.resources.service.impl;

import com.dessert.sys.common.bean.Page;
import com.dessert.sys.common.dao.DaoClient;
import com.dessert.sys.common.tool.SysToolHelper;
import com.dessert.system.service.resources.service.ResourcesService;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin-ding on 2016/6/30.
 */

@Service
public class ResourcesServiceImpl implements ResourcesService {

    //菜单序号递增量
    private static final int MENU_ORDER_ADD = 10;

    //层级：三级菜单
    private static final int MENU_LEVEL_THREE = 3;
    //层级：二级菜单
    private static final int MENU_LEVEL_ONE = 1;
    //层级：二级菜单
    private static final int MENU_LEVEL_TWO = 2;

    //序号/层级递增1
    private static final int ADD_ONE = 1;

    //菜单链接
    private static final String MENU_ACTION = "action";

    //菜单链接长度最大限度
    private static final int MENU_ACTION_MAXLENGTH = 300;

    //菜单中文名称长度最大限度
    private static final int MENUNAMEZH_MAXLENGTH = 30;

    //菜单新增标记
    private static final String ADD_MENU_FLAG = "A";


    @Autowired
    private DaoClient daoClient;

    @Override
    public Page findResourcesPage(Map<String, Object> params) {
        return daoClient.selectPage("com.dessert.resources.selectResources", params);
    }

    @Override
    public boolean addResources(Map<String, Object> params) {
        boolean result = true;
        if (checkData(params)) {
            Map<String, Object> supMenuParams = Maps.newHashMap();
            supMenuParams.put("supmenuid", SysToolHelper.getMapValue(params, "supmenuid"));
            //上级菜单信息
            Map<String, Object> supMenuMap = findResource(supMenuParams);
            //子菜单数目
            int menucount = Integer.valueOf(SysToolHelper.getMapValue(params, "menucount"));
            //新增菜单序号
            int menuorder = (menucount + ADD_ONE) * MENU_ORDER_ADD;

            //获取上级菜单ID
            String supmenuid = SysToolHelper.getMapValue(supMenuMap, "menuid", "0").trim();
            //获取上级菜单层级
            Integer supmenulevel = Integer.valueOf(SysToolHelper.getMapValue(supMenuMap, "menulevel", "0").trim());
            if (!(supmenulevel != null && supmenulevel != MENU_LEVEL_THREE)) {
                return false;
            }

            //新增菜单层级
            Integer menulevel = supmenulevel + ADD_ONE;
            //若层级为2，则生成第二层级菜单代码
            if (menulevel == MENU_LEVEL_TWO || menulevel == MENU_LEVEL_ONE) {
                //若不是叶子节点则action为空
                params.put(MENU_ACTION, "");
            } else if (menulevel == MENU_LEVEL_THREE) {//若层级为3，则生成第三层级菜单代码
                //校验以.action结尾
                //若层级为3，则action必传，并且符合校验规则
                if (SysToolHelper.isExists(params, MENU_ACTION)
                        && SysToolHelper.getMapValue(params, MENU_ACTION).length() <= MENU_ACTION_MAXLENGTH) {
                } else {
                    result = false;
                }

            } else {
                result = false;
            }

            if (!result) {
                return false;
            }
            params.put("menuorder", menuorder);
            params.put("menulevel", menulevel);
            params.put("parentid", supmenuid);
            params.put("menuid", SysToolHelper.readSeqBySeqKeyAndOwner("SYSMENU", "MENU", false));
            //插入新建菜单信息
            daoClient.update("com.dessert.resources.insertRole", params);

        } else {
            result = false;
        }

        return result;
    }

    @Override
    public boolean updateResources(Map<String, Object> params) {
        boolean result = true;
        if (SysToolHelper.isExists(params, "menuid")) {
            Map<String, Object> paraMap = new HashMap<String, Object>();
            paraMap.put("menuid", SysToolHelper.getMapValue(params, "menuid"));
            //校验必传字段
            if (!checkData(params)) {
                return false;
            }
            //根据菜单code查询所要修改的菜单信息
            Map<String, Object> menuInfoMap = findResource(paraMap);
            if (menuInfoMap != null && !menuInfoMap.isEmpty()) {
                Map<String, Object> menuMap = menuInfoMap;
                //获取菜单层级
                Integer menulevel = Integer.valueOf(SysToolHelper.getMapValue(menuMap, "menulevel"));
                if (menulevel == MENU_LEVEL_THREE) {

                    //若层级为3，则action必传，并且符合校验规则
                    if (!(SysToolHelper.isExists(params, MENU_ACTION)
                            && SysToolHelper.getMapValue(params, MENU_ACTION).length() <= MENU_ACTION_MAXLENGTH)) {
                        return false;
                    }
                } else {
                    //若不是叶子节点则action为空
                    params.put(MENU_ACTION, "");
                }

                //插入新建菜单信息
                daoClient.update("com.dessert.resources.updateByPrimaryKeySelective", params);
            }

        } else {
            result = false;
        }
        return result;
    }

    @Override
    public boolean deleteResources(Map<String, Object> params) {
        if (!SysToolHelper.isExists(params, "menuid")) {
            return false;
        }
        daoClient.update("com.dessert.resources.deleteByPrimaryKey", params);
        return daoClient.update("com.dessert.resources.deleteRoleMenu", params) > 0;
    }

    @Override
    public List<Map<String, Object>> findResources(Map<String, Object> params) {
        return daoClient.selectList("com.dessert.resources.selectResources", params);
    }

    @Override
    public Map<String, Object> findResource(Map<String, Object> params) {
        return daoClient.selectMap("com.dessert.resources.selectResources", params);
    }

    @Override
    public boolean updateMenuOrder(Map<String, Object> params) {
        boolean result = false;
        //校验必传字段
        if (SysToolHelper.isExists(params, "menuid", "movemenuid", "menuorder", "movemenuorder")) {
            //更新选择菜单序号
            result = daoClient.update("com.dessert.resources.updateMenuOrder", params) > 0;
            if (result) {
                Map<String, Object> paraMap = new HashMap<String, Object>();
                paraMap.put("menuid", SysToolHelper.getMapValue(params, "movemenuid"));
                paraMap.put("menuorder", SysToolHelper.getMapValue(params, "movemenuorder"));
                //更新被交换菜单序号
                daoClient.update("com.dessert.resources.updateMenuOrder", paraMap);
            }
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> selectResourcesByrole(Map<String, Object> params) {
        return daoClient.selectList("com.dessert.resources.selectResourcesByrole", params);
    }


    /**
     * 校验前台参数
     * 〈功能详细描述〉
     *
     * @param params
     * @return
     * @see [相关类/方法]（可选）
     * @since [产品/模块版本] （可选）
     */
    private boolean checkData(Map<String, Object> params) {
        boolean result = true;
        if (SysToolHelper.isExists(params, "menutype", "status", "menuname")) {
            String menuname = SysToolHelper.getMapValue(params, "menuname");

            //校验菜单英文名称只能输入英文；菜单类型长度不能超过2个字节
            //菜单中文描述不能超过30个汉字，菜单英文描述不能超过30
            if (menuname.length() > MENUNAMEZH_MAXLENGTH) {
                return false;
            }

        } else {
            result = false;
        }

        return result;
    }
}
