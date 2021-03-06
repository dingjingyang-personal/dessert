package com.dessert.sys.log.service.impl;

import com.dessert.sys.common.bean.Page;
import com.dessert.sys.common.bean.User;
import com.dessert.sys.common.dao.DaoClient;
import com.dessert.sys.common.tool.SysToolHelper;
import com.dessert.sys.log.service.SysLogService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class SysLogServiceImpl implements SysLogService {

    private DaoClient daoClient;

    @Autowired(required = false)
    public void setDaoClient(DaoClient daoClient) {
        this.daoClient = daoClient;
    }


    private Logger logger = LoggerFactory.getLogger(SysLogServiceImpl.class);

    @Override
    public void error(String errorItem, String errorInfo) {
        error(errorItem, errorInfo, "", "", "");
    }

    @Override
    public void error(String errorItem, Exception e) {
        error(errorItem, e, "", "");
    }

    @Override
    public void error(String item, String content, String username, String ip, String userid) {
        if (StringUtils.isEmpty(content) || StringUtils.isEmpty(item)) {
            return;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("logid", SysToolHelper.getUuid());
        map.put("item", item);
        map.put("error", content);
        map.put("ip", ip == null ? "" : ip);
        map.put("username", username == null ? "" : username);
        map.put("userid", userid == null ? "" : userid);
        String sql = "com.dessert.sys_error_log.addLog";
        try {
            daoClient.update(sql, map);
        } catch (RuntimeException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        logger.error(item + ":" + content);
    }

    @Override
    public void error(String errorItem, Exception e, String username, String userIp) {
        error(errorItem, getExceptionDesc(e), username, userIp, "");

    }

    /**
     * 〈获取异常描述〉
     *
     * @param e
     * @return
     * @see [相关类/方法]（可选）
     * @since [产品/模块版本] （可选）
     */
    private String getExceptionDesc(Exception e) {
        StringBuilder builder = new StringBuilder();
        if (e != null && e.getStackTrace() != null && e.getStackTrace().length > 0) {
            builder.append("from:").append(e.getStackTrace()[0].toString())
                    .append("<br/>");
        }
        builder.append("error:").append(String.valueOf(e));
        return builder.toString();
    }

    @Override
    public void error(Exception e) {
        String item;
        if (e != null && e.getStackTrace() != null
                && e.getStackTrace().length > 0) {
            item = e.getStackTrace()[0].toString();
        } else {
            item = "未知";
        }
        error(item, e);
    }

    @Override
    public Page<Map<String, Object>> selectLog(Map<String, Object> params) {
        String sqlId = "log.service.selectLog";
        return (Page<Map<String, Object>>) daoClient.selectPage(sqlId, params);
    }

    @Override
    public boolean deleteLog(Map<String, Object> params) {
        String sqlId = "log.service.deleteLog";
        return daoClient.update(sqlId, params) > 0;
    }

    @Override
    public boolean addOperatingLog(Map<String, Object> logMap) {
        if (SysToolHelper.isExists(logMap, "operatinglogid", "username", "module", "methods")) {
            return daoClient.update("com.dessert.sys_operating_log.insert", logMap) > 0;
        }
        return false;

    }

    @Override
    public Page findErrorLogPage(Map<String, Object> params) {
        return daoClient.selectPage("com.dessert.sys_error_log.selectLog",params);
    }

    @Override
    public Page findOperationLogPage(Map<String, Object> params) {
        return daoClient.selectPage("com.dessert.sys_operating_log.selectLog",params);
    }

    @Override
    public Page findLoginLogPage(Map<String, Object> params) {
        return daoClient.selectPage("com.dessert.sys_login_log.selectLog",params);
    }

    @Override
    public boolean addLoginLog(Map<String, Object> loginMap) {
        if (SysToolHelper.isExists(loginMap, "loginlogid")) {
            return daoClient.update("com.dessert.sys_login_log.insert", loginMap) > 0;
        }
        return false;
    }

    @Override
    public void error(User user, String ip, Exception e) {
        String item;
        if (e != null && e.getStackTrace() != null
                && e.getStackTrace().length > 0) {
            item = e.getStackTrace()[0].toString();
        } else {
            item = "未知";
        }
        String username;
        username = user == null ? "未登录" : user.getUsername();
        String userid = user == null ? "未登录" : user.getUserid();
        error(item, String.valueOf(e), username, ip, userid);
    }


}
