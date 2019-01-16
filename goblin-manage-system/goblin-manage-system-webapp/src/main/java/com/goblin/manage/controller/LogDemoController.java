package com.goblin.manage.controller;

import com.github.pagehelper.PageInfo;
import com.goblin.common.PagingRequest;
import com.goblin.common.ResponseEntityPro;
import com.goblin.common.annotation.Log;
import com.goblin.manage.bean.domain.User;
import com.goblin.manage.permission.Pass;
import com.goblin.manage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author : 披荆斩棘
 * @date : 2017/8/26
 */
@Pass
@RestController
@RequestMapping( "log" )
public class LogDemoController {

    @Autowired
    private UserService userService;

    @Log
    @GetMapping( "users" )
    public ResponseEntity< List< User > > users () {
        return ResponseEntityPro.ok( userService.list() );
    }

    @GetMapping( "users-page" )
    public ResponseEntity< PageInfo< User > > listPage ( PagingRequest pagingRequest ) {
        return ResponseEntityPro.ok( userService.listPage( pagingRequest ) );
    }


}
