package com.aidijing.service.impl;

import com.aidijing.domain.User;
import com.aidijing.mapper.UserMapper;
import com.aidijing.service.UserService;
import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.PageRowBounds;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 后台管理用户 服务实现类
 * </p>
 *
 * @author 披荆斩棘
 * @since 2017-06-19
 */
@Service
public class UserServiceImpl extends ServiceImpl< UserMapper, User > implements UserService {


    /**
     * 用户分页在缓存中存储名称key的前缀
     */
    private static final String CACHE_User_LIST_PAGE_NAME_PREFIX = "UserService.User.list.namespace";


    /**
     * SpEL表达式 : T(完整的类名),因为key只能是String类型,下面的key使用SpEL表达式进行了转换
     * 这里
     *
     * @param pageRowBounds
     * @return
     */
    @Cacheable( value = CACHE_User_LIST_PAGE_NAME_PREFIX, key = "#pageRowBounds.offset + '.' + #pageRowBounds.getLimit()" )
    @Override
    public PageInfo listPage ( PageRowBounds pageRowBounds ) {
        PageHelper.startPage( pageRowBounds.getOffset(), pageRowBounds.getLimit() );
        return new PageInfo( super.selectList( null ) );
    }

    @Cacheable( value = CACHE_User_LIST_PAGE_NAME_PREFIX, key = "#root.methodName" )
    @Override
    public List< User > list () {
        return super.selectList( null );
    }

    @Cacheable( key = "T(java.lang.String).valueOf(#id)" )
    @Override
    public User get ( Long id ) {
        return super.selectById( id );
    }


    @Caching(
            evict = {
                    @CacheEvict( value = CACHE_User_LIST_PAGE_NAME_PREFIX, allEntries = true )
            },
            put = {
                    @CachePut( key = "T(java.lang.String).valueOf(#User.id)", condition = "#result != null" )
            }
    )
    @Override
    public User update ( User User ) {
        if ( ! super.updateById( User ) ) {
            return null;
        }
        return super.selectById( User.getId() );
    }


    @Caching(
            evict = {
                    @CacheEvict( value = CACHE_User_LIST_PAGE_NAME_PREFIX, allEntries = true )
            },
            put = {
                    @CachePut( key = "T(java.lang.String).valueOf(#User.id)", condition = "#result != null" )
            }
    )
    @Override
    public User save ( User User ) {
        if ( ! super.insert( User ) ) {
            return null;
        }
        return User;
    }

    @Caching( evict = {
            @CacheEvict( value = CACHE_User_LIST_PAGE_NAME_PREFIX, allEntries = true ) ,
            @CacheEvict( key = "T(java.lang.String).valueOf(#id)" )

    } )
    @Override
    public boolean delete ( Long id ) {
        return super.deleteById( id );
    }

    @Async
    @Override
    public ListenableFuture< Boolean > asyncUpdate () {
        return new AsyncResult( this.sleepUpdate( 3 ) );
    }

    private Boolean sleepUpdate ( int second ) {
        try {
            TimeUnit.SECONDS.sleep( second );
        } catch ( InterruptedException e ) {
        }
        final List< User > Users = super.selectList( null );
        Users.forEach( User -> User.setUpdateTime( Calendar.getInstance()
                                                           .getTime() ) );
        return super.updateBatchById( Users );
    }

    @Override
    public User findByUsername ( String Username ) {
        return this.selectOne( new Condition().eq( "username", Username ) );
    }


}
