package in.hocg.web.modules.weather.domain.repository.custom;

import in.hocg.web.modules.weather.domain.RequestCache;

/**
 * Created by hocgin on 2017/11/23.
 * email: hocgin@gmail.com
 */
public interface RequestCacheRepositoryCustom {
    RequestCache findByParamOnToday(String param, String type);
}
