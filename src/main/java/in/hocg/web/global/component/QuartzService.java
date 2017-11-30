package in.hocg.web.global.component;

import in.hocg.web.lang.exception.QuartzException;
import in.hocg.web.modules.system.domain.SysTask;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;

/**
 * Created by hocgin on 2017/11/30.
 * email: hocgin@gmail.com
 */
@Component
public class QuartzService {
    
    private Scheduler scheduler;
    
    @Autowired
    public QuartzService(Scheduler scheduler) {
        this.scheduler = scheduler;
    }
    
    /**
     * 未完成
     *
     * @throws SchedulerException
     */
    @Deprecated
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
    
    public void delete(String id, String group) {
        TriggerKey triggerKey = TriggerKey.triggerKey(id, group);
        try {
            if (exists(id, group)) {
                scheduler.pauseTrigger(triggerKey);
                scheduler.unscheduleJob(triggerKey);
                System.out.println(String.format("删除任务 %s", triggerKey));
            }
        } catch (SchedulerException e) {
            throw new QuartzException(e.getMessage());
        }
    }
    
    public void update(SysTask task) throws SchedulerException {
        String id = task.getId(),
                group = task.getGroup(),
                cron = task.getCron(),
                note = task.getDescription(),
                createdAt = task.getCreatedAt().toString();
        if (!exists(id, group)) {
            throw new QuartzException(String.format("Job不存在, jobName:{%s},jobGroup:{%s}", id, group));
        }
        TriggerKey triggerKey = TriggerKey.triggerKey(id, group);
        JobKey jobKey = new JobKey(id, group);
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
    }
    
    public void add(SysTask task) throws SchedulerException, ClassNotFoundException {
        String execClass = task.getExecClass(),
                id = task.getId(),
                group = task.getGroup(),
                cron = task.getCron(),
                note = task.getDescription(),
                createdAt = task.getCreatedAt().toString();
        if (exists(id, group)) {
            System.out.println(String.format("(%s, %s) 已经存在", id, group));
            return;
        }
        TriggerKey triggerKey = TriggerKey.triggerKey(id, group);
        JobKey jobKey = JobKey.jobKey(id, group);
        
        CronScheduleBuilder schedBuilder = CronScheduleBuilder
                .cronSchedule(cron)
                .withMisfireHandlingInstructionDoNothing();
        CronTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(triggerKey)
                .withDescription(createdAt)
                .withSchedule(schedBuilder).build();
        
        
        Class<? extends Job> clazz = (Class<? extends Job>) Class.forName(execClass);
        JobDetail jobDetail = JobBuilder.newJob(clazz)
                .withIdentity(jobKey)
                .setJobData(new JobDataMap(task.getParams()))
                .withDescription(note).build();
        scheduler.scheduleJob(jobDetail, trigger);
    }
    
    public void resume(String id, String group) {
        TriggerKey triggerKey = TriggerKey.triggerKey(id, group);
        try {
            if (exists(id, group)) {
                scheduler.resumeTrigger(triggerKey);
                System.out.println(String.format("重启 %s", triggerKey));
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
    
    public void restart(SysTask task) throws ClassNotFoundException, SchedulerException {
        delete(task.getId(), task.getGroup());
        add(task);
    }
    
    public void pause(String id, String group) {
        TriggerKey triggerKey = TriggerKey.triggerKey(id, group);
        try {
            if (exists(id, group)) {
                scheduler.pauseTrigger(triggerKey);
                System.out.println(String.format("暂停 %s", triggerKey));
            }
        } catch (SchedulerException e) {
            throw new QuartzException(e.getMessage());
        }
    }
    
    public boolean exists(String id, String group) throws SchedulerException {
        TriggerKey triggerKey = TriggerKey.triggerKey(id, group);
        return scheduler.checkExists(triggerKey);
    }
}
