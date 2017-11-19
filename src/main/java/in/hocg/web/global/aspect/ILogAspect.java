package in.hocg.web.global.aspect;

import com.google.gson.Gson;
import in.hocg.web.lang.utils.RequestKit;
import in.hocg.web.lang.utils.ResponseKit;
import in.hocg.web.modules.admin.domain.SysLog;
import in.hocg.web.lang.utils.SecurityKit;
import in.hocg.web.modules.admin.service.SysLogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * Created by hocgin on 2017/11/17.
 * email: hocgin@gmail.com
 *
 * @ILog 处理
 */
@Aspect
@Component
public class ILogAspect {
    private Gson gson;
    private HttpServletRequest request;
    private SysLogService sysLogService;
    
    @Autowired
    public ILogAspect(Gson gson,
                      HttpServletRequest request,
                      SysLogService sysLogService) {
        this.gson = gson;
        this.request = request;
        this.sysLogService = sysLogService;
    }
    
    @Pointcut("execution(* in.hocg..*(..)) && @annotation(in.hocg.web.global.aspect.ILog)")
    public void ILogPointcut() {
    }
    
    @Around("ILogPointcut()")
    public Object ILogHandler(ProceedingJoinPoint point) {
        long start = System.currentTimeMillis();
        
        Object result = null;
        try {
            result = point.proceed();
            doLog(start, point, result, null);
        } catch (Throwable throwable) {
            doLog(start, point, null, throwable);
            throwable.printStackTrace();
        }
        
        
        return result;
    }
    
    private void doLog(long start, ProceedingJoinPoint point, Object result, Throwable e) {
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        Method method = methodSignature.getMethod();
        ILog annotation = method.getAnnotation(ILog.class);
        Class aClass = point.getSourceLocation().getWithinType();
        String src = String.format("%s#%s", aClass.getName(), method.getName());
        
        String msg = annotation.msg();
        try {
            SpelParserConfiguration config = new SpelParserConfiguration(true, true);
            ExpressionParser parser = new SpelExpressionParser(config);
            Expression expression = parser.parseExpression(msg);
            
            StandardEvaluationContext context = new StandardEvaluationContext();
            context.setVariable("args", point.getArgs());
            context.setVariable("request", RequestKit.get());
            context.setVariable("response", ResponseKit.get());
            context.setVariable("return", result);
            msg = expression.getValue(context, String.class);
        } catch (Throwable ignored) {
        }
        
        
        SysLog sysLog = SysLog.NEW(ObjectUtils.isEmpty(e) ? annotation.type().name() : SysLog.Type.ERROR.name(),
                annotation.value(),
                src,
                RequestKit.getClientIP(request),
                msg,
                gson.toJson(point.getArgs()),
                gson.toJson(result),
                SecurityKit.username(),
                annotation.from().getValue());
        sysLog.setUsageTime(System.currentTimeMillis() - start);
        sysLogService.save(sysLog);
    }
}
