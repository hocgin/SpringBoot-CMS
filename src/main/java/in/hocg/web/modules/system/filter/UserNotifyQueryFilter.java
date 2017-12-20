package in.hocg.web.modules.system.filter;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Created by hocgin on 2017/12/20.
 * email: hocgin@gmail.com
 */
@Data
public class UserNotifyQueryFilter {
    /**
     * 分页
     */
    private Integer size = 10;
    private Integer page = 1;
    
    @NotNull(message = "通知类型异常")
    private String type;
}
