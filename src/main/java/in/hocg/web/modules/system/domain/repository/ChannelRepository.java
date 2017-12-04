package in.hocg.web.modules.system.domain.repository;

import in.hocg.web.modules.system.domain.Channel;
import in.hocg.web.modules.system.domain.repository.custom.ChannelRepositoryCustom;
import org.springframework.data.mongodb.datatables.repository.DataTablesRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by hocgin on 2017/12/4.
 * email: hocgin@gmail.com
 */
public interface ChannelRepository extends MongoRepository<Channel, String>,
        DataTablesRepository<Channel, String>,
        ChannelRepositoryCustom {
//    @Query("{$orderby: {$and: {location: 1, path: 1}}")
    List<Channel> findAllByParentIn(String... parent);
    
    void deleteAllByIdIn(String[] id);
}
