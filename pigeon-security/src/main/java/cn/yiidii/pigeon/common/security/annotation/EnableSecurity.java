package cn.yiidii.pigeon.common.security.annotation;

import cn.yiidii.pigeon.common.security.config.TokenStoreConfig;
import cn.yiidii.pigeon.common.security.service.PigeonUserDetailsService;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

import java.lang.annotation.*;

/**
 * 授权服务
 *
 * @author: YiiDii Wang
 * @create: 2021-02-11 17:44
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@EnableAuthorizationServer
@Import({

})
public @interface EnableSecurity {
}
