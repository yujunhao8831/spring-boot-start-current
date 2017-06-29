package com.aidijing.common.util;

import com.aidijing.common.regex.RegexType;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author : 余峻豪
 * @date : 16/6/16
 */
public abstract class RequestUtils {
    /** 手机正则模式 **/
    private static final Pattern PHONE_REGEX_PATTERN = Pattern.compile(
            RegexType.PHONE_TERMINAL_REGEX,
            Pattern.CASE_INSENSITIVE
    );
    /** 平板正则模式 **/
    private static final Pattern PAD_REGEX_PATTERN   = Pattern.compile(
            RegexType.PAD_TERMINAL_REGEX,
            Pattern.CASE_INSENSITIVE
    );

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
            return ip.split( "," )[0].trim();
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
     * 查询浏览器终端
     *
     * @param userAgent : 浏览器代理 <pre> request.getHeader("user-agent");</pre>
     * @return @see {@link TerminalEnum}
     */
    public static TerminalEnum queryTerminal ( String userAgent ) {
        if ( null == userAgent ) {
            return null;
        }
        userAgent = userAgent.toLowerCase();
        if ( userAgent.indexOf( "micromessenger" ) > 0 ) {
            return TerminalEnum.WX;
        }
        // 匹配
        Matcher matcherPhone = PHONE_REGEX_PATTERN.matcher( userAgent );
        Matcher matcherTable = PAD_REGEX_PATTERN.matcher( userAgent );
        if ( matcherPhone.find() || matcherTable.find() ) {
            return TerminalEnum.APP;
        } else {
            return TerminalEnum.PC;
        }
    }

    /**
     * 存放数据把获取过来的数据倒到params中
     *
     * @param request
     * @return
     */
    public static Map< String, String > getParameter ( HttpServletRequest request ) {
        Map< String, String > params = new HashMap<>();
        for ( Map.Entry< String, String[] > param : request.getParameterMap().entrySet() ) {
            String paramKey      = param.getKey(); // 参数key
            String paramValues[] = param.getValue(); // 参数value(数组)
            String paramValue    = "";
            for ( int i = 0 ; i < paramValues.length ; i++ ) {
                paramValue = ( i == paramValues.length - 1 )
                             ? paramValue + paramValues[i]
                             : paramValue + paramValues[i] + ",";
            }
            // 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            // paramKey = new String(paramValue.getBytes("ISO-8859-1"), "gbk");
            params.put( paramKey, paramValue );
        }
        return params;
    }

    /**
     * 得到域名,比如 : www.google.com
     *
     * @param request
     * @return www.xxx.com
     */
    public static String getRequestDomain ( HttpServletRequest request ) {
        StringBuffer url = request.getRequestURL();
        return url.delete( url.length() - request.getRequestURI().length(), url.length() )
                  .append( "/" )
                  .toString();
    }

    /**
     * 是否是 Content-Type=application/json; json传输
     *
     * @param request
     * @return
     */
    public static boolean isApplicationJsonHeader ( HttpServletRequest request ) {
        String contentType = request.getHeader( HttpHeaders.CONTENT_TYPE );
        return contentType != null && contentType.trim()
                                                 .replaceAll( StringUtils.SPACE, StringUtils.EMPTY )
                                                 .contains( MediaType.APPLICATION_JSON_VALUE );
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
    public static String getRequestMessage ( HttpServletRequest request, Object userId, String username ) throws
                                                                                                          IOException {
        StringBuffer parameters = new StringBuffer();
        parameters.append( "\n用户ID : " )
                  .append( userId )
                  .append( "\n用户姓名 : " )
                  .append( username );
        return getRequestMessage( request, parameters );
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
        StringBuffer parameters = new StringBuffer();
        return getRequestMessage( request, parameters );
    }

    /**
     * 得到请求参数 username=[披荆斩棘]
     *
     * @param request
     * @return
     * @throws IOException
     */
    public static String getRequestParameters ( HttpServletRequest request ) throws IOException {
        final Map< String, String[] > parameterMap = request.getParameterMap();
        StringBuffer                  parameters   = new StringBuffer();
        if ( RequestUtils.isApplicationJsonHeader( request ) ) {
            parameters.append( IOUtils.toString( request.getInputStream(), "UTF-8" ) );
        } else {
            for ( Map.Entry< String, String[] > parameter : parameterMap.entrySet() ) {
                String[] values = parameter.getValue();
                parameters.append( parameter.getKey() + "=" + Arrays.toString( values ) + "\t" );
            }
        }
        return parameters.toString();
    }

    private static String getRequestMessage ( HttpServletRequest request, StringBuffer parameters ) throws IOException {
        parameters.append( "\n请求URL : " + request.getRequestURI() )
                  .append( "\n请求URI : " + request.getRequestURL() )
                  .append( "\n请求方式 : " + request.getMethod() + ( RequestUtils.isAjaxRequest( request ) == true
                                                                 ? "\tajax请求"
                                                                 : "\t同步请求" ) )
                  .append( "\n请求者IP : " + request.getRemoteAddr() )
                  .append( "\n请求时间 : " + Instant.now() );
        final Enumeration< String > headerNames = request.getHeaderNames(); // 请求头
        while ( headerNames.hasMoreElements() ) {
            String element = headerNames.nextElement();
            if ( null != element ) {
                String header = request.getHeader( element );
                parameters.append( "\n请求头内容 : " + element + "=" + header );
            }
        }
        parameters.append( "\n请求参数 : " + getRequestParameters( request ) );
        final Enumeration< String > sessionAttributeNames = request.getSession().getAttributeNames(); // 请求Session内容
        while ( sessionAttributeNames.hasMoreElements() ) {
            parameters.append( "\nSession内容 : " + sessionAttributeNames.nextElement() );
        }
        return parameters.toString();
    }

    /**
     * 终端
     */
    public enum TerminalEnum {
        WX, PC, APP
    }

}