package in.hocg.web.modules.system.service;

import in.hocg.web.lang.CheckError;
import in.hocg.web.lang.utils.tree.Node;
import in.hocg.web.modules.system.domain.Role;
import in.hocg.web.modules.system.domain.SysMenu;
import in.hocg.web.modules.system.domain.user.User;
import in.hocg.web.modules.system.filter.UserDataTablesInputFilter;
import in.hocg.web.modules.system.filter.UserFilter;
import org.springframework.data.mongodb.datatables.mapping.DataTablesOutput;

import java.util.Collection;
import java.util.List;

/**
 * Created by hocgin on 2017/11/2.
 * email: hocgin@gmail.com
 */
public interface ManagerService {
    List<User> findAll();
    
    DataTablesOutput<User> data(UserDataTablesInputFilter input);
    
    void delete(CheckError checkError, String... id);
    
    void insert(UserFilter user, CheckError checkError);
    
    void updateAvailable(String id, boolean available);
    
    User find(String id);
    
    List<User> findAllById(String... ids);
    
    void update(UserFilter filter, CheckError checkError);
    
    void update(User user);
    
    void addRoleToUser(String roleId, String... userIds);
    
    void removeRoleFormUser(String roleId, String... userIds);
    
    void removeDepartmentField(String... ids);
    
    Collection<Role> findRoleByUser(String id);
    
    Collection<SysMenu> findSysMenuByUser(String id);
    
    List<Node<SysMenu>> getLeftMenu(String id);
    
    
    List<User> findAllByRoles(String... rolesId);
}
