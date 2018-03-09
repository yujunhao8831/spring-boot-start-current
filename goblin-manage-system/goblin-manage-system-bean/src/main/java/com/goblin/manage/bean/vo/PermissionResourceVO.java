package com.goblin.manage.bean.vo;

import com.goblin.common.util.RecursionUtils;
import com.goblin.manage.bean.domain.PermissionResource;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : 披荆斩棘
 * @date : 2017/6/21
 */
@Getter
@Setter
@Accessors( chain = true )
@ToString( callSuper = true )
public class PermissionResourceVO extends PermissionResource implements RecursionUtils.ParentChildrenRecursion< PermissionResourceVO > {

    private List< String >               methods  = new ArrayList<>();
    private List< PermissionResourceVO > children = new ArrayList<>();

}
