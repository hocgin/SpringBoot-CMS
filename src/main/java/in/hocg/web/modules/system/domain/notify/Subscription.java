package in.hocg.web.modules.system.domain.notify;

import in.hocg.web.modules.base.BaseDomain;
import in.hocg.web.modules.system.domain.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by hocgin on 2017/12/19.
 * email: hocgin@gmail.com
 */
@Data
@Document(collection = "Subscriptions")
@NoArgsConstructor
public class Subscription extends BaseDomain {
    @Id
    private String id;
    private String target;
    private String targetType;
    private String action;
    @DBRef
    private User user;
    
    public Subscription(String target, String targetType, String action, User user) {
        this.target = target;
        this.targetType = targetType;
        this.action = action;
        this.user = user;
        createdAt();
    }
}
