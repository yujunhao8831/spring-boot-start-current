package com.goblin.security;

import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Date;

/**
 * @author : 披荆斩棘
 * @date : 2017/7/3
 */
public interface BasicJwtUser extends UserDetails {

    /**
     * 主键
     *
     * @return id
     */
    Serializable getId ();

    /**
     * 密码最后重置(修改)日期
     *
     * @return Date
     */
    Date getLastPasswordResetDate ();
}
