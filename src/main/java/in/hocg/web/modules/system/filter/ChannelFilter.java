package in.hocg.web.modules.system.filter;

import in.hocg.web.modules.base.BaseDomain;
import in.hocg.web.modules.base.filter.group.Insert;
import in.hocg.web.modules.base.filter.group.Update;
import in.hocg.web.modules.system.domain.Channel;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;

/**
 * Created by hocgin on 2017/12/4.
 * email: hocgin@gmail.com
 */
@Data
public class ChannelFilter extends BaseDomain {
    
    /**
     * 仅更新拥有
     */
    @NotBlank(message = "ID异常", groups = {Update.class})
    private String id;
    
    /**
     * 更新 与 增加 均拥有
     */
    @NotBlank(message = "栏目名称为必填", groups = {Update.class, Insert.class})
    @Pattern(regexp = "^[0-9a-zA-Z\\u4e00-\\u9fa5._]+", message = "栏目名称只允许由中文、英文、点、下划线组成", groups = {Update.class, Insert.class})
    private String name; // 名称: 2017年文章
    @NotBlank(message = "栏目类型为必填", groups = {Update.class, Insert.class})
    @Pattern(regexp = "[0]", message = "栏目类型错误", groups = {Update.class, Insert.class})
    private String type; // 类型 0 文章
    private String url;  // 路径.
    private String target; // a 标签打开方式
    private boolean available = Boolean.FALSE;
    
    /**
     * 仅增加拥有
     */
    private String parent;   // 父ID
    
    
    
    public Channel get() {
        Channel channel = new Channel();
        channel.setAvailable(available);
        channel.setParent(parent);
        channel.setTarget(target);
        channel.setName(name);
        channel.setUrl(url);
        channel.setType(Integer.valueOf(type));
        
        channel.createdAt();
        return channel;
    }
    
    public Channel update(Channel channel) {
        channel.setAvailable(available);
        channel.setTarget(target);
        channel.setName(name);
        channel.setUrl(url);
        channel.setType(Integer.valueOf(type));
        
        channel.updatedAt();
        return channel;
    }
}
