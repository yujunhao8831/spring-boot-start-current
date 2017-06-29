package com.aidijing.vo;

import com.aidijing.domain.PermissionResource;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : 披荆斩棘
 * @date : 2017/6/21
 */
@Data
@Accessors( chain = true )
public class PermissionResourceVO extends PermissionResource {

    private List< String >               methods  = new ArrayList<>();
    private List< PermissionResourceVO > children = new ArrayList<>();


}
