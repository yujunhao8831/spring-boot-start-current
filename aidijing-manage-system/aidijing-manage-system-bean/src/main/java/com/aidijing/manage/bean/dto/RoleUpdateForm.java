package com.aidijing.manage.bean.dto;

import com.aidijing.manage.bean.domain.enums.RoleType;
import lombok.Data;
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
@Data
@Accessors( chain = true )
public class RoleUpdateForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 角色名称
     */
    private String   roleName;
    /**
     * 角色名称core(组code+角色code)
     */
    private String   roleNameCode;
    /**
     * 角色类型(ROOT:根,SUPER_ADMIN:超级管理员,ADMIN:管理员,USER:普通用户)
     */
    private RoleType roleType;
    /**
     * 角色状态(1:激活,0:锁定)
     */
    private Boolean  enabled;
    /**
     * 描述
     */
    private String   description;
    /**
     * 备注
     */
    private String   remark;


}
