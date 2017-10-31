package in.hocg.web.modules.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * Created by hocgin on 2017/10/24.
 * email: hocgin@gmail.com
 */
@Data
@Document(collection = "Permission")
public class Permission implements Serializable {
    @Id
    private String id;
    
    private String name; // 名称: 用户添加
    private String type; // 资源类型，[menu|button]
    private String url;  // 资源路径.
    private String permission; // 权限字符串,menu例子：role:*，button例子：role:create,role:update,role:delete,role:view
    private String parent;   // 父ID
    private String path;
    private String target;
    private String icon;  // 图标 class
    private Boolean available = Boolean.FALSE;// 是否可用, 默认保留, 不分配。
    private boolean hasChildren = Boolean.FALSE; // 是否有子节点
    
}
