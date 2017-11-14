package in.hocg.web;

import in.hocg.web.database.BuiltInSeeder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.datatables.repository.DataTablesRepositoryFactoryBean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(repositoryFactoryBeanClass = DataTablesRepositoryFactoryBean.class)
public class Application {
    final Logger logger = LoggerFactory.getLogger(getClass());
    @Value("${i.mongo.init}")
    private Boolean mongoInit = false;
    
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
    
    
    /**
     * MongoDB 初始化
     */
    @Autowired
    private BuiltInSeeder builtInSeeder;
    
    @Bean
    CommandLineRunner preLoadMongo() throws Exception {
        return args -> {
            if (mongoInit) {
                logger.info("正在初始化 MongoDB 数据");
                builtInSeeder.drop().init();
            }
        };
    }
}
