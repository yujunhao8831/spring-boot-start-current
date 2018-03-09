package com.goblin.common;

/**
 * 全局常量
 *
 * @author : 披荆斩棘
 * @date : 2017/5/19
 */
public abstract class GlobalConstant {

	/** 默认分页起始值 */
	public static final int    DEFAULT_PAGE_NUMBER        = 1;
	/** 默认分页大小值 */
	public static final int    DEFAULT_PAGE_SIZE          = 10;
	/** 分页起始参数名称 */
	public static final String PAGE_NUMBER_PARAM_NAME     = "pageNumber";
	/** 分页大小参数名称 */
	public static final String PAGE_SIZE_PARAM_NAME       = "pageSize";
	/**
	 * 祖先id
	 */
	public static final Long   ROOT_ID                    = 0L;
	/** 验证码请求头key **/
	public static final String CAPTCHA_REQUEST_HEADER_KEY = "captcha";
	/** 验证码有效期,单位:秒 **/
	public static final int    CAPTCHA_TIME_OUT_SECOND    = 600;

}
