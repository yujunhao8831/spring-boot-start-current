package com.aidijing.manage.service.impl;

import com.aidijing.manage.bean.domain.User;
import com.aidijing.manage.mapper.UserMapper;
import com.aidijing.manage.service.UserService;
import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;

/**
 * 用户服务实现类
 * <p>
 * 这里 {@code @CacheConfig 的 cacheNames = "userService.user"},表示该类下面所有方法在使用Spring Cache注解后
 * 默认的cacheNames,如果不指定则使用@CacheConfig所指定的cacheNames,如果指定了,则使用指定的.
 *
 * @author 披荆斩棘
 * @since 2017-05-11
 */
@CacheConfig( cacheNames = "userService.user.namespace" )
@Service
public class UserServiceImpl extends ServiceImpl< UserMapper, User > implements UserService {


	/**
	 * 用户分页在缓存中存储名称key的前缀
	 */
	private static final String CACHE_USER_LIST_PAGE_NAME_PREFIX = "UserService.User.namespace";


	@Cacheable( key = "T(java.lang.String).valueOf(#id)" )
	@Override
	public User get ( Long id ) {
		return super.selectById( id );
	}


	@Caching(
		evict = {
			@CacheEvict( value = CACHE_USER_LIST_PAGE_NAME_PREFIX, allEntries = true, condition = "#result != null" )
		},
		put = {
			@CachePut( key = "T(java.lang.String).valueOf(#user.id)", condition = "#result != null" )
		}
	)
	@Override
	public User update ( User user ) {
		if ( ! super.updateById( user ) ) {
			return null;
		}
		return super.selectById( user.getId() );
	}


	@Caching(
		evict = {
			@CacheEvict( value = CACHE_USER_LIST_PAGE_NAME_PREFIX, allEntries = true, condition = "#result != null" )
		},
		put = {
			@CachePut( key = "T(java.lang.String).valueOf(#user.id)", condition = "#result != null" )
		}
	)
	@Override
	public boolean save ( User user ) {
		return super.insert( user );
	}

	@Caching( evict = {
		@CacheEvict( value = CACHE_USER_LIST_PAGE_NAME_PREFIX, allEntries = true, condition = "#result != null" ) ,
		@CacheEvict( key = "T(java.lang.String).valueOf(#id)" )
	} )
	@Override
	public boolean delete ( Long id ) {
		return super.deleteById( id );
	}


	@Override
	public User findByUsername ( String username ) {
		return this.selectOne( Condition.create().eq( "username" , username ) );
	}


	@Override
	public boolean isExist ( Long userId ) {
		return super.selectCount( Condition.create().eq( "id" , userId ) ) > 0;
	}

	@Override
	public boolean isNotExist ( Long userId ) {
		return ! this.isExist( userId );
	}


}
