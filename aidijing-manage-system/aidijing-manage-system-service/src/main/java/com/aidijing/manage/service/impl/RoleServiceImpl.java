package com.aidijing.manage.service.impl;

import com.aidijing.common.util.AssertUtils;
import com.aidijing.manage.bean.domain.Role;
import com.aidijing.manage.bean.domain.UserRole;
import com.aidijing.manage.mapper.RoleMapper;
import com.aidijing.manage.service.PermissionResourceService;
import com.aidijing.manage.service.RolePermissionResourceService;
import com.aidijing.manage.service.RoleService;
import com.aidijing.manage.service.UserRoleService;
import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 后台管理角色表 服务实现类
 * </p>
 *
 * @author 披荆斩棘
 * @since 2017-06-19
 */
@Service
public class RoleServiceImpl extends ServiceImpl< RoleMapper, Role > implements RoleService {


	@Autowired
	private UserRoleService               userRoleService;
	@Autowired
	private PermissionResourceService     permissionResourceService;
	@Autowired
	private RolePermissionResourceService rolePermissionResourceService;


	@Override
	public List< Role > listByUserId ( Long userId ) {
		final List< Long > roleIds = userRoleService
			.selectObjs( new Condition().eq( "user_id" , userId ).setSqlSelect( "role_id" ) );

		if ( CollectionUtils.isEmpty( roleIds ) ) {
			return null;
		}
		return this.selectBatchIds( roleIds );
	}


	@Override
	public boolean save ( Long userId , Long roleId ) {
		if ( Objects.isNull( userId ) ) {
			return false;
		}
		final Role role = super.selectById( roleId );
		AssertUtils.isTrue( Objects.isNull( role ) , "角色不存在" );
		UserRole userRole = new UserRole();
		userRole.setUserId( userId )
				.setRoleId( roleId );
		return userRoleService.insert( userRole );
	}

	@Override
	public List< Role > listByUserRole ( List< UserRole > userRoles ) {
		if ( CollectionUtils.isEmpty( userRoles ) ) {
			return Collections.EMPTY_LIST;
		}
		final List< Long > roleIds = userRoles.parallelStream()
											  .map( UserRole::getRoleId )
											  .collect( Collectors.toList() );
		return super.selectBatchIds( roleIds );
	}

	@Override
	public List< Role > listSuperAdminRole () {
		return super.selectList( null );
	}

	@Override
	public boolean deleteRelatedById ( Long id ) {
		AssertUtils.isTrue( ! super.deleteById( id ) , "角色删除失败" );
		AssertUtils.isTrue( ! rolePermissionResourceService.delete( new Condition().eq( "role_id" , id ) ) , "角色删除失败" );
		return true;
	}

	@Override
	public Role getByRoleNameCode ( String roleCode ) {
		return super.selectOne( new EntityWrapper< Role >().eq( "role_name_code" , roleCode ) );
	}


}












