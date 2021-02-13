package cn.yiidii.pigeon.common.security.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

import javax.annotation.PostConstruct;

/**
 * 资源服务器配置
 *
 * @author: YiiDii Wang
 * @create: 2021-02-13 21:49
 */
@Slf4j
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @PostConstruct
    public void init() {
        log.info("=== security === ResourceServerConfig init...");
    }

    @Autowired
    private TokenStore tokenStore;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .logout().disable()
                .authorizeRequests()
                .antMatchers("/test/**").permitAll()
                .antMatchers("/**").authenticated();
    }


    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("oauth-client").tokenStore(tokenStore);
    }


}
