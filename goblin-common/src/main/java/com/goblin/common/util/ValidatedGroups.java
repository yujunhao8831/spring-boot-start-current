package com.goblin.common.util;

/**
 * Hibernate Validated 分组验证,针对一个实体多种情况下的验证.
 * <p>
 * {@link javax.validation.groups.Default} 不指定组的情况下这个是默认的
 * <p>
 * <pre>
 *     public class User {
 *         // 该字段校验只做用于 ValidatedGroups.Update.class
 *         {@code @NotBlank}( message = "id can not be null", groups = ValidatedGroups.Update.class )
 *         private Long id;
 *
 *         // 该字段校验只做用于 ValidatedGroups.Save.class
 *         {@code @NotBlank}( message = "username can not be null", groups = ValidatedGroups.Save.class )
 *         private String username;
 *
 *         // 该字段校验只做用于 ValidatedGroups.Save.class 和 ValidatedGroups.Special.class
 *         {@code @NotBlank}( message = "password can not be null", groups = { ValidatedGroups.Save.class , ValidatedGroups.Special.class } )
 *         private String password;
 *
 *         // 该字段校验只做用于 Default.class,默认情况下就是 Default.class
 *         {@code @NotBlank}( message = "address can not be null", groups = Default.class)
 *         private String address;
 *         // 不指定 groups , 默认情况下就是 Default.class
 *         {@code @NotBlank}( message = "age can not be null" )
 *         private String age;
 *     }
 * </pre>
 * <p>
 * 控制器中使用
 * <p>
 * <pre>
 *     // 校验指定组校验
 *     {@code @RequestMapping}( method = RequestMethod.POST )
 *     public ResponseEntity save ( @Validated( ValidatedGroups.Save.class ) @RequestBody User user ,BindingResult result ) {
 *         // ... ...
 *         return ResponseEntity.ok().body( "" );
 *     }
 *
 *
 * </pre>
 *
 * @author : 披荆斩棘
 * @date : 2017/12/11
 */
public interface ValidatedGroups {


	/**
	 * 更新
	 */
	interface Update {
	}

	/**
	 * 保存
	 */
	interface Save {
	}

	/**
	 * 保存
	 */
	interface Delete {
	}

	/**
	 * 查询
	 */
	interface Query {
	}

	/**
	 * 特殊的
	 */
	interface Special {
	}


}
