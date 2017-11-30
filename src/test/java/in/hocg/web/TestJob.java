package in.hocg.web;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by hocgin on 2017/11/30.
 * email: hocgin@gmail.com
 */
public class TestJob implements Job {
    Logger logger = LogManager.getLogger(getClass());
    
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap dataMap = context.getMergedJobDataMap();
        System.out.println(String.format("任务 %s", context.getJobDetail().getKey().getName()));
        System.out.println(String.format("参数 %s", dataMap.getString("params")));
    }
}
