package com.aidijing;

import com.aidijing.common.ResponseEntity;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * api {@link ResponseEntity} 返回类型处理,针对 {@link com.aidijing.domain.PermissionResource#resourceApiUriShowFields} 字段
 *
 * @author : 披荆斩棘
 * @date : 2017/6/29
 */
@RestControllerAdvice
public class GlobalResponseController implements ResponseBodyAdvice< ResponseEntity > {

    @Override
    public boolean supports ( MethodParameter returnType, Class converterType ) {
        return ResponseEntity.class.isAssignableFrom( returnType.getParameterType() );
    }

    @Override
    public ResponseEntity beforeBodyWrite ( ResponseEntity body,
                                            MethodParameter returnType,
                                            MediaType selectedContentType,
                                            Class selectedConverterType,
                                            ServerHttpRequest request,
                                            ServerHttpResponse response ) {
        // 如果自行设置了,那么就以自行设置的为主
        if ( body.isFieldsFilter() ) {
            return body.filterFieldsFlush();
        }
        // 如果当前用户的这个接口权限本来就可以查看全部
        if ( ResponseEntity.WILDCARD_ALL.equals( ContextUtils.getCurrentRequestApiShowFields() ) ) {
            return body;
        }
        return body.setFilterFieldsAndFlush( ContextUtils.getCurrentRequestApiShowFields() );
    }
}
