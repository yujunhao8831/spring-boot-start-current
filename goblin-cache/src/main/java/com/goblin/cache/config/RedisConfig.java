package com.goblin.cache.config;

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
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * <pre>
 *     goblin:
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
@ConfigurationProperties( prefix = "goblin.redis" )
public class RedisConfig {

    private static final String  REDISSON_ADDRESS_PREFIX = "redis://";
    private              String  host                    = "127.0.0.1";
    private              Integer port                    = 6379;
    private              Integer timeout                 = 60;
    private              String  password                = null;
    private              Integer database                = 0;

    /**
     * <a href="https://github.com/redisson/redisson/wiki/%E7%9B%AE%E5%BD%95">redisson文档</a>
     */
    @Bean( destroyMethod = "shutdown" )
    public RedissonClient redisson (){
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
		RedisStandaloneConfiguration standaloneConfig = new RedisStandaloneConfiguration(  );
		standaloneConfig.setHostName( host );
		standaloneConfig.setPort( port );
		standaloneConfig.setPassword( RedisPassword.of( password ) );
		standaloneConfig.setDatabase( database );
        return new JedisConnectionFactory(standaloneConfig);
    }




}
