package com.aidijing.common;

import com.aidijing.common.util.JsonUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author : 披荆斩棘
 * @date : 16/6/16
 */
@ToString
@Getter
@Setter
@Accessors(chain = true)
public class ResponseEntity <T> {
	/** 通配符 **/
	public static final String						WILDCARD_ALL	= "*";
	/** 响应状态码 **/
	private volatile String							statusCode		= StatusCode.BAD_REQUEST.getStatusCode();
	/** 响应状态码对应的提示信息 **/
	private volatile String							statusMessage	= StatusCode.BAD_REQUEST.getStatusMessage();
	/** 响应内容 **/
	private volatile T								responseContent	= null;
	/**
	 * json处理时需要过滤的字段,默认不过滤 具体看 {@link JsonUtils#toFilterJson}
	 */
	@JsonIgnore
	private volatile String							filterFields	= WILDCARD_ALL;

	/**
	 * 自定义导出标题，key需与数据对象的属性名对应
	 */
	@JsonIgnore
	private volatile LinkedHashMap<String, String>	exportTitleMap;

	public ResponseEntity () {}

	public ResponseEntity ( final String statusCode) {
		this(statusCode, null, null);
	}

	public ResponseEntity ( final String statusCode, final String statusMessage) {
		this(statusCode, statusMessage, null);
	}

	public ResponseEntity ( final String statusCode, final String statusMessage, final T responseContent) {
		this.statusCode = statusCode;
		this.statusMessage = statusMessage;
		this.responseContent = responseContent;
	}

	public static ResponseEntity empty() {
		return new ResponseEntity();
	}

	/**
	 * 成功请求,成功状态码自行指定
	 *
	 * @param ok
	 * @param message
	 * @return
	 */
	public static ResponseEntity ok(final StatusCode ok, final String message) {
		return new ResponseEntity(ok.getStatusCode(), message);
	}

	/**
	 * 失败请求,失败状态码自行指定
	 *
	 * @param fail
	 * @param message
	 * @return
	 */
	public static ResponseEntity badRequest(final StatusCode fail, final String message) {
		return new ResponseEntity(fail.getStatusCode(), message);
	}

	public static ResponseEntity ok() {
		return ok( StatusCode.OK.getStatusCode(), StatusCode.OK.getStatusMessage());
	}

	public static ResponseEntity ok(final String message) {
		return ok( StatusCode.OK.getStatusCode(), message);
	}

	public static ResponseEntity ok(final String ok, final String message) {
		return new ResponseEntity(ok, message);
	}


	public static ResponseEntity internalServerError(final String message) {
		return new ResponseEntity( StatusCode.INTERNAL_SERVER_ERROR.getStatusCode(), message);
	}

	public static ResponseEntity internalServerError(final StatusCode error, final String message) {
		return new ResponseEntity(error.getStatusCode(), message);
	}


	public static ResponseEntity badRequest() {
		return badRequest( StatusCode.BAD_REQUEST.getStatusCode(), StatusCode.BAD_REQUEST.getStatusMessage());
	}

	public static ResponseEntity badRequest(final String message) {
		return badRequest( StatusCode.BAD_REQUEST.getStatusCode(), message);
	}

	public static ResponseEntity badRequest(final String fail, final String message) {
		return new ResponseEntity(fail, message);
	}


	public static ResponseEntity forbidden() {
		return forbidden( StatusCode.FORBIDDEN.getStatusMessage());
	}

	public static ResponseEntity forbidden(final String message) {
		return new ResponseEntity( StatusCode.FORBIDDEN.getStatusCode(), message);
	}

	public static ResponseEntity unauthorized() {
		return unauthorized( StatusCode.UNAUTHORIZED.getStatusMessage());
	}

	public static ResponseEntity unauthorized(final String message) {
		return new ResponseEntity( StatusCode.UNAUTHORIZED.getStatusCode(), message);
	}


	public static ResponseEntity serviceUnavailable() {
		return serviceUnavailable( StatusCode.SERVICE_UNAVAILABLE.getStatusMessage());
	}

	public static ResponseEntity serviceUnavailable(final String message) {
		return new ResponseEntity( StatusCode.SERVICE_UNAVAILABLE.getStatusCode(), message);
	}


	/**
	 * 给 responseContent 添加内容
	 * <p>
	 * 
	 * <pre>
	 *     ResponseEntity.ok( "success" )
	 *                   .add( "username", "披荆斩棘" )
	 *                   .add( "password", "123456" )
	 *                   .add( "ip", "localhost" );
	 *
	 *     ResponseEntity{statusCode='200', statusMessage='success', filterFields='*', responseContent={password=123456, ip=localhost, username=披荆斩棘}}
	 * </pre>
	 *
	 * @param key : <code>String</code>类型
	 * @param value : <code>Object</code>类型
	 * @return <code>this</code>
	 */
	public ResponseEntity add(final String key, final Object value) {
		if (null == this.responseContent) {
			this.responseContent = (T) new HashMap<String, Object>();
			Map<String, Object> content = (Map<String, Object>) this.responseContent;
			content.put(key, value);
			return this;
		}
		if (!(this.responseContent instanceof Map)) {
			return this;
		}
		((Map) this.responseContent).put(key, value);
		return this;
	}


	/**
	 * 非分页-设置过滤字段
	 * <p>
	 * <b style="color:red">注意只会过滤 responseContent 中的内容</b>
	 *
	 * @param filterFields : 过滤字段,{@link JsonUtils#toFilterJson(Object , String)}
	 * @return this
	 */
	public ResponseEntity setFilterFields(final String filterFields) {
		if (null == filterFields || WILDCARD_ALL.equals(filterFields)) {
			return this;
		}
		StringBuilder builder = new StringBuilder(WILDCARD_ALL).append(",responseContent[");
		builder.append(filterFields.trim()).append("]");
		this.filterFields = builder.toString();
		return this;
	}


	/**
	 * 分页结果-设置过滤字段
	 * <p>
	 * <b style="color:red">注意只会过滤 responseContent 中list下的内容</b>
	 * <p>
	 * 默认指定为{@link com.github.pagehelper.PageInfo#list}
	 *
	 * @param filterFields : 过滤字段,{@link JsonUtils#toFilterJson(Object , String)}
	 * @return this
	 */
	public ResponseEntity setFilterFieldsForPaging(final String filterFields) {
		if (null == filterFields || WILDCARD_ALL.equals(filterFields)) {
			return this;
		}
		StringBuilder builder = new StringBuilder(WILDCARD_ALL).append(",responseContent[");
		// 分页对象,则只对分页对象内的结果集进行处理
		builder.append("*,list[").append(filterFields.trim()).append("]]");
		this.filterFields = builder.toString();
		return this;
	}

	/**
	 * {@link #setFilterFieldsAndFlush(String , boolean)} 默认不是过滤分页结果集
	 */
	public ResponseEntity setFilterFieldsAndFlush(final String filterFields) {
		return this.setFilterFieldsAndFlush(filterFields, false);
	}

	/**
	 * 设置过滤字段并过滤刷新
	 * <p>
	 * <b style="color:red"> 注意该方法在controller中最后 <code>return</code> 时使用,可能会导致flush2次,因为在自定义
	 * <p>
	 * {@link org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice}
	 * 的实现类中,会再次flush(因为我在返回的时候会进行flush);
	 * <p>
	 * 如果,你未定义 {@link org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice} 实现类那么就需要在
	 * <code>return</code> 时调用 {@link #setFilterFieldsAndFlush(String , boolean)} 而不是 {@link #setFilterFields(String)}
	 * <p>
	 * 亦或者你自定义了实现类,但是没有进行flush,那么你还是得调用 {@link #setFilterFieldsAndFlush(String , boolean)} </b>
	 * <p>
	 * 当然我可以设置<code>boolean</code>状态值,但是我又不想这个状态值返回到外面,那么这样就需要对这个字段进行忽略,这样就不会进行序列化.
	 * <p>
	 * 但是有的时候在传输的时候是需要序列号和反序列化的,所以这里是使用约定而不是既定
	 * <p>
	 *
	 * @param filterFields : 需要过滤的字段
	 * @param isFilterPaging : 过滤的结果集是否存在分页, 更多 {@link #setFilterFields(String)}
	 *        {@link #setFilterFieldsForPaging(String)}
	 * @return
	 */
	public ResponseEntity setFilterFieldsAndFlush(final String filterFields, boolean isFilterPaging) {
		if (isFilterPaging) {
			return this.setFilterFieldsForPaging(filterFields).filterFieldsFlush();
		}
		return this.setFilterFields(filterFields).filterFieldsFlush();
	}

	/**
	 * 过滤字段刷新
	 *
	 * @return 刷新后的 <code>this</code>
	 */
	public ResponseEntity filterFieldsFlush() {
		return JsonUtils.jsonToType( this.toJson(), this.getClass());
	}

	/**
	 * 对<code>this</code>进行json序列号,如果设置了过滤字段则会进行过滤
	 *
	 * @return json
	 */
	public String toJson() {
		if (this.isNotFieldsFilter()) {
			return JsonUtils.toJson(this);
		}
		return JsonUtils.toFilterJson(this, this.getFilterFields());
	}


	/**
	 * 是否成功
	 *
	 * @return 如果状态 <b style="color:red">是<code>StatusCode.OK</code></b> 则返回 <code>true</code>
	 */
	@JsonIgnore
	public boolean isOk() {
		return StatusCode.OK.getStatusCode().equals( this.getStatusCode());
	}

	/**
	 * 是否不成功
	 *
	 * @return "!" {@link #isOk()}
	 */
	@JsonIgnore
	public boolean isNotOk() {
		return !isOk();
	}

	/**
	 * 是否需要过滤字段
	 *
	 * @return "!" {@link #isNotFieldsFilter()}
	 */
	@JsonIgnore
	public boolean isFieldsFilter() {
		return !this.isNotFieldsFilter();
	}

	/**
	 * 是否不需要过滤字段
	 *
	 * @return 如果 <b style="color:red"> null == this.getFilterFields() || {@link #filterFields} <code>equals</code>
	 *         {@link #WILDCARD_ALL} </b>则返回 <code>true</code>
	 */
	@JsonIgnore
	public boolean isNotFieldsFilter() {
		return null == this.getFilterFields() || WILDCARD_ALL.equals(this.getFilterFields());
	}


	public enum StatusCode {
		/** [GET]：服务器成功返回用户请求的数据，该操作是幂等的（Idempotent）。 **/
		OK("200", "请求成功"),
		/** [POST/PUT/PATCH]：用户新建或修改数据成功。 **/
		CREATED("201", "操作成功"),
		/** 202 Accepted - [*]：表示一个请求已经进入后台排队（异步任务） **/
		OK_NOT_HANDLER("202", "收到请求"),
		/** 204 NO CONTENT - [DELETE]：用户删除数据成功。 **/
		NO_CONTENT("204", "数据删除成功"),
		/** 400 INVALID REQUEST - [POST/PUT/PATCH]：用户发出的请求有错误，服务器没有进行新建或修改数据的操作，该操作是幂等的。 **/
		BAD_REQUEST("400", "请求失败"),
		/** 401 Unauthorized - [*]：表示用户没有权限（令牌、用户名、密码错误）。 **/
		UNAUTHORIZED("401", "身份验证失败"),
		/** 403 Forbidden - [*] 表示用户得到授权（与401错误相对），但是访问是被禁止的。 **/
		FORBIDDEN("403", "权限不足"),
		/** 404 NOT FOUND - [*]：用户发出的请求针对的是不存在的记录，服务器没有进行操作，该操作是幂等的。 **/
		NOT_FOUND("404", "记录不存在"), METHOD_NOT_ALLOWED("405", "目标资源不支持该请求方式"),
		/** 406 Not Acceptable - [GET]：用户请求的格式不可得（比如用户请求JSON格式，但是只有XML格式）。 **/
		NOT_ACCEPTABLE("404", "请求格式错误"), REQUEST_TIME_OUT("408", "服务器等待客户端发送的请求时间过长,超时"),
		/** 410 Gone -[GET]：用户请求的资源被永久删除，且不会再得到的。 **/
		GONE("410", "请求的资源被永久删除"),
		/** 422 请求格式正确，但是由于含有语义错误，无法响应 **/
		UNPROCESSABLE_ENTITY("422", "验证失败"), TOO_MANY_REQUESTS("429", "太多的请求"), TRADE_REPETITION("460", "重复交易"),
		/** 500 INTERNAL SERVER INTERNAL_SERVER_ERROR - [*]：服务器发生错误，用户将无法判断发出的请求是否成功。 **/
		INTERNAL_SERVER_ERROR("500", "请求出错"), SERVICE_UNAVAILABLE("503", "由于临时的服务器维护或者过载,服务器当前无法处理请求");

		private final String	statusCode;
		private final String	statusMessage;

		StatusCode(String statusCode, String statusMessage) {
			this.statusCode = statusCode;
			this.statusMessage = statusMessage;
		}

		public String getStatusMessage() {
			return statusMessage;
		}

		public String getStatusCode() {
			return statusCode;
		}

	}


}
