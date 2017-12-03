package in.hocg.web.global.component;

import com.sun.istack.internal.NotNull;
import in.hocg.web.global.StringTemplateResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.templateresolver.TemplateResolver;
import org.thymeleaf.util.MapUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
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
    private HttpServletRequest request;
    private ServletContext servletContext;
    private HttpServletResponse response;
    private SpringTemplateEngine springTemplateEngine;
    
    @Autowired
    public MailService(JavaMailSender mailSender,
                       HttpServletRequest request,
                       ServletContext servletContext,
                       HttpServletResponse response) {
        this.mailSender = mailSender;
        this.servletContext = servletContext;
        this.request = request;
        this.response = response;
        
        
        TemplateResolver resolver = new TemplateResolver();
        resolver.setResourceResolver(new StringTemplateResolver());
        resolver.setPrefix("_mail/"); // src/main/resources/_mail
        resolver.setSuffix(".html");
        resolver.setTemplateMode("LEGACYHTML5");
        resolver.setCharacterEncoding("UTF-8");
        resolver.setOrder(1);
        
        springTemplateEngine = new SpringTemplateEngine();
        springTemplateEngine.setTemplateResolver(resolver);
    }
    
    /**
     * 邮件发送
     *
     * @param to         收件人
     * @param subject    标题
     * @param text       内容
     * @param inline     图片
     * @param attachment 附件
     * @throws javax.mail.MessagingException
     * @throws UnsupportedEncodingException
     */
    public void send(@NotNull String[] to, @NotNull String subject, @NotNull String text,
                     Map<String, File> inline,
                     Map<String, File> attachment) throws javax.mail.MessagingException, UnsupportedEncodingException {
        
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
        messageHelper.setTo(to);
        messageHelper.setFrom(from, personal);
        messageHelper.setCc(from); // - 给自己邮箱抄录一份
        messageHelper.setSubject(subject);
        messageHelper.setText(text, true);
        if (!MapUtils.isEmpty(attachment)) {
            attachment.forEach((k, v) -> {
                try {
                    messageHelper.addAttachment(k, v);
                } catch (javax.mail.MessagingException e) {
                    e.printStackTrace();
                }
            });
        }
        if (!MapUtils.isEmpty(inline)) {
            inline.forEach((k, v) -> {
                try {
                    messageHelper.addInline(k, v);
                } catch (javax.mail.MessagingException e) {
                    e.printStackTrace();
                }
            });
        }
        mailSender.send(messageHelper.getMimeMessage());
    }
    public void send(@NotNull String to, @NotNull String subject, @NotNull String text,
                     Map<String, File> inline,
                     Map<String, File> attachment) throws javax.mail.MessagingException, UnsupportedEncodingException {
        send(new String[]{to}, subject, text, inline, attachment);
    }
    
    public void send(@NotNull String to, @NotNull String subject, @NotNull String text) throws UnsupportedEncodingException, javax.mail.MessagingException {
        send(to, subject, text, null, null);
    }
    
    /**
     * 使用 src/main/resources/_mail/ 内置模版解析后发送
     *
     * @param to           收件人
     * @param subject      标题
     * @param templateName 模版
     * @param params       自定义参数
     * @param inline       图片
     * @param attachment   附件
     * @throws UnsupportedEncodingException
     * @throws javax.mail.MessagingException
     */
    public void sendUseTemplate(@NotNull String to, @NotNull String subject, @NotNull String templateName,
                                Map<String, Object> params,
                                Map<String, File> inline,
                                Map<String, File> attachment) throws UnsupportedEncodingException, javax.mail.MessagingException {
        send(to, subject, thymeleafTemplate(templateName, params), inline, attachment);
    }
    
    /**
     * 解析 Thymeleaf 文本进行发送
     * @param to
     * @param subject
     * @param thymeleafText
     * @param params
     * @param inline
     * @param attachment
     * @throws UnsupportedEncodingException
     * @throws MessagingException
     */
    public void sendUseThymeleafText(@NotNull String[] to, @NotNull String subject, @NotNull String thymeleafText,
                                     Map<String, Object> params,
                                     Map<String, File> inline,
                                     Map<String, File> attachment) throws UnsupportedEncodingException, MessagingException {
        send(to, subject, thymeleaf(thymeleafText, params), inline, attachment);
    }
    
    /**
     * 文件路径
     * @param to
     * @param subject
     * @param thymeleafFilePath
     * @param params
     * @param inline
     * @param attachment
     * @throws IOException
     * @throws MessagingException
     */
    public void sendUseThymeleafFile(@NotNull String to, @NotNull String subject, @NotNull Path thymeleafFilePath,
                                     Map<String, Object> params,
                                     Map<String, File> inline,
                                     Map<String, File> attachment) throws IOException, MessagingException {
        send(to, subject, thymeleaf(thymeleafFilePath, params), inline, attachment);
    }
    
    public void sendUseThymeleafFile(@NotNull Collection<String> to, @NotNull String subject, @NotNull Path thymeleafFilePath,
                                     Map<String, Object> params,
                                     Map<String, File> inline,
                                     Map<String, File> attachment) throws IOException, MessagingException {
        send(to.toArray(new String[]{}), subject, thymeleaf(thymeleafFilePath, params), inline, attachment);
    }
    
    /**
     * 解析 thymeleaf 语法
     */
    public String thymeleafTemplate(String templateName, Map<String, Object> params) {
        WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
        Optional.ofNullable(params).ifPresent(ctx::setVariables);
        return springTemplateEngine.process(templateName, ctx);
    }
    
    public String thymeleaf(String text, Map<String, Object> params) {
        StringTemplateResolver.StringContext ctx = new StringTemplateResolver.StringContext(text);
        Optional.ofNullable(params).ifPresent(map -> ctx.getVariables().putAll(params));
        return springTemplateEngine.process("_all_", ctx);
    }
    
    public String thymeleaf(Path path, Map<String, Object> params) throws IOException {
        return thymeleaf(new String(Files.readAllBytes(path)), params);
    }
}
