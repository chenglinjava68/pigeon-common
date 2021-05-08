package cn.yiidii.pigeon.common.sftp.prop;

import com.jcraft.jsch.ChannelSftp;
import lombok.Data;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * sftp属性配置
 *
 * @author: YiiDii Wang
 * @create: 2021-05-08 12:58
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "pigeon.sftp")
public class SftpProperties {

    private Pool pool = new Pool();

    @Data
    public static class Pool extends GenericObjectPoolConfig<ChannelSftp> {
        private int maxTotal = DEFAULT_MAX_TOTAL;
        private int maxIdle = DEFAULT_MAX_IDLE;
        private int minIdle = DEFAULT_MIN_IDLE;
    }
}
