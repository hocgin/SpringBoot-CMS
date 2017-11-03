package in.hocg.web;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.data.mongodb.datatables.repository.DataTablesRepositoryFactoryBean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(repositoryFactoryBeanClass = DataTablesRepositoryFactoryBean.class)
public class Application {
    
    public static void main(String[] args) {
        // Enable MongoDB logging in general
        System.setProperty("DEBUG.MONGO", "true");
        // Enable DB operation tracing
        System.setProperty("DB.TRACE", "true");
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
}
