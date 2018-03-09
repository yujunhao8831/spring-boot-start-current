package com.goblin.security;

import com.goblin.common.ResponseEntityPro;
import com.goblin.common.util.JsonUtils;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;

/**
 * {@link AuthenticationEntryPoint} 拒绝所有请求与未经授权的错误消息。
 *
 * @author pijingzhanji
 */
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

	@Override
	public void commence ( HttpServletRequest request ,
						   HttpServletResponse response ,
						   AuthenticationException authException ) throws IOException {
		response.setHeader( "Content-type" , MediaType.APPLICATION_JSON_UTF8_VALUE );
		response.setCharacterEncoding( StandardCharsets.UTF_8.displayName() );
		try ( PrintWriter out = response.getWriter() ) {
			out.print( JsonUtils.toCustomizationJson( ResponseEntityPro.unauthorized( "未经授权:身份验证令牌丢失或无效。" ) ) );
		}
	}
}
