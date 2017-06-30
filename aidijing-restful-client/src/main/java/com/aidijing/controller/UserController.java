package com.aidijing.controller;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.PageRowBounds;
import com.aidijing.common.ResponseEntity;
import com.aidijing.common.util.LogUtils;
import com.aidijing.domain.User;
import com.aidijing.permission.Pass;
import com.aidijing.service.UserService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 后台管理用户 前端控制器
 * </p>
 *
 * @author 披荆斩棘
 * @since 2017-06-15
 */
@RestController
@RequestMapping( "/api/user" )
public class UserController {

    @Autowired
    private UserService    userService;
    @Autowired
    private RedissonClient redissonClient;


    @GetMapping
    public ResponseEntity< PageInfo< User > > listPage ( PageRowBounds pageRowBounds ) {
        return ResponseEntity.ok().setResponseContent( userService.listPage( pageRowBounds ) )
                             .setFilterFields( "-password" );
    }


    @GetMapping( "distributed-lock" )
    public ResponseEntity distributedLock () throws InterruptedException {
        int timeout = 10;
        // 分布式锁
        final RLock lock = redissonClient.getLock( this.getClass().getSimpleName() + "LOCK" );
        lock.lock();

        for ( int i = 0 ; i < timeout ; i++ ) {
            TimeUnit.SECONDS.sleep( 1 );
            LogUtils.getLogger().warn( "lock ing ... ... ... " );
        }
        // N 时间后后解锁
        lock.unlock();
        LogUtils.getLogger().warn( "unlock" );
        return ResponseEntity.ok( "success" );
    }


    @Pass
    @GetMapping( "async" )
    public ResponseEntity asyncUpdate () {
        // 异步操作  
        userService.asyncUpdate();
        return ResponseEntity.ok( "success" );
    }


    @GetMapping( "list" )
    public ResponseEntity< List< User > > listPageCache () {
        return ResponseEntity.ok().setResponseContent( userService.list() );
    }


    @GetMapping( "{id}" )
    public ResponseEntity< User > get ( @PathVariable Long id ) {
        return ResponseEntity.ok().setResponseContent( userService.get( id ) );
    }

    @PutMapping( "{id}" )
    public ResponseEntity update ( @PathVariable Long id,
                                   @RequestBody User user ) {
        userService.update( user.setId( id ) );
        return ResponseEntity.ok();
    }

    @PostMapping
    public ResponseEntity save ( @RequestBody User user ) {
        userService.save( user );
        return ResponseEntity.ok();
    }

    @DeleteMapping( "{id}" )
    public ResponseEntity delete ( @PathVariable Long id ) {
        userService.delete( id );
        return ResponseEntity.ok();
    }


}
