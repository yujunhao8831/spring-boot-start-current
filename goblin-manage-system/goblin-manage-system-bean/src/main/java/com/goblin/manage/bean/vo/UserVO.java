package com.goblin.manage.bean.vo;

import com.goblin.manage.bean.domain.Role;
import com.goblin.manage.bean.domain.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 后台管理用户
 * </p>
 *
 * @author 披荆斩棘
 * @since 2017-06-19
 */
@EqualsAndHashCode( callSuper = true )
@Data
@Accessors( chain = true )
public class UserVO extends User {

	private Role role;


}
