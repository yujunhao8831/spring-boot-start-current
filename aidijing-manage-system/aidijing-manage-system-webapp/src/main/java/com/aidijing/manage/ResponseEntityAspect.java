package com.aidijing.manage;

import com.aidijing.common.ResponseEntityPro;
import com.aidijing.manage.bean.domain.RolePermissionResource;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static com.aidijing.common.util.JsonUtils.jsonToType;
import static com.aidijing.common.util.JsonUtils.toFilterJson;

/**
 * @author : 披荆斩棘
 * @date : 2017/11/14
 */
@Aspect
// @Component // TODO: 2017/11/14 还未测试
public class ResponseEntityAspect {


	@AfterReturning( pointcut = "execution(* com.aidijing.*.controller.*Controller.*(..))", returning = "returnValue" )
	public void dataPlatform ( Object returnValue ) {
		if ( ! ( returnValue instanceof ResponseEntity ) ) {
			return;
		}

		ResponseEntity responseEntity = ( ResponseEntity ) returnValue;

		// 用户权限或者用户自定义处理
		final RolePermissionResource currentRequestRolePermissionResource = ContextUtils.getCurrentRequestRolePermissionResource();
		if ( Objects.isNull( currentRequestRolePermissionResource ) ) {
			return;
		}
		if ( ResponseEntityPro.WILDCARD_ALL.equals( currentRequestRolePermissionResource.getResourceApiUriShowFields() ) ) {
			return;
		}

		final String resourceApiUriShowFields = currentRequestRolePermissionResource.getResourceApiUriShowFields();
		final String filterAfterJsonBody      = toFilterJson( responseEntity.getBody() , resourceApiUriShowFields );
		final Object filterAfterBody          = jsonToType( filterAfterJsonBody , responseEntity.getBody().getClass() );

		returnValue = new ResponseEntity<>( filterAfterBody ,
											responseEntity.getHeaders() ,
											responseEntity.getStatusCode() );

	}


}
