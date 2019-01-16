package com.goblin.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.goblin.common.PagingRequest;
import com.goblin.manage.bean.domain.Role;
import com.goblin.manage.bean.domain.RolePermissionResource;
import com.goblin.manage.bean.domain.User;
import com.goblin.manage.mapper.RolePermissionResourceMapper;
import com.goblin.manage.service.RolePermissionResourceService;
import com.goblin.manage.service.RoleService;
import com.goblin.manage.service.UserService;
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
    public PageInfo< RolePermissionResource > listPage ( PagingRequest pagingRequest ) {
        PageHelper.startPage( pagingRequest.getPageNumber() , pagingRequest.getPageSize() );
        return new PageInfo<>( super.list() );
    }

    @Override
    public List< RolePermissionResource > listByUserId ( Long userId ) {
        // 1. 得到用户
        final User user = userService.getById( userId );
        
        if ( Objects.isNull( user ) ) {
            return Collections.emptyList();
        }
        // 2. 得到角色
        final List< Role > roles = roleService.listByUserId( user.getId() );

        if ( CollectionUtils.isEmpty( roles ) ) {
            return Collections.emptyList();
        }
        // 3. 得到角色资源中间表信息
        return super.list(
                new QueryWrapper< RolePermissionResource >().lambda().in(
                        RolePermissionResource::getRoleId ,
                        roles.parallelStream().map( Role::getId ).collect( Collectors.toList() )
                )
        );
    }

}
