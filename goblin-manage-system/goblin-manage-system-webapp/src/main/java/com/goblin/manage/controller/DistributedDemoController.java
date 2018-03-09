package com.goblin.manage.controller;

import com.goblin.manage.permission.Pass;
import com.goblin.manage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 分布式异步业务示例
 *
 * @author : 披荆斩棘
 * @date : 2017/12/22
 */
@Pass
@RestController
@RequestMapping( "distributed" )
public class DistributedDemoController {


	@Autowired
	private UserService userService;

	/**
	 * 支付
	 */
	@GetMapping( "pay" )
	public ResponseEntity pay () {
		// 支付处理
		userService.pay();
		return ResponseEntity.ok().body( "支付成功" );
	}



}
