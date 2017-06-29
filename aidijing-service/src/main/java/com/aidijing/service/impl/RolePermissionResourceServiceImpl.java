package com.aidijing.service.impl;

import com.aidijing.domain.RolePermissionResource;
import com.aidijing.mapper.RolePermissionResourceMapper;
import com.aidijing.service.RolePermissionResourceService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.PageRowBounds;

/**
 * <p>
 * 后台管理角色资源中间表 服务实现类
 * </p>
 *
 * @author 披荆斩棘
 * @since 2017-06-19
 */
@Service
public class RolePermissionResourceServiceImpl extends ServiceImpl<RolePermissionResourceMapper, RolePermissionResource> implements RolePermissionResourceService {


    @Override
    public PageInfo listPage( PageRowBounds pageRowBounds ) {
        PageHelper.startPage( pageRowBounds.getOffset(), pageRowBounds.getLimit() );
        return new PageInfo( super.selectList( null ) );
    }
    
    
}
