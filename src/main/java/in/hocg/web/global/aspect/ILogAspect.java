package in.hocg.web.global.aspect;

import com.google.common.base.Optional;
import com.google.gson.Gson;
import in.hocg.web.lang.utils.RequestKit;
import in.hocg.web.modules.domain.SysLog;
import in.hocg.web.modules.domain.repository.SysLogRepository;
import in.hocg.web.modules.security.SecurityKit;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.security.Security;

/**
 * Created by hocgin on 2017/11/17.
 * email: hocgin@gmail.com
 */
@Aspect
@Component
public class ILogAspect {
    private Gson gson;
    private HttpServletRequest request;
    private SysLogRepository sysLogRepository;
    
    @Autowired
    public ILogAspect(Gson gson,
                      HttpServletRequest request,
                      SysLogRepository sysLogRepository) {
        this.gson = gson;
        this.request = request;
        this.sysLogRepository = sysLogRepository;
    }
    
    @Pointcut("execution(* in.hocg..*(..)) && @annotation(in.hocg.web.global.aspect.ILog)")
    public void ILogPointcut() {
    }
    
    @Around("ILogPointcut()")
    public Object ILogHandler(ProceedingJoinPoint point) {
        long start = System.currentTimeMillis();
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        Method method = methodSignature.getMethod();
        ILog annotation = method.getAnnotation(ILog.class);
        SysLog.Type type = annotation.type();
        // 类的位置
        Class aClass = point.getSourceLocation().getWithinType();
        String src = String.format("%s#%s", aClass.getName(), method.getName());
        
        SysLog sysLog = SysLog.NEW(annotation.type().name(),
                annotation.tag(),
                src,
                RequestKit.getClientIP(request),
                annotation.value(),
                gson.toJson(point.getArgs()), null,
                null,
                annotation.from().getValue());
        if (SecurityKit.isLogged()) {
            sysLog.setUsername(SecurityKit.username());
        }
        if (SysLog.Type.BEFORE.equals(type)) {
            sysLog = sysLogRepository.save(sysLog);
        }
        
        Object result = null;
        try {
            result = point.proceed();
        } catch (Throwable throwable) {
            sysLog.setType(SysLog.Type.ERROR.name());
            sysLog.setResult(gson.toJson(throwable));
            sysLogRepository.save(sysLog);
            throwable.printStackTrace();
        }
    
    
        sysLog.setUsageTime(System.currentTimeMillis() - start);
        sysLog.setUsername(Optional.of(sysLog.getUsername())
                .or(SecurityKit.username()));
        sysLog.setResult(gson.toJson(result));
        sysLogRepository.save(sysLog);
        return result;
    }
}
