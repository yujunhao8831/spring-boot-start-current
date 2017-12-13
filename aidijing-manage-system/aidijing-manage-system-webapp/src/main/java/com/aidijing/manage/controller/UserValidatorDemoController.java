package com.aidijing.manage.controller;

import com.aidijing.common.ResponseEntityPro;
import com.aidijing.common.util.AssertUtils;
import com.aidijing.common.util.LogUtils;
import com.aidijing.common.util.ValidatedGroups;
import com.aidijing.manage.bean.dto.UserForm;
import com.aidijing.manage.permission.Pass;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.groups.Default;

/**
 * @author : 披荆斩棘
 * @date : 2017/8/26
 */
@Pass
@RestController
@RequestMapping( "user" )
public class UserValidatorDemoController {


	@PostMapping
	public ResponseEntity< UserForm > save ( @RequestBody
											 @Validated( { ValidatedGroups.Save.class , Default.class } ) UserForm user ,
											 BindingResult result ) {
		if ( result.hasErrors() ) {
			AssertUtils.isTrue( true , result.getFieldError().getDefaultMessage() );
		}
		LogUtils.getLogger().debug( "user : {}" , user );
		return ResponseEntityPro.ok( user );
	}


}
