package com.aidijing.mapper;

import com.aidijing.domain.User;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
  * 后台管理用户 Mapper 接口
 * </p>
 *
 * @author 披荆斩棘
 * @since 2017-06-19
 */
public interface UserMapper extends BaseMapper<User> {

    List<User> test ();
}