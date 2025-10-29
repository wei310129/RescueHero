package tw.com.aidenmade.rescuehero.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import tw.com.aidenmade.rescuehero.definition.cache.CacheDefinition;

import java.time.Duration;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@EnableCaching
@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.password}")
    private String password;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
        config.setUsername("default");
        config.setPassword(password); // 此時 Jasypt 已解密完畢
        return new LettuceConnectionFactory(config);
    }

    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory factory) {

        // 預設設定：所有 Cache 的共通部分
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(1)) // 預設 TTL：1 小時
                .computePrefixWith(cacheName -> "rescuehero::" + cacheName + "::") // key 前綴
                .serializeKeysWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
                .disableCachingNullValues();

        // 各 cacheName 自訂 TTL
        Map<String, RedisCacheConfiguration> cacheConfigs = Arrays.stream(CacheDefinition.values())
                .collect(Collectors.toMap(
                        CacheDefinition::getName,
                        e -> defaultConfig.entryTtl(Duration.ofMinutes(e.getTtlType().getMinutes())),
                        (_, _) -> {
                            throw new IllegalStateException("Duplicate key detected");
                        }
                ));

        return RedisCacheManager.builder(factory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(cacheConfigs)
                .build();
    }
}
