package cn.yiidii.pigeon.log.aspect;

import cn.yiidii.pigeon.common.core.util.SpringContextHolder;
import cn.yiidii.pigeon.common.core.util.WebUtils;
import cn.yiidii.pigeon.log.annotation.Log;
import cn.yiidii.pigeon.log.event.LogEvent;
import cn.yiidii.pigeon.rbac.api.form.OptLogForm;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * 日志切面
 *
 * @author YiiDii Wang
 * @date 2021/4/6 15:03:22
 */
@Slf4j
@Aspect
@Component
public class LogAspect {

    /**
     * 解析spel表达式
     */
    ExpressionParser parser = new SpelExpressionParser();

    /**
     * 将方法参数纳入Spring管理
     */
    LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();

    @Autowired
    private final ApplicationContext applicationContext;

    public LogAspect(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Pointcut("@annotation(cn.yiidii.pigeon.log.annotation.Log)")
    public void pointcut() {
    }

    /**
     * 配置环绕通知,使用在方法logPointcut()上注册的切入点
     *
     * @param point
     * @return
     * @throws Throwable
     */
    @Around("pointcut()")
    public Object recordLog(ProceedingJoinPoint point) throws Throwable {
        Object result;

        //　获取request
        HttpServletRequest request = WebUtils.getRequest();
        // 判断为空则直接跳过执行
        if (ObjectUtils.isEmpty(request)) {
            return point.proceed();
        }
        //　获取注解里的value值
        Method method = ((MethodSignature) point.getSignature()).getMethod();
        Log logAnn = method.getAnnotation(Log.class);
        // 打印执行时间
        long startTime = System.nanoTime();
        // 请求方法
        String url = request.getRequestURI();
        String reqMethod = request.getMethod();

        // 获取IP和地区

        // 参数
        Object[] args = point.getArgs();
        String spel = logAnn.content();
        String[] parameterNameArr = discoverer.getParameterNames(method);
        //将参数纳入Spring管理
        EvaluationContext context = new StandardEvaluationContext();
        for (int len = 0; len < parameterNameArr.length; len++) {
            context.setVariable(parameterNameArr[len], args[len]);
        }
        spel = parser.parseExpression(spel).getValue(context).toString();

        // 计算耗时
        long tookTime = 0L;
        try {
            result = point.proceed();
        } finally {
            tookTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);
        }

        OptLogForm optLogForm = OptLogForm.builder()
                .type("OPTLOG")
                .traceId("")
                .title("")
                .operation(spel)
                .url(url)
                .method(reqMethod)
                .params(JSONObject.toJSONString(args))
                .ip("")
                .executeTime(tookTime)
                .location("")
                .exception(logAnn.exception())
                .build();
        log.info("publish logEvent: {}", JSONObject.toJSON(optLogForm));
        SpringContextHolder.publishEvent(new LogEvent(optLogForm));
        return result;
    }

    /**
     * 配置异常通知
     *
     * @param point 切点
     * @param e     异常
     */
    @AfterThrowing(pointcut = "pointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint point, Throwable e) {
        log.info("Error Result: {}", e.getMessage());
    }

    private Method resolveMethod(ProceedingJoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Class<?> targetClass = point.getTarget().getClass();

        Method method = getDeclaredMethod(targetClass, signature.getName(),
                signature.getMethod().getParameterTypes());
        if (method == null) {
            throw new IllegalStateException("无法解析目标方法: " + signature.getMethod().getName());
        }
        return method;
    }

    private Method getDeclaredMethod(Class<?> clazz, String name, Class<?>... parameterTypes) {
        try {
            return clazz.getDeclaredMethod(name, parameterTypes);
        } catch (NoSuchMethodException e) {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null) {
                return getDeclaredMethod(superClass, name, parameterTypes);
            }
        }
        return null;
    }

}
