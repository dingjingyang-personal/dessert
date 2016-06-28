package com.dessert.sys.common.tool;

import com.sun.mail.util.MailSSLSocketFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.Properties;

/**
 * Created by admin-ding on 2016/5/18.
 */
public class SendEmail {


    public static final String HOST;//设置邮件服务器主机名
    public static final String PROTOCOL;//设置协议
    public static final int PORT;//设置端口
    public static final String FROM;//发件人的email
    public static final String PWD;//发件人密码

    static {
        PropertiesConfig config = new PropertiesConfig("config/setting");
        HOST = config.getPropByKey("mail.host");
        PROTOCOL = config.getPropByKey("mail.transport.protocol");
        PORT = config.getIntPropByKey("mail.port", 465);
        FROM = config.getPropByKey("mail.from");
        String encryptPWD = config.getPropByKey("mail.pwd");
        PWD = XXTEA.Decrypt(encryptPWD, config.getPropByKey("xxxtea.key"));
    }


    /**
     * 发送邮件
     * @param toEmail
     * @param content
     * @throws Exception
     */
    public static void send(String toEmail, String content) throws Exception {

        Session session = getSession();

        //创建邮件
        Message message = createSimpleMail(session,toEmail,content);

        //通过session得到transport对象
        Transport transport = session.getTransport();
        //使用邮箱的用户名和密码连上邮件服务器，发送邮件时，发件人需要提交邮箱的用户名和密码给smtp服务器，用户名和密码都通过验证之后才能够正常发送邮件给收件人。
        transport.connect(HOST, FROM, PWD);

        transport.sendMessage(message, new Address[] { new InternetAddress(toEmail) });
        transport.close();

    }

    /**
     * 获取Session
     *
     * @return
     */
    private static Session getSession() throws GeneralSecurityException {

        Properties props = new Properties();

        // 开启debug调试
        props.setProperty("mail.debug", "true");
        // 发送服务器需要身份验证
        props.setProperty("mail.smtp.auth", "true");
        // 设置邮件服务器主机名
        props.setProperty("mail.host", HOST);
        // 发送邮件协议名称
        props.setProperty("mail.transport.protocol", PROTOCOL);
        props.setProperty("mail.smtp.port", String.valueOf(PORT));

        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.ssl.socketFactory", sf);

        Session session = Session.getInstance(props);

        return session;
    }


    /**
     * 创建只包含文本的邮件
     * @param session
     * @param toEmail
     * @param content
     * @return
     * @throws Exception
     */
    public static MimeMessage createSimpleMail(Session session,String toEmail, String content)throws Exception {
        //创建邮件对象
        MimeMessage message = new MimeMessage(session);
        //指明邮件的发件人
        message.setFrom(new InternetAddress(FROM));
        //指明邮件的收件人，现在发件人和收件人是一样的，那就是自己给自己发
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
        //邮件的标题
        message.setSubject("验证邮箱");
        message.setSentDate(new Date());
        StringBuilder stb = new StringBuilder();

        stb.append("\n" +
                "  <div id=\"contentDiv\" onmouseover=\"getTop().stopPropagation(event);\" onclick=\"getTop().preSwapLink(event, 'html', 'ZC3117-aoCPGkxD62BbcmYxrZVOI65');\" style=\"position:relative;font-size:14px;height:auto;padding:15px 15px 10px 15px;z-index:1;zoom:1;line-height:1.7;\" class=\"body\"> \n" +
                "   <div id=\"qm_con_body\">\n" +
                "    <div style=\"\" id=\"mailContentContainer\" class=\"qmbox qm_con_body_content qqmail_webmail_only\">\n" +
                "     <div style=\"background: #eee; padding: 10px 0;\"> \n" +
                "      <table style=\"width: 100%; border: none;\"> \n" +
                "       <tbody>\n" +
                "        <tr>\n" +
                "         <td align=\"center\"> \n" +
                "          <div class=\"lea-container\" style=\"\n" +
                "        text-align: left;\n" +
                "\twidth: 600px;\n" +
                "\tmargin: auto;\n" +
                "    margin-left: auto;\n" +
                "    margin-right: auto;\n" +
                "\tbackground: #fff;\n" +
                "\tpadding: 20px 10px;\n" +
                "\tborder-radius: 3px;\n" +
                "font-family: 'Helvetica Neue', Arial, 'Hiragino Sans GB', 'Microsoft YaHei', 'WenQuanYi Micro Hei', sans-serif;\n" +
                "\t\"> \n" +
                "           <a href=\"http://leanote.com\" target=\"_blank\" class=\"lea-header\" style=\"\n" +
                "\t\tline-height: 45px;\n" +
                "\t\tdisplay: block;\n" +
                "\t\ttext-align: left;\n" +
                "\t\tborder-bottom: 1px solid #eee;\n" +
                "\t\tpadding-bottom: 10px;\n" +
                "\t\tfont-size: 16px;\n" +
                "\t\tfont-family: 'Helvetica Neue', Arial, 'Hiragino Sans GB', 'Microsoft YaHei', 'WenQuanYi Micro Hei', sans-serif;\n" +
                "\t\tcolor: #000;\n" +
                "\t\ttext-decoration: none;\n" +
                "\t\t\"> <img style=\"width: 45px; vertical-align: middle;\" src=\"http://leanote.com/images/logo/leanote_icon_blue.png\" /> 请验证邮箱 </a> \n" +
                "           <div class=\"lea-contents\" style=\"padding: 10px 10px;\"> \n");
        stb.append(content);
        stb.append("            <p> 48小时后过期. </p> \n" +
                "            <hr style=\"border: none; border-bottom: 1px solid #eee\" /> \n" +
                "            <p> 任何问题请联系: <a target=\"_blank\" href=\"mailto:dingjingyang@foxmail.com\">dingjingyang@foxmail.com</a> </p> \n" +
                " \n" +
                "           </div> \n" +
                "          </div> \n" +
                "          <div style=\"width:600px; margin: auto; margin-top: 10px; font-size: 12px; color: #ccc; text-align: center;margin-left: auto; margin-right: auto;\"> \n" +
                "           <a target=\"_blank\" href=\"\" style=\"color: #868686; font-size: 12px; text-decoration: none\">关于此站</a>\n" +
                "\n" +
                "          </div> </td>\n" +
                "        </tr> \n" +
                "       </tbody>\n" +
                "      </table> \n" +
                "     </div> \n" +
                "     <style type=\"text/css\">.qmbox style, .qmbox script, .qmbox head, .qmbox link, .qmbox meta {display: none !important;}</style>\n" +
                "    </div>\n" +
                "   </div>\n" +
                "   <!-- -->\n" +
                "   <style>#mailContentContainer .txt {height:auto;}</style> \n" +
                "  </div>\n");

        //邮件的文本内容
        message.setContent(stb.toString(), "text/html;charset=UTF-8");
        //返回创建好的邮件对象
        return message;
    }



}
