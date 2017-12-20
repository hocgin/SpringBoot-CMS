package in.hocg.web.modules.system.domain.repository.custom;

import in.hocg.web.modules.base.body.Page;
import in.hocg.web.modules.system.domain.notify.UserNotify;

/**
 * Created by hocgin on 2017/12/20.
 * email: hocgin@gmail.com
 */
public interface UserNotifyRepositoryCustom {
    Page<UserNotify> pageByUser_IdIsAndNotify_IdInOrderByCreatedAtDesc(int page, int size, String userID, String... notifyIDs);
}
