package com.aidijing.permission;

import com.aidijing.ContextUtils;
import com.aidijing.common.util.AssertUtils;
import com.aidijing.common.util.LogUtils;
import com.aidijing.domain.enums.ResourceType;
import com.aidijing.vo.PermissionResourceVO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * 权限拦截器
 * <p>
 * {@link Pass} 直接放行
 *
 * @author : 披荆斩棘
 * @date : 2017/6/14
 */
public class AdminPermissionInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private AntPathMatcher        antPathMatcher;
    @Autowired
    private List< Set< String > > requestMappingUris;


    @Override
    public boolean preHandle ( HttpServletRequest request, HttpServletResponse response, Object handler ) throws
                                                                                                          Exception {
        if ( ! ( handler instanceof HandlerMethod ) ) {
            return false;
        }
        HandlerMethod handlerMethod = ( HandlerMethod ) handler;

        final Pass pass = this.getHandlerAnnotation( handlerMethod, Pass.class );

        if ( LogUtils.getLogger().isDebugEnabled() ) {
            LogUtils.getLogger().debug( "handler '@Pass' annotation  : {} ", pass );
        }

        if ( Objects.nonNull( pass ) ) {
            if ( LogUtils.getLogger().isDebugEnabled() ) {
                LogUtils.getLogger().debug( "当前请求直接放行" );
            }
            return true;
        }

        final NotNeedPermission notNeedPermission = this.getHandlerAnnotation( handlerMethod, NotNeedPermission.class );

        if ( LogUtils.getLogger().isDebugEnabled() ) {
            LogUtils.getLogger().debug( "handler '@NotNeedPermission' annotation  : {} ", notNeedPermission );
        }


        if ( ContextUtils.isNotLogin() ) {
            throw new AuthenticationCredentialsNotFoundException( "未登录" );
        }

        if ( Objects.nonNull( notNeedPermission ) ) {
            return true;
        }

        final String                       method              = request.getMethod();
        final String                       uri                 = request.getRequestURI().trim();
        final List< PermissionResourceVO > permissionResources = ContextUtils.getPermissionResource();

        if ( LogUtils.getLogger().isDebugEnabled() ) {
            LogUtils.getLogger().debug( "当前用户权限资源 : {}", permissionResources );
        }

        AssertUtils.assertPermissionIsTrue(
                ! checkPermission( permissionResources, method, uri ),
                "对不起权限不足,您没有以 : '" + method + "' 方式,访问 '" + uri + "' 的权限"
        );
        return true;
    }


    private < T extends Annotation > T getHandlerAnnotation ( HandlerMethod handlerMethod, Class< T > clazz ) {
        T annotation = handlerMethod.getMethodAnnotation( clazz );
        if ( Objects.nonNull( annotation ) ) {
            return annotation;
        }
        return handlerMethod.getBean().getClass().getAnnotation( clazz );
    }


    /**
     * 权限校验,递归
     *
     * @param resources : 当前用户的所有权限列表
     * @param method    : 当前请求方法
     * @param uri       : 当前请求uri
     * @return
     */
    private boolean checkPermission ( List< PermissionResourceVO > resources, String method, String uri ) {
        if ( CollectionUtils.isEmpty( resources ) ) {
            return false;
        }
        for ( PermissionResourceVO resource : resources ) {
            if ( this.check( resource, method, uri ) ) {
                return true;
            }
            // 递归校验
            if ( this.checkPermission( resource.getChildren(), method, uri ) ) {
                return true;
            }
        }
        return false;
    }

    /**
     * 权限校验
     *
     * @param resource : 当前用户的权限资源
     * @param method   : 当前请求方法
     * @param uri      : 当前请求uri
     * @return 如果有权限返回 <code>true</code>,否则返回 <code>false</code>
     */
    private boolean check ( PermissionResourceVO resource, String method, String uri ) {
        if ( ! ResourceType.API.getCode().equals( resource.getResourceType().getCode() ) ) {
            return false;
        }
        if ( StringUtils.isEmpty( resource.getResourceApiUriMethods() ) ) {
            return false;
        }
        if ( ! resource.getResourceApiUriMethods().contains( method ) ) {
            return false;
        }
        // 这里解决了,配置了 /api/user/{id} 那么就算不配 /api/user/distributed-lock 一样通过的问题
        for ( Set< String > requestMapping : requestMappingUris ) {
            /**
             * 如果请求uri和requestMapping( {@link org.springframework.web.bind.annotation.RequestMapping} 中配置的uri )匹配 
             *
             */
            if ( requestMapping.contains( uri ) ) {
                // 那么就用这个请求的uri和数据库中的uri对比
                if ( ! uri.equals( resource.getResourceApiUri() ) ) {
                    return false;
                }
            }
        }
        // /api/user/{id} 这种形式匹配
        final boolean match = antPathMatcher.match( resource.getResourceApiUri(), uri );
        // 如果用户有该api的权限
        if ( match ) {
            ContextUtils.setCurrentRequestApiShowFields( resource.getResourceApiUriShowFields() );
        }
        return match;
    }


}

















