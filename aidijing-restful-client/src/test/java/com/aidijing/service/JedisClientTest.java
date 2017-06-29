package com.aidijing.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author : 披荆斩棘
 * @date : 2017/6/27
 */
@RunWith( SpringRunner.class )
@SpringBootTest
public class JedisClientTest {
    @Autowired
    private JedisClient jedisClient;

    @Test
    public void crud () throws Exception {
        final String key   = "ok";
        final String value = "1";
        System.err.println( "set = " + jedisClient.set( key, value ) );

        System.err.println( "get = " + jedisClient.get( key ) );

        System.err.println( "expire = " + jedisClient.expire( key, 60 ) );

        System.err.println( "incr = " + jedisClient.incr( key ) );

        System.err.println( "del = " + jedisClient.del( key, "没有的key" ) );
        System.err.println( "del = " + jedisClient.del( "没有的key" ) );

        System.err.println();
        System.err.println();
        System.err.println();

        System.err.println( "setex = " + jedisClient.setex( "setex", "setex的值", 30 ) );

    }
}
