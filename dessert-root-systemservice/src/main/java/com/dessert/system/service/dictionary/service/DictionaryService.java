package com.dessert.system.service.dictionary.service;


import com.dessert.sys.common.bean.Page;

import java.util.Map;

public interface DictionaryService {
    /**
     * 查询字典页面
     * @param params
     * @return
     */
    Page findDictionaryPage(Map<String, Object> params);

    /**
     * 查询单个字典
     * @param params
     * @return
     */
    Map<String,Object> findDictionaryMap(Map<String, Object> params);

    /**
     * 添加字典
     * @param params
     * @return
     */
    boolean addDictionary(Map<String, Object> params);

    /**
     * 更新字典
     * @param params
     * @return
     */
    boolean updateDictionary(Map<String, Object> params);

    /**
     * 删除字典
     * @param params
     * @return
     */
    boolean deleteDictionary(Map<String, Object> params);
}
