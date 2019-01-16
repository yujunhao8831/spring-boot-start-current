package com.goblin.manage.controller;

import com.goblin.manage.bean.domain.PermissionResource;
import com.goblin.manage.permission.Pass;
import com.goblin.manage.service.PermissionResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 枚举映射
 *
 * @author : 披荆斩棘
 * @date : 2017/12/22
 */
@Pass
@RestController
@RequestMapping( "enums" )
public class EnumDemoController {

    @Autowired
    private PermissionResourceService permissionResourceService;


    @GetMapping
    public ResponseEntity< List< PermissionResource > > list () {
        return ResponseEntity.ok().body( permissionResourceService.list() );
    }


}
