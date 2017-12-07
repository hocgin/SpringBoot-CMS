package in.hocg.web.modules.system.domain.repository;

import in.hocg.web.modules.system.domain.Comment;
import in.hocg.web.modules.system.domain.repository.custom.CommentRepositoryCustom;
import org.springframework.data.mongodb.datatables.repository.DataTablesRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by hocgin on 2017/12/7.
 * email: hocgin@gmail.com
 */
public interface CommentRepository extends MongoRepository<Comment, String>,
        DataTablesRepository<Comment, String>,
        CommentRepositoryCustom {
    void deleteAllByRootOrParentAndOidAndType(String root, String parent, String oid, Integer type);
}
