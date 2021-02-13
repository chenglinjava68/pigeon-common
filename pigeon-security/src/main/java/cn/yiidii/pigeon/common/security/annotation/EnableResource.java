package cn.yiidii.pigeon.common.security.annotation;

import cn.yiidii.pigeon.common.security.config.ResourceServerConfig;
import cn.yiidii.pigeon.common.security.config.TokenStoreConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author: YiiDii Wang
 * @create: 2021-02-11 17:44
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.TYPE})
@Documented
@EnableResourceServer
@Import({TokenStoreConfig.class, ResourceServerConfig.class})
@Configuration
public @interface EnableResource {
}
