package com.goblin.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.goblin.common.GlobalConstant;
import com.goblin.common.PagingRequest;
import com.goblin.common.util.AssertUtils;
import com.goblin.manage.GlobalCacheConstant;
import com.goblin.manage.bean.domain.PermissionResource;
import com.goblin.manage.bean.domain.RolePermissionResource;
import com.goblin.manage.bean.domain.enums.ResourceType;
import com.goblin.manage.bean.dto.PermissionResourceForm;
import com.goblin.manage.bean.vo.PermissionResourceVO;
import com.goblin.manage.mapper.PermissionResourceMapper;
import com.goblin.manage.service.PermissionResourceService;
import com.goblin.manage.service.RolePermissionResourceService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private RolePermissionResourceService rolePermissionResourceService;

    @Override
    public PageInfo< PermissionResource > listPage ( PagingRequest pagingRequest ) {
        PageHelper.startPage( pagingRequest.getPageNumber() , pagingRequest.getPageSize() );
        return new PageInfo<>( super.list() );
    }


    @Override
    public List< PermissionResourceVO > listPermission () {
        final List< PermissionResource > resources = super.list();
        if ( CollectionUtils.isEmpty( resources ) ) {
            return Collections.emptyList();
        }
        return this.tree( resources );
    }

    @Override
    public List< PermissionResourceVO > listUserPermission ( Long userId ) {
        return this.listByRolePermissionResource( rolePermissionResourceService.listByUserId( userId ) );
    }


    @Override
    public List< PermissionResourceVO > listUserPermissionByRolePermissionResource ( List< RolePermissionResource > rolePermissionResources ) {
        return this.listByRolePermissionResource( rolePermissionResources );
    }

    @Override
    @CacheEvict( value = GlobalCacheConstant.USER_DETAILS_SERVICE_NAMESPACE, allEntries = true, condition = "#result != null" )
    public boolean save ( PermissionResourceForm form ) {
        PermissionResource resource = new PermissionResource();
        BeanUtils.copyProperties( form , resource );
        // 排序字段处理
        if ( Objects.isNull( resource.getPermissionSort() ) ) {
            Object                             sort;
            QueryWrapper< PermissionResource > query = new QueryWrapper<>();
            query.orderByAsc( "id" ).select( "permission_sort" );
            if ( Objects.isNull( form.getParentId() ) ) {
                query.eq( "parent_id" , GlobalConstant.ROOT_ID );
                sort = super.getObj( query );
            } else {
                query.eq( "parent_id" , form.getParentId() );
                sort = super.getObj( query );
            }
            resource.setPermissionSort( ( Integer ) sort );
        }
        apiHandle( form , resource );
        return super.save( resource );
    }

    // api 处理
    private void apiHandle ( PermissionResourceForm form , PermissionResource resource ) {
        if ( Objects.equals( resource.getResourceType().getValue() , ResourceType.API.getValue() ) ) {
            final Set< String > methods = Stream.of( RequestMethod.values() )
                                                .map( RequestMethod::name )
                                                .collect( Collectors.toSet() );
            for ( String method : form.getResourceApiUriMethods() ) {
                AssertUtils.isTrue( ! methods.contains( method ) , "操作失败,resourceApiUriMethods格式不正确" );
            }

            resource.setResourceApiUriMethods(
                    form.getResourceApiUriMethods()
                        .parallelStream()
                        .collect( Collectors.joining( "," ) )
            );

            // 接口类型处理
            // + 接口类型权限资源, 这两个字段不能为空 -> resourceApiUri  resourceApiUriMethods
            AssertUtils.isTrue(
                    StringUtils.isBlank( resource.getResourceApiUri() ) ,
                    "api类型权限资源,resourceApiUri字段不能为空"
            );
            AssertUtils.isTrue(
                    StringUtils.isBlank( resource.getResourceApiUriMethods() ) ,
                    "api类型权限资源,resourceApiUriMethods字段不能为空"
            );
        }
    }

    @Override
    @CacheEvict( value = GlobalCacheConstant.USER_DETAILS_SERVICE_NAMESPACE, allEntries = true, condition = "#result != null" )
    public boolean deleteRelatePermissionResource ( List< PermissionResourceVO > vos ) {
        final List< Long > resourceIds = vos.parallelStream()
                                            .map( PermissionResourceVO::getId )
                                            .collect( Collectors.toList() );
        // 删除资源
        AssertUtils.isTrue( ! super.removeByIds( resourceIds ) , "资源删除失败" );


        // 删除相关角色资源中间表信息
        final List< Object > middleIds = rolePermissionResourceService.listObjs(
                new QueryWrapper< RolePermissionResource >().in( "permission_resource_id" , resourceIds )
                                                            .select( "id" )
        );
        if ( CollectionUtils.isNotEmpty( middleIds ) ) {
            AssertUtils.isTrue( ! rolePermissionResourceService.removeByIds( middleIds.parallelStream()
                                                                                      .map( Object::toString )
                                                                                      .collect( Collectors.toList() ) ) ,
                                "资源删除失败" );
        }
        return true;
    }

    @Override
    @CacheEvict( value = GlobalCacheConstant.USER_DETAILS_SERVICE_NAMESPACE, allEntries = true, condition = "#result != null" )
    public boolean update ( PermissionResourceForm form ) {
        PermissionResource permissionResource = new PermissionResource();
        BeanUtils.copyProperties( form , permissionResource );
        apiHandle( form , permissionResource );
        return super.updateById( permissionResource );
    }

    @Override
    public List< PermissionResourceVO > listSuperAdminPermissionResource () {
        return this.tree( super.list() );
    }

    @Override
    public boolean roleHasResource ( Long roleId , Long permissionResourceId ) {
        final RolePermissionResource rolePermissionResource = rolePermissionResourceService.getOne(
                new QueryWrapper< RolePermissionResource >().eq( "role_id" , roleId )
                                                            .eq( true ,
                                                                 "permission_resource_id" ,
                                                                 permissionResourceId )
        );
        return Objects.nonNull( rolePermissionResource );
    }

    private List< PermissionResourceVO > listByRolePermissionResource ( List< RolePermissionResource > rolePermissionResources ) {
        if ( CollectionUtils.isEmpty( rolePermissionResources ) ) {
            return Collections.emptyList();
        }
        // 得到权限资源
        final Collection< PermissionResource > permissionResources = super.listByIds(
                rolePermissionResources.parallelStream()
                                       .map( RolePermissionResource::getId )
                                       .collect( Collectors.toList() )
        );
        if ( CollectionUtils.isEmpty( permissionResources ) ) {
            return Collections.emptyList();
        }
        return this.tree( new ArrayList<>( permissionResources ) );
    }


    private List< PermissionResourceVO > tree ( List< PermissionResource > permissionResources ) {
        Map< Long, List< PermissionResourceVO > > content = new HashMap<>();
        permissionResources.forEach( permissionResource -> {
            List< PermissionResourceVO > resources = content.get( permissionResource.getParentId() );
            if ( CollectionUtils.isEmpty( resources ) ) {
                resources = new ArrayList<>();
            }
            PermissionResourceVO vo = new PermissionResourceVO();
            BeanUtils.copyProperties( permissionResource , vo );
            // method处理
            if ( StringUtils.isNotEmpty( permissionResource.getResourceApiUriMethods() ) ) {
                String[] methods = StringUtils.split( permissionResource.getResourceApiUriMethods() , "," );
                if ( ArrayUtils.isNotEmpty( methods ) ) {
                    vo.setMethods( Arrays.asList( methods ) );
                }
            }
            resources.add( vo );
            content.put( permissionResource.getParentId() , resources );
        } );
        return this.treeOrder( GlobalConstant.ROOT_ID , content );
    }

    /**
     * 规整
     *
     * @param parentId : 上级ID
     * @param content
     * @return
     */
    private List< PermissionResourceVO > treeOrder ( Long parentId ,
                                                     Map< Long, List< PermissionResourceVO > > content ) {
        List< PermissionResourceVO > result   = new ArrayList<>();
        List< PermissionResourceVO > children = content.get( parentId );
        if ( CollectionUtils.isNotEmpty( children ) ) {
            children.forEach( resource -> {
                resource.setChildren( this.treeOrder( resource.getId() , content ) );
                result.add( resource );
            } );
        }
        return result;
    }


}
