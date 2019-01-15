package com.goblin.manage.jwt;

import com.goblin.manage.bean.domain.Role;
import com.goblin.manage.bean.domain.RolePermissionResource;
import com.goblin.manage.bean.vo.PermissionResourceVO;
import com.goblin.security.BasicJwtUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
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
 *
 * @author pijingzhanji
 */
@Getter
@Setter
@ToString
@Accessors( chain = true )
@AllArgsConstructor
@NoArgsConstructor
public class JwtUser implements BasicJwtUser {

	private Long                           id;
	private String                         username;
	private String                         password;
	private String                         nickName;
	private String                         realName;
	private String                         email;
	private String                         phone;
	private String                         userImageUrl;
	private Date                           lastPasswordResetDate;
	private Long                           createManagerAdminUserId;
	private Date                           createTime;
	private Date                           updateTime;
	private String                         remark;
	private boolean                        enabled;
	/** 用户角色信息 **/
	private List< Role >                   roles;
	/** 用户权限资源信息 **/
	private List< PermissionResourceVO >   permissionResource;
	/** 用户后台管理角色资源中间表 **/
	private List< RolePermissionResource > rolePermissionResources;


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
