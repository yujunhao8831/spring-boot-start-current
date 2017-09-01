package com.aidijing.manage;

import com.aidijing.manage.bean.domain.PermissionResource;
import com.aidijing.manage.bean.domain.Role;
import com.aidijing.manage.bean.domain.RolePermissionResource;
import com.aidijing.manage.bean.domain.User;
import com.aidijing.manage.bean.domain.enums.ResourceType;
import com.aidijing.manage.bean.domain.enums.RoleType;
import com.aidijing.manage.service.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.*;

/**
 * @author : 披荆斩棘
 * @date : 2017/7/21
 */
@RunWith( SpringRunner.class )
@SpringBootTest
public class InitAppSql {

	@Autowired
	private UserService                                        userService;
	@Autowired
	private PasswordEncoder                                    passwordEncoder;
	@Autowired
	private RoleService                                        roleService;
	@Autowired
	private UserRoleService                                    userRoleService;
	@Autowired
	private PermissionResourceService                          permissionResourceService;
	@Autowired
	private RolePermissionResourceService                      rolePermissionResourceService;
	@Autowired
	private List< Map< Set< String >, Set< RequestMethod > > > requestMappingInfos;

	@Test
	public void init () throws Exception {
		User user = new User();
		if ( Objects.nonNull( userService.findByUsername( "admin" ) ) ) {
			return;
		}
		// 用户
		user.setUsername( "admin" )
			.setPassword( passwordEncoder.encode( "123456" ) );
		userService.save( user );


		List< PermissionResource > permissionResources = new ArrayList<>( requestMappingInfos.size() );
		// 资源
		requestMappingInfos.forEach( requestMappingInfo -> {
			requestMappingInfo.forEach( ( apis , methods ) -> {
				PermissionResource permissionResource = new PermissionResource();
				final String       api                = apis.iterator().next();
				permissionResource.setPermissionName( api )
								  .setResourceType( ResourceType.API )
								  .setResourceApiUri( api )
								  .setResourceApiUriMethods( methods.iterator().next().name() );
				permissionResources.add( permissionResource );
			} );
		} );
		permissionResourceService.insertBatch( permissionResources );

		// 角色
		if ( Objects.isNull( roleService.getByRoleNameCode( "ROOT" ) ) ) {
			Role role = new Role();
			role.setRoleNameCode( "ROOT" )
				.setRoleType( RoleType.ROOT )
				.setRoleName( "普罗米修斯" );
			roleService.insert( role );
			roleService.save( user.getId() , role.getId() );
			// 角色资源
			permissionResources.forEach( permissionResource -> {
				if ( ! permissionResourceService.roleHasResource( role.getId() , permissionResource.getId() ) ) {
					RolePermissionResource rolePermissionResource = new RolePermissionResource();
					rolePermissionResource.setRoleId( role.getId() )
										  .setPermissionResourceId( permissionResource.getId() );
					rolePermissionResourceService.insert( rolePermissionResource );
				}

			} );


		}


	}
}













