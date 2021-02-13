package cn.yiidii.pigeon.common.security.config;

import cn.yiidii.pigeon.common.core.constant.ContextConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import javax.annotation.PostConstruct;

/**
 * @author: YiiDii Wang
 * @create: 2021-02-12 10:29
 */
@Slf4j
@Configuration
public class TokenStoreConfig {

    @PostConstruct
    public void init() {
        log.info("=== security === TokenStoreConfig init...");
    }

    @Bean
    public TokenStore tokenStore() {
        //JWT令牌存储方案
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        //对称秘钥，资源服务器使用该秘钥来验证
        converter.setSigningKey(ContextConstants.JWT_SIGNING_KEY);
        return converter;
    }

}
