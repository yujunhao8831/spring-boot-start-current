package com.aidijing.security;

import com.aidijing.common.util.LogUtils;
import com.aidijing.model.JwtUser;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    ///////////////////////////////////////////////////////////////////////////
    // 在log4j2.xml配置文件通过 %X{xxx} 获取 
    ///////////////////////////////////////////////////////////////////////////
    /** 用户ID */
    private static final String USER_ID   = "userId";
    /** 用户姓名 */
    private static final String USER_NAME = "userName";
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtTokenUtil       jwtTokenUtil;
    @Value( "${jwt.header}" )
    private String             tokenHeader;


    @Override
    protected void doFilterInternal ( HttpServletRequest request,
                                      HttpServletResponse response,
                                      FilterChain chain ) throws ServletException, IOException {

        final String authToken = request.getHeader( this.tokenHeader );
        final String username  = jwtTokenUtil.getUsernameFromToken( authToken );

        if ( LogUtils.getLogger().isDebugEnabled() ) {
            LogUtils.getLogger().debug( "authToken : {},username : {}", authToken, username );
        }
        
        if ( username != null && SecurityContextHolder.getContext().getAuthentication() == null ) {
            // 对于简单的验证，只需检查令牌的完整性即可。 您不必强制调用数据库。 由你自己决定
            // 是否查询数据看情况,目前是查询数据库
            // 因为后台项目,用户信息较多,特别是权限信息,每次查询的话,需要递归组织上下关系,比较麻烦
            // 所以这里是每次从缓存中获取,放到 spring security 的 context 中,拦截器中就可以直接获取了
            // 注意 : 这里并没有使用到session
            UserDetails userDetails = this.userDetailsService.loadUserByUsername( username );
            if ( jwtTokenUtil.validateToken( authToken, userDetails ) ) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                ThreadContext.put( USER_ID, String.valueOf( ( ( JwtUser ) userDetails ).getId() ) );
                ThreadContext.put( USER_NAME, username );

                authentication.setDetails( new WebAuthenticationDetailsSource().buildDetails( request ) );

                if ( LogUtils.getLogger().isDebugEnabled() ) {
                    LogUtils.getLogger().debug( "authToken : {},username : {}", authToken, username );
                }
                if ( LogUtils.getLogger().isDebugEnabled() ) {
                    LogUtils.getLogger().debug( "该 " + username + "用户已认证, 设置安全上下文" );
                }
                SecurityContextHolder.getContext().setAuthentication( authentication );
            }
        }
        chain.doFilter( request, response );
        return;
    }
}