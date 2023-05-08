package com.example.redis.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * data를 가져오고 보낼 때 직접 만든 도메인 모델을 Serialize 해주기 위한 설정
 * @See https://www.woolog.dev/backend/spring-boot/spring-boot-redis-cache-simple/
 * 여기서 생성한 testCacheManager 객체를 앞으로 사용할 레디스 어노테이션에 명시 해 주어야 함
 */
@Configuration
@EnableCaching
public class RedisCacheConfig {

    /**
     * 해당 빈은  RedisConnectionFactory를 인자로 받아서 Redis 서버와의 연결을 생성하고 그 연결을 이용하여 Redis 캐시를 관리
     */
    @Bean
    public CacheManager testCacheManager(RedisConnectionFactory cf) {
        
        // RedisCacheConfiguration 객체를 생성하여 Redis 캐시의 기본 구성을 설정
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer())) // Redis 캐시에 저장되는 key 값은 StringRedisSerializer를 사용하여 직렬화
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer())) // Redis 캐시에 저장되는 value는 GenericJackson2JsonRedisSerializer을 사용하여 직렬화
                .entryTtl(Duration.ofMinutes(3L)); // 각각의 캐시 엔트리는 3분동안 유효

        // RedisCacheManagerBuilder는 Redis 연결을 이용하여 캐시를 관리하기 위한 다양한 구성 옵션을 제공
        return RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(cf) // Redis 연결을 설정
                .cacheDefaults(redisCacheConfiguration) // 앞서 생성한 RedisCacheConfiguration 객체를 캐시의 기본 구성으로 사용하도록 설정
                .build();
    }
}
