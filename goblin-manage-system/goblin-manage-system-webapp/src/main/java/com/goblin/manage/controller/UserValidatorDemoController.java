package com.goblin.manage.controller;

import com.goblin.common.ResponseEntityPro;
import com.goblin.common.util.AssertUtils;
import com.goblin.common.util.LogUtils;
import com.goblin.common.util.ValidatedGroups;
import com.goblin.manage.bean.dto.UserForm;
import com.goblin.manage.permission.Pass;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.groups.Default;

/**
 * HibernateValidator 校验示例
 *
 * @author : 披荆斩棘
 * @date : 2017/8/26
 */
@Pass
@RestController
@RequestMapping( "user" )
public class UserValidatorDemoController {


	@PostMapping
	public ResponseEntity< UserForm > saveValidated ( @RequestBody
													  @Validated( { ValidatedGroups.Save.class , Default.class } ) UserForm user ,
													  BindingResult result ) {
		// 这里可以自己进行处理
		AssertUtils.bindingResult( result );
		LogUtils.getLogger().debug( "user : {},验证通过" , user );
		return ResponseEntityPro.ok( user );
	}

	/**
	 * 如果说不在被 {@link Validated} 注解的实体后面加入 {@link BindingResult} ,
	 * 进行校验过程中,一旦验证不通过那么Spring会抛异常, {@link org.springframework.web.bind.MethodArgumentNotValidException} 异常 <p>
	 * 会在 {@link com.goblin.GlobalErrorController#methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException)} 中得到处理
	 * <p>
	 * 这样代码整洁些,多余的动作交给框架处理,当然如果要进行细粒度的处理还是要使用 {@link BindingResult} 进行处理
	 *
	 * @param user
	 * @return
	 */
	@PostMapping( "save-validated-2" )
	public ResponseEntity< UserForm > saveValidated2 ( @RequestBody
													   @Validated( { ValidatedGroups.Save.class , Default.class } ) UserForm user ) {
		LogUtils.getLogger().debug( "user : {},验证通过" , user );
		return ResponseEntityPro.ok( user );
	}


}
