package com.dessert.controller.system;


import com.dessert.sys.common.annotation.SystemOperatingLog;
import com.dessert.sys.common.bean.Page;
import com.dessert.sys.common.tool.SysToolHelper;
import com.dessert.system.service.dictionary.service.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping("system/dictionary")
public class DictionaryController {

    @Autowired
    private DictionaryService dictionaryService;


    /**
     * 查询数据字典
     *
     * @return
     */
    @RequestMapping("findDictionarys.htm")
    public String findDictionarys() {
        return "system/dictionary/dictionaryMian";
    }


    /**
     * 返回字典数据
     *
     * @param request
     * @param response
     */
    @SystemOperatingLog(module = "系统管理-基础配置", methods = "字典管理-返回字典数据")
    @RequestMapping("findDictionaryJson.htm")
    public void findDictionaryJson(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = SysToolHelper.getRequestParams(request);
        Page dictionaryPag = dictionaryService.findDictionaryPage(params);
        SysToolHelper.outputByResponse(SysToolHelper.getJsonOfObject(dictionaryPag), response);
    }


    /**
     * 准备添加或修改字典
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("addOrupdateDictionary")
    public String addOrupdateDictionary(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = SysToolHelper.getRequestParams(request);
        Map<String, Object> dictionaryMap = dictionaryService.findDictionaryMap(params);
        if (dictionaryMap != null && !dictionaryMap.isEmpty()) {
            request.setAttribute("dictionaryMap", dictionaryMap);
        }
        return "system/dictionary/addOrupdateDictionary";
    }


    /**
     * 添加字典
     * @param request
     * @param response
     * @return
     */
    @SystemOperatingLog(module = "系统管理-基础配置", methods = "字典管理-添加字典")
    @RequestMapping("addDictionary.htm")
    public void addDictionary(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = SysToolHelper.getRequestParams(request);
        SysToolHelper.outputByResponse(dictionaryService.addDictionary(params) ? "1" : "2", response);
    }


    /**
     * 更新字典
     * @param request
     * @param response
     */
    @SystemOperatingLog(module = "系统管理-基础配置", methods = "字典管理-更新字典")
    @RequestMapping("updateDictionary.htm")
    public void updateDictionary(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = SysToolHelper.getRequestParams(request);
        SysToolHelper.outputByResponse(dictionaryService.updateDictionary(params) ? "1" : "2", response);
    }


    /**
     * 删除字典
     * @param request
     * @param response
     */
    @SystemOperatingLog(module = "系统管理-基础配置", methods = "字典管理-删除字典")
    @RequestMapping("deleteDictionary.htm")
    public void deleteDictionary(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = SysToolHelper.getRequestParams(request);
        SysToolHelper.outputByResponse(dictionaryService.deleteDictionary(params) ? "1" : "2", response);
    }

}
