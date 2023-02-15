package com.cn.xiguaapp.common.starter.autoconfigure;

import com.cn.xiguapp.common.core.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author xiguaapp
 * @Date 2020/10/10
 * @desc redis缓存配置
 */
@Slf4j
@Configuration
@AutoConfigureAfter(RedisCacheConfiguration.class)
public class RedisCacheAutoConfiguration {
    /**
     * 重新配置一个RedisTemplate
     *
     * @param factory
     * @return
     */
    @Bean
    public ReactiveRedisTemplate<String,Object> redisTemplate(ReactiveRedisConnectionFactory factory) {
        StringRedisSerializer keySerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer<Object> valueSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        RedisSerializationContext.RedisSerializationContextBuilder<String,Object>builder =
                RedisSerializationContext.newSerializationContext(keySerializer);
        RedisSerializationContext<String,Object>context = builder.value(valueSerializer).build();
        return new ReactiveRedisTemplate<>(factory,context);
    }

//    @Bean
//    public ReactiveKeyCommands keyCommands(ReactiveRedisConnectionFactory
//                                                   reactiveRedisConnectionFactory) {
//        return reactiveRedisConnectionFactory.getReactiveConnection().keyCommands();
//    }
//    @Bean
//    public ReactiveStringCommands stringCommands(ReactiveRedisConnectionFactory
//                                                         reactiveRedisConnectionFactory) {
//        return reactiveRedisConnectionFactory.getReactiveConnection().stringCommands();
//    }
    @Bean
    @ConditionalOnMissingBean(RedisUtils.class)
    @ConditionalOnBean(ReactiveRedisTemplate.class)
    public RedisUtils redisUtils(ReactiveRedisTemplate<String,Object> redisTemplate) {
        RedisUtils redisUtil = new RedisUtils(redisTemplate);
        log.info("RedisUtil [{}]", redisUtil);
        return redisUtil;
    }
}
