package com.aidijing.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.pagehelper.PageInfo;
import com.aidijing.common.util.JsonUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author : 披荆斩棘
 * @date : 16/6/16
 */
@ToString
@Getter
@Setter
@Accessors( chain = true )
public final class ResponseEntity < T > {
    /** 通配符 **/
    public static final String        WILDCARD_ALL         = "*";
    /** 响应状态码 **/
    private volatile    String        statusCode           = StatusCode.FAIL.getStatusCode();
    /** 失败或成功的提示信息 **/
    private volatile    String        statusMessage        = StatusCode.FAIL.getStatusMessage();
    /** 响应内容 **/
    private volatile    T             responseContent      = null;
    /**
     * 可选择的过滤字段
     * <p><b>为什么要有这个字段?</b></p>
     * <ul>
     * <li>1. 首先这个字段默认是 <code>null</code> ,json已设置 <code>null</code> 值不会被序列化,所以不用这个字段的时候它是不可见的</li>
     * <li>
     * 2. 是为了在添加权限时,可以给前端用,这个API到底是有哪些字段一目了然,如果单独再提供接口,这样每个API都要提供一个,会非常不优雅
     * <p>
     * 这里提供了2个方法,一个是方式生成(这个最简洁),还有一个是给非pojo类型用的
     * {@link #setOptionalFilterFields(Set)}
     * {@link #reflectionToOptionalFilterFields(Class)}
     * </li>
     * </ul>
     * 当然一般情况下这个字段也用不到
     */
    private volatile    Set< String > optionalFilterFields = null;
    /**
     * json处理时需要过滤的字段,默认不过滤
     * 具体看 {@link JsonUtils#toFilterJson}
     */
    @JsonIgnore
    private volatile    String        filterFields         = WILDCARD_ALL;

    public ResponseEntity () {
    }

    public ResponseEntity ( final String statusCode ) {
        this( statusCode, null, null );
    }

    public ResponseEntity ( final String statusCode, final String statusMessage ) {
        this( statusCode, statusMessage, null );
    }

    public ResponseEntity ( final String statusCode, final String statusMessage, final T responseContent ) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.responseContent = responseContent;
    }

    public static ResponseEntity empty () {
        return new ResponseEntity();
    }

    public static ResponseEntity ok ( final String message ) {
        return new ResponseEntity( StatusCode.SUCCESS.getStatusCode(), message );
    }

    public static ResponseEntity ok ( final StatusCode ok, final String message ) {
        return new ResponseEntity( ok.getStatusCode(), message );
    }

    public static ResponseEntity ok () {
        return new ResponseEntity( StatusCode.SUCCESS.getStatusCode(), StatusCode.SUCCESS.getStatusMessage() );
    }

    public static ResponseEntity error ( final String message ) {
        return new ResponseEntity( StatusCode.ERROR.getStatusCode(), message );
    }

    public static ResponseEntity error ( final StatusCode error, final String message ) {
        return new ResponseEntity( error.getStatusCode(), message );
    }

    public static ResponseEntity fail () {
        return new ResponseEntity( StatusCode.FAIL.getStatusCode(), StatusCode.FAIL.getStatusMessage() );
    }

    public static ResponseEntity fail ( final String message ) {
        return new ResponseEntity( StatusCode.FAIL.getStatusCode(), message );
    }

    public static ResponseEntity fail ( final StatusCode fail, final String message ) {
        return new ResponseEntity( fail.getStatusCode(), message );
    }


    public static ResponseEntity unauthorized () {
        return new ResponseEntity(
                StatusCode.UNAUTHORIZED.getStatusCode(),
                StatusCode.UNAUTHORIZED.getStatusMessage()
        );
    }

    public static ResponseEntity unauthorized ( final String message ) {
        return new ResponseEntity( StatusCode.UNAUTHORIZED.getStatusCode(), message );
    }

    public static ResponseEntity serviceUnavailable () {
        return new ResponseEntity(
                StatusCode.SERVICE_UNAVAILABLE.getStatusCode(),
                StatusCode.SERVICE_UNAVAILABLE.getStatusMessage()
        );
    }

    public static ResponseEntity serviceUnavailable ( final String message ) {
        return new ResponseEntity( StatusCode.SERVICE_UNAVAILABLE.getStatusCode(), message );
    }


    /**
     * 给 responseContent 添加内容
     * <pre>
     *     ResponseEntity.ok( "success" )
     *                   .add( "username", "披荆斩棘" )
     *                   .add( "password", "123456" )
     *                   .add( "ip", "localhost" );
     *
     *     ResponseEntity{statusCode='200', statusMessage='success', filterFields='*', responseContent={password=123456, ip=localhost, username=披荆斩棘}}
     * </pre>
     *
     * @param key   : <code>String</code>类型
     * @param value : <code>Object</code>类型
     * @return <code>this</code>
     */
    public ResponseEntity add ( final String key, final Object value ) {
        if ( null == this.responseContent ) {
            this.responseContent = ( T ) new HashMap< String, Object >();
            Map< String, Object > content = ( Map< String, Object > ) this.responseContent;
            content.put( key, value );
            return this;
        }
        if ( ! ( this.responseContent instanceof Map ) ) {
            return this;
        }
        ( ( Map ) this.responseContent ).put( key, value );
        return this;
    }


    /**
     * 反射获取对象字段并设置到当前对象的 {@link #optionalFilterFields} 中,忽略 serialVersionUID
     *
     * @param clazz {@link Class}
     * @return this
     */
    public ResponseEntity reflectionToOptionalFilterFields ( final Class clazz ) {
        final Field[] declaredFields = clazz.getDeclaredFields();
        this.optionalFilterFields = new LinkedHashSet<>( declaredFields.length );
        for ( Field declaredField : declaredFields ) {
            if ( "serialVersionUID".equalsIgnoreCase( declaredField.getName() ) ) {
                continue;
            }
            optionalFilterFields.add( declaredField.getName() );
        }
        return this;
    }

    /**
     * 设置过滤字段
     * <p>
     * <b style="color:red">注意只会过滤 responseContent 中的内容</b>
     *
     * @param filterFields : 过滤字段,{@link JsonUtils#toFilterJson(Object , String)}
     * @return this
     */
    public ResponseEntity setFilterFields ( final String filterFields ) {
        if ( null == filterFields || WILDCARD_ALL.equals( filterFields ) ) {
            return this;
        }
        StringBuilder buffer = new StringBuilder( WILDCARD_ALL ).append( ",responseContent[" );
        // 如果是分页对象,则只对分页对象内的结果集进行处理
        if ( this.getResponseContent() instanceof PageInfo ) {
            buffer.append( "*,list[" )
                  .append( filterFields )
                  .append( "]]" );
        } else {
            buffer.append( filterFields )
                  .append( "]" );
        }
        this.filterFields = buffer.toString();
        return this;
    }

    /**
     * 设置过滤字段并过滤刷新
     * <p>
     * <b style="color:red">
     * 注意该方法在controller中最后 <code>return</code> 时使用,可能会导致flush2次,因为在自定义 <p>
     * {@link org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice} 的实现类中,会再次flush(因为我在返回的时候会进行flush); <p>
     * 如果,你未定义 {@link org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice}
     * 实现类那么就需要在 <code>return</code> 时调用 {@link #setFilterFieldsAndFlush(String)} 而不是 {@link #setFilterFields(String)} <p>
     * 亦或者你自定义了实现类,但是没有进行flush,那么你还是得调用 {@link #setFilterFieldsAndFlush(String)}
     * </b> <p>
     * 当然我可以设置<code>boolean</code>状态值,但是我又不想这个状态值返回到外面,那么这样就需要对这个字段进行忽略,这样就不会进行序列化.<p>
     * 但是有的时候在传输的时候是需要序列号和反序列化的,所以这里是使用约定而不是既定
     * <p>
     * 更多 {@link #setFilterFields(String)}
     */
    public ResponseEntity setFilterFieldsAndFlush ( final String filterFields ) {
        return this.setFilterFields( filterFields ).filterFieldsFlush();
    }

    /**
     * 过滤字段刷新
     *
     * @return 刷新后的 <code>this</code>
     */
    public ResponseEntity filterFieldsFlush () {
        return JsonUtils.jsonToType( this.toJson(), this.getClass() );
    }

    /**
     * 对<code>this</code>进行json序列号,如果设置了过滤字段则会进行过滤
     *
     * @return json
     */
    public String toJson () {
        if ( this.isNotFieldsFilter() ) {
            return JsonUtils.toJson( this );
        }
        return JsonUtils.toFilterJson( this, this.getFilterFields() );
    }


    /**
     * 是否成功
     *
     * @return 如果状态 <b style="color:red">是<code>StatusCode.SUCCESS</code></b> 则返回 <code>true</code>
     */
    @JsonIgnore
    public boolean isOk () {
        return StatusCode.SUCCESS.getStatusCode().equals( this.getStatusCode() );
    }

    /**
     * 是否不成功
     *
     * @return "!" {@link #isOk()}
     */
    @JsonIgnore
    public boolean isNotOk () {
        return ! isOk();
    }

    /**
     * 是否需要过滤字段
     *
     * @return "!" {@link #isNotFieldsFilter()}
     */
    @JsonIgnore
    public boolean isFieldsFilter () {
        return ! this.isNotFieldsFilter();
    }

    /**
     * 是否不需要过滤字段
     *
     * @return 如果 <b style="color:red"> null == this.getFilterFields() || {@link #filterFields} <code>equals</code> {@link #WILDCARD_ALL} </b>则返回 <code>true</code>
     */
    @JsonIgnore
    public boolean isNotFieldsFilter () {
        return null == this.getFilterFields() || WILDCARD_ALL.equals( this.getFilterFields() );
    }


    public enum StatusCode {
        SUCCESS( "200", "请求成功" ),
        OK_NOT_HANDLER( "202", "收到请求,但是不会做任何处理" ),
        NO_CONTENT( "204", "收到请求,但是没有内容" ),
        FAIL( "400", "请求失败" ),
        UNAUTHORIZED( "401", "身份验证失败" ),
        REQUEST_TIME_OUT( "408", "服务器等待客户端发送的请求时间过长,超时" ),
        TOO_MANY_REQUESTS( "429", "太多的请求" ),
        TRADE_REPETITION( "460", "重复交易" ),
        ERROR( "500", "请求出错" ),
        SERVICE_UNAVAILABLE( "503", "由于临时的服务器维护或者过载,服务器当前无法处理请求" );

        private final String statusCode;
        private final String statusMessage;

        StatusCode ( String statusCode, String statusMessage ) {
            this.statusCode = statusCode;
            this.statusMessage = statusMessage;
        }

        public String getStatusMessage () {
            return statusMessage;
        }

        public String getStatusCode () {
            return statusCode;
        }

    }


}
