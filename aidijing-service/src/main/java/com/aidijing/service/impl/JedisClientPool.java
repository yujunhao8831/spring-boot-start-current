package com.aidijing.service.impl;


import com.aidijing.service.JedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisDataException;

@Service
public class JedisClientPool implements JedisClient {

    @Autowired
    private JedisPool jedisPool;

    @Override
    public String get ( String key ) {
        try ( Jedis jedis = jedisPool.getResource() ) {
            return jedis.get( key );
        }
    }

    @Override
    public String set ( String key, String value ) {
        try ( Jedis jedis = jedisPool.getResource() ) {
            return jedis.set( key, value );
        }
    }

    @Override
    public Long incr ( String key ) throws JedisDataException {
        try ( Jedis jedis = jedisPool.getResource() ) {
            return jedis.incr( key );
        }
    }


    @Override
    public Long del ( final String... keys ) {
        try ( Jedis jedis = jedisPool.getResource() ) {
            return jedis.del( keys );
        }
    }


    @Override
    public Long expire ( String key, int second ) {
        try ( Jedis jedis = jedisPool.getResource() ) {
            return jedis.expire( key, second );
        }
    }

    @Override
    public String setex ( String key, String value, int second ) {
        try ( Jedis jedis = jedisPool.getResource() ) {
            return jedis.setex( key, second, value );
        }
    }
}
