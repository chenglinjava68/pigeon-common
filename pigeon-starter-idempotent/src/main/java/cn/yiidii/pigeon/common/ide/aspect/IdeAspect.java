package cn.yiidii.pigeon.common.ide.aspect;

import cn.yiidii.pigeon.common.core.base.aspect.BaseAspect;
import cn.yiidii.pigeon.common.redis.core.RedisOps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

/**
 * 幂等处理切面
 *
 * @author: YiiDii Wang
 * @create: 2021-04-28 18:56
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
@ConditionalOnClass(RedisOps.class)
public class IdeAspect extends BaseAspect {

    @Pointcut("@annotation(cn.yiidii.pigeon.common.ide.annotation.Ide)")
    public void watchIde() {
    }

    @Before("watchIde()")
    public void doBefore(JoinPoint joinPoint) {
    
    }

    @After("watchIde()")
    public void doAfter(JoinPoint joinPoint) throws Throwable {

    }

}
