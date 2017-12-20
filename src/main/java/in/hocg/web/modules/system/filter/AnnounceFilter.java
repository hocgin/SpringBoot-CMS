package in.hocg.web.modules.system.filter;

import in.hocg.web.modules.base.BaseDomain;
import in.hocg.web.modules.base.filter.group.Insert;
import in.hocg.web.modules.base.filter.group.Update;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by hocgin on 2017/12/20.
 * email: hocgin@gmail.com
 */
@Data
public class AnnounceFilter extends BaseDomain {
    
    /**
     * 仅更新拥有
     */
    @NotBlank(message = "ID异常", groups = {Update.class})
    private String id;
    
    /**
     * 仅增加拥有
     */
    @NotBlank(message = "文章内容为必填", groups = {Insert.class})
    private String content;
}
