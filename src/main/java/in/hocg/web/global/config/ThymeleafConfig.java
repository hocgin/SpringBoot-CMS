package in.hocg.web.global.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.StandardTemplateModeHandlers;

/**
 * Created by hocgin on 2017/11/26.
 * email: hocgin@gmail.com
 */
//@Component
@Deprecated
public class ThymeleafConfig implements ApplicationContextAware {
    private static final String UTF8 = "UTF-8";
    private ApplicationContext applicationContext;
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
    
    @Bean("MailTemplateEngine")
    public TemplateEngine mailViewResolver() {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setSuffix(".html");
        resolver.setCharacterEncoding(UTF8);
        resolver.setPrefix("classpath:/_mail/");
        resolver.setTemplateMode(StandardTemplateModeHandlers.LEGACYHTML5.getTemplateModeName());
        resolver.setApplicationContext(applicationContext);
        resolver.setCacheable(false);
        
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.addTemplateResolver(resolver);
        return templateEngine;
    }
}
