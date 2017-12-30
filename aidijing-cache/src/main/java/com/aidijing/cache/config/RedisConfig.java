package com.aidijing.cache.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;

/**
 * <pre>
 *     aidijing:
 *       redis:
 *         host: 127.0.0.1
 *         port: 6379
 *         password: 123456
 *         timeout: 60
 *         database: 1
 * </pre>
 *
 * @author : 披荆斩棘
 * @date : 2017/5/15
 */
@Getter
@Setter
@ToString
@Configuration
@ConfigurationProperties( prefix = "aidijing.redis" )
public class RedisConfig {

    private static final String  REDISSON_ADDRESS_PREFIX = "redis://";
    private              String  host                    = "127.0.0.1";
    private              Integer port                    = 6379;
    private              Integer timeout                 = 60;
    private              String  password                = null;
    private              Integer database                = 0;

    /**
     * <a href="https://github.com/redisson/redisson/wiki/%E7%9B%AE%E5%BD%95">redisson文档</a>
     *
     * @return
     * @throws IOException
     */
    @Bean( destroyMethod = "shutdown" )
    public RedissonClient redisson () throws IOException {
        Config config = new Config();
        config.useSingleServer()
              .setAddress( this.getRedissonAddress( host , port ) )
              .setPassword( password )
              .setDatabase( database );
        return Redisson.create( config );
    }

    private String getRedissonAddress ( String host , Integer port ) {
        return REDISSON_ADDRESS_PREFIX + host + ":" + port;
    }


    @Bean
    public JedisPoolConfig jedisPoolConfig () {
		return new JedisPoolConfig();
    }

    @Bean
    public JedisPool jedisPool () {
        return new JedisPool( jedisPoolConfig() , host , port , timeout , password , database );
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory () {
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setHostName( host );
        jedisConnectionFactory.setPort( port );
        jedisConnectionFactory.setPassword( password );
        jedisConnectionFactory.setDatabase( database );
        return jedisConnectionFactory;
    }


    @Bean
    public RedisTemplate< String, Object > redisTemplate () {
        Jackson2JsonRedisSerializer< Object > valueSerializer = jackson2JsonRedisSerializer();
        StringRedisSerializer                 keySerializer   = new StringRedisSerializer();
        RedisTemplate< String, Object >       redisTemplate   = new RedisTemplate<>();
        redisTemplate.setConnectionFactory( redisConnectionFactory() );
        redisTemplate.setKeySerializer( keySerializer );
        redisTemplate.setValueSerializer( valueSerializer );
        redisTemplate.setHashKeySerializer( keySerializer );
        redisTemplate.setHashValueSerializer( valueSerializer );
        return redisTemplate;
    }


    @Bean
    public Jackson2JsonRedisSerializer< Object > jackson2JsonRedisSerializer () {
        Jackson2JsonRedisSerializer< Object > jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>( Object.class );
        ObjectMapper                          objectMapper                = Jackson2ObjectMapperBuilder.json().build();
        objectMapper.setVisibility( PropertyAccessor.ALL , JsonAutoDetect.Visibility.ANY );
        objectMapper.enableDefaultTyping( ObjectMapper.DefaultTyping.NON_FINAL );
        jackson2JsonRedisSerializer.setObjectMapper( objectMapper );
        return jackson2JsonRedisSerializer;
    }


}
