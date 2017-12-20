package in.hocg.web.modules.system.domain.notify;

import in.hocg.web.modules.system.domain.user.User;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;

/**
 * Created by hocgin on 2017/12/19.
 * email: hocgin@gmail.com
 */
@Data
//@Document(collection = "SubscriptionConfig")
@Deprecated
public class SubscriptionConfig {
    
    private Object action;
    
    @DBRef
    private User user;
}
