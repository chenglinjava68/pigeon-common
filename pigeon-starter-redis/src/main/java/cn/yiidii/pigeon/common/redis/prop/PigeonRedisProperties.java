package cn.yiidii.pigeon.common.redis.prop;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author: YiiDii Wang
 * @create: 2021-04-28 12:48
 */
@Getter
@Setter
@ConfigurationProperties(PigeonRedisProperties.PREFIX)
public class PigeonRedisProperties {

    public static final String PREFIX = "pigeon.redis";

    private Boolean enable = true;

}
