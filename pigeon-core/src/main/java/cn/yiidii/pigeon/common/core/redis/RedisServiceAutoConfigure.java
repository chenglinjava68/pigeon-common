package cn.yiidii.pigeon.common.core.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * 自动配置类
 *
 * @author LiKe
 * @version 1.0.0
 * @date 2020-05-10 13:42
 */
@Configuration
@ConditionalOnClass(RedisOps.class)
public class RedisServiceAutoConfigure {

    private RedisTemplate<String, String> redisTemplate;

    @Bean
    @ConditionalOnMissingBean
    public RedisOps redisService() {
        return new RedisOps(redisTemplate);
    }

    @Autowired
    public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // ~ redis configuration
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * key 的序列化器
     */
    private final StringRedisSerializer keyRedisSerializer = new StringRedisSerializer();

    /**
     * value 的序列化器
     */
    private final RedisFastJsonSerializer<Object> valueRedisSerializer = new RedisFastJsonSerializer<>(Object.class);

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        // RedisCacheWriter
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory);
        // RedisCacheConfiguration - 值的序列化方式
        RedisSerializationContext.SerializationPair<Object> serializationPair = RedisSerializationContext.SerializationPair.fromSerializer(valueRedisSerializer);
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig().serializeValuesWith(serializationPair);

        return new RedisCacheManager(redisCacheWriter, redisCacheConfiguration);
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();

        // 配置连接工厂
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // 值序列化-RedisFastJsonSerializer
        redisTemplate.setValueSerializer(valueRedisSerializer);
        redisTemplate.setHashValueSerializer(valueRedisSerializer);
        // 键序列化-StringRedisSerializer
        redisTemplate.setKeySerializer(keyRedisSerializer);
        redisTemplate.setHashKeySerializer(keyRedisSerializer);

        return redisTemplate;
    }

}
