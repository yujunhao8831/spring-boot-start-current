package com.goblin.manage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.goblin.common.PagingRequest;
import com.goblin.manage.bean.domain.RolePermissionResource;

import java.util.List;

/**
 * <p>
 * 后台管理角色资源中间表 服务类
 * </p>
 *
 * @author 披荆斩棘
 * @since 2017-06-19
 */
public interface RolePermissionResourceService extends IService<RolePermissionResource> {

    PageInfo<RolePermissionResource> listPage ( PagingRequest pagingRequest );

    List<RolePermissionResource> listByUserId ( Long userId );


}
