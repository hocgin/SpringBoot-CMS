package in.hocg.web.database;

import in.hocg.web.modules.system.domain.Department;
import in.hocg.web.modules.system.domain.SysMenu;
import in.hocg.web.modules.system.domain.Role;
import in.hocg.web.modules.system.domain.User;

import java.util.Collection;
import java.util.Date;

/**
 * Created by hocgin on 2017/11/14.
 * email: hocgin@gmail.com
 */
public class DocumentFactory {
    /**
     * 单位对象
     *
     * @param path
     * @param name
     * @return
     */
    public static Department department(String name, String path) {
        Department department = new Department();
        department.setCreatedAt(new Date());
        department.setHasChildren(true);
        department.setBuiltIn(true);
        department.setPath(path);
        department.setName(name);
        return department;
    }
    
    /**
     * 角色
     *
     * @param role
     * @param name
     * @param department
     * @return
     */
    public static Role role(String role, String name,
                            Department department) {
        Role role1 = new Role();
        role1.setBuiltIn(true);
        role1.setRole(role);
        role1.setAvailable(true);
        role1.setName(name);
        role1.setDepartment(department);
        role1.setCreatedAt(new Date());
        return role1;
    }
    
    /**
     * 菜单, 菜单类型
     * @param name
     * @param path
     * @param flag
     * @param url
     * @return
     */
    public static SysMenu menu(String name, String path, String flag, String url) {
        SysMenu menu = new SysMenu();
        menu.setBuiltIn(true);
        menu.setName(name);
        menu.setHasChildren(true);
        menu.setPath(path);
        menu.setAvailable(true);
        menu.setPermission(flag);
        menu.setType(0);
        menu.setCreatedAt(new Date());
        menu.setUrl(url);
        menu.setTarget("data-pjax");
        return menu;
    }
    
    /**
     * 菜单, 数据类型
     * @param name
     * @param path
     * @param flag
     * @return
     */
    public static SysMenu data(String name, String path, String flag, String parent) {
        SysMenu menu = new SysMenu();
        menu.setBuiltIn(true);
        menu.setName(name);
        menu.setHasChildren(false);
        menu.setPath(path);
        menu.setAvailable(true);
        menu.setPermission(flag);
        menu.setParent(parent);
        menu.setType(1);
        menu.setCreatedAt(new Date());
        return menu;
    }
    
    /**
     * 后台用户
     * @param username
     * @param roles
     * @param email
     * @param password
     * @return
     */
    public static User user(String username,
                            Collection<Role> roles,
                            String email,
                            String password) {
        User user = new User();
        user.setCreatedAt(new Date());
        user.setAvailable(true);
        user.setBuiltIn(true);
        user.setUsername(username);
        user.setRole(roles);
        user.setEmail(email);
        user.setPassword(password);
        return user;
    }
}
