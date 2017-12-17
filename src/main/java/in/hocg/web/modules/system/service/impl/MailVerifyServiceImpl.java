package in.hocg.web.modules.system.service.impl;

import in.hocg.web.lang.CheckError;
import in.hocg.web.lang.utils.StringKit;
import in.hocg.web.modules.base.Base2Service;
import in.hocg.web.modules.system.domain.MailVerify;
import in.hocg.web.modules.system.domain.repository.MailVerifyRepository;
import in.hocg.web.modules.system.domain.user.User;
import in.hocg.web.modules.system.service.MailVerifyService;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * Created by hocgin on 2017/12/17.
 * email: hocgin@gmail.com
 */
@Service
public class MailVerifyServiceImpl extends Base2Service<MailVerify, String, MailVerifyRepository>
        implements MailVerifyService {
    @Override
    public MailVerify create(User user, String code, int minute) {
        MailVerify mailVerify = new MailVerify();
        mailVerify.setVerified(false);
        mailVerify.setMail(user.getEmail());
        mailVerify.setUser(user);
        mailVerify.setCode(code);
        mailVerify.setDurationOfValidity(minute);
        mailVerify.createdAt();
        return repository.insert(mailVerify);
    }
    
    @Override
    public MailVerify create(User user) {
        return create(user, null, 24 * 60);
    }
    
    @Override
    public boolean verify(MailVerify mailVerify, CheckError checkError) {
        
        if (!Objects.equals(mailVerify.getMail(), mailVerify.getUser().getEmail())) {
            checkError.putError("校验错误");
            return false;
        }
        if (mailVerify.isVerified()) {
            checkError.putError("已经使用过");
            return false;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mailVerify.getCreatedAt());
        calendar.add(Calendar.MINUTE, mailVerify.getDurationOfValidity());
        if (new Date().after(calendar.getTime())) {
            checkError.putError("已经过期");
            return false;
        }
        mailVerify.setVerified(true);
        mailVerify.updatedAt();
        repository.save(mailVerify);
        return true;
    }
    
    /**
     * genCode(user.getId())
     * @param uid
     * @return
     */
    private String genCode(String uid) {
        return StringKit.md5(String.format("%d%s%f", System.currentTimeMillis(), uid, Math.random()));
    }
}
