package in.hocg.web.modules.system.filter;

import in.hocg.web.modules.base.filter.BaseFilter;
import in.hocg.web.modules.base.filter.group.Insert;
import in.hocg.web.modules.base.filter.group.Update;
import in.hocg.web.modules.system.domain.ShortUrl;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.Pattern;

/**
 * Created by hocgin on 2017/12/4.
 * email: hocgin@gmail.com
 */
@Data
public class ShortUrlFilter extends BaseFilter {
    /**
     * 仅更新拥有
     */
    @NotBlank(message = "ID异常", groups = {Update.class})
    private String id;
    
    /**
     * 更新 与 增加 均拥有
     */
    @NotBlank(message = "Code 不能为空", groups = {Update.class})
    @Pattern(regexp = "^(?!_).*$", message = "自定义 Code 不能以_开头", groups = {Insert.class, Update.class})
    private String code;
    
    /**
     * 仅增加拥有
     */
    @URL(message = "URL不合法", groups = {Insert.class})
    private String originalUrl; // 原始URL
    
    public ShortUrl get() {
        ShortUrl shortUrl = new ShortUrl();
        shortUrl.setAvailable(true);
        shortUrl.setCode(code);
        shortUrl.setOriginalUrl(originalUrl);
        shortUrl.createdAt();
        return shortUrl;
    }
    
    public ShortUrl update(ShortUrl shortUrl) {
        shortUrl.setCode(code);
        shortUrl.updatedAt();
        return shortUrl;
    }
}
