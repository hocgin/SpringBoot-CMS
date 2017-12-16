package in.hocg.web.modules.system.service;

import in.hocg.web.lang.CheckError;
import in.hocg.web.modules.system.domain.user.User;
import in.hocg.web.modules.system.filter.MemberDataTablesInputFilter;
import in.hocg.web.modules.system.filter.MemberFilter;
import org.springframework.data.mongodb.datatables.mapping.DataTablesOutput;

import java.util.List;

/**
 * Created by hocgin on 2017/11/26.
 * email: hocgin@gmail.com
 */
public interface MemberService {
    List<User> findAll();
    
    DataTablesOutput<User> data(MemberDataTablesInputFilter input);
    
    void delete(CheckError checkError, String... id);
    
    void insert(MemberFilter filter, CheckError checkError);
    
    void updateAvailable(String id, boolean available);
    
    User find(String id);
    
    List<User> findAllById(String... ids);
    
    void update(MemberFilter filter, CheckError checkError);
    
    void update(User member);
    
    void verifyEmail(String id, CheckError checkError);
    
    void sendVerifyEmail(String id, CheckError checkError);
    
    User findOneByToken(String token);
    
    void updateTokenAvailable(String id, boolean available);
    
    void resumeToken();
    
    List<User> findAllByRoles(String... role);
    
    User findByEmail(String email);
}
