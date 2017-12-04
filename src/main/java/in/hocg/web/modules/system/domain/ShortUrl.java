package in.hocg.web.modules.system.domain;

import in.hocg.web.modules.base.BaseDomain;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by hocgin on 2017/12/4.
 * email: hocgin@gmail.com
 */
@Data
@Document(collection = "ShortUrl")
public class ShortUrl extends BaseDomain {
    @Id
    private String id;
    
    /**
     * 原始URL
     */
    private String originalUrl;
    
    /**
     * 生成Code
     */
    private String code;
    
    // 是否启用, 默认禁止
    private Boolean available = Boolean.FALSE;
    
}
