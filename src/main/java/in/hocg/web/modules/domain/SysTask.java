package in.hocg.web.modules.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by hocgin on 2017/11/17.
 * email: hocgin@gmail.com
 */

@Document(collection = "SysTask")
@Data
public class SysTask extends BaseDomain {
    @Transient
    public static String Document = "SysTask";
    
    @Id
    private String id;
    
    private String name;
    
    /**
     * 执行参数
     */
    private String data;
    
    private String cron;
    
    private String note;
    
    private String execClass;
    
    private Boolean available = Boolean.FALSE;// 是否可用, 默认不可用。
    
    /**
     * 消耗时间
     */
    private long usageTime;
}
