package in.hocg.web.modules.system.domain.repository.impl;

import in.hocg.web.modules.base.BaseMongoCustom;
import in.hocg.web.modules.system.domain.Articles;
import in.hocg.web.modules.system.domain.repository.custom.ArticlesRepositoryCustom;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * Created by hocgin on 2017/12/5.
 * email: hocgin@gmail.com
 */
public class ArticlesRepositoryImpl
        extends BaseMongoCustom<Articles, String>
        implements ArticlesRepositoryCustom {
    @Override
    public void deleteAllByChannelIdIn(String... channelsId) {
        remove(Query.query(Criteria.where("channel.id").in(channelsId)));
    }
    
    @Override
    public List<Articles> findAllByChannel(String channelID) {
        return find(Query.query(Criteria.where("channel.id").is(channelID)));
    }
}
