package in.hocg.web.lang.utils;

import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * Created by hocgin on 2017/11/14.
 * email: hocgin@gmail.com
 */
public class RequestKit {
    
    /**
     * 获取客户端真实IP
     *
     * @param request
     * @return
     */
    public static String getClientIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (!StringUtils.isEmpty(ip)
                && !"unKnown".equalsIgnoreCase(ip)) {
            int index = ip.indexOf(",");
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if (!StringUtils.isEmpty(ip)
                && !"unKnown".equalsIgnoreCase(ip)) {
            return ip;
        }
    
        /**
         * 本地名单
         */
        if (Arrays.asList(new String[]{
                "0:0:0:0:0:0:0:1",
                "127.0.0.1"
        }).contains(request.getRemoteAddr())) {
            return "110.80.68.212";
        }
        return request.getRemoteAddr();
    }
    
    /**
     * 获取 User-Agent
     * User-Agent:Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.89 Safari/537.36
     *
     * @param request
     * @return
     */
    public static String getUserAgent(HttpServletRequest request) {
        return request.getHeader("User-Agent");
    }
    
    /**
     * 获取请求对象
     *
     * @return
     */
    public static HttpServletRequest get() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }
    
    public static boolean isAjax(HttpServletRequest request) {
        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    }
}
