package com.dessert.sys.common.tool;

import com.dessert.sys.common.constants.SysConstants;
import com.dessert.sys.common.constants.SysSettings;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;


public class CookieHelper {

    private static final CookieHelper COOKIE = new CookieHelper();

    private Logger logger = LoggerFactory.getLogger(CookieHelper.class);

    private CookieHelper() {
    }

    public static CookieHelper getInstance() {
        return COOKIE;
    }

    /**
     * 设置cookie 信息
     *
     * @param response
     * @param cookieName
     * @see [相关类/方法]（可选）
     */
    public void setCookie(HttpServletResponse response, String key, String value, String domain, int maxAge) {
        Cookie cookie = null;
        try {
            cookie = new Cookie(key, java.net.URLEncoder.encode(value, "utf-8"));
            cookie.setPath(SysSettings.COOKIE_PATH);
            if (StringUtils.isNotEmpty(domain)) {
                cookie.setDomain(domain);
            }
            cookie.setMaxAge(maxAge);
            response.addCookie(cookie);
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(), value);
        }

    }

    /**
     * 功能描述: 设置cookie 浏览器退出即生效
     *
     * @param response
     * @param key
     * @param value
     * @param domain
     * @param maxAge
     */
    public void setCookie(HttpServletResponse response, String key, String value, String domain) {
        Cookie cookie = null;
        try {
            cookie = new Cookie(key, java.net.URLEncoder.encode(value, "utf-8"));
            cookie.setPath(SysSettings.COOKIE_PATH);
            if (StringUtils.isNotEmpty(domain)) {
                cookie.setDomain(domain);
            }
            response.addCookie(cookie);
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(), value);
        }

    }

    /**
     * 功能描述：获取cookie的值
     *
     * @param request    HttpServletRequest
     * @param cookieName cookie的名称
     * @return
     */
    public String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            Cookie cookie = null;
            for (int i = 0; i < cookies.length; i++) {
                cookie = cookies[i];
                if (cookieName.equals(cookie.getName())) {
                    try {
                        return java.net.URLDecoder.decode(cookie.getValue(), "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        logger.error(e.getMessage(), cookieName);
                    }
                }
            }
        }
        return "";
    }

    /**
     * 功能描述：清除cookie信息
     *
     * @param request   HttpServletRequest
     * @param response  HttpServletResponse
     * @param cookieKey 要删除的cookie的key
     */
    public void clearCookie(HttpServletRequest request, HttpServletResponse response, String key) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie k : cookies) {
                if (key.equals(k.getName())) {
                    Cookie cookie = new Cookie(k.getName(), null);
                    cookie.setMaxAge(0);
                    cookie.setPath(SysSettings.COOKIE_PATH);
                    if (StringUtils.isNotEmpty(SysSettings.COOKIE_DOMAIN)) {
                        cookie.setDomain(SysSettings.COOKIE_DOMAIN);
                    }
                    response.addCookie(cookie);
                    break;
                }
            }
        }
    }

    /**
     * 功能描述：清除cookie信息,解决会话标识未更新安全问题，供登录时调用
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    public void deleteCookie(HttpServletRequest request, HttpServletResponse response, boolean isSupplier) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie k : cookies) {
                if ("JSESSIONID".equalsIgnoreCase(k.getName())) {
                    Cookie cookie = new Cookie(k.getName(), null);
                    cookie.setMaxAge(0);
                    cookie.setPath(SysSettings.COOKIE_PATH);
                    if (StringUtils.isNotEmpty(SysSettings.COOKIE_DOMAIN)) {
                        cookie.setDomain(SysSettings.COOKIE_DOMAIN);
                    }
                    response.addCookie(cookie);
                }
            }
        }
    }

    /**
     * 生成 登录 ticket cookie
     *
     * @param response
     * @param request
     * @param username 用户名
     * @see [相关类/方法]（可选）
     * @since [产品/模块版本] （可选）
     */
    public void setUserTicket(HttpServletResponse response, String userTicket) {
        setCookie(response, SysConstants.TICKET_NAME, userTicket, SysSettings.COOKIE_DOMAIN);
    }

    /**
     * 功能描述：Cookie已保存username的密文，根据密文从数据缓存获取明文
     *
     * @param request
     * @return String
     */
    public String getUserTicket(HttpServletRequest request) {
        // 从Cookie获取密文
        String useridChpher = getCookieValue(request, SysConstants.TICKET_NAME);
        return useridChpher;
    }
}
