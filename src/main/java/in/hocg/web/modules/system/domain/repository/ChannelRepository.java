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
    
    void deleteAllByIdIn(String[] id);
    
    
    
    /**
     * 用正则匹配查询目录路径的值
     *
     * @param path
     * @return
     */
    List<Channel> findAllByPathRegex(String path);
}
