package com.goblin.security;

import com.goblin.common.util.LogUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.ThreadContext;
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

/**
 * @author pijingzhanji
 */
@RequiredArgsConstructor
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

	///////////////////////////////////////////////////////////////////////////
	// 在log4j2.xml配置文件通过 %X{xxx} 获取
	///////////////////////////////////////////////////////////////////////////
	/** 用户ID */
	private static final String USER_ID   = "userId";
	/** 用户姓名 */
	private static final String USER_NAME = "userName";
	private final UserDetailsService userDetailsService;
	private final JwtTokenUtil       jwtTokenUtil;
	private final String             tokenHeader;

	@Override
	protected void doFilterInternal ( HttpServletRequest request , HttpServletResponse response ,
									  FilterChain chain ) throws ServletException, IOException {
		final String authToken = this.extractAuthTokenFromRequest( request , this.tokenHeader );
		String username = null;
		if ( StringUtils.isNotBlank( authToken ) ) {
			username  = jwtTokenUtil.getUsernameFromToken( authToken );
		}

		LogUtils.getLogger().debug( "authToken : {},username : {}" , authToken , username );

		if ( username != null && SecurityContextHolder.getContext().getAuthentication() == null ) {
			// 对于简单的验证，只需检查令牌的完整性即可。 您不必强制调用数据库。 由你自己决定
			// 是否查询数据看情况,目前是查询数据库
			UserDetails userDetails = this.userDetailsService.loadUserByUsername( username );
			if ( jwtTokenUtil.validateToken( authToken , userDetails ) ) {
				UsernamePasswordAuthenticationToken authentication =
					new UsernamePasswordAuthenticationToken( userDetails , null , userDetails.getAuthorities() );

				ThreadContext.put( USER_ID , String.valueOf( ( ( BasicJwtUser ) userDetails ).getId() ) );
				ThreadContext.put( USER_NAME , username );

				authentication.setDetails( new WebAuthenticationDetailsSource().buildDetails( request ) );

				LogUtils.getLogger().debug( "authToken : {},username : {}" , authToken , username );

				LogUtils.getLogger().debug( "该 " + username + "用户已认证, 设置安全上下文" );

				SecurityContextHolder.getContext().setAuthentication( authentication );
			}
		}
		chain.doFilter( request , response );
		ThreadContext.clearAll();
	}

	private String extractAuthTokenFromRequest ( HttpServletRequest httpRequest , String tokenHeader ) {
		/* Get token from header */
		String authToken = httpRequest.getHeader( tokenHeader );
		/* 如果请求头没有找到,那么从请求参数中获取 */
		if ( StringUtils.isEmpty( authToken ) ) {
			authToken = httpRequest.getParameter( tokenHeader );
		}
		return authToken;
	}
}
