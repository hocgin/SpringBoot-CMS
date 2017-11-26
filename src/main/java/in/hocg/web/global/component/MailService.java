package in.hocg.web.global.component;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.IContext;
import org.thymeleaf.context.WebContext;

import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Optional;

/**
 * Created by hocgin on 2017/11/26.
 * email: hocgin@gmail.com
 */
@Component
public class MailService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Value("${spring.mail.username}")
    private String from;
    private String personal = "hocg.in 官方邮件";
    
    private JavaMailSender mailSender;
    private TemplateEngine templateEngine;
    HttpServletRequest request;
    ServletContext servletContext;
    HttpServletResponse response;
    
    @Autowired
    public MailService(JavaMailSender mailSender,
                       TemplateEngine templateEngine,
                       HttpServletRequest request,
                       ServletContext servletContext,
                       HttpServletResponse response) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.servletContext = servletContext;
        this.request = request;
        this.response = response;
    }
    
    /**
     * 邮件发送
     * @param to 收件人
     * @param subject 标题
     * @param text 内容
     * @param inline 图片
     * @param attachment 附件
     * @throws javax.mail.MessagingException
     * @throws UnsupportedEncodingException
     */
    public void send(@NotNull String to, @NotNull String subject, @NotNull String text,
                     Map<String, File> inline,
                     Map<String, File> attachment) throws javax.mail.MessagingException, UnsupportedEncodingException {
    
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
        messageHelper.setTo(to);
        messageHelper.setFrom(from, personal);
        messageHelper.setCc(from); // - 给自己邮箱抄录一份
        messageHelper.setSubject(subject);
        messageHelper.setText(text, true);
        Optional.ofNullable(attachment).ifPresent(map -> {
            map.forEach((k, v) -> {
                try {
                    messageHelper.addAttachment(k, v);
                } catch (javax.mail.MessagingException e) {
                    e.printStackTrace();
                }
            });
        });
        Optional.ofNullable(inline).ifPresent(map -> {
            map.forEach((k, v) -> {
                try {
                    messageHelper.addInline(k, v);
                } catch (javax.mail.MessagingException e) {
                    e.printStackTrace();
                }
            });
        });
        mailSender.send(messageHelper.getMimeMessage());
    }
    
    public void send(@NotNull String to, @NotNull String subject, @NotNull String text) throws UnsupportedEncodingException, javax.mail.MessagingException {
        send(to, subject, text, null, null);
    }
    
    /**
     * 使用 Thymeleaf 解析后进行发送
     * @param to 收件人
     * @param subject 标题
     * @param templateName 模版
     * @param params 自定义参数
     * @param inline 图片
     * @param attachment 附件
     * @throws UnsupportedEncodingException
     * @throws javax.mail.MessagingException
     */
    public void sendUseTemplate(@NotNull String to, @NotNull String subject, @NotNull String templateName, Map<String, Object> params,
                     Map<String, File> inline,
                     Map<String, File> attachment) throws UnsupportedEncodingException, javax.mail.MessagingException {
        // 解析邮件
        WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
        // .. 还可以存储一些内置变量
        Optional.ofNullable(params).ifPresent(param -> ctx.setVariables(params));
        String text = templateEngine.process(templateName, ctx);
        send(to, subject, text, inline, attachment);
    }
    
    public void sendseTemplate(@NotNull String to, @NotNull String subject, @NotNull String templateName, Map<String, Object> params) throws UnsupportedEncodingException, javax.mail.MessagingException {
        sendUseTemplate(to, subject, templateName, params, null, null);
    }
}
