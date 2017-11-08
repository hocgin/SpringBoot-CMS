package in.hocg.web.modules.domain;

import lombok.Data;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * Created by hocgin on 2017/10/28.
 * email: hocgin@gmail.com
 */
@Data
public abstract class BaseDomain implements Serializable {
    protected Date createdAt;
    protected String createBy;
    protected Date updatedAt;
    protected String updatedBy;
    protected Date deletedAt;
    protected String deletedBy;
    
    @Transient
    protected Map<String, Object> exposed; // 扩展字段
}
