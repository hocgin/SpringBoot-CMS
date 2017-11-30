package in.hocg.web.global.config.quartz;

import org.quartz.spi.JobFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by hocgin on 2017/11/30.
 * email: hocgin@gmail.com
 */
@Configuration
public class QuartzConfig {

    /**
     * 配置
     */
    private Properties quartzProperties() throws IOException {
        Properties prop = new Properties();
        prop.put("quartz.scheduler.instanceName", "ServerScheduler");
        prop.put("org.quartz.scheduler.instanceId", "AUTO");
        prop.put("org.quartz.scheduler.skipUpdateCheck", "true");
        prop.put("org.quartz.scheduler.instanceId", "NON_CLUSTERED");
        prop.put("org.quartz.scheduler.jobFactory.class", "org.quartz.simpl.SimpleJobFactory");
//        prop.put("org.quartz.jobStore.class", "org.quartz.impl.jdbcjobstore.JobStoreTX");
//        prop.put("org.quartz.jobStore.driverDelegateClass", "org.quartz.impl.jdbcjobstore.StdJDBCDelegate");
//        prop.put("org.quartz.jobStore.dataSource", "quartzDataSource");
//        prop.put("org.quartz.jobStore.tablePrefix", "QRTZ_");
//        prop.put("org.quartz.jobStore.isClustered", "true");
        prop.put("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
        prop.put("org.quartz.threadPool.threadCount", "5");

//        prop.put("org.quartz.dataSource.quartzDataSource.driver", "com.mysql.jdbc.Driver");
//        prop.put("org.quartz.dataSource.quartzDataSource.URL", "jdbc:mysql://localhost:3306/demo-schema");
//        prop.put("org.quartz.dataSource.quartzDataSource.user", "root");
//        prop.put("org.quartz.dataSource.quartzDataSource.password", "123456");
//        prop.put("org.quartz.dataSource.quartzDataSource.maxConnections", "10");
        return prop;
    }
    
    
    
    @Bean
    public JobFactory jobFactory(ApplicationContext applicationContext) {
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }
    
    @Bean
    public SchedulerFactoryBean scheduler(JobFactory jobFactory)
            throws IOException{
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setJobFactory(jobFactory);
        factory.setQuartzProperties(quartzProperties());
        return factory;
    }
    
}
