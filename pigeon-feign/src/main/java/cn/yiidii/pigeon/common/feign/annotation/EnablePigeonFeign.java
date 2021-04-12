package cn.yiidii.pigeon.common.feign.annotation;

import cn.yiidii.pigeon.common.core.constant.BizConstants;
import cn.yiidii.pigeon.common.feign.config.FeignInterceptorConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 *  Feign注解
 * 
 * @author YiiDii Wang
 * @date 2021/4/12 14:58:55
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableFeignClients(value = BizConstants.BUSINESS_PACKAGE)
public @interface EnablePigeonFeign {
}
