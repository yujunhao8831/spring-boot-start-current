package com.goblin.manage.bean.dto;

import com.goblin.manage.bean.domain.enums.ResourceType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

/**
 * <p>
 * 权限资源表单
 * </p>
 *
 * @author 披荆斩棘
 * @since 2017-06-19
 */
@Getter
@Setter
@ToString
@Accessors( chain = true )
public class PermissionResourceForm implements Serializable {


	private static final long serialVersionUID = 1L;

	private Long          id;
	/**
	 * 父权限资源ID(0:表示root级)
	 */
	private Long          parentId;
	/**
	 * 排序字段
	 */
	private Integer       permissionSort;
	/**
	 * 权限名称
	 */
	@NotEmpty( message = "permissionName不能为空" )
	private String        permissionName;
	/**
	 * 资源样式class(前端class属性)
	 */
	private String        resourceClass;
	/**
	 * 资源样式style(前端style属性)
	 */
	private String        resourceStyle;
	/**
	 * 资源路由URL(前端使用)
	 */
	private String        resourceRouterUrl;
	/**
	 * 资源类型(API:接口,MENU:菜单,BUTTON:按钮)
	 */
	@NotNull( message = "resourceType不能为空" )
	private ResourceType  resourceType;
	/**
	 * 资源API URI(非必须,api才有)
	 */
	private String        resourceApiUri;
	/**
	 * 资源API URI方法methods
	 */
	private Set< String > resourceApiUriMethods;
	/**
	 * 资源API URI 显示字段列表
	 * 提供选择以','逗号分隔
	 */
	private String        resourceApiUriOptionsFields;
	/**
	 * 备注
	 */
	private String        remark;
	/**
	 * 分类(C,R,U,D)冗余字段
	 */
	private String        categoryCode;
	/**
	 * 资源API URI 可显示字段
	 */
	private String        resourceApiUriShowFields;

}
