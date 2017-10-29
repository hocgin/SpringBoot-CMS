package in.hocg.web.modules.domain.repository.impl;

import in.hocg.web.modules.domain.Department;
import in.hocg.web.modules.domain.repository.custom.DepartmentCustom;

/**
 * Created by hocgin on 2017/10/29.
 * email: hocgin@gmail.com
 */
public class DepartmentCustomImpl
        extends BaseMongoCustom<Department, String>
        implements DepartmentCustom {
    
    public void iDelete(String id) {
    
    }
}
