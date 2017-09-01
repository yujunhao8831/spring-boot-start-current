package com.aidijing.manage.bean.dto;

import com.aidijing.manage.bean.domain.enums.RoleType;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 角色表单
 * </p>
 *
 * @author 披荆斩棘
 * @since 2017-07-04
 */
@Data
@Accessors( chain = true )
public class RoleForm implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long                           groupId;
    /**
     * 角色名称
     */
    @NotEmpty( message = "roleName不能为空" )
    private String                         roleName;
    /**
     * 角色名称code
     */
    private String                         roleNameCode;
    /**
     * 角色类型(root:根,super_admin:超级管理员,admin:管理员,user:普通用户)
     */
    @NotNull( message = "roleType不能为空" )
    private RoleType                       roleType;
    /**
     * 角色状态(1:激活,0:锁定)
     */
    private Boolean                        enabled;
    /**
     * 描述
     */
    private String                         description;
    /**
     * 备注
     */
    private String                         remark;
    /**
     * 权限
     */
    private List< PermissionResourceForm > permissionResources;


}
