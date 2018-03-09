package com.goblin.manage;

import com.goblin.common.PagingRequest;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * application/json
 * 处理 {@link org.springframework.web.bind.annotation.RequestBody}
 *
 * @author : 披荆斩棘
 * @date : 2017/7/14
 */
@RestControllerAdvice
public class PagingRequestBodyAdvice implements RequestBodyAdvice {

    @Override
    public boolean supports ( MethodParameter methodParameter,
                              Type targetType,
                              Class< ? extends HttpMessageConverter< ? > > converterType ) {
        return PagingRequest.class.isAssignableFrom( methodParameter.getParameterType() );
    }

    @Override
    public Object handleEmptyBody ( Object body,
                                    HttpInputMessage inputMessage,
                                    MethodParameter parameter,
                                    Type targetType,
                                    Class< ? extends HttpMessageConverter< ? > > converterType ) {
        // 如果为空,给个默认值
        return new PagingRequest();
    }

    @Override
    public HttpInputMessage beforeBodyRead ( HttpInputMessage inputMessage,
											 MethodParameter parameter,
											 Type targetType,
											 Class< ? extends HttpMessageConverter< ? > > converterType ) throws
                                                                                                          IOException {
        return inputMessage;
    }

    @Override
    public Object afterBodyRead ( Object body,
                                  HttpInputMessage inputMessage,
                                  MethodParameter parameter,
                                  Type targetType,
                                  Class< ? extends HttpMessageConverter< ? > > converterType ) {
        return body;
    }
}
