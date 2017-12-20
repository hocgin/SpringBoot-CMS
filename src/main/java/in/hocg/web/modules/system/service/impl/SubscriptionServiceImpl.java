package in.hocg.web.modules.system.service.impl;

import com.google.common.collect.ImmutableMap;
import in.hocg.web.modules.base.Base2Service;
import in.hocg.web.modules.system.domain.notify.Subscription;
import in.hocg.web.modules.system.domain.notify.TargetType;
import in.hocg.web.modules.system.domain.repository.SubscriptionRepository;
import in.hocg.web.modules.system.domain.user.User;
import in.hocg.web.modules.system.service.notify.SubscriptionService;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by hocgin on 2017/12/19.
 * email: hocgin@gmail.com
 */
@Service
public class SubscriptionServiceImpl extends Base2Service<Subscription, String, SubscriptionRepository> implements SubscriptionService {
    private static final Map<String, String[]> NOTIFY_CONFIG = ImmutableMap.<String, String[]>builder()
            .put("like_post", Stream.of("comment").toArray(String[]::new))
            .build();
    
    @Override
    public void subscribe(User user, String target, TargetType targetType, String reason) {
        String[] actions = NOTIFY_CONFIG.get(reason);
        if (!ObjectUtils.isEmpty(actions)) {
            List<Subscription> subscriptions = Arrays.stream(actions)
                    .map(action -> new Subscription(target, targetType.name(), action, user))
                    .collect(Collectors.toList());
            repository.insert(subscriptions);
        }
    }
    
    @Override
    public void unsubscribe(User user, String target, TargetType targetType) {
        repository.deleteAllByUser_IdIsAndTargetIsAndTargetTypeIs(user.getId(), target, targetType.name());
    }
    
    @Override
    public List<Subscription> findAll(User user) {
        return repository.findAllByUser_Id(user.getId());
    }
}
