package com.aidijing.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.aidijing.common.util.JsonUtils;
import com.aidijing.domain.*;
import com.aidijing.domain.enums.ResourceType;
import com.aidijing.vo.PermissionResourceVO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

/**
 * @author : 披荆斩棘
 * @date : 2017/6/21
 */
@RunWith( SpringRunner.class )
@SpringBootTest
public class PermissionResourceServiceTest {

    List< PermissionResource > permissionResources = new ArrayList<>();
    @Autowired
    private PermissionResourceService     permissionResourceService;
    @Autowired
    private UserService                   userService;
    @Autowired
    private RoleService                   roleService;
    @Autowired
    private UserRoleService               userRoleService;
    @Autowired
    private RolePermissionResourceService rolePermissionResourceService;

    @Before
    public void setUp () throws Exception {
        permissionResources = JsonUtils.jsonToType(
                "[{\"id\":1,\"parentId\":0,\"resourceDepth\":null,\"permissionSort\":1,\"permissionName\":\"用户管理\",\"resourceClass\":null,\"resourceStyle\":null,\"resourceRouterUrl\":null,\"resourceType\":\"MENU\",\"resourceApiUri\":null,\"resourceApiUriMethods\":\"GET\",\"createTime\":\"2017-06-21 09:29:45\",\"updateTime\":\"2017-06-21 09:29:45\",\"remark\":null,\"categoryCode\":\"C\"},{\"id\":2,\"parentId\":1,\"resourceDepth\":null,\"permissionSort\":1,\"permissionName\":\"用户列表\",\"resourceClass\":null,\"resourceStyle\":null,\"resourceRouterUrl\":null,\"resourceType\":\"MENU\",\"resourceApiUri\":null,\"resourceApiUriMethods\":\"GET\",\"createTime\":\"2017-06-21 09:29:45\",\"updateTime\":\"2017-06-21 09:29:45\",\"remark\":null,\"categoryCode\":\"C\"},{\"id\":3,\"parentId\":2,\"resourceDepth\":null,\"permissionSort\":1,\"permissionName\":\"查看用户\",\"resourceClass\":null,\"resourceStyle\":null,\"resourceRouterUrl\":null,\"resourceType\":\"API\",\"resourceApiUri\":\"/api/user\",\"resourceApiUriMethods\":\"GET,POST,DELETE,PUT\",\"createTime\":\"2017-06-21 09:29:45\",\"updateTime\":\"2017-06-21 15:22:48\",\"remark\":null,\"categoryCode\":\"C\"},{\"id\":4,\"parentId\":2,\"resourceDepth\":null,\"permissionSort\":2,\"permissionName\":\"查看用户\",\"resourceClass\":null,\"resourceStyle\":null,\"resourceRouterUrl\":null,\"resourceType\":\"API\",\"resourceApiUri\":\"/api/user/list\",\"resourceApiUriMethods\":\"GET\",\"createTime\":\"2017-06-21 09:29:45\",\"updateTime\":\"2017-06-21 11:29:13\",\"remark\":null,\"categoryCode\":\"C\"},{\"id\":5,\"parentId\":2,\"resourceDepth\":null,\"permissionSort\":3,\"permissionName\":\"查看用户\",\"resourceClass\":null,\"resourceStyle\":null,\"resourceRouterUrl\":null,\"resourceType\":\"API\",\"resourceApiUri\":\"/api/user/{id}\",\"resourceApiUriMethods\":\"GET,POST,DELETE,PUT\",\"createTime\":\"2017-06-21 09:29:45\",\"updateTime\":\"2017-06-21 15:22:48\",\"remark\":null,\"categoryCode\":\"C\"},{\"id\":6,\"parentId\":2,\"resourceDepth\":null,\"permissionSort\":5,\"permissionName\":\"删除用户\",\"resourceClass\":null,\"resourceStyle\":null,\"resourceRouterUrl\":null,\"resourceType\":\"API\",\"resourceApiUri\":\"/api/user/{id}\",\"resourceApiUriMethods\":\"DELETE\",\"createTime\":\"2017-06-21 09:29:45\",\"updateTime\":\"2017-06-21 11:29:13\",\"remark\":null,\"categoryCode\":\"C\"}]",
                new TypeReference< List< PermissionResource > >() {
                }
        );
    }

    @Test
    public void query () throws Exception {
//        User user = userService.selectById( 1L );
//        // 1. 得到角色
//        final List< Role > roles = roleService.getByUserId( user.getId() );
//        // 2. 得到角色资源中间表信息
//        final List< RolePermissionResource > rolePermissionResources = rolePermissionResourceService.selectList(
//                new Condition().in(
//                        "role_id",
//                        roles.parallelStream().map( Role::getId ).collect( Collectors.toList() )
//                )
//        );
//        // 3. 得到权限资源
//        final List< PermissionResource > permissionResources = permissionResourceService.selectBatchIds(
//                rolePermissionResources.parallelStream()
//                                       .map( RolePermissionResource::getId )
//                                       .collect( Collectors.toList() )
//        );


        Map< Long, List< PermissionResourceVO > > content = new HashMap<>();

        permissionResources.forEach( permissionResource -> {
            List< PermissionResourceVO > resources = content.get( permissionResource.getParentId() );
            if ( CollectionUtils.isEmpty( resources ) ) {
                resources = new ArrayList<>();
            }
            PermissionResourceVO vo = new PermissionResourceVO();
            BeanUtils.copyProperties( permissionResource, vo );

            if ( StringUtils.isNotEmpty( permissionResource.getResourceApiUriMethods() ) ) {
                String[] methods = permissionResource.getResourceApiUriMethods().split( "," );
                if ( ArrayUtils.isNotEmpty( methods ) ) {
                    vo.setMethods( Arrays.asList( methods ) );
                }
            }
            resources.add( vo );
            content.put( permissionResource.getParentId(), resources );
        } );

        final List< PermissionResourceVO > resources = treeOrder( 0L, content );

        // ----------------------------------------------------------------------------------------
        // ----------------------------------------------------------------------------------------
        // ----------------------------------------------------------------------------------------

        // permissionResources
        // root id : 0

        permissionResources.forEach( permissionResource -> {

        } );

    }

    private List< PermissionResourceVO > treeOrder ( Long rootId,
                                                     Map< Long, List< PermissionResourceVO > > content ) {
        List< PermissionResourceVO > result   = new ArrayList<>();
        List< PermissionResourceVO > children = content.get( rootId );
        if ( CollectionUtils.isNotEmpty( children ) ) {
            children.forEach( resource -> {
                resource.setChildren( treeOrder( resource.getId(), content ) );
                result.add( resource );
            } );
        }
        return result;
    }


    @Test
    public void init () throws Exception {
        User user = userService.selectById( 1L );

        Role role = new Role();
        role.setRoleName( "超级管理员" )
            .setRoleNameCode( "superAdmin" );

        roleService.insert( role );

        UserRole userRole = new UserRole();
        userRole.setRoleId( role.getId() )
                .setUserId( user.getId() );

        userRoleService.insert( userRole );

        final List< PermissionResource > permissionResources = permissionResourceService.selectList( null );

        permissionResources.forEach( permissionResource -> {
            RolePermissionResource rolePermissionResource = new RolePermissionResource();
            rolePermissionResource.setRoleId( role.getId() )
                                  .setPermissionResourceId( permissionResource.getId() );
            rolePermissionResourceService.insert( rolePermissionResource );
        } );

    }

    @Test
    public void permissionResourceServiceTest () throws Exception {
        PermissionResource menuResource = new PermissionResource()
                .setResourceType( ResourceType.MENU )
                .setPermissionName( "用户管理" )
                .setCategoryCode( "C" )
                .setParentId( 0L )
                .setPermissionSort( 1 )
                .setResourceApiUriMethods( HttpMethod.GET.name() );
        permissionResourceService.insert( menuResource );



        PermissionResource menuChildrenResource = new PermissionResource()
                .setResourceType( ResourceType.MENU )
                .setPermissionName( "用户列表" )
                .setCategoryCode( "C" )
                .setParentId( menuResource.getId() )
                .setPermissionSort( 1 )
                .setResourceApiUriMethods( HttpMethod.GET.name() );
        permissionResourceService.insert( menuChildrenResource );

        PermissionResource apiResource = new PermissionResource()
                .setParentId( menuChildrenResource.getId() )
                .setCategoryCode( "C" )
                .setPermissionSort( 1 )
                .setPermissionName( "查看用户" )
                .setResourceApiUriMethods( HttpMethod.GET.name() )
                .setResourceApiUri( "/api/user" )
                .setResourceType( ResourceType.API );
        permissionResourceService.insert( apiResource );


        PermissionResource apiResource2 = new PermissionResource()
                .setParentId( menuChildrenResource.getId() )
                .setCategoryCode( "C" )
                .setPermissionSort( 2 )
                .setPermissionName( "查看用户" )
                .setResourceApiUriMethods( HttpMethod.GET.name() )
                .setResourceApiUri( "/api/user/list" )
                .setResourceType( ResourceType.API );
        permissionResourceService.insert( apiResource2 );


        PermissionResource apiResource3 = new PermissionResource()
                .setParentId( menuChildrenResource.getId() )
                .setCategoryCode( "C" )
                .setPermissionSort( 3 )
                .setPermissionName( "查看用户" )
                .setResourceApiUriMethods( HttpMethod.GET.name() )
                .setResourceApiUri( "/api/user/{id}" )
                .setResourceType( ResourceType.API );

        permissionResourceService.insert( apiResource3 );

        PermissionResource apiResource5 = new PermissionResource()
                .setParentId( menuChildrenResource.getId() )
                .setCategoryCode( "C" )
                .setPermissionSort( 5 )
                .setPermissionName( "删除用户" )
                .setResourceApiUriMethods( HttpMethod.DELETE.name() )
                .setResourceApiUri( "/api/user/{id}" )
                .setResourceType( ResourceType.API );

        permissionResourceService.insert( apiResource5 );

    }
}






















