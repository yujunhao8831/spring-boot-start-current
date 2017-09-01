package com.aidijing.manage.service.impl;

import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.aidijing.manage.bean.domain.Role;
import com.aidijing.manage.bean.domain.RolePermissionResource;
import com.aidijing.manage.bean.domain.User;
import com.aidijing.common.PagingRequest;
import com.aidijing.manage.mapper.RolePermissionResourceMapper;
import com.aidijing.manage.service.RolePermissionResourceService;
import com.aidijing.manage.service.RoleService;
import com.aidijing.manage.service.UserService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 后台管理角色资源中间表 服务实现类
 * </p>
 *
 * @author 披荆斩棘
 * @since 2017-06-19
 */
@Service
public class RolePermissionResourceServiceImpl extends ServiceImpl< RolePermissionResourceMapper, RolePermissionResource > implements RolePermissionResourceService {

    @Autowired
    private RoleService roleService;
    @Autowired
    private UserService userService;

    @Override
    public PageInfo listPage ( PagingRequest pagingRequest ) {
        PageHelper.startPage( pagingRequest.getPageNumber(), pagingRequest.getPageSize() );
        return new PageInfo( super.selectList( null ) );
    }

    @Override
    public List< RolePermissionResource > listByUserId ( Long userId ) {
        // 1. 得到用户
        final User user = userService.selectById( userId );

        if ( Objects.isNull( user ) ) {
            return Collections.emptyList();
        }

        // 2. 得到角色
        final List< Role > roles = roleService.listByUserId( user.getId() );

        if ( CollectionUtils.isEmpty( roles ) ) {
            return Collections.emptyList();
        }
        // 3. 得到角色资源中间表信息
        final List< RolePermissionResource > rolePermissionResources = super.selectList(
                new Condition().in(
                        "role_id",
                        roles.parallelStream().map( Role::getId ).collect( Collectors.toList() )
                )
        );
        return rolePermissionResources;
    }

    @Override
    public List< RolePermissionResource > listSuperAdminRolePermissionResource () {
        return selectList( null );
    }


}
