package com.goblin.common.filter;


import com.goblin.common.util.LogUtils;
import com.goblin.common.util.RequestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.PatternMatchUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 请求信息打印过滤器
 *
 * @author : 披荆斩棘
 * @date : 2017/1/17
 */
public class RequestLoggingFilter implements Filter {

    private static final String   PASSWORD_FILTER_REGEX =
            "(password=\\[([\\S\\s])*\\])|(\"password\":\"([\\S\\s])*\")";
    private              String[] excludeUrlPatterns;


    @Override
    public void doFilter ( ServletRequest request , ServletResponse response , FilterChain chain )
            throws IOException, ServletException {

        if ( PatternMatchUtils.simpleMatch( excludeUrlPatterns , ( ( HttpServletRequest ) request ).getRequestURI() ) ) {
            chain.doFilter( request , response );
            return;
        }


        final BodyReaderWrapper wrapper        = new BodyReaderWrapper( ( HttpServletRequest ) request );
        String                  requestMessage = RequestUtils.getRequestMessage( wrapper );
        if ( ! LogUtils.getLogger().isDebugEnabled() ) {
            requestMessage = StringUtils.replaceAll( requestMessage , PASSWORD_FILTER_REGEX ,
                                                     "enable password protection, if not debug so do not see"
            );
        }
        LogUtils.getLogger().info( requestMessage );
        chain.doFilter( wrapper , response );
    }

    @Override
    public void init ( FilterConfig filterConfig ) { }

    @Override
    public void destroy () { }

    public RequestLoggingFilter setExcludeUrlPatterns ( String[] excludeUrlPatterns ) {
        this.excludeUrlPatterns = excludeUrlPatterns;
        return this;
    }
}
