package in.hocg.web.abs;

import org.springframework.stereotype.Component;

/**
 * Created by hocgin on 2017/12/4.
 * email: hocgin@gmail.com
 */
@Component
public class IService extends BaseService<ADao> {
    
    public void test() {
        dao.to();
    }
}
