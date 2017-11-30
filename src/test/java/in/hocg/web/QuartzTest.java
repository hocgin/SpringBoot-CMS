package in.hocg.web;

import in.hocg.web.modules.system.domain.SysTask;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;

/**
 * Created by hocgin on 2017/11/14.
 * email: hocgin@gmail.com
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class QuartzTest {
    @Autowired
    Scheduler scheduler;
    
    @Test
    public void testQuartz() throws SchedulerException, IOException {
        queryAll();
        SysTask task = new SysTask();
        task.setName("test Task");
        task.setCron("* * * * * ? *");
        task.setExecClass("in.hocg.web.TestJob");
        task.setGroup("1");
        task.setDescription("测试1");
        task.createdAt();
        add(task);
    
        new BufferedReader(new InputStreamReader(System.in)).readLine();
    }
    
    public void queryAll() throws SchedulerException {
        for (String jobGroupName : scheduler.getJobGroupNames()) {
            for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.<JobKey>groupEquals(jobGroupName))) {
                List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
                for (Trigger trigger : triggers) {
                    Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                    JobDetail jobDetail = scheduler.getJobDetail(jobKey);
//                    String cronExpression = "", createTime = "";
                    if (trigger instanceof CronTrigger) {
                        CronTrigger cronTrigger = (CronTrigger) trigger;
//                        cronExpression = cronTrigger.getCronExpression();
//                        createTime = cronTrigger.getDescription();
                    }
                    System.out.println(String.format("任务名: %s", jobKey.getName()));
                }
            }
        }
    }
    
    public void delete(String name, String jobGroup) {
        TriggerKey triggerKey = TriggerKey.triggerKey(name, jobGroup);
        try {
            if (exists(name, jobGroup)) {
                scheduler.pauseTrigger(triggerKey);
                scheduler.unscheduleJob(triggerKey);
                System.out.println(String.format("删除任务 %s", triggerKey));
            }
        } catch (SchedulerException e) {
            throw new QuartzException(e.getMessage());
        }
    }
    
    public void update(SysTask task) {
        String  name = task.getName(),
                group = task.getGroup(),
                cron = task.getCron(),
                note = task.getDescription(),
                createdAt = task.getCreatedAt().toString();
        try {
            if (!exists(name, group)) {
                throw new QuartzException(String.format("Job不存在, jobName:{%s},jobGroup:{%s}", name, group));
            }
            TriggerKey triggerKey = TriggerKey.triggerKey(name, group);
            JobKey jobKey = new JobKey(name, group);
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cron)
                    .withMisfireHandlingInstructionDoNothing();
            CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                    .withIdentity(triggerKey)
                    .withDescription(createdAt)
                    .withSchedule(cronScheduleBuilder).build();
            
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            jobDetail.getJobBuilder()
                    .withDescription(note);
            HashSet<Trigger> triggerSet = new HashSet<>();
            triggerSet.add(cronTrigger);
            
            scheduler.scheduleJob(jobDetail, triggerSet, true);
        } catch (SchedulerException e) {
            throw new QuartzException("类名不存在或执行表达式错误");
        }
    }
    
    public void add(SysTask task) {
        String execClass = task.getExecClass(),
                name = task.getName(),
                group = task.getGroup(),
                cron = task.getCron(),
                note = task.getDescription(),
                createdAt = task.getCreatedAt().toString();
        try {
            if (exists(name, group)) {
                System.out.println(String.format("(%s, %s) 已经存在", name, group));
                return;
            }
            TriggerKey triggerKey = TriggerKey.triggerKey(name, group);
            JobKey jobKey = JobKey.jobKey(name, group);
            
            CronScheduleBuilder schedBuilder = CronScheduleBuilder
                    .cronSchedule(cron)
                    .withMisfireHandlingInstructionDoNothing();
            CronTrigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(triggerKey)
                    .withDescription(createdAt)
                    .withSchedule(schedBuilder).build();
            
            
            Class<? extends Job> clazz = null;
            try {
                clazz = (Class<? extends Job>) Class.forName(execClass);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                System.out.println("未找到任务执行类");
            }
            JobDetail jobDetail = JobBuilder.newJob(clazz)
                    .withIdentity(jobKey)
                    .setJobData(new JobDataMap(task.getParams()))
                    .withDescription(note).build();
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
    
    public void resume(String name, String group) {
        TriggerKey triggerKey = TriggerKey.triggerKey(name, group);
        
        try {
            if (exists(name, group)) {
                scheduler.resumeTrigger(triggerKey);
                System.out.println(String.format("重启 %s", triggerKey));
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
    
    public void pause(String name, String group) {
        TriggerKey triggerKey = TriggerKey.triggerKey(name, group);
        try {
            if (exists(name, group)) {
                scheduler.pauseTrigger(triggerKey);
                System.out.println(String.format("暂停 %s", triggerKey));
            }
        } catch (SchedulerException e) {
            throw new QuartzException(e.getMessage());
        }
    }
    
    private boolean exists(String name, String jobGroup) throws SchedulerException {
        TriggerKey triggerKey = TriggerKey.triggerKey(name, jobGroup);
        return scheduler.checkExists(triggerKey);
    }
}