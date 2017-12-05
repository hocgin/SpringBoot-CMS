package in.hocg.web.lang.utils.tree;

import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by hocgin on 2017/12/5.
 * email: hocgin@gmail.com
 */
public class TreeKit {
    
//    public void testQueryMenuList() {
//        // 原始的数据
//        List<SysMenu> rootMenu = service.queryAllOrderByLocationAscAndPathAsc();
//
//        // 查看结果
//        for (SysMenu menu : rootMenu) {
//            System.out.println(menu);
//        }
//        // 最后的结果
//        List<Node<SysMenu>> menuList = new ArrayList<>();
//        // 先找到所有的一级菜单
//        for (int i = 0; i < rootMenu.size(); i++) {
//            // 一级菜单没有parentId
//            if (StringUtils.isEmpty(rootMenu.get(i).getParent())) {
//                Node<SysMenu> node = new Node<>();
//                node.setNode(rootMenu.get(i));
//                menuList.add(node);
//            }
//        }
//        // 为一级菜单设置子菜单，getChild是递归调用的
//        for (Node<SysMenu> menu : menuList) {
//            List<Node<SysMenu>> nodes = getChild(menu.getNode().getId(), rootMenu);
//            menu.setChildren(nodes);
//        }
//        Map<String, Object> jsonMap = new HashMap<>();
//        jsonMap.put("menu", menuList);
//        System.out.println(gson.toJson(jsonMap));
//
//    }
    
    public static  <T extends TreeNode> List<Node<T>> getChild(String id, Collection<T> rootMenu) {
        // 子菜单
        List<Node<T>> childList = new ArrayList<>();
        for (T menu : rootMenu) {
            // 遍历所有节点，将父菜单id与传过来的id比较
            if (!StringUtils.isEmpty(menu.getParent())
                    && menu.getParent().equals(id)) {
                Node<T> node = new Node<>();
                node.setNode(menu);
                childList.add(node);
            }
        }
        // 把子菜单的子菜单再循环一遍
        for (Node<T> menu : childList) {// 没有url子菜单还有子菜单
            if (menu.getNode().getHasChildren()) {
                // 递归
                menu.setChildren(getChild(menu.getNode().getId(), rootMenu));
            }
        }
        // 递归退出条件
        if (childList.isEmpty()) {
            return null;
        }
        return childList;
    }
}
