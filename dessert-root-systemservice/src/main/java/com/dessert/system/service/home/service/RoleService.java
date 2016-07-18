package com.dessert.system.service.home.service;

import com.dessert.sys.common.bean.Page;

import java.util.List;
import java.util.Map;

/**
 * Created by admin-ding on 2016/6/22.
 */
public interface RoleService {

    /**
     * 查询角色页面
     * @param params
     * @return
     */
    Page findRolesPage(Map<String, Object> params);

    /**
     * 查询角色
     * @param params
     * @return
     */
    List<Map<String,Object>> findRoles(Map<String, Object> params);

    /**
     * 查询单个角色
     * @param params
     * @return
     */
    Map<String,Object> findRole(Map<String, Object> params);

    /**
     * 添加角色
     * @param params
     * @return
     */
    boolean addRole(Map<String, Object> params);

    /**
     * 修改角色
     * @param params
     * @return
     */
    boolean updateRole(Map<String, Object> params);

    /**
     * 删除角色
     * @param params
     * @return
     */
    boolean deleteRole(Map<String, Object> params);



}
