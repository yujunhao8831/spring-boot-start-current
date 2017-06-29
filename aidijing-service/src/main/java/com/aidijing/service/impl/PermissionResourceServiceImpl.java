package com.aidijing.service.impl;

import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.PageRowBounds;
import com.aidijing.common.GlobalConstant;
import com.aidijing.domain.PermissionResource;
import com.aidijing.domain.Role;
import com.aidijing.domain.RolePermissionResource;
import com.aidijing.domain.User;
import com.aidijing.mapper.PermissionResourceMapper;
import com.aidijing.service.PermissionResourceService;
import com.aidijing.service.RolePermissionResourceService;
import com.aidijing.service.RoleService;
import com.aidijing.service.UserService;
import com.aidijing.vo.PermissionResourceVO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 后台管理权限资源表 服务实现类
 * </p>
 *
 * @author 披荆斩棘
 * @since 2017-06-19
 */
@Service
public class PermissionResourceServiceImpl extends ServiceImpl< PermissionResourceMapper, PermissionResource > implements PermissionResourceService {

    @Autowired
    private UserService                   userService;
    @Autowired
    private RoleService                   roleService;
    @Autowired
    private RolePermissionResourceService rolePermissionResourceService;

    @Override
    public PageInfo listPage ( PageRowBounds pageRowBounds ) {
        PageHelper.startPage( pageRowBounds.getOffset(), pageRowBounds.getLimit() );
        return new PageInfo( super.selectList( null ) );
    }


    @Override
    public List< PermissionResourceVO > listPermission () {
        final List< PermissionResource > resources = super.selectList( null );
        if ( CollectionUtils.isEmpty( resources ) ) {
            return Collections.emptyList();
        }
        return this.tree( resources );
    }

    @Override
    public List< PermissionResourceVO > listUserPermission ( Long userId ) {
        // 1. 得到用户
        final User user = userService.selectById( userId );

        if ( Objects.isNull( user ) ) {
            return Collections.emptyList();
        }

        // 2. 得到角色
        final List< Role > roles = roleService.getByUserId( user.getId() );

        if ( CollectionUtils.isEmpty( roles ) ) {
            return Collections.emptyList();
        }

        // 3. 得到角色资源中间表信息
        final List< RolePermissionResource > rolePermissionResources = rolePermissionResourceService.selectList(
                new Condition().in(
                        "role_id",
                        roles.parallelStream().map( Role::getId ).collect( Collectors.toList() )
                )
        );

        if ( CollectionUtils.isEmpty( rolePermissionResources ) ) {
            return Collections.emptyList();
        }

        // 4. 得到权限资源
        final List< PermissionResource > permissionResources = super.selectBatchIds(
                rolePermissionResources.parallelStream()
                                       .map( RolePermissionResource::getId )
                                       .collect( Collectors.toList() )
        );

        if ( CollectionUtils.isEmpty( permissionResources ) ) {
            return Collections.emptyList();
        }

        return this.tree( permissionResources );
    }


    private List< PermissionResourceVO > tree ( List< PermissionResource > permissionResources ) {
        Map< Long, List< PermissionResourceVO > > content = new HashMap<>();
        permissionResources.forEach( permissionResource -> {
            List< PermissionResourceVO > resources = content.get( permissionResource.getParentId() );
            if ( CollectionUtils.isEmpty( resources ) ) {
                resources = new ArrayList<>();
            }
            PermissionResourceVO vo = new PermissionResourceVO();
            BeanUtils.copyProperties( permissionResource, vo );

            // method处理
            if ( StringUtils.isNotEmpty( permissionResource.getResourceApiUriMethods() ) ) {
                String[] methods = permissionResource.getResourceApiUriMethods().split( "," );
                if ( ArrayUtils.isNotEmpty( methods ) ) {
                    vo.setMethods( Arrays.asList( methods ) );
                }
            }
            resources.add( vo );
            content.put( permissionResource.getParentId(), resources );
        } );
        return this.treeOrder( GlobalConstant.PERMISSION_RESOURCE_ROOT_ID, content );
    }

    /**
     * 规整
     *
     * @param parentId : 上级ID
     * @param content
     * @return
     */
    private List< PermissionResourceVO > treeOrder ( Long parentId,
                                                     Map< Long, List< PermissionResourceVO > > content ) {
        List< PermissionResourceVO > result   = new ArrayList<>();
        List< PermissionResourceVO > children = content.get( parentId );
        if ( CollectionUtils.isNotEmpty( children ) ) {
            children.forEach( resource -> {
                resource.setChildren( this.treeOrder( resource.getId(), content ) );
                result.add( resource );
            } );
        }
        return result;
    }


}
