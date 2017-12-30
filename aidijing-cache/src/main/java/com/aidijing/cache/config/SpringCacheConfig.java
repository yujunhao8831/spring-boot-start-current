package com.aidijing.cache.config;

import com.aidijing.common.util.LogUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
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

import java.util.Map;

/**
 * Spring 缓存配置
 * <p>
 * <pre>
 *     aidijing:
 *       spring:
 *         cache:
 *           redis-manager:
 *             default-expiration: 1800
 *             use-prefix: true
 *             expires:
 *               users: 900
 * </pre>
 *
 * @author : 披荆斩棘
 * @date : 2017/5/15
 */
@Setter
@Getter
@Configuration
@EnableCaching
@ConfigurationProperties( prefix = "aidijing.spring.cache.redis-manager" )
@ConditionalOnBean( RedisTemplate.class )
@ConditionalOnExpression
public class SpringCacheConfig extends CachingConfigurerSupport {

	/** 指定命名空间下的过期时间,默认不指定 **/
	private Map< String, Long > expires           = null;
	/** 指定全局的过期时间,默认60分钟 **/
	private long                defaultExpiration = 3600;
	/** 是否使用前缀,默认使用 **/
	private boolean             usePrefix         = true;


	@Bean
	public CacheManager cacheManager ( RedisTemplate redisTemplate ) {
		RedisCacheManager redisCacheManager = new RedisCacheManager( redisTemplate );
		// 默认30分钟
		redisCacheManager.setDefaultExpiration( defaultExpiration );
		redisCacheManager.setExpires( expires );
		redisCacheManager.setUsePrefix( usePrefix );
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
			public void handleCacheGetError ( RuntimeException exception , Cache cache , Object key ) {
				LogUtils.getLogger().error( "cache : {} , key : {}" , cache , key );
				LogUtils.getLogger().error( "handleCacheGetError" , exception );
				super.handleCacheGetError( exception , cache , key );
			}

			@Override
			public void handleCachePutError ( RuntimeException exception , Cache cache , Object key ,
											  Object value ) {
				LogUtils.getLogger().error( "cache : {} , key : {} , value : {} " , cache , key ,
											value
				);
				LogUtils.getLogger().error( "handleCachePutError" , exception );
				super.handleCachePutError( exception , cache , key , value );
			}

			@Override
			public void handleCacheEvictError ( RuntimeException exception , Cache cache , Object key ) {
				LogUtils.getLogger().error( "cache : {} , key : {}" , cache , key );
				LogUtils.getLogger().error( "handleCacheEvictError" , exception );
				super.handleCacheEvictError( exception , cache , key );
			}

			@Override
			public void handleCacheClearError ( RuntimeException exception , Cache cache ) {
				LogUtils.getLogger().error( "cache : {} " , cache );
				LogUtils.getLogger().error( "handleCacheClearError" , exception );
				super.handleCacheClearError( exception , cache );
			}
		};
	}
}
