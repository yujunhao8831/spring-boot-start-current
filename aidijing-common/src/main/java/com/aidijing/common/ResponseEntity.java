package com.aidijing.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.pagehelper.PageInfo;
import com.aidijing.common.util.JsonUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : 披荆斩棘
 * @date : 16/6/16
 */
public class ResponseEntity < T > {
    /** 通配符 **/
    private static final String WILDCARD_ALL  = "*";
    /** 响应状态码 **/
    private volatile     String statusCode    = StatusCode.FAIL.getStatusCode();
    /** 失败或成功的提示信息 **/
    private volatile     String statusMessage = StatusCode.FAIL.getStatusMessage();
    /** json处理时需要过滤的字段,默认不过滤 **/
    @JsonIgnore
    private volatile     String filterField   = WILDCARD_ALL;
    /** 响应内容 **/
    private volatile T responseContent;

    public ResponseEntity () {
    }

    public ResponseEntity ( String statusCode ) {
        this.statusCode = statusCode;
    }

    public ResponseEntity ( String statusCode, String statusMessage ) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }

    public ResponseEntity ( String statusCode, String statusMessage, T responseContent ) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.responseContent = responseContent;
    }

    public static ResponseEntity ok ( String message ) {
        return new ResponseEntity( StatusCode.SUCCESS.getStatusCode(), message );
    }

    public static ResponseEntity ok ( StatusCode ok, String message ) {
        return new ResponseEntity( ok.getStatusCode(), message );
    }

    public static ResponseEntity ok () {
        return new ResponseEntity( StatusCode.SUCCESS.getStatusCode(), StatusCode.SUCCESS.getStatusMessage() );
    }

    public static ResponseEntity error ( String message ) {
        return new ResponseEntity( StatusCode.ERROR.getStatusCode(), message );
    }

    public static ResponseEntity error ( StatusCode error, String message ) {
        return new ResponseEntity( error.getStatusCode(), message );
    }

    public static ResponseEntity fail () {
        return new ResponseEntity( StatusCode.FAIL.getStatusCode(), StatusCode.FAIL.getStatusMessage() );
    }

    public static ResponseEntity fail ( String message ) {
        return new ResponseEntity( StatusCode.FAIL.getStatusCode(), message );
    }

    public static ResponseEntity fail ( StatusCode fail, String message ) {
        return new ResponseEntity( fail.getStatusCode(), message );
    }


    public static ResponseEntity unauthorized () {
        return new ResponseEntity(
                StatusCode.UNAUTHORIZED.getStatusCode(),
                StatusCode.UNAUTHORIZED.getStatusMessage()
        );
    }

    public static ResponseEntity unauthorized ( String message ) {
        return new ResponseEntity( StatusCode.UNAUTHORIZED.getStatusCode(), message );
    }

    public static ResponseEntity serviceUnavailable () {
        return new ResponseEntity(
                StatusCode.SERVICE_UNAVAILABLE.getStatusCode(),
                StatusCode.SERVICE_UNAVAILABLE.getStatusMessage()
        );
    }

    public static ResponseEntity serviceUnavailable ( String message ) {
        return new ResponseEntity( StatusCode.SERVICE_UNAVAILABLE.getStatusCode(), message );
    }


    @JsonIgnore
    public boolean isOk () {
        return StatusCode.SUCCESS.getStatusCode().equals( this.getStatusCode() );
    }

    @JsonIgnore
    public boolean isNotOk () {
        return ! isOk();
    }

    /**
     * 给 responseContent 添加内容
     * <pre>
     *     ResponseEntity.ok( "success" )
     *                   .add( "username", "披荆斩棘" )
     *                   .add( "password", "123456" )
     *                   .add( "ip", "localhost" );
     *
     *     ResponseEntity{statusCode='200', statusMessage='success', filterField='*', responseContent={password=123456, ip=localhost, username=披荆斩棘}}
     * </pre>
     *
     * @param key   : <code>String</code>类型
     * @param value : <code>Object</code>类型
     * @return <code>this</code>
     */
    public ResponseEntity add ( String key, Object value ) {
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


    public String getStatusCode () {
        return statusCode;
    }

    public ResponseEntity setStatusCode ( String statusCode ) {
        this.statusCode = statusCode;
        return this;
    }

    public String getStatusMessage () {
        return statusMessage;
    }

    public ResponseEntity setStatusMessage ( String statusMessage ) {
        this.statusMessage = statusMessage;
        return this;
    }

    public T getResponseContent () {
        return responseContent;
    }

    public ResponseEntity setResponseContent ( T responseContent ) {
        this.responseContent = responseContent;
        return this;
    }

    public String getFilterField () {
        return filterField;
    }

    /**
     * 设置过滤字段
     * <b style="color:red">注意只会过滤 responseContent 中的内容</b>
     *
     * @param filterField : 过滤字段,{@link JsonUtils#toFilterJson(Object , String)}
     * @return
     */
    public ResponseEntity setFilterField ( final String filterField ) {
        if ( WILDCARD_ALL.equals( filterField ) ) {
            return this;
        }
        StringBuffer buffer = new StringBuffer( WILDCARD_ALL ).append( ",responseContent[" );
        // 如果是分页实体,则只对分页内的结果集进行处理
        if ( this.getResponseContent() instanceof PageInfo ) {
            buffer.append( "*,list[" )
                  .append( filterField )
                  .append( "]]" );
        } else {
            buffer.append( filterField )
                  .append( "]" );
        }
        this.filterField = buffer.toString();
        return JsonUtils.jsonToType( this.toJson(), this.getClass() );
    }

    /**
     * 把<code>this</code>进行json序列号
     *
     * @return
     */
    public String toJson () {
        if ( WILDCARD_ALL.equals( this.getFilterField() ) ) {
            return JsonUtils.toJson( this );
        }
        return JsonUtils.toFilterJson( this, this.getFilterField() );
    }


    @Override
    public String toString () {
        return "ResponseEntity{" +
                "statusCode='" + statusCode + '\'' +
                ", statusMessage='" + statusMessage + '\'' +
                ", filterField='" + filterField + '\'' +
                ", responseContent=" + responseContent +
                '}';
    }


    public enum StatusCode {
        SUCCESS( "200", "操作成功." ),
        OK_NOT_HANDLER( "202", "收到请求,但是不会做任何处理" ),
        NO_CONTENT( "204", "收到请求,但是没有内容" ),
        FAIL( "400", "操作失败,请求参数有误" ),
        UNAUTHORIZED( "401", "身份验证失败." ),
        REQUEST_TIME_OUT( "408", "服务器等待客户端发送的请求时间过长,超时" ),
        TOO_MANY_REQUESTS( "429", "太多的请求" ),
        TRADE_REPETITION( "460", "重复交易" ),
        ERROR( "500", "操作错误." ),
        SERVICE_UNAVAILABLE( "503", "由于临时的服务器维护或者过载,服务器当前无法处理请求." );

        private String statusCode;
        private String statusMessage;

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
