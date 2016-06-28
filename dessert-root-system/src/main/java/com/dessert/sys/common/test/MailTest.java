package com.dessert.sys.common.test;


import com.sun.mail.util.MailSSLSocketFactory;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

/**
 * Created by admin-ding on 2016/5/19.
 */
public class MailTest {
    public static void main(String[] args) throws Exception {
        Properties props = new Properties();

        // 开启debug调试
        props.setProperty("mail.debug", "true");
        // 发送服务器需要身份验证
        props.setProperty("mail.smtp.auth", "true");
        // 设置邮件服务器主机名
        props.setProperty("mail.host", "smtp.qq.com");
        // 发送邮件协议名称
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.smtp.port", "465");

        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.ssl.socketFactory", sf);

        Session session = Session.getInstance(props);

        Message msg = new MimeMessage(session);
        msg.setSubject("seenews error");
        StringBuilder builder = new StringBuilder();
        builder.append("url = " + "http://www.baidu.com");
        builder.append("\ndate " + new Date());
        msg.setText(builder.toString());
        msg.setFrom(new InternetAddress("dingjingyang_work@foxmail.com"));

        Transport transport = session.getTransport();
        transport.connect("smtp.qq.com", "dingjingyang_work@foxmail.com", "obtwlhukvtnibhac");

        transport.sendMessage(msg, new Address[] { new InternetAddress("dingjingyang@foxmail.com") });
        transport.close();
    }

}
