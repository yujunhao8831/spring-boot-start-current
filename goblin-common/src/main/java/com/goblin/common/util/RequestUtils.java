package com.goblin.common.util;

import eu.bitwalker.useragentutils.UserAgent;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Arrays;
import java.util.Enumeration;

/**
 * @author : 披荆斩棘
 * @date : 16/6/16
 */
public abstract class RequestUtils {

	/**
	 * 请求头(host)内容 : localhost:8080
	 * 请求头(connection)内容 : keep-alive
	 * 请求头(cache-control)内容 : max-age=0
	 * 请求头(upgrade-insecure-requests)内容 : 1
	 * 请求头(user-agent)内容 : Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36
	 * 请求头(accept)内容 : text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*\/*;q=0.8
	 * 请求头(referer)内容 : http://localhost:8082/access-logs/recharge
	 * 请求头(accept-encoding)内容 : gzip, deflate, br
	 * 请求头(accept-language)内容 : zh-CN,zh;q=0.8,en;q=0.6
	 * 请求头(cookie)内容 : Hm_lvt_14b72f1e13e0586c5f38e5d1d16c549e=1480300179; Hm_lvt_3838faa5c4b1fd15b49f3dd6c90109b7=1480041314,1480057365,1480300285; Hm_lvt_263206af6573c0a362453bee56cd61b6=1480300319; Hm_lvt_e7019b8edb5edab3e54c5e229af23014=1480299450,1480557169,1480667339; Hm_lvt_69c2798f0b79a692b9d74581cf4bdef1=1480299476,1480650441,1481698113; b3log-latke="{\"userPassword\":\"f1c122ca4cf986be49d8e0922d9039b8\",\"userEmail\":\"yujunhao_8831@yahoo.com\"}"; Hm_lvt_9879c0fbf0414b572df462ca41b25798=1491013401; Webstorm-e3ef6d55=e16b3634-28fb-4ed9-9683-aa8c6f9d5bb9; tcd=a14fe7b56fe741d38dc3872a0ca159a9; 42a=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJwaG9uZSI6IiIsIndhc0xvZ2luIjpmYWxzZSwibG9nbyI6Imh0dHA6Ly9yZXNvdXJjZS4xMzMyMi5jb20vdXNlci91c2VyLnBuZyIsImlkIjoyMzI5LCJ1c2VyTmFtZSI6ImExNGZlN2I1NmZlNzQxZDM4ZGMzODcyYTBjYTE1OWE5IiwidHlwZSI6IlVzZXIifQ.m6m28ayhoXX8pjenjxLZblZzxtS6HMqfjTGe_cDCTNQ; UM_distinctid=15bddd43267107a-0ca374e9b56a97-153d655c-1aeaa0-15bddd43268eac; CNZZDATA1260716016=355481192-1494074298-null%7C1494117924; Hm_lvt_fe2c7555911b8560db5e56121c5d5960=1496670538; Hm_lvt_57e94d016e201fba3603a8a2b0263af0=1496719466; CNZZDATA5879641=cnzz_eid%3D400217219-1498785661-http%253A%252F%252Flocalhost%253A8080%252F%26ntime%3D1499299828; io=0vRDLPfIHaDlyA2rAAAA; _ga=GA1.1.205058624.1492656997; _gid=GA1.1.1246690044.1500369259
	 */
	public static final String REQUEST_HEADER_HOST                      = "host";
	public static final String REQUEST_HEADER_CONNECTION                = "connection";
	public static final String REQUEST_HEADER_CACHE_CONTROL             = "cache-control";
	public static final String REQUEST_HEADER_UPGRADE_INSECURE_REQUESTS = "upgrade-insecure-requests";
	public static final String REQUEST_HEADER_USER_AGENT                = "user-agent";
	public static final String REQUEST_HEADER_ACCEPT                    = "accept";
	public static final String REQUEST_HEADER_REFERER                   = "referer";
	public static final String REQUEST_HEADER_ACCEPT_ENCODING           = "accept-encoding";
	public static final String REQUEST_HEADER_ACCEPT_LANGUAGE           = "accept-language";
	public static final String REQUEST_HEADER_ACCEPT_COOKIE             = "cookie";


	/**
	 * 得到当前{@code HttpServletRequest}
	 * <p>
	 * <b style="color:red">
	 * 注意 : 只能在controller层中使用,service层 和 dao层 会报错
	 * </b>
	 *
	 * @return 当前HttpServletRequest
	 */
	public static HttpServletRequest getRequest () {
		return ( ( ServletRequestAttributes ) RequestContextHolder.getRequestAttributes() ).getRequest();
	}

	/**
	 * 判断请求是否为Ajax请求.
	 *
	 * @param request
	 * @return
	 */
	public static boolean isAjaxRequest ( HttpServletRequest request ) {
		return "XMLHttpRequest".equals( request.getHeader( "X-Requested-With" ) );
	}

	/**
	 * 得到当前请求的ip地址
	 * <p>
	 * <b style="color:red">
	 * 注意 : 只能在controller层中使用,service层 和 dao层 会报错
	 * </b>
	 *
	 * @return
	 */
	public static String getRequestIp () {
		HttpServletRequest request = getRequest();
		String             ip      = request.getHeader( "X-Real-IP" );
		if ( StringUtils.isNotBlank( ip ) && ! "unknown".equalsIgnoreCase( ip ) ) {
			return ip.trim();
		}
		ip = request.getHeader( "X-Forwarded-For" );
		if ( StringUtils.isNotBlank( ip ) && ! "unknown".equalsIgnoreCase( ip ) ) {
			// 多次反向代理后会有多个IP值，第一个为 真实 ip
			return StringUtils.split( ip , "," )[0].trim();
		}
		ip = request.getHeader( "Proxy-Client-IP" );
		if ( StringUtils.isNotBlank( ip ) && ! "unknown".equalsIgnoreCase( ip ) ) {
			return ip.trim();
		}
		ip = request.getHeader( "WL-Proxy-Client-IP" );
		if ( StringUtils.isNotBlank( ip ) && ! "unknown".equalsIgnoreCase( ip ) ) {
			return ip.trim();
		}
		ip = request.getHeader( "HTTP_CLIENT_IP" );
		if ( StringUtils.isNotBlank( ip ) && ! "unknown".equalsIgnoreCase( ip ) ) {
			return ip.trim();
		}
		ip = request.getHeader( "X-Cluster-Client-IP" );
		if ( StringUtils.isNotBlank( ip ) && ! "unknown".equalsIgnoreCase( ip ) ) {
			return ip.trim();
		}
		return request.getRemoteAddr();
	}

	/**
	 * 是否是 Content-Type=application/json; json传输
	 *
	 * @param request
	 * @return
	 */
	public static boolean isApplicationJsonHeader ( HttpServletRequest request ) {
		String contentType = request.getHeader( HttpHeaders.CONTENT_TYPE );
		return contentType != null && StringUtils.replaceAll(
			contentType.trim() ,
			StringUtils.SPACE ,
			StringUtils.EMPTY
		).contains( MediaType.APPLICATION_JSON_VALUE );
	}

	/**
	 * {@link HttpServletRequest#getHeader(String)}
	 *
	 * @param headerName
	 * @return
	 */
	public static String getRequestHeader ( String headerName ) {
		return getRequest().getHeader( headerName );
	}

	/**
	 * 得到 {@link #REQUEST_HEADER_USER_AGENT} 信息
	 *
	 * @return 描述信息
	 */
	public static String getUserAgentHeader () {
		return getRequestHeader( REQUEST_HEADER_USER_AGENT );
	}

	/**
	 * @return {@link eu.bitwalker.useragentutils.UserAgent}
	 */
	public static UserAgent getUserAgent () {
		return UserAgent.parseUserAgentString( getUserAgentHeader() );
	}

	/**
	 * 得到请求地址
	 *
	 * @return 如果是在http://www.google.com域名下请求当前服务器某个api,比如:http://www.goblin.com/api,<p>
	 * 那么获取到的就是http://www.google.com这个地址,而不是http://www.goblin.com/api
	 */
	public static String getRequestReferrerUrl () {
		return getRequestHeader( REQUEST_HEADER_REFERER );
	}


	/**
	 * 得到请求信息
	 * <p>
	 * <pre>
	 *  用户ID : userId
	 *  用户姓名 : 用户名
	 *  请求URL : /
	 *  请求URI : http://localhost:8080/
	 *  请求方式 : GET	同步请求
	 *  请求者IP : 0:0:0:0:0:0:0:1
	 *  请求时间 : 2017-06-04T12:07:05.575Z
	 *  请求头内容 : host=localhost:8080
	 *  请求头内容 : connection=keep-alive
	 *  请求头内容 : upgrade-insecure-requests=1
	 *  请求头内容 : user-agent=Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36
	 *  请求头内容 : accept=text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*\/*;q=0.8
	 *  请求头内容 : accept-encoding=gzip, deflate, sdch, br
	 *  请求头内容 : accept-language=zh-CN,zh;q=0.8,en;q=0.6
	 *  请求头内容 : cookie=Hm_lvt_14b72f1e13e0586c5f38e5d1d16c549e=1480300179; Hm_lvt_3838faa5c4b1fd15b49f3dd6c90109b7=1480041314,1480057365,1480300285; Hm_lvt_263206af6573c0a362453bee56cd61b6=1480300319; Hm_lvt_e7019b8edb5edab3e54c5e229af23014=1480299450,1480557169,1480667339; Hm_lvt_69c2798f0b79a692b9d74581cf4bdef1=1480299476,1480650441,1481698113; b3log-latke="{\"userPassword\":\"f1c122ca4cf986be49d8e0922d9039b8\",\"userEmail\":\"yujunhao_8831@yahoo.com\"}"; Hm_lvt_9879c0fbf0414b572df462ca41b25798=1491013401; Webstorm-e3ef6d55=e16b3634-28fb-4ed9-9683-aa8c6f9d5bb9; tcd=a14fe7b56fe741d38dc3872a0ca159a9; 42a=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJwaG9uZSI6IiIsIndhc0xvZ2luIjpmYWxzZSwibG9nbyI6Imh0dHA6Ly9yZXNvdXJjZS4xMzMyMi5jb20vdXNlci91c2VyLnBuZyIsImlkIjoyMzI5LCJ1c2VyTmFtZSI6ImExNGZlN2I1NmZlNzQxZDM4ZGMzODcyYTBjYTE1OWE5IiwidHlwZSI6IlVzZXIifQ.m6m28ayhoXX8pjenjxLZblZzxtS6HMqfjTGe_cDCTNQ; UM_distinctid=15bddd43267107a-0ca374e9b56a97-153d655c-1aeaa0-15bddd43268eac; CNZZDATA1260716016=355481192-1494074298-null%7C1494117924; _ga=GA1.1.205058624.1492656997
	 *  请求参数 : 如果有则会打印
	 * </pre>
	 *
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public static String getRequestMessage ( HttpServletRequest request , Object userId , String username ) throws
																											IOException {
		StringBuilder parameters = new StringBuilder();
		parameters.append( "\n用户ID : " )
				  .append( userId )
				  .append( "\n用户姓名 : " )
				  .append( username );
		return getRequestMessage( request , parameters );
	}

	/**
	 * 得到请求信息
	 * <p>
	 * <pre>
	 *  请求URL : /
	 *  请求URI : http://localhost:8080/
	 *  请求方式 : GET	同步请求
	 *  请求者IP : 0:0:0:0:0:0:0:1
	 *  请求时间 : 2017-06-04T12:07:05.575Z
	 *  请求头内容 : host=localhost:8080
	 *  请求头内容 : connection=keep-alive
	 *  请求头内容 : upgrade-insecure-requests=1
	 *  请求头内容 : user-agent=Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36
	 *  请求头内容 : accept=text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*\/*;q=0.8
	 *  请求头内容 : accept-encoding=gzip, deflate, sdch, br
	 *  请求头内容 : accept-language=zh-CN,zh;q=0.8,en;q=0.6
	 *  请求头内容 : cookie=Hm_lvt_14b72f1e13e0586c5f38e5d1d16c549e=1480300179; Hm_lvt_3838faa5c4b1fd15b49f3dd6c90109b7=1480041314,1480057365,1480300285; Hm_lvt_263206af6573c0a362453bee56cd61b6=1480300319; Hm_lvt_e7019b8edb5edab3e54c5e229af23014=1480299450,1480557169,1480667339; Hm_lvt_69c2798f0b79a692b9d74581cf4bdef1=1480299476,1480650441,1481698113; b3log-latke="{\"userPassword\":\"f1c122ca4cf986be49d8e0922d9039b8\",\"userEmail\":\"yujunhao_8831@yahoo.com\"}"; Hm_lvt_9879c0fbf0414b572df462ca41b25798=1491013401; Webstorm-e3ef6d55=e16b3634-28fb-4ed9-9683-aa8c6f9d5bb9; tcd=a14fe7b56fe741d38dc3872a0ca159a9; 42a=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJwaG9uZSI6IiIsIndhc0xvZ2luIjpmYWxzZSwibG9nbyI6Imh0dHA6Ly9yZXNvdXJjZS4xMzMyMi5jb20vdXNlci91c2VyLnBuZyIsImlkIjoyMzI5LCJ1c2VyTmFtZSI6ImExNGZlN2I1NmZlNzQxZDM4ZGMzODcyYTBjYTE1OWE5IiwidHlwZSI6IlVzZXIifQ.m6m28ayhoXX8pjenjxLZblZzxtS6HMqfjTGe_cDCTNQ; UM_distinctid=15bddd43267107a-0ca374e9b56a97-153d655c-1aeaa0-15bddd43268eac; CNZZDATA1260716016=355481192-1494074298-null%7C1494117924; _ga=GA1.1.205058624.1492656997
	 *  请求参数 : username=[披荆斩棘]
	 * </pre>
	 *
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public static String getRequestMessage ( HttpServletRequest request ) throws IOException {
		StringBuilder parameters = new StringBuilder();
		return getRequestMessage( request , parameters );
	}

	/**
	 * 得到请求参数 username=[披荆斩棘]
	 *
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public static String getRequestParameters ( HttpServletRequest request ) throws IOException {
		StringBuilder parameters = new StringBuilder();
		if ( RequestUtils.isApplicationJsonHeader( request ) ) {
			parameters.append( IOUtils.toString( request.getInputStream() , StandardCharsets.UTF_8.displayName() ) );
		} else {
			request.getParameterMap().forEach(
				( String key , String[] values ) -> parameters.append( key )
															  .append( "=" )
															  .append( Arrays.toString( values ) )
															  .append( "\t" )
			);
		}
		return parameters.toString();
	}

	private static String getRequestMessage ( HttpServletRequest request , StringBuilder parameters ) throws
																									  IOException {
		parameters.append( "\n请求URL : " )
				  .append( request.getRequestURL())
				  .append( "\n请求URI : " )
				  .append( request.getRequestURI())
				  .append( "\n请求方式 : " )
				  .append( request.getMethod() )
				  .append( RequestUtils.isAjaxRequest( request ) ? "\tajax请求" : "\t同步请求" )
				  .append( "\n请求者IP : " )
				  .append( request.getRemoteAddr() )
				  .append( "\n请求时间 : " )
				  .append( Instant.now() );
		// 请求头
		final Enumeration< String > headerNames = request.getHeaderNames();
		while ( headerNames.hasMoreElements() ) {
			final String element = headerNames.nextElement();
			if ( null != element ) {
				String header = request.getHeader( element );
				parameters.append( "\n请求头内容 : " ).append( element ).append( "=" ).append( header );
			}
		}
		parameters.append( "\n请求参数 : " ).append( getRequestParameters( request ) );
		// 请求Session内容
		final Enumeration< String > sessionAttributeNames = request.getSession().getAttributeNames();
		while ( sessionAttributeNames.hasMoreElements() ) {
			parameters.append( "\nSession内容 : " ).append( sessionAttributeNames.nextElement() );
		}
		return parameters.toString();
	}


}
