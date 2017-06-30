package com.aidijing.domain;

import com.aidijing.domain.enums.ResourceType;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 后台管理权限资源表
 * </p>
 *
 * @author 披荆斩棘
 * @since 2017-06-19
 */
@Data
@Accessors( chain = true )
@TableName( "manager_permission_resource" )
public class PermissionResource extends Model< PermissionResource > {


    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId( value = "id", type = IdType.AUTO )
    private Long         id;
    /**
     * 父权限资源ID(0:表示root级)
     */
    @TableField( "parent_id" )
    private Long         parentId;
    /**
     * 资源深度
     */
    @TableField( "resource_depth" )
    private Integer      resourceDepth;
    /**
     * 排序字段
     */
    @TableField( "permission_sort" )
    private Integer      permissionSort;
    /**
     * 权限名称
     */
    @TableField( "permission_name" )
    private String       permissionName;
    /**
     * 资源样式class(前端class属性)
     */
    @TableField( "resource_class" )
    private String       resourceClass;
    /**
     * 资源样式style(前端style属性)
     */
    @TableField( "resource_style" )
    private String       resourceStyle;
    /**
     * 资源路由URL(前端使用)
     */
    @TableField( "resource_router_url" )
    private String       resourceRouterUrl;
    /**
     * 资源类型(API:接口,MENU:菜单,BUTTON:按钮)
     */
    @TableField( "resource_type" )
    private ResourceType resourceType;
    /**
     * 资源API URI(非必须,api才有)
     */
    @TableField( "resource_api_uri" )
    private String       resourceApiUri;
    /**
     * 资源API URI方法methods(GET,POST,DELETE,PUT,以','分割)
     */
    @TableField( "resource_api_uri_methods" )
    private String       resourceApiUriMethods;
    /**
     * 资源API URI 显示字段
     * (
     * "*":表示显示所有[默认"*"].
     * "-" + 字段名,表示排除某个字段.如果要排除多个以","进行分隔,比如: -username,-password.
     * 字段名,表示只显示某个字段,如果只要显示某几个字段可以用","分隔,比如:username,password.
     * 示例 :
     * : 显示所有字段
     * -username,-password : 除了不显示username,password这2个字段,其他字段都显示
     * username,password : 只显示username,password这2个字段,都不显示
     * )
     */
    @TableField( "resource_api_uri_show_fields" )
    private String       resourceApiUriShowFields;
    /**
     * 创建时间
     */
    @TableField( "create_time" )
    private Date         createTime;
    /**
     * 修改时间
     */
    @TableField( "update_time" )
    private Date         updateTime;
    /**
     * 备注
     */
    private String       remark;
    /**
     * 分类(C,R,U,D)冗余字段
     */
    @TableField( "category_code" )
    private String       categoryCode;

    @Override
    protected Serializable pkVal () {
        return this.id;
    }

}
