package com.goblin.common.http;

import com.goblin.common.util.LogUtils;
import okhttp3.*;
import org.springframework.http.HttpMethod;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.URI;
import java.security.KeyStore;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * OkHttp工具类
 *
 * @author : 披荆斩棘
 * @date : 2016/10/30
 */
public class OkHttpFactory {

    public static final MediaType    APPLICATION_JSON_UTF8_VALUE = MediaType.parse( org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE );
    public static final MediaType    APPLICATION_XML_VALUE       = MediaType.parse( org.springframework.http.MediaType.APPLICATION_XML_VALUE );
    private             OkHttpClient client;

    /**
     * 创建一个工厂和一个默认的 {@link OkHttpClient} 实例。
     */
    public OkHttpFactory () {
        this.client = new OkHttpClient();
    }

    /**
     * 定制自己的client,可自己在外部设置 client 的一些参数,如过期时间或者更多的配置.
     *
     * @param client 自定义
     */
    public OkHttpFactory ( OkHttpClient client ) {
        this.client = client;
    }


    /**
     * 发起一个GET请求
     *
     * @param uri : 请求地址
     * @return 响应内容
     * @throws IOException /
     */
    public String get ( URI uri ) throws IOException {
        LogUtils.getLogger().debug( "请求地址 : {}" , uri.toURL() );

        Request request = new Request.Builder().url( uri.toURL() ).get().build();
        try ( Response response = client.newCall( request ).execute() ) {
            return response.body().string();
        }
    }


    public ResponseBody getResponseBody ( URI uri ) throws IOException {
        LogUtils.getLogger().debug( "请求地址 : {}" , uri.toURL() );

        Request request = new Request.Builder().url( uri.toURL() ).get().build();
        return client.newCall( request ).execute().body();
    }

    /**
     * @param uri         : 请求地址
     * @param httpMethod  : 请求方法 {@link HttpMethod}
     * @param contentType : 请求类型
     * @param content     : 请求内容
     * @return 响应内容
     * @throws IOException /
     */
    public String createRequest ( URI uri , HttpMethod httpMethod , MediaType contentType , String content ) throws
                                                                                                             IOException {
        LogUtils.getLogger().debug( "请求地址 : {}" , uri.toURL() );
        LogUtils.getLogger().debug( "请求方法 : {}" , httpMethod.name() );
        LogUtils.getLogger().debug( "请求类型 : {}" , contentType );
        LogUtils.getLogger().debug( "请求参数 : {}" , content );

        RequestBody body = RequestBody.create( contentType , content );
        Request request =
                new Request.Builder().url( uri.toURL() ).method( httpMethod.name() , body ).build();
        try ( Response response = client.newCall( request ).execute() ) {
            return response.body().string();
        }
    }

    /**
     * @param uri        : 请求地址
     * @param httpMethod : 请求方法 {@link HttpMethod}
     * @param content    : 请求内容
     * @return
     * @throws IOException
     */
    public String createJsonRequest ( URI uri , HttpMethod httpMethod , String content ) throws IOException {
        LogUtils.getLogger().debug( "请求地址 : {}" , uri.toURL() );
        LogUtils.getLogger().debug( "请求方法 : {}" , httpMethod.name() );
        LogUtils.getLogger().debug( "请求参数 : {}" , content );

        RequestBody body    = RequestBody.create( APPLICATION_JSON_UTF8_VALUE , content );
        Request     request = new Request.Builder().url( uri.toURL() ).method( httpMethod.name() , body ).build();
        try ( Response response = client.newCall( request ).execute() ) {
            return response.body().string();
        }
    }

    /**
     * @param uri
     * @param httpMethod
     * @param content
     * @param headers
     * @return
     * @throws IOException
     */
    public String createJsonRequestHeader ( URI uri ,
                                            HttpMethod httpMethod ,
                                            String content ,
                                            Map< String, String > headers ) throws IOException {
        LogUtils.getLogger().debug( "请求地址 : {}" , uri.toURL() );
        LogUtils.getLogger().debug( "请求方法 : {}" , httpMethod.name() );
        LogUtils.getLogger().debug( "请求参数 : {}" , content );
        LogUtils.getLogger().debug( "请求headers : {}" , headers );

        RequestBody body = RequestBody.create( APPLICATION_JSON_UTF8_VALUE , content );
        Request.Builder builder =
            new Request.Builder().url( uri.toURL() ).method( httpMethod.name() , body );
        headers.forEach( ( key , value ) -> builder.addHeader( key , value ) );
        Request request = builder.build();
        try ( Response response = client.newCall( request ).execute() ) {
            return response.body().string();
        }
    }


    /**
     * @param uri
     * @param httpMethod
     * @param headers
     * @return
     * @throws IOException
     */
    public String createJsonRequestHeader ( URI uri , HttpMethod httpMethod , Map< String, String > headers ) throws
                                                                                                              IOException {
        LogUtils.getLogger().debug( "请求地址 : {}" , uri.toURL() );
        LogUtils.getLogger().debug( "请求方法 : {}" , httpMethod.name() );
        LogUtils.getLogger().debug( "请求headers : {}" , headers );

        RequestBody body = RequestBody.create( APPLICATION_JSON_UTF8_VALUE , "" );
        Request.Builder builder =
            new Request.Builder().url( uri.toURL() ).method( httpMethod.name() , body );
        headers.forEach( ( key , value ) -> builder.addHeader( key , value ) );
        Request request = builder.build();
        try ( Response response = client.newCall( request ).execute() ) {
            return response.body().string();
        }
    }

    /**
     * @param uri
     * @param httpMethod
     * @param paramName
     * @param paramValue
     * @return
     * @throws IOException
     */
    public String createJsonRequest ( URI uri , HttpMethod httpMethod , String paramName , String paramValue )
            throws IOException {
        LogUtils.getLogger().debug( "请求地址 : {}" , uri.toURL() );
        LogUtils.getLogger().debug( "请求方法 : {}" , httpMethod.name() );
        LogUtils.getLogger().debug( "请求参数 : {}:{}" , paramName , paramValue );

        FormBody body    = new FormBody.Builder().addEncoded( paramName , paramValue ).build();
        Request  request = new Request.Builder().url( uri.toURL() ).method( httpMethod.name() , body ).build();
        try ( Response response = client.newCall( request ).execute() ) {
            return response.body().string();
        }
    }

    /**
     * @param uri
     * @param httpMethod
     * @param params
     * @return
     * @throws IOException
     */
    public String createFromRequest ( URI uri , HttpMethod httpMethod , Map< String, String > params )
            throws IOException {
        LogUtils.getLogger().debug( "请求地址 : {}" , uri.toURL() );
        LogUtils.getLogger().debug( "请求方法 : {}" , httpMethod );
        LogUtils.getLogger().debug( "请求参数 : {}" , params );

        FormBody.Builder body = new FormBody.Builder();
        for ( Map.Entry< String, String > param : params.entrySet() ) {
            body.addEncoded( param.getKey() , param.getValue() );
        }
        Request request = new Request.Builder().url( uri.toURL() )
                                               .method( httpMethod.name() , body.build() ).build();
        try ( Response response = client.newCall( request ).execute() ) {
            return response.body().string();
        }
    }

    public String createFromRequest ( URI uri ,
                                      HttpMethod httpMethod ,
                                      Map< String, String > params ,
                                      Map< String, String > headers ) throws IOException {
        LogUtils.getLogger().debug( "请求地址 : {}" , uri.toURL() );
        LogUtils.getLogger().debug( "请求方法 : {}" , httpMethod.name() );
        LogUtils.getLogger().debug( "请求headers : {}" , headers );

        FormBody.Builder body = new FormBody.Builder();
        params.forEach( body::addEncoded );
        final Request.Builder builder = new Request.Builder().url( uri.toURL() );
        headers.forEach( builder::header );
        Request request = builder.method( httpMethod.name() , body.build() ).build();
        try ( Response response = client.newCall( request ).execute() ) {
            return response.body().string();
        }
    }


    /**
     * 设置底层读超时,以毫秒为单位。值0指定无限超时。
     *
     * @see OkHttpClient.Builder#readTimeout(long , TimeUnit)
     */
    public OkHttpFactory setReadTimeout ( int readTimeout ) {
        this.client =
                this.client.newBuilder().readTimeout( readTimeout , TimeUnit.MILLISECONDS ).build();
        return this;
    }

    /**
     * 设置底层写超时,以毫秒为单位。值0指定无限超时。
     *
     * @see OkHttpClient.Builder#writeTimeout(long , TimeUnit)
     */
    public OkHttpFactory setWriteTimeout ( int writeTimeout ) {
        this.client =
                this.client.newBuilder().writeTimeout( writeTimeout , TimeUnit.MILLISECONDS ).build();
        return this;
    }

    /**
     * 设置底层连接超时,以毫秒为单位。值0指定无限超时。
     *
     * @see OkHttpClient.Builder#connectTimeout(long , TimeUnit)
     */
    public OkHttpFactory setConnectTimeout ( int connectTimeout ) {
        this.client = this.client.newBuilder().connectTimeout( connectTimeout , TimeUnit.MILLISECONDS )
                                 .build();
        return this;
    }


    /**
     * 忽略SSL
     *
     * @see OkHttpClient.Builder#sslSocketFactory(SSLSocketFactory , X509TrustManager)
     */
    public OkHttpFactory ignoreSslSocketFactory () throws Exception {
        TrustManagerFactory trustManagerFactory =
                TrustManagerFactory.getInstance( TrustManagerFactory.getDefaultAlgorithm() );
        trustManagerFactory.init( ( KeyStore ) null );
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        if ( trustManagers.length != 1 || ! ( trustManagers[0] instanceof X509TrustManager ) ) {
            throw new IllegalStateException(
                    "Unexpected default trust managers:" + Arrays.toString( trustManagers ) );
        }
        X509TrustManager trustManager = ( X509TrustManager ) trustManagers[0];
        SSLContext       sslContext   = SSLContext.getInstance( "TLS" );
        sslContext.init( null , new TrustManager[]{ trustManager } , null );
        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

        this.client = this.client.newBuilder().sslSocketFactory( sslSocketFactory , trustManager )
                                 .hostnameVerifier( this.getHostnameVerifier() ).build();
        return this;
    }


    private HostnameVerifier getHostnameVerifier () {
        return DefaultHostnameVerifier.instance;
    }


    private enum DefaultHostnameVerifier implements HostnameVerifier {
        /**
         *
         */
        instance;

        @Override
        public boolean verify ( String s , SSLSession sslSession ) {
            return true;
        }
    }


}
