package com.dessert.system.shiro.filter;

import com.dessert.system.service.user.service.UserService;
import com.google.common.collect.Maps;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.filter.PathMatchingFilter;

import javax.inject.Inject;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.Map;


public class SysUserFilter extends PathMatchingFilter {

	@Inject
	private UserService userService;

    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {

        String username = (String)SecurityUtils.getSubject().getPrincipal();
        Map<String,Object> params = Maps.newHashMap();
        params.put("username",username);
        request.setAttribute("user", userService.findUser(params));
        return true;
    }
}