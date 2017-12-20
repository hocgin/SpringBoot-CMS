package in.hocg.web.modules.system.domain.notify;

import in.hocg.web.modules.base.BaseDomain;
import in.hocg.web.modules.system.domain.user.User;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by hocgin on 2017/12/19.
 * email: hocgin@gmail.com
 * - 遍历 订阅(Subscription)表 拉取公告(Announce) 和 提醒(Remind)的时候创建
 * - 新建信息(Message)之后，立刻创建。
 */
@Data
@Document(collection = "UserNotifies")
public class UserNotify extends BaseDomain {
    @Id
    private String id;
    
    private boolean read;
    @DBRef
    private User user;
    @DBRef
    private Notify notify;
    
    public UserNotify(User user, Notify notify) {
        this.read = false;
        this.user = user;
        this.notify = notify;
        createdAt();
    }
}
