package in.hocg.web.abs;

import org.springframework.stereotype.Component;

/**
 * Created by hocgin on 2017/12/4.
 * email: hocgin@gmail.com
 */
@Component
public class ADao implements BaseDao {
    @Override
    public void to() {
        System.out.println("ADao");
    }
}
