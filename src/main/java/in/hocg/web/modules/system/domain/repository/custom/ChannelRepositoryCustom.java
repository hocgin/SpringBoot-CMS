package in.hocg.web.modules.system.domain.repository.custom;

import in.hocg.web.modules.system.domain.Channel;

import java.util.List;

/**
 * Created by hocgin on 2017/12/4.
 * email: hocgin@gmail.com
 */
public interface ChannelRepositoryCustom {
    
    void updateHasChildren(String id, boolean hasChildren);
    
    /**
     * 级联删除
     *
     * @param regexPath
     */
    void deleteAllByPathRegex(String regexPath);
    
    /**
     * 用正则匹配查询目录路径的值, 并进行降序
     *
     * @param regexPath
     * @return
     */
    List<Channel> findAllByPathRegexOrderByPathDesc(String regexPath);
    
    
    List<Channel> findAllByIdOrderByPathAsc(String... id);
    
    void updateLocation(String... ids);
    
    /**
     * path 00010002 -> 00010001 -> 0001
     * location 0 -> 1 -> 2
     *
     * @return
     */
    List<Channel> findAllOrderByLocationAscAndPathAsc();
    
    List<Channel> findAllByParentInOrderByLocationAscAndPathAsc(String... parent);
}
