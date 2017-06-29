package com.aidijing.config;

import com.aidijing.common.util.LogUtils;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleCacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : 披荆斩棘
 * @date : 2017/5/15
 */
@Configuration
@EnableCaching
public class SpringCacheConfig extends CachingConfigurerSupport {


    @Bean
    public CacheManager cacheManager ( RedisTemplate redisTemplate ) {
        RedisCacheManager   redisCacheManager = new RedisCacheManager( redisTemplate );
        Map< String, Long > expires           = new HashMap<>();
        // 默认30分钟
        redisCacheManager.setDefaultExpiration( 1800 );
        redisCacheManager.setExpires( expires );
        redisCacheManager.setUsePrefix( true );
        return redisCacheManager;
    }

    /**
     * 默认使用的是 : {@link org.springframework.cache.interceptor.SimpleCacheResolver}
     *
     * @return
     */
    @Override
    public CacheResolver cacheResolver () {
        return null;
    }

    /**
     * 默认使用的是 : {@link org.springframework.cache.interceptor.SimpleKeyGenerator}
     *
     * @return
     */
    @Override
    public KeyGenerator keyGenerator () {
        return null;
    }


    /**
     * 错误处理
     *
     * @return
     */
    @Bean
    @Override
    public CacheErrorHandler errorHandler () {
        return new SimpleCacheErrorHandler() {
            @Override
            public void handleCacheGetError ( RuntimeException exception, Cache cache, Object key ) {
                if ( LogUtils.getLogger().isErrorEnabled() ) {
                    LogUtils.getLogger().error( "cache : {} , key : {}", cache, key );
                    LogUtils.getLogger().catching( exception );
                }
                super.handleCacheGetError( exception, cache, key );
            }

            @Override
            public void handleCachePutError ( RuntimeException exception, Cache cache, Object key, Object value ) {
                if ( LogUtils.getLogger().isErrorEnabled() ) {
                    LogUtils.getLogger().error( "cache : {} , key : {} , value : {} ", cache, key, value );
                    LogUtils.getLogger().catching( exception );
                }
                super.handleCachePutError( exception, cache, key, value );
            }

            @Override
            public void handleCacheEvictError ( RuntimeException exception, Cache cache, Object key ) {
                if ( LogUtils.getLogger().isErrorEnabled() ) {
                    LogUtils.getLogger().error( "cache : {} , key : {}", cache, key );
                    LogUtils.getLogger().catching( exception );
                }
                super.handleCacheEvictError( exception, cache, key );
            }

            @Override
            public void handleCacheClearError ( RuntimeException exception, Cache cache ) {
                if ( LogUtils.getLogger().isErrorEnabled() ) {
                    LogUtils.getLogger().error( "cache : {} ", cache );
                    LogUtils.getLogger().catching( exception );
                }
                super.handleCacheClearError( exception, cache );
            }
        };
    }
}
