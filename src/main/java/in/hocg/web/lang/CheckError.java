package in.hocg.web.lang;

import java.util.HashMap;

/**
 * Created by hocgin on 2017/10/23.
 * email: hocgin@gmail.com
 * 存储检测异常
 */
public class CheckError extends HashMap<String, String> {
    private CheckError() {
    
    }
    
    public static CheckError get() {
        return new CheckError();
    }
    
    public boolean isPass() {
        return this.size() == 0;
    }
    
    /**
     * 获取第一个错误
     * @return
     */
    public String getFirstErrorMessage() {
        if (keySet().isEmpty()) {
            return "";
        } else {
            return get(keySet().toArray()[0]);
        }
    }
    
    public String getError() {
        return get("ERROR");
    }
    
    /**
     * 获取所有错误
     * @return
     */
    public String[] getAllErrorMessage() {
        return values().toArray(new String[]{});
    }
    
    public CheckError putError(String message) {
        put("ERROR", message);
        return this;
    }
}
