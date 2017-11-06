package in.hocg.web.lang;

import in.hocg.web.lang.body.response.Results;
import in.hocg.web.lang.exception.VailException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by hocgin on 2017/11/6.
 * email: hocgin@gmail.com
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = VailException.class)
    @ResponseBody
    public Results handle(VailException vailException) {
        return Results.error(vailException.getCode(), vailException.getMessage());
    }
    
    
    /**
     * 处理权限不足异常
     * @param exception
     * @return
     */
    @ExceptionHandler(value = AccessDeniedException.class)
    @ResponseBody
    public Results AccessDeniedExceptionHandle(AccessDeniedException exception) {
        return Results.error(HttpServletResponse.SC_UNAUTHORIZED, "权限不足");
    }
}
