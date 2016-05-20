package com.dessert.sys.common.test;

import com.dessert.sys.common.tool.SendEmail;

/**
 * Created by admin-ding on 2016/5/19.
 */
public class Demo {


    public static void main(String[] args) throws Exception {
        String str = "obtwlhukvtnibhac";
        String key = "DSYGMOUAGAJAKHSYRB";
//        byte[] encrypt_data = XXTEA.encrypt(str, key);
//        String decrypt_data = XXTEA.decryptToString(encrypt_data, key);
//        String encrypt_data1 = XXTEA.encryptToString(encrypt_data, key);
//        System.out.println(decrypt_data);
//        System.out.println(encrypt_data1);
//        assert(str.equals(decrypt_data));

//        String key01 = XXTEA.Encrypt(str,key);
//        System.out.println(key01);
//        String key02 = XXTEA.Decrypt(key01,key);
//        System.out.println(key02);


        StringBuilder stu = new StringBuilder();
        stu.append("            <p> <a target=\"_blank\" href=\"mailto:dingjingyang@foxmail.com\">dingjingyang<wbr />@foxmail.com</a> 您好, 欢迎来到小站. </p> \n" +
                "            <p> 请点击链接验证邮箱: <a target=\"_blank\" href=\"http://leanote.com/user/activeEmail?token=85ed70734d9c5eaff5b096a7e40f7ac9\">http://leanote.com/user/activeEmail?token=85ed70734d9c5eaff5b096a7e40f7ac9</a> </p> \n");

        SendEmail.send("dingjingyang@foxmail.com",stu.toString());
    }
}
