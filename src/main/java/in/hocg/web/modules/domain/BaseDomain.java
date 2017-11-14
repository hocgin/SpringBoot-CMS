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
    public Date createdAt;
    public String createBy;
    public Date updatedAt;
    public String updatedBy;
    public Date deletedAt;
    public String deletedBy;
    
    @Transient
    protected Map<String, Object> exposed; // 扩展字段
    
    public BaseDomain updatedAt() {
        this.setUpdatedAt(new Date());
        return this;
    }
    
    public BaseDomain deletedAt() {
        this.setDeletedAt(new Date());
        return this;
    }
    
    public BaseDomain createdAt() {
        this.setCreatedAt(new Date());
        return this;
    }
}
