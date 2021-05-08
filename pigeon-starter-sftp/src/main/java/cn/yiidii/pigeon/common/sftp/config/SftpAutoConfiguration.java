package cn.yiidii.pigeon.common.sftp.config;

import cn.yiidii.pigeon.common.sftp.factory.SftpFactory;
import cn.yiidii.pigeon.common.sftp.factory.SftpPool;
import cn.yiidii.pigeon.common.sftp.prop.SftpProperties;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * sftp自动配置类
 *
 * @author: YiiDii Wang
 * @create: 2021-05-08 14:28
 */
@Configuration
@Import(SftpProperties.class)
@ConditionalOnClass(SftpProperties.class)
public class SftpAutoConfiguration {

    @Bean
    public SftpFactory sftpFactory() {
        return new SftpFactory();
    }

    @Bean
    @ConditionalOnBean(SftpFactory.class)
    public SftpPool sftpPool(SftpFactory factory, SftpProperties sftpProperties) {
        GenericKeyedObjectPoolConfig poolConfig = new GenericKeyedObjectPoolConfig();
        poolConfig.setMaxTotalPerKey(sftpProperties.getPool().getMaxTotal());
        poolConfig.setMaxIdlePerKey(sftpProperties.getPool().getMaxIdle());
        poolConfig.setMinIdlePerKey(sftpProperties.getPool().getMinIdle());
        poolConfig.setMaxWaitMillis(sftpProperties.getPool().getMaxWaitMillis());
        poolConfig.setTestOnBorrow(sftpProperties.getPool().getTestOnBorrow());
        poolConfig.setTestOnReturn(sftpProperties.getPool().getTestOnReturn());
        poolConfig.setJmxEnabled(false);
        return new SftpPool(factory, poolConfig);
    }

}
