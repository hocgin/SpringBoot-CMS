package in.hocg.web.modules.system.domain.repository.custom;

import in.hocg.web.modules.system.domain.Articles;

import java.util.List;

/**
 * Created by hocgin on 2017/11/6.
 * email: hocgin@gmail.com
 */
public interface ArticlesRepositoryCustom {
    void deleteAllByChannelIdIn(String... channelsId);
    
    List<Articles> findAllByChannel(String channelID);
}
