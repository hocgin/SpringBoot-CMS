package in.hocg.web.modules.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by hocgin on 2017/10/24.
 * email: hocgin@gmail.com
 */
@Data
@Document(collection = "SysMenu")
public class SysMenu extends BaseDomain {
    @Transient
    public static String Document = "SysMenu";
    
    @Id
    private String id;
    
    private String name; // 名称: 用户添加
    private Integer type; // 资源类型，0 菜单 1 按钮
    private String url;  // 资源路径.
    private String permission; // 权限字符串,menu例子：role:*，button例子：role:create,role:update,role:delete,role:view
    private String parent;   // 父ID
    private String path;
    private String target;
    private String icon;  // 图标 class
    private Boolean available = Boolean.FALSE;// 是否可用, 默认保留, 不分配。
    private Boolean hasChildren = Boolean.FALSE; // 是否有子节点
    private Boolean builtIn = Boolean.FALSE; // 是否为内置, 默认False
    private Integer location = 0;  // 排序, 一级
    
    
    public static String type(Integer type) {
        switch (type) {
            case 0:
                return "菜单";
            case 1:
                return "数据";
            default:
                return "未知";
        }
    }
}
