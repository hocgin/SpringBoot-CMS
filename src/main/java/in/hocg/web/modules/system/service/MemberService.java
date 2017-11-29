package in.hocg.web.modules.system.service;

import in.hocg.web.lang.CheckError;
import in.hocg.web.modules.system.domain.Member;
import in.hocg.web.modules.system.filter.MemberDataTablesInputFilter;
import in.hocg.web.modules.system.filter.MemberFilter;
import org.springframework.data.mongodb.datatables.mapping.DataTablesOutput;

import java.util.List;

/**
 * Created by hocgin on 2017/11/26.
 * email: hocgin@gmail.com
 */
public interface MemberService {
    List<Member> findAll();
    
    DataTablesOutput<Member> data(MemberDataTablesInputFilter input);
    
    void delete(CheckError checkError, String... id);
    
    void insert(MemberFilter filter, CheckError checkError);
    
    void updateAvailable(String id, boolean available);
    
    Member find(String id);
    
    List<Member> findAllById(String... ids);
    
    void update(MemberFilter filter, CheckError checkError);
    
    void update(Member member);
    
    void verifyEmail(String id, CheckError checkError);
    
    void sendVerifyEmail(String id, CheckError checkError);
    
    Member findOneByToken(String token);
    
    void updateTokenAvailable(String id, boolean available);
}
