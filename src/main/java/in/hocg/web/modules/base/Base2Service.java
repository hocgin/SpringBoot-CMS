package in.hocg.web.modules.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.io.Serializable;

/**
 * Created by hocgin on 2017/12/4.
 * email: hocgin@gmail.com
 */
public abstract class Base2Service<T, ID extends Serializable, Z extends MongoRepository<T, ID>> {
    @Autowired
    protected Z repository;
    
    protected Z repository() {
        return this.repository;
    }
    
    public T findOne(ID id) {
        return repository.findOne(id);
    }
    
    public void delete(ID id) {
        repository.delete(id);
    }
    
}