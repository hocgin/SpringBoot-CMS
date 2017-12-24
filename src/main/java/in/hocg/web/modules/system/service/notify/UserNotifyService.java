package in.hocg.web.modules.system.service.notify;

import in.hocg.web.lang.CheckError;
import in.hocg.web.modules.base.body.Page;
import in.hocg.web.modules.im.filter.ChatLogQueryFilter;
import in.hocg.web.modules.system.domain.notify.Notify;
import in.hocg.web.modules.system.domain.notify.UserNotify;
import in.hocg.web.modules.system.domain.user.User;
import in.hocg.web.modules.system.filter.UserNotifyQueryFilter;

import java.util.List;

/**
 * Created by hocgin on 2017/12/19.
 * email: hocgin@gmail.com
 */
public interface UserNotifyService {
    
    /**
     * 拉取最后一条公告
     *
     * @param user the user
     */
    void pullAnnounce(User user);
    
    /**
     * 拉取通知
     *
     * @param user the user
     */
    void pullRemind(User user);
    
    
    /**
     * 获取用户通知
     *
     * @param user
     * @return
     */
    @Deprecated
    List<UserNotify> getUserNotify(User user);
    
    /**
     * 设置为已读
     *
     * @param userID
     * @param userNotifyIDs
     */
    void read(String userID, String... userNotifyIDs);
    
    /**
     * 创建一条 UserNotify
     * @param user
     * @param notifies
     * @return
     */
    List<UserNotify> createUserNotify(User user, List<Notify> notifies);
    
    
    
    
    
    @Deprecated
    List<UserNotify> getUserNotify(String userID);
    
    Page<UserNotify> pager(UserNotifyQueryFilter filter, String userID, CheckError checkError);
    
    
    List<User> getMostRecentContact(String userID);
    
    Page<UserNotify> getChatLog(ChatLogQueryFilter filter);
}
