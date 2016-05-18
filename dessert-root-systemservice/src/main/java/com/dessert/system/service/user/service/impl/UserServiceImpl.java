package com.dessert.system.service.user.service.impl;

import com.dessert.sys.common.dao.DaoClient;
import com.dessert.sys.common.tool.DateUtil;
import com.dessert.sys.common.tool.SendEmail;
import com.dessert.sys.common.tool.SysToolHelper;
import com.dessert.sys.exception.service.ServiceException;
import com.dessert.system.service.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ding-Admin on 2016/4/21.
 */

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private DaoClient daoClient;


    @Override
    public Map<String, Object> getUserMap(Map<String, Object> params) {

        return daoClient.selectMap("com.dessert.user.getUser", params);
    }




    /**
     * 处理注册
     */

    public void processregister(Map<String,Object> userMap){

        String email  = SysToolHelper.getMapValue(userMap, "mail");


        ///邮件的内容
        StringBuffer sb=new StringBuffer("点击下面链接激活账号，48小时生效，否则重新注册账号，链接只能使用一次，请尽快激活！</br>");
        sb.append("http://localhost:8080/springmvc/user/register?action=activate&email=");
        sb.append(email);
        sb.append("&validateCode=");
        sb.append(SysToolHelper.getMapValue(userMap, "acticode"));
        sb.append("\">http://localhost:8080/springmvc/user/register?action=activate&email=");
        sb.append(email);
        sb.append("&acticode=");
        sb.append(SysToolHelper.getMapValue(userMap, "acticode"));
        sb.append("</a>");

        //发送邮件
        SendEmail.send(email, sb.toString());

    }

    /**
     * 处理激活
     * @throws ParseException
     */
    ///传递激活码和email过来
    public void processActivate(Map<String,Object> params)throws ServiceException, ParseException {
        //数据访问层，通过email获取用户信息
        Map<String,Object> userMap=daoClient.selectMap("",params);
        //验证用户是否存在
        if(userMap!=null) {
            //验证用户激活状态
            if((SysToolHelper.getMapValue(params, "loginname")).equals(0)) {
                ///没激活
                Date currentTime = new Date();//获取当前时间
                //验证链接是否过期
                currentTime.before(DateUtil.StringToDate(SysToolHelper.getMapValue(params, "createdate")));
                if(currentTime.before(DateUtil.StringToDate(SysToolHelper.getMapValue(params, "createdate")))) {
                    //验证激活码是否正确
                    if(SysToolHelper.getMapValue(params, "acticode").equals(SysToolHelper.getMapValue(userMap, "acticode"))) {
                        //激活成功， //并更新用户的激活状态，为已激活

                        Map<String,Object> activUserMap = new HashMap<String, Object>();
                        activUserMap.put("userid",SysToolHelper.getMapValue(userMap, "userid"));
                        activUserMap.put("state",1);

                        daoClient.update("",activUserMap);
                    } else {
                        throw new ServiceException("激活码不正确");
                    }
                } else { throw new ServiceException("激活码已过期！");
                }
            } else {
                throw new ServiceException("邮箱已激活，请登录！");
            }
        } else {
            throw new ServiceException("该邮箱未注册（邮箱地址不存在）！");
        }

    }


}
