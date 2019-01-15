package com.goblin.manage;

import com.goblin.common.ResponseEntityPro;
import com.goblin.manage.bean.domain.RolePermissionResource;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.goblin.common.util.JsonUtils.jsonToType;
import static com.goblin.common.util.JsonUtils.toFilterJson;

/**
 * api {@link ResponseEntity} 返回类型处理,针对 {@link RolePermissionResource#getResourceApiUriShowFields()} 字段
 *
 * @author : 披荆斩棘
 * @date : 2017/11/14
 */
@Aspect
@Component
public class ResponseEntityAspect {


	@Around( "execution(org.springframework.http.ResponseEntity com.goblin.*.controller.*Controller.*(..)) )" )
	public Object returnValueHandle ( ProceedingJoinPoint joinPoint ) throws Throwable {

		Object returnValue = joinPoint.proceed();

		ResponseEntity responseEntity = ( ResponseEntity ) returnValue;

		// 用户权限或者用户自定义处理
		final RolePermissionResource currentRequestRolePermissionResource = ContextUtils.getCurrentRequestRolePermissionResource();
		if ( Objects.isNull( currentRequestRolePermissionResource ) ) {
			return returnValue;
		}
		if ( ResponseEntityPro.WILDCARD_ALL.equals( currentRequestRolePermissionResource.getResourceApiUriShowFields() ) ) {
			ContextUtils.removeCurrentRequestRolePermissionResource();
			return returnValue;
		}

		final String resourceApiUriShowFields = currentRequestRolePermissionResource.getResourceApiUriShowFields();
		final String filterAfterJsonBody      = toFilterJson( responseEntity.getBody() , resourceApiUriShowFields );
		final Object filterAfterBody          = jsonToType( filterAfterJsonBody , responseEntity.getBody().getClass() );
		ContextUtils.removeCurrentRequestRolePermissionResource();
		return new ResponseEntity<>( filterAfterBody ,
									 responseEntity.getHeaders() ,
									 responseEntity.getStatusCode() );


	}


}
