package in.hocg.web.modules.base.body;

import in.hocg.web.lang.CheckError;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;

import java.io.Serializable;

/**
 * Created by hocgin on 2017/10/14.
 * email: hocgin@gmail.com
 * 结果对象
 */
public class Results<T> implements Serializable {
    
    private int code;
    private String message;
    private T data;
    
    private Results() {
    
    }
    
    public int getCode() {
        return code;
    }
    
    public Results setCode(int code) {
        this.code = code;
        return this;
    }
    
    public String getMessage() {
        return message;
    }
    
    public Results setMessage(String message) {
        this.message = message;
        return this;
    }
    
    public T getData() {
        return data;
    }
    
    public Results setData(T data) {
        this.data = data;
        return this;
    }
    
    
    public static Results success(Object data) {
        return Results.result(ResultCode.SUCCESS, "success", data);
    }
    
    public static Results success() {
        return Results.success(null);
    }
    
    public static Results error(Integer code, String message) {
        return Results.result(code, message, null);
    }
    
    public static Results result(Integer code, String message, Object data) {
        Results result = new Results<>();
        return result.setCode(code)
                .setMessage(message)
                .setData(data);
    }
    
    public static Results check(CheckError checkError, String success) {
        if (checkError.isPass()) {
            Results results = Results.success();
            if (!StringUtils.isEmpty(success)) {
                results.setMessage(success);
            }
            return results;
        } else {
            return Results.error(ResultCode.PRECONDITION_FAILED, checkError.getFirstErrorMessage());
        }
    }
    
    public static Results check(BindingResult bindingResult) {
        return Results.error(ResultCode.VERIFICATION_FAILED, bindingResult.getFieldError().getDefaultMessage());
    }
    
    public static Results check(CheckError checkError) {
        return check(checkError, null);
    }
    
    public ResponseEntity<Results> asOkResponseEntity() {
        return ResponseEntity
                .ok()
                .body(this);
    }
}
