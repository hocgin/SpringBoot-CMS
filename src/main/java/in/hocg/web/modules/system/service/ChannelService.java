package in.hocg.web.modules.system.service;

import in.hocg.web.lang.CheckError;
import in.hocg.web.modules.system.domain.Channel;
import in.hocg.web.modules.system.filter.ChannelFilter;

import java.util.List;

/**
 * Created by hocgin on 2017/12/4.
 * email: hocgin@gmail.com
 */
public interface ChannelService {
    List<Channel> queryRoot();
    
    List<Channel> queryChildren(String parentId);
    
    Channel findOne(String id);
    
    void insert(ChannelFilter filter, CheckError checkError);
    
    void update(ChannelFilter filter, CheckError checkError);
    
    void available(String id, boolean available);
    
    void delete(String id, CheckError checkError);
    
    List<Channel> queryAllOrderByLocationAscAndPathAsc();
    
    void sort(String... id);
    
    Channel findOneByAlias(String alias);
}
