package pers.liam.config;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * @author liam
 * @date 2021/4/15 16:41
 */
@Configuration
@Slf4j
public class RedisConfig {

    //@Value("${spring.application.name:unknown}")
    //private String appName;
    //@Value("${spring.redis.timeToLive:15}")
    //private Long timeToLive;

    @Value("${spring.redis.redis-token.database}")
    private int tokenDatabase;

    @Value("${spring.redis.redis-sms.database}")
    private int smsDatabase;

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.timeout}")
    private int timeout;

    @Value("${spring.redis.lettuce.pool.max-idle}")
    private int maxIdle;

    @Value("${spring.redis.lettuce.pool.min-idle}")
    private int minIdle;

    @Value("${spring.redis.lettuce.pool.max-wait-millis}")
    private long maxWaitMillis;

    @Bean
    @Scope(value = "prototype")
    public GenericObjectPoolConfig redisPool() {
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setMaxWaitMillis(maxWaitMillis);
        return config;
    }

    @Bean
    public RedisStandaloneConfiguration redisConfigA() {
        RedisStandaloneConfiguration tokenRedisConfig = new RedisStandaloneConfiguration();
        tokenRedisConfig.setHostName(host);
        tokenRedisConfig.setPassword(password);
        tokenRedisConfig.setPort(port);
        tokenRedisConfig.setDatabase(tokenDatabase);
        return tokenRedisConfig;
    }

    @Bean
    public RedisStandaloneConfiguration redisConfigB() {
        RedisStandaloneConfiguration smsRedisConfig = new RedisStandaloneConfiguration();
        smsRedisConfig.setHostName(host);
        smsRedisConfig.setPassword(password);
        smsRedisConfig.setPort(port);
        smsRedisConfig.setDatabase(smsDatabase);
        return smsRedisConfig;
    }

    @Bean
    @Primary
    public LettuceConnectionFactory factoryA(GenericObjectPoolConfig config, RedisStandaloneConfiguration redisConfigA) {
        LettuceClientConfiguration clientConfiguration = LettucePoolingClientConfiguration.builder()
                .poolConfig(config).commandTimeout(Duration.ofMillis(config.getMaxWaitMillis())).build();
        return new LettuceConnectionFactory(redisConfigA, clientConfiguration);
    }

    @Bean
    public LettuceConnectionFactory factoryB(GenericObjectPoolConfig config, RedisStandaloneConfiguration redisConfigB) {
        LettuceClientConfiguration clientConfiguration = LettucePoolingClientConfiguration.builder()
                .poolConfig(config).commandTimeout(Duration.ofMillis(config.getMaxWaitMillis())).build();
        return new LettuceConnectionFactory(redisConfigB, clientConfiguration);
    }

    @Bean(name = "tokenRedisTemplate")
    public StringRedisTemplate tokenRedisTemplate(LettuceConnectionFactory factoryA) {
        StringRedisTemplate template = new StringRedisTemplate(factoryA);
        RedisSerializer<String> redisSerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        template.setKeySerializer(redisSerializer);
        //template.setValueSerializer(jackson2JsonRedisSerializer);
        template.setValueSerializer(new StringRedisSerializer());
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        return template;
    }

    @Bean(name = "smsRedisTemplate")
    public StringRedisTemplate smsRedisTemplate(@Autowired @Qualifier("factoryB") LettuceConnectionFactory factoryB) {
        StringRedisTemplate template = new StringRedisTemplate(factoryB);
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        //template.setValueSerializer(jackson2JsonRedisSerializer);
        template.setValueSerializer(new StringRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }

    private StringRedisTemplate getRedisTemplate() {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setValueSerializer(new GenericFastJsonRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }
/*    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        RedisSerializer<String> redisSerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        jackson2JsonRedisSerializer.setObjectMapper(om);

        // 配置序列化
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(timeToLive))
                .prefixKeysWith(appName + ":");
        RedisCacheConfiguration redisCacheConfiguration = config.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer));
        RedisCacheManager cacheManager = RedisCacheManager.builder(factory)
                .cacheDefaults(redisCacheConfiguration)
                .build();
        return cacheManager;
    }*/
}
