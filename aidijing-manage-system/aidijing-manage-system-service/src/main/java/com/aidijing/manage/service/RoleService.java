package com.aidijing.manage.service;

import com.aidijing.manage.bean.domain.Role;
import com.aidijing.manage.bean.domain.UserRole;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 * 后台管理角色表 服务类
 * </p>
 *
 * @author 披荆斩棘
 * @since 2017-06-19
 */
public interface RoleService extends IService< Role > {


    List< Role > listByUserId ( Long userId );

    boolean save ( Long userId , Long roleId );

    List< Role > listByUserRole ( List< UserRole > userRoles );

    List< Role > listSuperAdminRole ();

    boolean deleteRelatedById ( Long id );

	Role getByRoleNameCode ( String roleCode );

}
