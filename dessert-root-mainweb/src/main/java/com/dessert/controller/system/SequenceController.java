package com.dessert.controller.system;


import com.dessert.sys.common.bean.Page;
import com.dessert.sys.common.tool.SysToolHelper;
import com.dessert.sys.sequence.service.SequenceNumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping("system/sequence")
public class SequenceController {

    @Autowired
    private SequenceNumService sequenceNumService;

    /**
     * 返回序列主页
     *
     * @return
     */
    @RequestMapping("getSequencePage.htm")
    public String getSequencePage() {
        return "/system/sequence/sequenceMain";
    }

    /**
     * 返回序列数据
     *
     * @param request
     * @param response
     */
    @RequestMapping("findSequenceJson.htm")
    public void findSequenceJson(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = SysToolHelper.getRequestParams(request);
        Page sequencePage = sequenceNumService.getSeqPage(params);
        SysToolHelper.outputByResponse(SysToolHelper.getJsonOfObject(sequencePage), response);
    }


    /**
     * 准备添加或修改页面
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("addOrupdateSequence.htm")
    public String addOrupdateSequence(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = SysToolHelper.getRequestParams(request);
        Map<String, Object> sequenceMap = sequenceNumService.getSequence(params);
        if (!SysToolHelper.mapIsEmpty(sequenceMap)) {
            request.setAttribute("sequenceMap", sequenceMap);
        }
        return "system/sequence/addOrupdateSequence";
    }


    /**
     * 添加序列
     *
     * @param request
     * @param response
     */
    @RequestMapping("addSequence.htm")
    public void addSequence(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = SysToolHelper.getRequestParams(request);
        SysToolHelper.outputByResponse(sequenceNumService.addSequence(params)? "1" : "2",response);
    }

    /**
     * 修改序列
     *
     * @param request
     * @param response
     */
    @RequestMapping("updateSequence.htm")
    public void updateSequence(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = SysToolHelper.getRequestParams(request);
        SysToolHelper.outputByResponse(sequenceNumService.updatSequence(params)? "1" : "2",response);
    }


    /**
     * 配置序列页面
     * @return
     */
    @RequestMapping("settingSequencePage.htm")
    public String settingSequencePage() {
        return "system/sequence/settingSequence";
    }


    /**
     * 序列配置数据
     * @param request
     * @param response
     */
    @RequestMapping("findSettingSequenceJson.htm")
    public void findSettingSequenceJson(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = SysToolHelper.getRequestParams(request);
        params.put("pageSize",6);
        Page settingSequencePage = sequenceNumService.getSeqSettingPage(params);
        SysToolHelper.outputByResponse(SysToolHelper.getJsonOfObject(settingSequencePage), response);
    }


    /**
     * 准备添加或修改页面
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("addOrupdateSettingSequence.htm")
    public String addOrupdateSettingSequence(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = SysToolHelper.getRequestParams(request);
        Map<String, Object> settingSequenceMap = sequenceNumService.getSeqSetting(params);
        if (!SysToolHelper.mapIsEmpty(settingSequenceMap)) {
            request.setAttribute("settingSequenceMap", settingSequenceMap);
        }
        return "system/sequence/addOrupdateSettingSequence";
    }


    /**
     * 添加序列配置
     *
     * @param request
     * @param response
     */
    @RequestMapping("addSettingSequence.htm")
    public void addSettingSequence(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = SysToolHelper.getRequestParams(request);
        SysToolHelper.outputByResponse(sequenceNumService.addSettingSequence(params)? "1" : "2",response);
    }

    /**
     * 修改序列配置
     *
     * @param request
     * @param response
     */
    @RequestMapping("updateSettingSequence.htm")
    public void updateSettingSequence(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = SysToolHelper.getRequestParams(request);
        SysToolHelper.outputByResponse(sequenceNumService.updateSettingSequence(params)? "1" : "2",response);
    }



    /**
     * 当前序列值页面
     * @return
     */
    @RequestMapping("sequenceValuePage.htm")
    public String sequenceValuePage() {
        return "system/sequence/sequenceValuePage";
    }


    /**
     * 当前序列值数据
     * @param request
     * @param response
     */
    @RequestMapping("findSequenceValueJson.htm")
    public void findSequenceValueJson(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = SysToolHelper.getRequestParams(request);
        params.put("pageSize",6);
        Page settingSequencePage = sequenceNumService.getSeqValuePage(params);
        SysToolHelper.outputByResponse(SysToolHelper.getJsonOfObject(settingSequencePage), response);
    }


    /**
     * 修改序列值页面
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("updateSequenceValuePage.htm")
    public String updateSequenceValuePage(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = SysToolHelper.getRequestParams(request);
        Map<String, Object> sequenceValueMap = sequenceNumService.getSeqValue(params);
        if (!SysToolHelper.mapIsEmpty(sequenceValueMap)) {
            request.setAttribute("sequenceValueMap", sequenceValueMap);
        }
        return "system/sequence/updateSequenceValuePage";
    }



    /**
     * 修改序列值
     *
     * @param request
     * @param response
     */
    @RequestMapping("updateSequenceValue.htm")
    public void updateSequenceValue(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = SysToolHelper.getRequestParams(request);
        SysToolHelper.outputByResponse(sequenceNumService.updateSeqValue(params)? "1" : "2",response);
    }




}
