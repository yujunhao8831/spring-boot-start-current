package com.aidijing.manage;

import com.aidijing.common.ResponseEntityPro;
import com.aidijing.manage.bean.domain.RolePermissionResource;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.aidijing.common.util.JsonUtils.jsonToType;
import static com.aidijing.common.util.JsonUtils.toFilterJson;

/**
 * @author : 披荆斩棘
 * @date : 2017/11/14
 */
@Aspect
@Component
public class ResponseEntityAspect {


	@Around( "execution(org.springframework.http.ResponseEntity com.aidijing.*.controller.*Controller.*(..)) )" )
	public Object dataPlatform ( ProceedingJoinPoint joinPoint ) throws Throwable {

		Object returnValue = joinPoint.proceed();

		ResponseEntity responseEntity = ( ResponseEntity ) returnValue;

		// 用户权限或者用户自定义处理
		final RolePermissionResource currentRequestRolePermissionResource = ContextUtils.getCurrentRequestRolePermissionResource();
		if ( Objects.isNull( currentRequestRolePermissionResource ) ) {
			return returnValue;
		}
		if ( ResponseEntityPro.WILDCARD_ALL.equals( currentRequestRolePermissionResource.getResourceApiUriShowFields() ) ) {
			return returnValue;
		}

		final String resourceApiUriShowFields = currentRequestRolePermissionResource.getResourceApiUriShowFields();
		final String filterAfterJsonBody      = toFilterJson( responseEntity.getBody() , resourceApiUriShowFields );
		final Object filterAfterBody          = jsonToType( filterAfterJsonBody , responseEntity.getBody().getClass() );

		return new ResponseEntity<>( filterAfterBody ,
									 responseEntity.getHeaders() ,
									 responseEntity.getStatusCode() );


	}


}
