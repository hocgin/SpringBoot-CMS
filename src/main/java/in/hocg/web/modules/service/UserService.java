package in.hocg.web.modules.service;

import in.hocg.web.filter.UserInsertFilter;
import in.hocg.web.filter.UserQueryFilter;
import in.hocg.web.filter.UserUpdateFilter;
import in.hocg.web.lang.CheckError;
import in.hocg.web.modules.domain.User;
import org.springframework.data.mongodb.datatables.mapping.DataTablesOutput;

import java.util.List;

/**
 * Created by hocgin on 2017/11/2.
 * email: hocgin@gmail.com
 */
public interface UserService {
    List<User> findAll();
    
    DataTablesOutput<User> data(UserQueryFilter input);
    
    void delete(String... id);
    
    void insert(UserInsertFilter user, CheckError checkError);
    
    void updateAvailable(String id, boolean available);
    
    User find(String id);
    
    List<User> findAllById(String... ids);
    
    void update(UserUpdateFilter filter, CheckError checkError);
    
    void update(User user);
    
    void addRoleToUser(String roleId, String... userIds);
    
    void removeRoleFormUser(String roleId, String... userIds);
    
    void removeDepartmentField(String... ids);
}
