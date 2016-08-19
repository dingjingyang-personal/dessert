package com.dessert.sys.common.tool;

import com.google.common.collect.Maps;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.junit.Test;

import java.util.Map;

/**
 * Created by admin-ding on 2016/8/16.
 */
public class PasswordUtil {

    private static RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();
    private static String algorithmName = "md5";
    private static int hashIterations = 2;

    public static Map<String, Object> encryptPassword(String password) {
        Map<String, Object> passwordMap = Maps.newHashMap();
        String salt = randomNumberGenerator.nextBytes().toHex();
        String newPassword = new SimpleHash(algorithmName, password, ByteSource.Util.bytes(password + salt), hashIterations).toHex();

        passwordMap.put("salt", salt);
        passwordMap.put("password", newPassword);

        return passwordMap;
    }

    @Test
    public void getPassword() {
        Map<String, Object> passwordMap = encryptPassword("344676074");
        int i = 0;
    }

}
