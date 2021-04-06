package cn.yiidii.pigeon.log.aspect;

import cn.hutool.core.util.StrUtil;
import cn.yiidii.pigeon.common.core.constant.StringPool;
import cn.yiidii.pigeon.common.core.util.WebUtils;
import cn.yiidii.pigeon.log.annotation.Log;
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
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
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
        Method targetMethod = resolveMethod(point);
        Log logAnn = targetMethod.getAnnotation(Log.class);
        // 打印执行时间
        long startTime = System.nanoTime();
        // 请求方法
        String url = request.getRequestURI();
        String method = request.getMethod();

        // 获取IP和地区

        // 参数
        String requestParam = getArgs(point.getArgs(), request);

        // 计算耗时
        long tookTime = 0L;
        try {
            result = point.proceed();
        } finally {
            tookTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);
        }

        JSONObject jo = new JSONObject();
        jo.put("url", url);
        jo.put("method", method);
        jo.put("requestParam", requestParam);
        jo.put("tookTime", tookTime);
        jo.put("content", logAnn.value());

        // 这里先只打印
        log.info(StrUtil.format("操作日志: {}", jo.toJSONString()));

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

    /**
     * 获取请求参数
     *
     * @param args
     * @param request
     * @return
     */
    private String getArgs(Object[] args, HttpServletRequest request) {
        String strArgs = StringPool.EMPTY;

        try {
            if (!request.getContentType().contains("multipart/form-data")) {
                strArgs = JSONObject.toJSONString(args);
            }
        } catch (Exception e) {
            try {
                strArgs = Arrays.toString(args);
            } catch (Exception ex) {
                log.warn("解析参数异常", ex);
            }
        }
        return strArgs;
    }


}
