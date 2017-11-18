package com.aidijing.manage.jwt;

import com.aidijing.manage.bean.domain.Role;
import com.aidijing.manage.bean.domain.RolePermissionResource;
import com.aidijing.manage.bean.vo.PermissionResourceVO;
import com.aidijing.security.BasicJwtUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * jwt对象
 * @author pijingzhanji
 */
@Data
@Accessors( chain = true )
@AllArgsConstructor
public class JwtUser implements BasicJwtUser {

	private final Long                           id;
	private final String                         username;
	private final String                         password;
	private final String                         nickName;
	private final String                         realName;
	private final String                         email;
	private final String                         phone;
	private final String                         userImageUrl;
	private final Date                           lastPasswordResetDate;
	private final Long                           createManagerAdminUserId;
	private final Date                           createTime;
	private final Date                           updateTime;
	private final String                         remark;
	private final boolean                        enabled;
	/** 用户角色信息 **/
	private final List< Role >                   roles;
	/** 用户权限资源信息 **/
	private final List< PermissionResourceVO >   permissionResource;
	/** 用户后台管理角色资源中间表 **/
	private final List< RolePermissionResource > rolePermissionResources;


	@Override
	public String getUsername () {
		return this.username;
	}

	@Override
	public boolean isEnabled () {
		return this.enabled;
	}

	@JsonIgnore
	@Override
	public Collection< ? extends GrantedAuthority > getAuthorities () {
		if ( CollectionUtils.isEmpty( this.getRoles() ) ) {
			return null;
		}
		return this.getRoles().parallelStream()
				   .map( role -> new SimpleGrantedAuthority( role.getRoleNameCode() ) )
				   .collect( Collectors.toList() );
	}

	@Override
	public String getPassword () {
		return this.password;
	}

	@Override
	public boolean isAccountNonExpired () {
		return true;
	}

	@Override
	public boolean isAccountNonLocked () {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired () {
		return true;
	}


}
