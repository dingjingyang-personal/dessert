package com.dessert.system.service.user.service;

import com.dessert.sys.common.bean.Page;
import com.dessert.sys.common.bean.User;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by ding-Admin on 2016/4/21.
 */
public interface UserService {

    Map<String,Object> findUserMap(Map<String,Object> params);

    boolean updateUser(Map<String,Object> params);



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

    /**
     * 添加用户
     * @param params
     * @return
     */
    boolean addUser(Map<String, Object> params);

    /**
     * 删除
     * @param params
     * @return
     */
    boolean deleteUser(Map<String, Object> params);

    /**
     * 查询用户角色
     * @param params
     * @return
     */
    List<Map<String,Object>> findRoles(Map<String, Object> params);


    Set<String> findRoles(String username);

    /**
     * 分配角色
     * @param params
     * @return
     */
    boolean addAssigningRoles(Map<String, Object> params);

    /**
     * 查询用户拥有的权限
     * @param userid
     * @return
     */
    List<Map<String,Object>> findResources(String userid);
}
