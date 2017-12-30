package com.aidijing.manage.controller;

import com.aidijing.common.PagingRequest;
import com.aidijing.common.ResponseEntityPro;
import com.aidijing.common.annotation.Log;
import com.aidijing.manage.bean.domain.User;
import com.aidijing.manage.permission.Pass;
import com.aidijing.manage.service.UserService;
import com.github.pagehelper.PageInfo;
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
		return ResponseEntityPro.ok( userService.selectList( null ) );
	}

	@GetMapping( "users-page" )
	public ResponseEntity< PageInfo< User > > listPage ( PagingRequest pagingRequest ) {
		return ResponseEntityPro.ok( userService.listPage( pagingRequest ) );
	}


}
