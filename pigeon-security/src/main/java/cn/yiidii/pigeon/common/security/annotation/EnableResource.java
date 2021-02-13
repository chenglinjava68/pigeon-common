package cn.yiidii.pigeon.common.security.annotation;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author: YiiDii Wang
 * @create: 2021-02-11 17:44
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ java.lang.annotation.ElementType.TYPE })
@Documented
@Import({ })
@Configuration
public @interface EnableResource {
}
