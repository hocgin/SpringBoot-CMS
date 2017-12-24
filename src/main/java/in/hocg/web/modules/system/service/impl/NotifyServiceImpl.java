package in.hocg.web.modules.system.service.impl;

import in.hocg.web.lang.CheckError;
import in.hocg.web.modules.base.Base2Service;
import in.hocg.web.modules.system.domain.notify.Notify;
import in.hocg.web.modules.system.domain.notify.TargetType;
import in.hocg.web.modules.system.domain.repository.NotifyRepository;
import in.hocg.web.modules.system.domain.user.User;
import in.hocg.web.modules.system.service.UserService;
import in.hocg.web.modules.system.service.kit.NSNotifyService;
import in.hocg.web.modules.system.service.notify.NotifyService;
import in.hocg.web.modules.system.service.notify.UserNotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.mongodb.datatables.mapping.DataTablesInput;
import org.springframework.data.mongodb.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by hocgin on 2017/12/19.
 * email: hocgin@gmail.com
 */
@Service
public class NotifyServiceImpl extends Base2Service<Notify, String, NotifyRepository> implements NotifyService {
    
    private UserNotifyService userNotifyService;
    private UserService userService;
    NSNotifyService nsNotifyService;
    
    @Autowired
    public NotifyServiceImpl(@Lazy UserNotifyService userNotifyService,
                             NSNotifyService nsNotifyService,
                             UserService userService) {
        this.userNotifyService = userNotifyService;
        this.userService = userService;
        this.nsNotifyService = nsNotifyService;
    }
    
    @Override
    public void createAnnounce(String content, User sender) {
        repository.insert(new Notify(content, null, null, null, Notify.Type.Announce, sender));
        nsNotifyService.sendAnnouncement(content);
    }
    
    @Override
    public void createAnnounce(String content, String userID, CheckError checkError) {
        User sender = userService.findOne(userID);
        if (Objects.isNull(sender)) {
            checkError.putError("异常");
            return;
        }
        createAnnounce(content, sender);
    }
    
    @Override
    public void createRemind(String target, TargetType targetType, String action, String content, User sender) {
        repository.insert(new Notify(content, target, targetType.name(), action, Notify.Type.Remind, sender));
    }
    
    @Override
    public void createMessage(String content, User sender, User receiver) {
        Notify notify = repository.insert(new Notify(content, receiver.getId(),
                null, null, Notify.Type.Message, sender));
        userNotifyService.createUserNotify(receiver, Collections.singletonList(notify));
    }
    
    @Override
    public List<Notify> findAll(String target, String targetType, String action, Date createAt) {
        return repository.findAllByTargetIsAndTargetTypeIsAndActionIsAndCreatedAtAfter(target, targetType, action, createAt);
    }
    
    @Override
    public List<Notify> findAllByType(Notify.Type type, Date lastedAt) {
        return repository.findAllByTypeIsAndCreatedAtAfterOrderByCreatedAtDesc(type.name(), lastedAt);
    }
    
    @Override
    public List<Notify> findAllByType(String type) {
        return repository.findAllByTypeIs(type);
    }
    
    @Override
    public DataTablesOutput data(DataTablesInput filter) {
        return repository.findAll(filter);
    }
    
    @Override
    public void deletes(String... notifyIDs) {
        repository.deleteAllByIdIn(notifyIDs);
    }
    
}
