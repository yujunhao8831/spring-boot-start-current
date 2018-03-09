package com.goblin.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author : 披荆斩棘
 * @date : 2017/5/15
 */
@Getter
@Setter
@ToString
@Configuration
@ConfigurationProperties( prefix = "redis" )
public class RedisConfig {

    private String  host;
    private Integer port;
    private Integer timeout;


    @Bean
    public JedisPoolConfig jedisPoolConfig () {
		return new JedisPoolConfig();
    }

    @Bean
    public JedisPool jedisPool () {
        return new JedisPool( jedisPoolConfig(), host, port, timeout );
    }


}
