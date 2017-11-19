package in.hocg.web.global;

import in.hocg.web.modules.base.body.ResultCode;
import in.hocg.web.modules.base.body.Results;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Created by hocgin on 2017/11/19.
 * email: hocgin@gmail.com
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    
    @ExceptionHandler(value = MultipartException.class)
    @ResponseBody
    public Results handle(MultipartException e) {
        return Results.error(ResultCode.BAD_REQUEST, "未找到上传文件")
                .setData(e.getClass().getName());
    }
    
}
