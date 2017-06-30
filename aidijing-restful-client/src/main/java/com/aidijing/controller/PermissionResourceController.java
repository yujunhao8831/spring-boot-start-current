package com.aidijing.controller;

import com.aidijing.common.ResponseEntity;
import com.aidijing.domain.PermissionResource;
import com.aidijing.permission.Pass;
import com.aidijing.service.PermissionResourceService;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.PageRowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * <p>
 * 后台管理权限资源表 前端控制器
 * </p>
 *
 * @author 披荆斩棘
 * @since 2017-06-26
 */
@RestController
@RequestMapping( "permission-resource" )
public class PermissionResourceController {
    @Autowired
    private PermissionResourceService                          permissionResourceService;
    @Autowired
    private List< Map< Set< String >, Set< RequestMethod > > > requestMappingInfos;


    @Pass
    @RequestMapping( "request-mapping-infos" )
    public ResponseEntity listRequestMappingInfos () {
        return ResponseEntity.ok().setResponseContent( requestMappingInfos );
    }

    /**
     * URI    : { GET } /permission-resource/{id}
     *
     * @param id : 主键
     * @return
     */
    @GetMapping( "{id}" )
    public ResponseEntity< PermissionResource > get ( @PathVariable Long id ) {
        return ResponseEntity.ok().setResponseContent( permissionResourceService.selectById( id ) );
    }

    /**
     * URI    : { GET } /permission-resource
     *
     * @param pageRowBounds : {@link PageRowBounds}
     * @return
     */
    @GetMapping
    public ResponseEntity< PageInfo< PermissionResource > > list ( PageRowBounds pageRowBounds ) {
        return ResponseEntity.ok().setResponseContent( permissionResourceService.listPage( pageRowBounds ) );
    }

    /**
     * URI    : { POST } /permission-resource
     *
     * @param permissionResource : {@link PermissionResource}
     * @return
     */
    @PostMapping
    public ResponseEntity insert ( @RequestBody PermissionResource permissionResource ) {
        if ( ! permissionResourceService.insert( permissionResource ) ) {
            return ResponseEntity.ok( "保存失败" );
        }
        return ResponseEntity.ok( "保存成功" );
    }

    /**
     * URI    : { PUT } /permission-resource/{id}
     *
     * @param id                 : 主键
     * @param permissionResource : {@link PermissionResource}
     * @return
     */
    @PutMapping( "{id}" )
    public ResponseEntity update ( @PathVariable Long id,
                                   @RequestBody PermissionResource permissionResource ) {
        if ( ! permissionResourceService.updateById( permissionResource.setId( id ) ) ) {
            return ResponseEntity.ok( "更新失败" );
        }
        return ResponseEntity.ok( "更新成功" );
    }

    /**
     * URI    : { DELETE } /permission-resource/{id}
     *
     * @param id : 主键
     * @return
     */
    @DeleteMapping( "{id}" )
    public ResponseEntity delete ( @PathVariable Long id ) {
        if ( ! permissionResourceService.deleteById( id ) ) {
            return ResponseEntity.ok( "删除失败" );
        }
        return ResponseEntity.ok( "删除成功" );
    }


}