package in.hocg.web.modules.system.domain.repository;

import in.hocg.web.modules.system.domain.Member;
import in.hocg.web.modules.system.domain.repository.custom.MemberRepositoryCustom;
import org.springframework.data.mongodb.datatables.repository.DataTablesRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by hocgin on 2017/11/25.
 * email: hocgin@gmail.com
 */
public interface MemberRepository extends DataTablesRepository<Member, String>,
        MongoRepository<Member, String>,
        MemberRepositoryCustom {
    void deleteAllByIdIn(String... id);
    
    List<Member> findAllByIdIn(String... ids);
}
