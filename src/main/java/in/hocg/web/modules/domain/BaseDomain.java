package in.hocg.web.modules.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * Created by hocgin on 2017/10/28.
 * email: hocgin@gmail.com
 */
public abstract class BaseDomain implements Serializable {
    protected Date createdAt;
    protected Date updatedAt;
    protected Date deletedAt;
    protected Map<String, Object> exposed; // 扩展字段
}
