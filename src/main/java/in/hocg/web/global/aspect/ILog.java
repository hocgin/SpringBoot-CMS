package in.hocg.web.global.aspect;

import in.hocg.web.modules.domain.SysLog;

import java.lang.annotation.*;

/**
 * Created by hocgin on 2017/11/17.
 * email: hocgin@gmail.com
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ILog {
    String value();
    
    String tag();
    
    SysLog.From from() default SysLog.From.Admin;
    
    SysLog.Type type() default SysLog.Type.AFTER;
}
