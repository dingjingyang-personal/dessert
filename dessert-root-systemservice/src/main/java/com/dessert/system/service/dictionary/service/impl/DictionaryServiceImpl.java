package com.dessert.system.service.dictionary.service.impl;

import com.dessert.sys.common.bean.Page;
import com.dessert.sys.common.dao.DaoClient;
import com.dessert.sys.common.tool.SysToolHelper;
import com.dessert.system.service.dictionary.service.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public class DictionaryServiceImpl implements DictionaryService {

    @Autowired
    private DaoClient daoClient;

    @Override
    public Page findDictionaryPage(Map<String, Object> params) {
        return daoClient.selectPage("com.dessert.dictionary.getDictionary", params);
    }

    @Override
    public Map<String, Object> findDictionaryMap(Map<String, Object> params) {
        return daoClient.selectMap("com.dessert.dictionary.getDictionary", params);
    }

    @Override
    public boolean addDictionary(Map<String, Object> params) {
        if (SysToolHelper.isExists(params, "diccode", "dicvalue")) {
            params.put("dicid", SysToolHelper.getUuid());
            return daoClient.update("com.dessert.dictionary.addDictionary", params) > 0;
        }
        return false;
    }

    @Override
    public boolean updateDictionary(Map<String, Object> params) {
        if (SysToolHelper.isExists(params, "dicid")) {
            return daoClient.update("com.dessert.dictionary.updateDictionary", params) > 0;
        }
        return false;
    }

    @Override
    public boolean deleteDictionary(Map<String, Object> params) {
        if (SysToolHelper.isExists(params, "dicid")) {
            return daoClient.update("com.dessert.dictionary.deleteByPrimaryKey", params) > 0;
        }
        return false;
    }
}
