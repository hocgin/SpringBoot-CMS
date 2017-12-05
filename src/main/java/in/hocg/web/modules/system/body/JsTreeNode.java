package in.hocg.web.modules.system.body;

import com.fasterxml.jackson.annotation.JsonIgnore;
import in.hocg.web.lang.utils.tree.TreeNode;
import lombok.Data;

import java.util.List;

/**
 * Created by hocgin on 2017/12/5.
 * email: hocgin@gmail.com
 */
@Data
public class JsTreeNode implements TreeNode{
    
    /**
     * text : Root node 2
     * state : {"opened":true,"selected":true}
     * children : [{"text":"Child 1"},"Child 2"]
     */
    
    private String id;
    @JsonIgnore
    private String parent;
    private boolean hasChildren;
    private String text;
    private StateBean state;
    private List<JsTreeNode> children;
    
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
    
    @Data
    public static class StateBean {
        /**
         * opened : true
         * selected : true
         */
        
        private boolean opened;
        private boolean selected;
    }
}
