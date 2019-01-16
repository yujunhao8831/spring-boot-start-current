package com.goblin.manage.permission;

import com.goblin.common.util.AssertUtils;
import com.goblin.common.util.LogUtils;
import com.goblin.manage.ContextUtils;
import com.goblin.manage.bean.domain.RolePermissionResource;
import com.goblin.manage.bean.domain.enums.ResourceType;
import com.goblin.manage.bean.vo.PermissionResourceVO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 权限拦截器
 * <p>
 * {@link Pass} 直接放行 {@link NotNeedPermission} 不需要权限
 *
 * @author : 披荆斩棘
 * @date : 2017/6/14
 */
public class AdminPermissionInterceptor extends HandlerInterceptorAdapter {

    private final RolePermissionResource EMPTY_ROLE_PERMISSION_RESOURCE = new RolePermissionResource();
    @Autowired
    private       AntPathMatcher         antPathMatcher;
    @Autowired
    private       List< Set< String > >  requestMappingUris;


    @Override
    public boolean preHandle ( HttpServletRequest request , HttpServletResponse response ,
                               Object handler ) {
        if ( ! ( handler instanceof HandlerMethod ) ) {
            LogUtils.getLogger().debug( "! ( handler instanceof HandlerMethod ) pass" );
            LogUtils.getLogger().debug( "handler : {}" , handler );
            return true;
        }
        HandlerMethod handlerMethod = ( HandlerMethod ) handler;


        // 不需要登录,也不需要权限
        final Pass pass = this.getHandlerAnnotation( handlerMethod , Pass.class );

        LogUtils.getLogger().debug( "handler '@Pass' annotation  : {} " , pass );


        if ( Objects.nonNull( pass ) ) {
            LogUtils.getLogger().debug( "当前请求直接放行" );

            return true;
        }

        // 如果未登录
        ContextUtils.assertNotLogin();

        final NotNeedPermission notNeedPermission =
                this.getHandlerAnnotation( handlerMethod , NotNeedPermission.class );

        LogUtils.getLogger().debug(
                "handler '@NotNeedPermission' annotation  : {} " ,
                notNeedPermission
        );

        // 不需要权限
        if ( Objects.nonNull( notNeedPermission ) ) {
            return true;
        }

        // 权限认证处理
        final String                       method              = request.getMethod();
        final String                       uri                 = request.getRequestURI().trim();
        final List< PermissionResourceVO > permissionResources = ContextUtils.getPermissionResource();

        LogUtils.getLogger().debug( "当前用户权限资源 : {}" , permissionResources );


        AssertUtils.assertPermissionIsTrue(
                ! checkPermission( permissionResources , method , uri ) ,
                "对不起权限不足,您没有以 : '" + method + "' 方式,访问 '" + uri + "' 的权限"
        );

        // 越权处理
        ultraViresHandle( handlerMethod , request );

        return true;
    }

    /**
     * 用户越权处理,使用注解还是在添加资源或者授权资源的时候指定持久化到数据库?
     * <p>
     * 未完善
     */
    private void ultraViresHandle ( HandlerMethod handlerMethod , HttpServletRequest request ) {
        userUltraViresHandle( handlerMethod , request );
    }


    private void userUltraViresHandle ( HandlerMethod handlerMethod , HttpServletRequest request ) {

        /**
         * 大概流程
         * {@link org.springframework.web.servlet.DispatcherServlet#doDispatch(HttpServletRequest , HttpServletResponse)}
         * {@link org.springframework.web.servlet.DispatcherServlet#getHandler(HttpServletRequest)}
         * {@link org.springframework.web.servlet.handler.AbstractHandlerMapping#getHandler(HttpServletRequest)}
         * {@link org.springframework.web.servlet.handler.AbstractHandlerMethodMapping#getHandlerInternal(HttpServletRequest)}
         * ... ...
         * 在下面方法中Spring会把解析后的Path URI存放到域中
         * {@link org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping#handleMatch(Object , String , HttpServletRequest)}
         *
         */
        // /roles/{userId}/{roleId}
        final String uriVariables =
                ( String ) request.getAttribute( HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE );
        // {userId=1, roleId=10000}
        final Map< String, String > decodedUriVariables = ( Map< String, String > ) request.getAttribute( HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE );

    }


    private < T extends Annotation > T getHandlerAnnotation ( HandlerMethod handlerMethod ,
                                                              Class< T > clazz ) {
        T annotation = handlerMethod.getMethodAnnotation( clazz );
        if ( Objects.nonNull( annotation ) ) {
            return annotation;
        }
        return handlerMethod.getBeanType().getAnnotation( clazz );
    }

    /**
     * 权限校验,递归
     *
     * @param resources : 当前用户的所有权限列表
     * @param method    : 当前请求方法
     * @param uri       : 当前请求uri
     * @return
     */
    private boolean checkPermission ( List< PermissionResourceVO > resources , String method ,
                                      String uri ) {
        if ( CollectionUtils.isEmpty( resources ) ) {
            return false;
        }
        for ( PermissionResourceVO resource : resources ) {
            if ( this.check( resource , method , uri ) ) {
                return true;
            }
            // 递归校验
            if ( this.checkPermission( resource.getChildren() , method , uri ) ) {
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
    private boolean check ( PermissionResourceVO resource , String method , String uri ) {

        if ( ! ResourceType.API.getValue().equals( resource.getResourceType().getValue() ) ) {
            return false;
        }
        if ( StringUtils.isEmpty( resource.getResourceApiUriMethods() ) ) {
            return false;
        }
        if ( ! resource.getResourceApiUriMethods().contains( method ) ) {
            return false;
        }
        // 这里解决了,配置了 /api/userUltraViresHandle/{id} 那么就算不配
        // /api/userUltraViresHandle/distributed-lock 一样通过的问题
        for ( Set< String > requestMapping : requestMappingUris ) {
            /**
             * 如果请求uri和requestMapping(
             * {@link org.springframework.web.bind.annotation.RequestMapping} 中配置的uri )匹配
             */
            if ( requestMapping.contains( uri ) ) {
                // 那么就用这个请求的uri和数据库中的uri对比
                if ( ! uri.equals( resource.getResourceApiUri() ) ) {
                    return false;
                }
            }
        }
        // /api/userUltraViresHandle/{id} 这种形式匹配
        final boolean match = antPathMatcher.match( resource.getResourceApiUri() , uri );

        // 如果用户有该api的权限
        if ( match ) {
            final RolePermissionResource rolePermissionResource =
                    ContextUtils.getJwtUser().getRolePermissionResources().parallelStream()
                                .filter( element -> Objects.equals(
                                        element.getPermissionResourceId() ,
                                        resource.getId()
                                ) )
                                .findFirst().orElse( EMPTY_ROLE_PERMISSION_RESOURCE );
            ContextUtils.setCurrentRequestRolePermissionResource( rolePermissionResource );

        }
        return match;
    }


}


