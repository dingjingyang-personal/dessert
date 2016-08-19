package com.dessert.system.shiro.realm;

import com.dessert.sys.common.bean.User;
import com.dessert.sys.common.tool.SysToolHelper;
import com.dessert.sys.common.tool.ValidateUtils;
import com.dessert.system.service.user.service.UserService;
import com.google.common.collect.Maps;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * Created by admin-ding on 2016/6/17.
 */
public class MainWebRealm extends AuthorizingRealm {


    @Autowired
    private UserService userService;


    /**
     * 授权，即权限验证，验证某个已认证的用户是否拥有某个权限；即判断用户是否能做事情，常见的如：验证某个用户是否拥有某个角色。
     * 或者细粒度的验证某个用户对某个资源是否具有某个权限
     *
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = (String) principals.getPrimaryPrincipal();
        if(username!=null){

            String userID = SecurityUtils.getSubject().getSession().getAttribute("userSessionId").toString();
            List<Map<String,Object>> listResources = userService.findResources(userID);

            // 权限信息对象info,用来存放查出的用户的所有的角色（role）及权限（permission）
            SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
            for(Map<String,Object> resources :listResources) {
                authorizationInfo.addStringPermission(SysToolHelper.getMapValue(resources,"menuid"));
            }

            return authorizationInfo;
        }
        return null;
    }


    /**
     * 身份认证/登录，验证用户是不是拥有相应的身份
     *
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) {

        String userAccunt = (String) token.getPrincipal();
        String userPassword = String.valueOf((char[])token.getCredentials());

        Map<String, Object> params = Maps.newHashMap();

        if (ValidateUtils.Email(userAccunt)) {
            params.put("email", userAccunt);
        } else {
            params.put("loginname", userAccunt);
        }

        User user = userService.findUser(params);

        if (user != null) {

            if (Boolean.TRUE.equals(user.getStatus())) {
                throw new LockedAccountException(); //帐号锁定
            }
            //交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配，如果觉得人家的不好可以自定义实现
            SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                    userAccunt, //用户名
                    user.getUserpwd(), //密码
                    ByteSource.Util.bytes( userPassword + user.getSalt()),// salt=password+salt
                    getName()  //realm name
            );
            Session session = SecurityUtils.getSubject().getSession();
            session.setAttribute("userSessionId",user.getUserid());

            session.setAttribute("userSession", user);

            return authenticationInfo;

        } else {
            throw new UnknownAccountException();//没找到帐号
        }


    }
}
