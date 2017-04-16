import com.sun.mail.util.MailSSLSocketFactory;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Created by Coder on 2017/4/16.
 */
public class EmailSender {

    private static final String HOST = "smtp.qq.com";
    private static final String USERNAME = "";
    private static final String PASSWORD = "";
    private static final String FROMADDRESS = "";

    public static Session getSession() throws Exception {
        //跟smtp服务器建立一个连接
        Properties properties = new Properties();
        // 设置邮件服务器主机名
        properties.setProperty("mail.host", HOST);//指定邮件服务器，默认端口 25
        // 发送服务器需要身份验证
        properties.setProperty("mail.smtp.auth", "true");//要采用指定用户名密码的方式去认证
        // 发送邮件协议名称
        properties.setProperty("mail.transport.protocol", "smtp");

        // 开启SSL加密，否则会失败
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.ssl.socketFactory", sf);

        // 开启debug调试，以便在控制台查看
        //session.setDebug(true);也可以这样设置
        //p.setProperty("mail.debug", "true");

        // 创建session
        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                //用户名可以用QQ账号也可以用邮箱的别名
                PasswordAuthentication pa = new PasswordAuthentication(USERNAME, PASSWORD);
                // 后面的字符是授权码，用qq密码不行！！
                return pa;
            }
        });

        session.setDebug(true);//设置打开调试状态
        return session;
    }

    public void sendMail(Email email) throws Exception {
        Session session = getSession();

        //声明一个Message对象(代表一封邮件),从session中创建
        MimeMessage msg = new MimeMessage(session);
        //邮件信息封装
        //1发件人
        msg.setFrom(new InternetAddress(FROMADDRESS));
        //2收件人
        msg.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(email.getToAddress()));
        //3邮件内容:主题、内容
        msg.setSubject(email.getSubject());
        //msg.setContent("Hello, 今天没下雨!!!", "text/plain;charset=utf-8");//纯文本
        msg.setContent(email.getContent(), email.getContentType());//发html格式的文本
        //发送动作
        Transport.send(msg);
    }

    public void sendEmailWithBodyPart(Email email) throws Exception {

        // 创建session
        Session session = getSession();

        //声明一个Message对象(代表一封邮件),从session中创建
        MimeMessage msg = new MimeMessage(session);
        //邮件信息封装
        //1发件人
        msg.setFrom(new InternetAddress(FROMADDRESS));
        //2收件人
        msg.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(email.getToAddress()));
        //3邮件主题
        msg.setSubject(email.getSubject());
        //4.设置邮件内容和附件
        MimeMultipart mm = new MimeMultipart();

        //邮件内容部分1---文本内容
        MimeBodyPart contentBody = new MimeBodyPart(); //邮件中的文字部分
        contentBody.setContent(email.getContent(), email.getContentType());
        mm.addBodyPart(contentBody);

        //添加附件部分
        for (String mimeMultipart : email.getMimeBodyParts()) {
            //邮件内容部分2---附件
            MimeBodyPart body = new MimeBodyPart(); //附件
            body.setDataHandler(new DataHandler(new FileDataSource(mimeMultipart)));//./文件路径

            String bodyName = mimeMultipart.substring(mimeMultipart.lastIndexOf('\\') + 1);//取文件名作为附件名，解决乱码
            body.setFileName(MimeUtility.encodeText(bodyName));

            //把附件组装在一起，设置到msg中
            mm.addBodyPart(body);
        }

        msg.setContent(mm);

        // 发送邮件
        Transport.send(msg, msg.getAllRecipients());
    }

    public static void main(String[] args) {
        //新建一个Email对象
        Email email = new Email();
        email.setContent("这是两张<font color='red'>图片</font>....");
        email.setContentType(Email.HTML);
        email.setToAddress("451034898@qq.com");
        email.setSubject("这是我用Java发来的邮件--带附件的....");
        email.setMimeBodyParts(new ArrayList<>());
        email.getMimeBodyParts().add("D:\\zxing.png");
        email.getMimeBodyParts().add("D:\\zxing.png");
        email.getMimeBodyParts().add("D:\\zxing.png");


        EmailSender sender = new EmailSender();
        try {
            sender.sendMail(email);
            sender.sendEmailWithBodyPart(email);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
