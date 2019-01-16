package com.goblin.manage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.goblin.manage.bean.domain.PermissionResource;
import com.goblin.manage.bean.domain.RolePermissionResource;
import com.goblin.manage.bean.dto.PermissionResourceForm;
import com.goblin.manage.bean.vo.PermissionResourceVO;
import com.goblin.common.PagingRequest;

import java.util.List;

/**
 * <p>
 * 后台管理权限资源表 服务类
 * </p>
 *
 * @author 披荆斩棘
 * @since 2017-06-19
 */
public interface PermissionResourceService extends IService< PermissionResource > {

    PageInfo< PermissionResource > listPage ( PagingRequest pagingRequest );
    /**
     * 所有权限资源列表,<b style="color:red">层级关系已整理</b>
     *
     * @return
     */
    List< PermissionResourceVO > listPermission ();

    /**
     * 用户权限资源列表,<b style="color:red">层级关系已整理</b>
     *
     * @param userId
     * @return
     */
    List< PermissionResourceVO > listUserPermission ( Long userId );

    List< PermissionResourceVO > listUserPermissionByRolePermissionResource ( List< RolePermissionResource > rolePermissionResources );

    boolean save ( PermissionResourceForm form );

    boolean deleteRelatePermissionResource ( List< PermissionResourceVO > vo );

    boolean update ( PermissionResourceForm form );

    List< PermissionResourceVO > listSuperAdminPermissionResource ();

	boolean roleHasResource ( Long roleId , Long permissionResourceId );
}
