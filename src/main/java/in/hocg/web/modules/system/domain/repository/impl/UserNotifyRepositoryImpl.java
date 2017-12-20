package in.hocg.web.modules.system.domain.repository.impl;

import in.hocg.web.modules.base.BaseMongoCustom;
import in.hocg.web.modules.base.body.Page;
import in.hocg.web.modules.system.domain.notify.UserNotify;
import in.hocg.web.modules.system.domain.repository.custom.UserNotifyRepositoryCustom;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Arrays;

/**
 * Created by hocgin on 2017/12/20.
 * email: hocgin@gmail.com
 */
public class UserNotifyRepositoryImpl extends BaseMongoCustom<UserNotify, String>
        implements UserNotifyRepositoryCustom {
    @Override
    public Page<UserNotify> pageByUser_IdIsAndNotify_IdInOrderByCreatedAtDesc(int page, int size, String userID, String... notifyIDs) {
        Criteria criteria = Criteria.where("user.$id").is(new ObjectId(userID))
                .and("notify.$id").in(Arrays.stream(notifyIDs).map(ObjectId::new).toArray(ObjectId[]::new));
        Query query = Query.query(criteria).with(new Sort(new Sort.Order(Sort.Direction.DESC, "createdAt")));
        return pageX(query, (page - 1) < 0 ? 0 : (page - 1), size);
    }
    
}
