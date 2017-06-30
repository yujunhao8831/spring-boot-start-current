package com.aidijing;

import com.aidijing.domain.User;
import com.aidijing.model.JwtUser;
import com.aidijing.vo.PermissionResourceVO;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

/**
 * 上下文
 *
 * @author : 披荆斩棘
 * @date : 2017/6/20
 */
public final class ContextUtils {

    private static final SimpleGrantedAuthority ROLE_ANONYMOUS                  = new SimpleGrantedAuthority(
            "ROLE_ANONYMOUS" );
    private static final String                 ANONYMOUS_USER_PRINCIPAL        = "anonymousUser";
    /** 当前请求API能查看到的字段 **/
    private static final ThreadLocal< String >  CURRENT_REQUEST_API_SHOW_FIELDS = new ThreadLocal<>();

    /**
     * 得到用户当前请求api可见字段
     *
     * @return 
     */
    public static String getCurrentRequestApiShowFields () {
        return CURRENT_REQUEST_API_SHOW_FIELDS.get();
    }

    /**
     * 设置用户当前请求api可见字段
     *
     * @param currentRequestApiShowFields {@link com.aidijing.domain.PermissionResource#resourceApiUriShowFields}
     */
    public static void setCurrentRequestApiShowFields ( final String currentRequestApiShowFields ) {
        CURRENT_REQUEST_API_SHOW_FIELDS.set( currentRequestApiShowFields );
    }

    /**
     * 是否登录
     * <p>
     * {@link Authentication#isAuthenticated()}
     *
     * @return <code>true</code> , 匿名用户(未登录)返回 <code>false</code>
     */
    public static boolean isLogin () {
        Authentication authentication = getAuthentication();
        if ( authentication.getAuthorities().contains( ROLE_ANONYMOUS )
                || ANONYMOUS_USER_PRINCIPAL.equals( authentication.getPrincipal() ) ) {
            return false;
        }
        return authentication.isAuthenticated();
    }

    /**
     * ! {@link #isLogin()}
     *
     * @return
     */
    public static boolean isNotLogin () {
        return ! isLogin();
    }

    /**
     * 得到jwt对象
     *
     * @return
     */
    public static JwtUser getJwtUser () {
        return ( JwtUser ) getAuthentication().getPrincipal();
    }


    /**
     * 得到 PermissionResourceVO
     *
     * @return
     */
    public static List< PermissionResourceVO > getPermissionResource () {
        return getJwtUser().getPermissionResource();
    }

    /**
     * 得到User对象
     *
     * @return
     */
    public static User getUser () {
        final JwtUser jwtUser = getJwtUser();
        User          user    = new User();
        BeanUtils.copyProperties( jwtUser, user );
        return user;
    }

    /**
     * 得到用户id
     *
     * @return {@link User#id}
     */
    public static Long getUserId () {
        return getJwtUser().getId();
    }

    /**
     * 得到用户详细信息
     *
     * @return {@link UserDetails}
     */
    public static UserDetails getUserDetails () {
        return ( UserDetails ) getAuthentication().getPrincipal();
    }


    /**
     * 得到凭证
     */
    private static Authentication getAuthentication () {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if ( null == authentication ) {
            throw new AuthenticationCredentialsNotFoundException( "未授权" );
        }
        return authentication;
    }


}
