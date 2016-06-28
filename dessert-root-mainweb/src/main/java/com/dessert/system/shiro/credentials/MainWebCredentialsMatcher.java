package com.dessert.system.shiro.credentials;

import com.dessert.sys.common.tool.MD5Util;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by admin-ding on 2016/6/16.
 */
public class MainWebCredentialsMatcher extends SimpleCredentialsMatcher {


    private Cache<String, AtomicInteger> passwordRetryCache;


    public MainWebCredentialsMatcher(CacheManager cacheManager) {
        passwordRetryCache = cacheManager.getCache("passwordRetryCache");
    }


    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {

        String username = (String) token.getPrincipal();

        // retry count + 1
        AtomicInteger retryCount = passwordRetryCache.get(username);

        if (retryCount == null) {
            retryCount = new AtomicInteger(0);
            passwordRetryCache.put(username, retryCount);
        }

        if (retryCount.incrementAndGet() > 5) {
            // if retry count > 5 throw
            throw new ExcessiveAttemptsException();
        }


        Object tokenStr = String.valueOf((char[]) token.getCredentials());

        Object tokenCredentials = MD5Util.encode2hex(tokenStr.toString());
        Object accountCredentials = String.valueOf(getCredentials(info));
        boolean matches = equals(tokenCredentials,accountCredentials);

        if(matches) {
            passwordRetryCache.remove(username);
        }

        return matches;
    }

}
