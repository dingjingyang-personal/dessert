package com.dessert.system.shiro.filter;


import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.servlet.AdviceFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import static org.apache.shiro.web.util.WebUtils.issueRedirect;


/**
 * Created by admin-ding on 2016/9/7.
 */
public class IsLoginFilter extends AdviceFilter {

    private static final Logger log = LoggerFactory.getLogger(IsLoginFilter.class);

    private String redirectUrl ;

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = getSubject(request, response);
        String redirectUrl = getRedirectUrl(request, response, subject);
        if(!subject.isAuthenticated()){
            issueRedirect(request, response, redirectUrl);
            return false;
        }
        return true;
    }

    protected Subject getSubject(ServletRequest request, ServletResponse response) {
        return SecurityUtils.getSubject();
    }

    protected String getRedirectUrl(ServletRequest request, ServletResponse response, Subject subject) {
        return getRedirectUrl();
    }


    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }


}
