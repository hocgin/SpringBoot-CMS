package in.hocg.web;

import in.hocg.web.database.BuiltInSeeder;
import in.hocg.web.modules.system.domain.Variable;
import in.hocg.web.modules.system.service.SysTaskService;
import in.hocg.web.modules.system.service.VariableService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.session.SessionAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.datatables.repository.DataTablesRepositoryFactoryBean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.context.request.RequestContextListener;

@SpringBootApplication(exclude = SessionAutoConfiguration.class)
@EnableAsync
@EnableMongoRepositories(repositoryFactoryBeanClass = DataTablesRepositoryFactoryBean.class)
public class Application {
    final Logger logger = LoggerFactory.getLogger(getClass());
    
    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .sources(Application.class)
                .listeners(new ApplicationListener<ApplicationEvent>() {
                    @Override
                    public void onApplicationEvent(ApplicationEvent event) {
//                        System.out.println(String.format("%s: %s", "监听日志", event));
                    }
                })
                .run(args);
    }
    
    @Bean
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }
    
    /**
     * MongoDB 初始化
     */
    @Autowired
    private BuiltInSeeder builtInSeeder;
    @Autowired
    private SysTaskService sysTaskService;
    @Autowired
    private VariableService variableService;
    
    @Bean
    CommandLineRunner preLoadMongo() {
        return args -> {
            if (variableService.getBool(Variable.DEV_INIT_MONGO, true)) {
                logger.info("正在初始化 Mongo 数据..");
                builtInSeeder.drop().init();
            }
            logger.info("正在唤醒定时任务..");
            sysTaskService.init();
        };
    }
    
    
}
