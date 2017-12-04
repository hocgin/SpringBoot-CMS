package in.hocg.web.modules.system.domain.repository.custom;

/**
 * Created by hocgin on 2017/12/4.
 * email: hocgin@gmail.com
 */
public interface ShortUrlRepositoryCustom {
    boolean alreadyExists(String code, String... notInIds);
    
}
