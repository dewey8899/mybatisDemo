package com.system.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author dewey.duØ
 * @date 2019-11-17 13:36
 */
@Configuration
//@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

    @Bean
    public CacheManager cacheManager(RedisTemplate<?, ?> redisTemplate) {
        CacheManager cacheManager = new RedisCacheManager(redisTemplate);
        return cacheManager;
        /*RedisCacheManager rcm = new RedisCacheManager(redisTemplate);
        // 多个缓存的名称,目前只定义了一个
        rcm.setCacheNames(Arrays.asList("thisredis"));
        //设置缓存默认过期时间(秒)
        rcm.setDefaultExpiration(600);
        return rcm;*/
    }
    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // 可以配置对象的转换规则，比如使用json格式对object进行存储。
        // Object --> 序列化 --> 二进制流 --> redis-server存储
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
        return redisTemplate;
    }
}
