package in.hocg.web.global.aspect;

import in.hocg.web.modules.admin.domain.SysLog;

import java.lang.annotation.*;

/**
 * Created by hocgin on 2017/11/17.
 * email: hocgin@gmail.com
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ILog {
    /**
     * 标志,例如 [后台登陆]
     *
     * @return
     */
    String value();
    
    /**
     * 消息内容, 支持SpEL
     * - #args
     * - #request
     * - #response
     * - #return
     * @return
     */
    String msg() default "''";
    
    SysLog.From from() default SysLog.From.Admin;
    
    SysLog.Type type() default SysLog.Type.INFO;
}
