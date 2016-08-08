package com.dessert.system.service.resources.service;

import com.dessert.sys.common.bean.Page;

import java.util.List;
import java.util.Map;

/**
 * Created by admin-ding on 2016/6/30.
 */
public interface ResourcesService {
    /**
     * 查询资源页面
     * @param params
     * @return
     */
    Page findResourcesPage(Map<String, Object> params);

    /**
     * 添加
     * @param params
     * @return
     */
    boolean addResources(Map<String, Object> params);

    /**
     * 修改
     * @param params
     * @return
     */
    boolean updateResources(Map<String, Object> params);

    /**
     * 删除
     * @param params
     * @return
     */
    boolean deleteResources(Map<String, Object> params);

    /**
     * 查询资源
     * @param params
     * @return
     */
    List<Map<String,Object>> findResources(Map<String, Object> params);

    /**
     * 查询单个资源
     * @param params
     * @return
     */
    Map<String,Object> findResource(Map<String, Object> params);

    /**
     * 资源排序
     * @param params
     * @return
     */
    boolean updateMenuOrder(Map<String, Object> params);

    /**
     * 查询资源
     * @param params
     * @return
     */
    List<Map<String,Object>> selectResourcesByrole(Map<String, Object> params);
}
