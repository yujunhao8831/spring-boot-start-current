package com.aidijing.manage.controller;

import com.aidijing.common.ResponseEntity;
import com.aidijing.common.annotation.PassInjectionAttackIntercept;
import com.aidijing.common.util.GenerationCode;
import com.aidijing.manage.permission.Pass;
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
	public ResponseEntity< String > demo () {
		return ResponseEntity.ok().setResponseContent( GenerationCode.globalUniqueId() );
	}

	@Pass
	@GetMapping( "injection" )
	@PassInjectionAttackIntercept( { "update" , "delete" } )
	public ResponseEntity< String > injection () {
		return ResponseEntity.ok().setResponseContent( GenerationCode.globalUniqueId() );
	}


}
