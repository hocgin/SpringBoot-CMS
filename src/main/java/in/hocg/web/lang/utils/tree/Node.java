package in.hocg.web.lang.utils.tree;

import lombok.Data;

import java.util.List;

/**
 * Created by hocgin on 2017/12/4.
 * email: hocgin@gmail.com
 */
@Data
public class Node<T extends TreeNode> {
    T node;
    List<Node<T>> children;
}
