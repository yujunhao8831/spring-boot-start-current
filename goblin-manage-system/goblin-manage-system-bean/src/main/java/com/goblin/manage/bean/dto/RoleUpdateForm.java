package com.goblin.manage.bean.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 后台管理角色表
 * </p>
 *
 * @author 披荆斩棘
 * @since 2017-07-04
 */
@Getter
@Setter
@ToString
@Accessors( chain = true )
public class RoleUpdateForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 角色名称
     */
    private String  roleName;
    /**
     * 角色名称core(组code+角色code)
     */
    private String  roleNameCode;
    /**
     * 角色状态(1:激活,0:锁定)
     */
    private Boolean enabled;
    /**
     * 描述
     */
    private String  description;
    /**
     * 备注
     */
    private String  remark;


}
