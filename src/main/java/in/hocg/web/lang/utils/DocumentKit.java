package in.hocg.web.lang.utils;

import org.springframework.util.StringUtils;

/**
 * Created by hocgin on 2017/11/14.
 * email: hocgin@gmail.com
 *
 */
public class DocumentKit {
    
    /**
     * DocumentKit
     * @param path
     * @return
     */
    public static String getParentId(String path) {
        if (!StringUtils.isEmpty(path) && path.length() > 4) {
            return path.substring(0, path.length() - 4);
        }
        return "";
    }
}
