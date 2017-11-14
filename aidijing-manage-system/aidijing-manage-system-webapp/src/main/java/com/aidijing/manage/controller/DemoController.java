package com.aidijing.manage.controller;

import com.aidijing.common.ResponseEntityPro;
import com.aidijing.common.annotation.PassInjectionAttackIntercept;
import com.aidijing.common.util.GenerationCode;
import com.aidijing.manage.bean.domain.User;
import com.aidijing.manage.permission.Pass;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : 披荆斩棘
 * @date : 2017/8/26
 */
@RestController
public class DemoController {


	@Pass
	@GetMapping( "demo" )
	public ResponseEntity demo () {
		return new ResponseEntityPro().add( "id" , GenerationCode.globalUniqueId() )
									  .add( "password" , "123456" )
									  .buildOk();
	}

	@Pass
	@GetMapping( "demo2" )
	public ResponseEntity demo2 () {
		return ResponseEntityPro.badRequest( new User().setPassword( "123456" ).setNickName( "披荆斩棘" ) , "-password" );
	}

	@Pass
	@GetMapping( "injection" )
	@PassInjectionAttackIntercept( { "update" , "delete" } )
	public ResponseEntity< String > injection () {
		return ResponseEntityPro.ok( GenerationCode.globalUniqueId() );
	}


}
