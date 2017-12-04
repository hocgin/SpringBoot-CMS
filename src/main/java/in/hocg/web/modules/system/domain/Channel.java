package in.hocg.web.modules.system.domain;

import in.hocg.web.lang.utils.tree.TreeNode;
import in.hocg.web.modules.base.BaseDomain;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by hocgin on 2017/12/4.
 * email: hocgin@gmail.com
 */
@Data
@Document(collection = "Channel")
public class Channel extends BaseDomain implements TreeNode {
    @Id
    private String id;
    
    private String name;
    /**
     * 路径值
     */
    private String path;
    /**
     * 父ID
     */
    private String parent;
    
    /**
     * 栏目的URL
     */
    private String url;
    
    /**
     * 打开方式 []
     */
    private String target;
    
    /**
     * 栏目的类型[0:文章]
     */
    private int type;
    
    private int location = 0;  // 排序, 默认:一级
    
    /**
     * 是否启用, 默认禁止。
     */
    private Boolean available = Boolean.FALSE;
    
    /**
     * 是否有子节点
     */
    private Boolean hasChildren = Boolean.FALSE;
    
    public static String type(int type) {
        switch (type) {
            case 0:
                return "文章";
            default:
                return "未知";
        }
    }
    
    @Override
    public String getId() {
        return id;
    }
    
    @Override
    public String getParent() {
        return parent;
    }
    
    @Override
    public boolean getHasChildren() {
        return hasChildren;
    }
}
