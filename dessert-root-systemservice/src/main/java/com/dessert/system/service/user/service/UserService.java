package com.dessert.system.service.user.service;

import com.dessert.sys.common.bean.Page;
import com.dessert.sys.common.bean.User;

import java.util.Map;
import java.util.Set;

/**
 * Created by ding-Admin on 2016/4/21.
 */
public interface UserService {

    public Map<String,Object> getUserMap(Map<String,Object> params);

    public boolean updateUser(Map<String,Object> params);

    /**
     * 根据用户名查询角色
     * @param username
     * @return
     */
    Set<String> findRoles(String username);

    /**
     * 根据用户名查询权限
     * @param username
     * @return
     */
    Set<String> findPermissions(String username);


    /**
     * 查询单个用户
     * @param userAccunt
     * @return
     */
    User findUser(Map<String, Object> userAccunt);

    /**
     * 查询用户页面
     * @param params
     * @return
     */
    Page findUsersPage(Map<String, Object> params);
}
