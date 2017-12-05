package in.hocg.web.modules.system.domain.repository.custom;

/**
 * Created by hocgin on 2017/11/6.
 * email: hocgin@gmail.com
 */
public interface ArticlesRepositoryCustom {
    void deleteAllByChannelIdIn(String... channelsId);
}
