package cn.yiidii.pigeon.common.strategy.annotation;

import java.lang.annotation.*;

/**
 * 策略模式注解
 *
 * @author YiiDii Wang
 * @date 2021/6/6 12:00:20
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HandlerType {

    String bizCode();

    String beanName();

}
