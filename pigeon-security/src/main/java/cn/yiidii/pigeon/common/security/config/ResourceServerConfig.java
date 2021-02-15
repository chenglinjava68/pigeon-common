package cn.yiidii.pigeon.common.security.config;

import cn.yiidii.pigeon.common.security.handler.PigeonAccessDeniedHandler;
import cn.yiidii.pigeon.common.security.handler.PigeonAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 资源服务器配置
 *
 * @author: YiiDii Wang
 * @create: 2021-02-13 21:49
 */
@Slf4j
@Import({PigeonAccessDeniedHandler.class, PigeonAuthenticationEntryPoint.class})
@RequiredArgsConstructor
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    private final PigeonAccessDeniedHandler pigeonAccessDeniedHandler;
    private final PigeonAuthenticationEntryPoint pigeonAuthenticationEntryPoint;

    @PostConstruct
    public void init() {
        log.info("=== security === ResourceServerConfig init...");
    }

    @Autowired
    private IgnoreUrlProperties ignoreUrlProperties;

    @Autowired
    private TokenStore tokenStore;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        List<String> ignoreUrls = ignoreUrlProperties.getIgnoreUrls();
        log.info("ResourceServerConfig ignoreUrls: {}", ignoreUrls);
        http
                .csrf().disable()
                .logout().disable()
                .authorizeRequests()
                .antMatchers(ignoreUrls.toArray(new String[ignoreUrls.size()])).permitAll()
                .antMatchers("/**").authenticated();
    }


    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources
                .resourceId("oauth-client")
                .tokenStore(tokenStore)
                .accessDeniedHandler(pigeonAccessDeniedHandler)
                .authenticationEntryPoint(pigeonAuthenticationEntryPoint);
    }


}
