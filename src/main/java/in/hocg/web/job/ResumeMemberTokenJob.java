package in.hocg.web.job;

import in.hocg.web.modules.system.service.MemberService;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by hocgin on 2017/11/30.
 * email: hocgin@gmail.com
 * 0 0 0 1 * ? * 每月
 * 重置会员 Token 次数
 */
@DisallowConcurrentExecution
public class ResumeMemberTokenJob implements Job {
    @Autowired
    private MemberService memberService;
    
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
//        JobDataMap dataMap = context.getMergedJobDataMap();
        memberService.resumeToken();
    }
}
