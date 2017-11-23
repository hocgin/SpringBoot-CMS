package in.hocg.web.modules.system.domain.repository.impl;

import in.hocg.web.modules.base.BaseMongoCustom;
import in.hocg.web.modules.system.domain.SysLog;
import in.hocg.web.modules.system.domain.repository.custom.SysLogRepositoryCustom;

import java.util.List;

/**
 * Created by hocgin on 2017/11/23.
 * email: hocgin@gmail.com
 */
public class SysLogRepositoryImpl
        extends BaseMongoCustom<SysLog, String>
        implements SysLogRepositoryCustom {
    @Override
    public List getTags() {
        return mongoTemplate().getCollection(SysLog.Document).distinct("tag");
    }
}
