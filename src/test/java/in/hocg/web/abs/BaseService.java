package in.hocg.web.abs;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by hocgin on 2017/12/4.
 * email: hocgin@gmail.com
 */
public class BaseService<T extends BaseDao> {
    @Autowired
    protected T dao;
}
