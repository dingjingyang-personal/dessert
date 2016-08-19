package com.dessert.sys.remote.service.impl;

import com.dessert.sys.common.constants.SysSettings;
import com.dessert.sys.common.tool.NetUtil;
import com.dessert.sys.common.tool.SysToolHelper;
import com.dessert.sys.remote.service.RemoteService;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;


@Service("remoteService")
public class RemoteServiceImpl implements RemoteService {

    @Override
    public Map<String, Object> invokeRemoteMethod(String beanId,
                                                  String methodName, Map<String, Object> params) {
        if (StringUtils.isEmpty(beanId) || StringUtils.isEmpty(methodName)) {
            return null;
        }
        Map<String, String> temp = new HashMap<String, String>(3);
        temp.put("beanId", beanId);
        temp.put("methodName", methodName);
        if (params != null) {
            temp.put("params", SysToolHelper.getJsonOfCollection(params));
        }
        String resString = NetUtil.postMap(SysSettings.REMOTE_INVOKE_URL, temp);
        try {
            resString = URLDecoder.decode(resString, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (temp.containsKey("error")) {
            if (params != null) {
                params.put("error", temp.get("error"));
            }
            return null;
        }
        return JSONObject.fromObject(resString);
    }



}
