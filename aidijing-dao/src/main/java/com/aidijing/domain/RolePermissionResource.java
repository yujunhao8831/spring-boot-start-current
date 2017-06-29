package com.aidijing.domain;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 后台管理角色资源中间表
 * </p>
 *
 * @author 披荆斩棘
 * @since 2017-06-19
 */
@Data
@Accessors(chain = true)
@TableName("manager_role_permission_resource")
public class RolePermissionResource extends Model<RolePermissionResource> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	@TableId(value="id", type= IdType.AUTO)
	private  Long  id;
    /**
     * 后台管理角色_id
     */
	@TableField("role_id")
	private  Long  roleId;
    /**
     * 后台管理权限资源_id
     */
	@TableField("permission_resource_id")
	private  Long  permissionResourceId;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
