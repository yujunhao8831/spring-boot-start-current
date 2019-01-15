package com.goblin.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bohnman.squiggly.Squiggly;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * json工具类
 *
 *
 *
 * @author : 披荆斩棘
 * @date : 2016/10/2
 */
public final class JsonUtils {


    /**
     * <p>jackson</p>
     * <a href="https://github.com/FasterXML/jackson-docs">document</a>
     */
    private static final ObjectMapper BASIC         = new ObjectMapper();
    private static final ObjectMapper CUSTOMIZATION = new CustomizationObjectMapper();

    static {
        BASIC.setDateFormat( new SimpleDateFormat( DateFormatStyle.CN_DATE_BASIC_STYLE.getDateStyle() ) );
		BASIC.configure( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false );
    }

    /**
     * <p>
     * 转换为Json,可过滤属性
     * <p>默认使用Jackson进行转换,{@link #CUSTOMIZATION}</p>
     * 注意 : <b style="color:red"><code>null</code>将不会被序列化</b>
     * <pre>
     *      <code>@Data</code>
     *      <code>@Accessors(chain = true)</code>
     *      public class User implements Serializable {
     *          private Long           id;
     *          private String         name;
     *          private String[]       names;
     *          private String         username;
     *          private List< String > info;
     *          private Date           time;
     *          private Address        address;
     *          private Order          order;
     *
     *          public User () {
     *              this.id = 1001L;
     *              this.name = null;
     *              this.names = new String[]{ "令狐冲" , "张三" , "大毛" };
     *              this.info = Arrays.asList( "北京", "朝阳", "密云" );
     *              this.time = new Date();
     *              this.username = "admin";
     *              this.address = new Address().setZip( "518000" ).setProvince( "北京" ).setName( "地址" );
     *              this.order = new Order().setId( 8888L ).setName( "支付宝" );
     *          }
     *          <code>@Data</code>
     *          <code>@Accessors(chain = true)</code>
     *          public class Order implements Serializable {
     *              private Long id;
     *              private String name;
     *          }
     *          <code>@Data</code>
     *          <code>@Accessors(chain = true)</code>
     *          public class Address implements Serializable {
     *              private String name;
     *              private String province;
     *              private String zip;
     *          }
     *      }
     *
     *      {@link JsonUtils#toFilterJson(Object , String)}
     *      String filter = "表达式";
     *      JsonUtils.toFilterJson(user,filter);
     *
     *      Object     String                        Presentation              Examples
     *      ------     ------                        ------------              -------
     *      user       ""                            空字符串                   {}
     *      user       null                          null                      {"id":1001,"names":["令狐冲","张三","大毛"],"username":"admin","info":["北京","朝阳","密云"],"time":"2017-06-23 17:37:06","address":{"name":"地址","province":"北京","zip":"518000"},"order":{"id":8888,"name":"支付宝"}}
     *      user       *                             '*'通配符                  {"id":1001,"names":["令狐冲","张三","大毛"],"username":"admin","info":["北京","朝阳","密云"],"time":"2017-06-23 17:37:06","address":{"name":"地址","province":"北京","zip":"518000"},"order":{"id":8888,"name":"支付宝"}}
     *      user       username,address              只显示某些字段               {"username":"admin","address":{"name":"地址","province":"北京","zip":"518000"}}
     *      user       na*,result                    '*'通配符                  {"names":["令狐冲","张三","大毛"]}
     *      user       **                            '*'通配符                  {"id":1001,"names":["令狐冲","张三","大毛"],"username":"admin","info":["北京","朝阳","密云"],"time":"2017-06-23 17:37:06","address":{"name":"地址","province":"北京","zip":"518000"},"order":{"id":8888,"name":"支付宝"}}
     *      user       address[province,zip]         对象字段内部过滤             {"address":{"province":"北京","zip":"518000"}}
     *      user       (address,order)[name]         同时指定多个对象字段内部过滤   {"address":{"province":"北京","zip":"518000"}}
     *      user       address.zip,address.name      '.' 的方式                 {"address":{"name":"地址","zip":"518000"}}
     *      user       address.zip,address[name]     '.' 的方式                 {"address":{"name":"地址","zip":"518000"}}
     *      user       ~na[a-z]es~                   正则表达式                  {"names":["令狐冲","张三","大毛"]}
     *      user       -names,-username              '-' 排除字段                {"id":1001,"info":["北京","朝阳","密云"],"time":"2017-06-23 18:27:58","address":{"name":"地址","province":"北京","zip":"518000"},"order":{"id":8888,"name":"支付宝"}}
     *      user       -names,username               '-' 排除字段(注意)           {"username":"admin"}
     *      user       -names,-username,*            '-' 排除字段                {"id":1001,"info":["北京","朝阳","密云"],"time":"2017-06-23 18:27:58","address":{"name":"地址","province":"北京","zip":"518000"},"order":{"id":8888,"name":"支付宝"}}
     *      user       -user.names,-order.id         '-' 排除字段                {"user":{"id":1001,"username":"admin","info":["北京","朝阳","密云"],"time":"2017-07-05 13:58:20","address":{"name":"地址","province":"北京","zip":"518000"},"order":{"id":8888,"name":"支付宝"}}}
     *
     * </pre>
     *
     * @param input  :
     * @param filter : 过滤字段
     * @return 如果转换失败返回 <code>null</code> ,否则返回转换后的json
     * @see <a href="https://github.com/bohnman/squiggly-filter-jackson">更多内容请看:Squiggly-document</a> <br/>
     */
    public static String toFilterJson ( Object input , String filter ) {
        return toJson( Squiggly.init( new CustomizationObjectMapper() , filter ) , input );
    }

    /**
     * 转换为Json
     * <p>默认使用Jackson进行转换,{@link #BASIC}</p>
     *
     * @param input 待转换对象
     * @return 如果转换失败返回 <code>null</code> ,否则返回转换后的json
     */
    public static String toJson ( Object input ) {
        return toJson( BASIC , input );
    }

    /**
     * 转换为Json
     * <p>默认使用Jackson进行转换,{@link #BASIC}</p>
     *
     * @param input 待转换对象
     * @return 如果转换失败返回 <code>null</code> ,否则返回转换后的json
     */
    public static String toCustomizationJson ( Object input ) {
        return toJson( CUSTOMIZATION , input );
    }

    /**
     * json转换为指定类型
     * <p>默认使用Jackson进行转换,{@link #BASIC}</p>
     * 注意 : 指定类型是内部类会报错 jackson can only instantiate non-static inner class by using default, no-arg
     *
     * @param inputJson  : json
     * @param targetType : 目标类型
     * @param <T> 目标类型
     * @return 如果解析失败返回 <code>null</code> ,否则返回解析后的json
     */
    public static < T > T jsonToType ( String inputJson , Class< T > targetType ) {
        return jsonToType( BASIC , inputJson , targetType );
    }

    /**
     * json转换为指定类型
     * <p>默认使用Jackson进行转换,{@link #BASIC}</p>
     * 注意 : 指定类型是内部类会报错 jackson can only instantiate non-static inner class by using default, no-arg
     *
     * @param inputJson  : json
     * @param targetType : 目标类型
     * @param <T> 目标类型
     * @return 如果解析失败返回 <code>null</code> ,否则返回解析后的json
     */
    public static < T > List< T > jsonToListType ( String inputJson , Class< T > targetType ) {
        return jsonToListType( BASIC , inputJson , targetType );
    }

    /**
     * json转换为指定类型(支持泛型)
     * <pre class="code">
     * 示例 :
     * ResponseEntity< User > responseEntity = JsonUtils.jsonToType( jsonValue ,new TypeReference< ResponseEntity< User > >() {} );
     * </pre>
     *
     * @param inputJson  : json
     * @param targetType : 目标类型
     * @param <T> 目标类型
     * @return
     */
    public static < T > T jsonToType ( String inputJson , TypeReference targetType ) {
        return jsonToType( BASIC , inputJson , targetType );
    }

    public static ObjectMapper getCustomizationMapper () {
        return CUSTOMIZATION;
    }

	public static ObjectMapper buildCustomizationMapper () {
		return new CustomizationObjectMapper();
	}

    public static ObjectMapper getBasicMapper () {
        return BASIC;
    }


    private static < T > T jsonToType ( ObjectMapper objectMapper , String inputJson , TypeReference targetType ) {
        try {
            return objectMapper.readValue( inputJson , targetType );
        } catch ( Exception e ) {
            LogUtils.getLogger().error( "jsonToType" , e );
        }
        return null;
    }

    private static < T > T jsonToType ( ObjectMapper objectMapper , String inputJson , Class< T > targetType ) {
        try {
            return objectMapper.readValue( inputJson , targetType );
        } catch ( Exception e ) {
            LogUtils.getLogger().error( "jsonToType" , e );
        }
        return null;
    }

    private static < T > List< T > jsonToListType ( ObjectMapper objectMapper ,
                                                    String inputJson ,
                                                    Class< T > targetType ) {
        try {
            return objectMapper.readValue(
                    inputJson ,
                    objectMapper.getTypeFactory().constructCollectionType( List.class , targetType )
            );
        } catch ( Exception e ) {
            LogUtils.getLogger().error( "jsonToListType" , e );
        }
        return null;
    }

    private static String toJson ( ObjectMapper objectMapper , Object input ) {
        try {
            return objectMapper.writeValueAsString( input );
        } catch ( JsonProcessingException e ) {
            LogUtils.getLogger().error( "toJson" , e );
        }
        return null;
    }

    private static class CustomizationObjectMapper extends ObjectMapper {

		private CustomizationObjectMapper (DateFormat dateFormat) {
			super();
			// 设置格式化
			setDateFormat( dateFormat );
			// <code>null<code> 不序列化
			setSerializationInclusion( JsonInclude.Include.NON_NULL );
			// 遇到未知属性,略过
			configure( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false );
		}

        private CustomizationObjectMapper () {
			// 默认只支持 yyyy-MM-dd HH:mm:ss
			this(new SimpleDateFormat(DateFormatStyle.CN_DATE_BASIC_STYLE.getDateStyle()));
        }



	}


}
