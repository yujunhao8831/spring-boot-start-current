package com.aidijing.manage.jwt;

import com.aidijing.manage.GlobalCacheConstant;
import com.aidijing.manage.bean.domain.Role;
import com.aidijing.manage.bean.domain.RolePermissionResource;
import com.aidijing.manage.bean.domain.User;
import com.aidijing.manage.bean.domain.enums.RoleType;
import com.aidijing.manage.bean.vo.PermissionResourceVO;
import com.aidijing.manage.service.PermissionResourceService;
import com.aidijing.manage.service.RolePermissionResourceService;
import com.aidijing.manage.service.RoleService;
import com.aidijing.manage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;


@Service
@CacheConfig( cacheNames = GlobalCacheConstant.USER_DETAILS_SERVICE_NAMESPACE )
public class JwtUserDetailsService implements UserDetailsService {

	@Lazy
	@Autowired
	private UserService                   userService;
	@Autowired
	private RoleService                   roleService;
	@Autowired
	private PermissionResourceService     permissionResourceService;
	@Autowired
	private RolePermissionResourceService rolePermissionResourceService;


	@Cacheable( key = "#username" )
	@Override
	public UserDetails loadUserByUsername ( String username ) throws UsernameNotFoundException {
		User user = userService.findByUsername( username );
		if ( user == null ) {
			throw new UsernameNotFoundException( String.format( "该'%s'用户名不存在." , username ) );
		}
		// 虽然说可以对SuperAdmin和Root直接放行,但在程序上还是应该让他们有归属,该有的角色和权限信息还是得有
		List< Role > roles = roleService.listByUserId( user.getId() );
		if ( Objects.nonNull( roles ) && roles.parallelStream().anyMatch( this::isSuperAdmin ) ) {
			return buildSuperAdminJwtUser( user );
		}
		final List< RolePermissionResource > rolePermissionResources = rolePermissionResourceService.listByUserId( user.getId() );
		final List< PermissionResourceVO >   permissionResource      = permissionResourceService.listUserPermissionByRolePermissionResource( rolePermissionResources );
		return new JwtUser(
			user.getId() ,
			user.getUsername() ,
			user.getPassword() ,
			user.getNickName() ,
			user.getRealName() ,
			user.getEmail() ,
			user.getPhone() ,
			user.getUserImageUrl() ,
			user.getLastPasswordResetDate() ,
			user.getCreateUserId() ,
			user.getCreateTime() ,
			user.getUpdateTime() ,
			user.getRemark() ,
			user.getEnabled() ,
			roles ,
			permissionResource ,
			rolePermissionResources
		);
	}

	private JwtUser buildSuperAdminJwtUser ( User user ) {
		final List< Role >                   roles                   = roleService.listSuperAdminRole();
		final List< RolePermissionResource > rolePermissionResources = rolePermissionResourceService.listSuperAdminRolePermissionResource();
		final List< PermissionResourceVO >   permissionResource      = permissionResourceService.listSuperAdminPermissionResource();
		return new JwtUser(
			user.getId() ,
			user.getUsername() ,
			user.getPassword() ,
			user.getNickName() ,
			user.getRealName() ,
			user.getEmail() ,
			user.getPhone() ,
			user.getUserImageUrl() ,
			user.getLastPasswordResetDate() ,
			user.getCreateUserId() ,
			user.getCreateTime() ,
			user.getUpdateTime() ,
			user.getRemark() ,
			user.getEnabled() ,
			roles ,
			permissionResource ,
			rolePermissionResources
		);
	}

	private boolean isSuperAdmin ( Role role ) {
		final boolean isSuperAdmin = Objects.equals( role.getRoleType().getCode() , RoleType.SUPER_ADMIN.getCode() );
		final boolean isRoot       = Objects.equals( role.getRoleType().getCode() , RoleType.ROOT.getCode() );
		return isSuperAdmin || isRoot;
	}


}










