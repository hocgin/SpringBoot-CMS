package in.hocg.web.lang.exception;

/**
 * Created by hocgin on 2017/10/14.
 * email: hocgin@gmail.com
 */
public class VailException extends RuntimeException {
    private Integer code;
    
    public VailException(Integer code, String message) {
        super(message);
        this.code = code;
    }
    
    public Integer getCode() {
        return code;
    }
    
    public void setCode(Integer code) {
        this.code = code;
    }
}
