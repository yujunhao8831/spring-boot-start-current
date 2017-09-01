package com.aidijing.manage.permission;


import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.*;

/**
 * 需要认证是当前用户信息
 * <p>
 * {@link AdminPermissionInterceptor#ultraViresHandle(HandlerMethod , HttpServletRequest)}
 * 权重问题,如果全部配置了,校验顺序由权重觉得.权重高低(数越小权重越大)
 *
 * @author : 披荆斩棘
 * @date : 2017/7/8
 */
@Target( { ElementType.METHOD } )
@Retention( RetentionPolicy.RUNTIME )
@Documented
public @interface NeedAuthenticationCurrentUser {


    /**
     * 验证是当前用户是这个用户的管理员用户,如果不是则属于越权操作
     * <p>
     * <ul>
     * <li><b style="color:red">权重 : 1</b></li>
     * <li><b style="color:red">{@link #pathVariableUserIdName()} 必须,并且需要匹配</b></li>
     * </ul>
     * <p>
     * <p>
     * <pre>
     *      <b>示例 : </b>
     *      // /users/{id},验证是当前用户是这个用户的管理员用户
     *      <code>@GetMapping( "/users/{id}" )</code>
     *      <code>@NeedAuthenticationCurrentUser(pathVariableUserIdName = "id",authenticationIsSuperAdminUser = true)</code>
     *      public ResponseEntity< User > get ( @PathVariable Long id ) {
     *          return ResponseEntity.ok().setResponseContent( userService.get( id ) );
     *      }
     *
     *      // /users/{id},验证是当前用户是这个用户的管理员用户
     *      <code>@GetMapping( "/roles/{userId}" )</code>
     *      <code>@NeedAuthenticationCurrentUser(pathVariableUserIdName = "userId",authenticationIsSuperAdminUser = true)</code>
     *      public ResponseEntity< User > get ( @PathVariable Long userId ) {
     *          return ResponseEntity.ok().setResponseContent( roleService.listCurrentUserGroupByUserId( userId ) );
     *      }
     * </pre>
     */
    boolean authenticationIsTargetUserAdminUser () default false;

    /**
     * 验证是当前用户,如果不是则属于越权操作
     * <p>
     * <ul>
     * <li><b style="color:red">权重 : 2</b></li>
     * <li><b style="color:red">{@link #pathVariableUserIdName()} 必须,并且需要匹配</b></li>
     * </ul>
     * <p>
     * <p>
     * <pre>
     *      <b>示例 : </b>
     *      // /users/{id},验证是不是当前用户
     *      <code>@GetMapping( "/users/{id}" )</code>
     *      <code>@NeedAuthenticationCurrentUser(pathVariableUserIdName = "id",authenticationIsCurrentUser = true)</code>
     *      public ResponseEntity< User > get ( @PathVariable Long id ) {
     *          return ResponseEntity.ok().setResponseContent( userService.get( id ) );
     *      }
     *
     *      // /users/{id},验证是不是当前用户
     *      <code>@GetMapping( "/roles/{userId}" )</code>
     *      <code>@NeedAuthenticationCurrentUser(pathVariableUserIdName = "userId",authenticationIsCurrentUser = true)</code>
     *      public ResponseEntity< User > get ( @PathVariable Long userId ) {
     *          return ResponseEntity.ok().setResponseContent( roleService.listCurrentUserGroupByUserId( userId ) );
     *      }
     * </pre>
     */
    boolean authenticationIsCurrentUser () default true;

    /**
     * 需要验证用户ID的路径变量名称
     */
    String pathVariableUserIdName () default "id";


}
