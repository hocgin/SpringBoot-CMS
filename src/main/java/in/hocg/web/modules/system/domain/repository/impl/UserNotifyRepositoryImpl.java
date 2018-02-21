package in.hocg.web.modules.system.domain.repository.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBRef;
import in.hocg.web.modules.base.BaseMongoCustom;
import in.hocg.web.modules.base.body.Page;
import in.hocg.web.modules.system.domain.notify.Notify;
import in.hocg.web.modules.system.domain.notify.UserNotify;
import in.hocg.web.modules.system.domain.repository.custom.UserNotifyRepositoryCustom;
import in.hocg.web.modules.system.domain.user.User;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by hocgin on 2017/12/20.
 * email: hocgin@gmail.com
 */
public class UserNotifyRepositoryImpl extends BaseMongoCustom<UserNotify, String>
        implements UserNotifyRepositoryCustom {
    @Override
    public Page<UserNotify> pageByUserAndNotifyTypeOrderByCreatedAtDesc(int page, int size, String userID, String notifyType) {
        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where("user.$id").is(new ObjectId(userID)),
                Criteria.where("notify.type").in(notifyType)
        );
        Query query = Query.query(criteria).with(new Sort(new Sort.Order(Sort.Direction.DESC, "createdAt")));
        return pageX(query, (page - 1) < 0 ? 0 : (page - 1), size);
    }
    
    @Override
    public List<User> getMostRecentContact(String userID) {
        BasicDBObject basicDBObject = new BasicDBObject("user.$id", new ObjectId(userID));
        List userIDs = ((List<DBRef>) mongoTemplate().getCollection(UserNotify.COLLECTION)
                .distinct("notify.sender", basicDBObject)).stream()
                .map(DBRef::getId)
                .map(Object::toString)
                .map(ObjectId::new)
                .collect(Collectors.toList());
        return mongoTemplate().find(Query.query(Criteria.where("id").in(userIDs)), User.class);
    }
    
    @Override
    public Page<UserNotify> pageByUserIDsAndNotifySenderIDsAndNotifyTypeOrderByCreatedAtDesc(int page, int size,
                                                                                             String[] userIDs,
                                                                                             String[] senderIDs,
                                                                                             String notifyType) {
        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where("user.$id").in(Arrays.stream(userIDs)
                        .map(ObjectId::new)
                        .toArray(ObjectId[]::new)),
                Criteria.where("notify.type").in(notifyType),
                Criteria.where("notify.sender.$id").in(Arrays.stream(senderIDs)
                        .map(ObjectId::new)
                        .toArray(ObjectId[]::new))
        );
        Query query = Query.query(criteria).with(new Sort(new Sort.Order(Sort.Direction.DESC, "createdAt")));
        return pageX(query, (page - 1) < 0 ? 0 : (page - 1), size);
    }
    
    @Override
    public List<UserNotify> findAllUnreadyUserNotifyOrderByCreatedAtDesc(String userID, Notify.Type type) {
        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where("read").is(false),
                Criteria.where("notify.type").is(type.name()));
        return find(Query.query(criteria).with(new Sort(new Sort.Order(Sort.Direction.DESC, "createdAt"))));
    }
    
    
}

