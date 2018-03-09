package com.goblin.common;

import com.goblin.common.util.DateUtils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 多重解析,单一格式化
 *
 * @author : 披荆斩棘
 * @date : 2017/12/21
 * @see SimpleDateFormat
 */
public class SimpleDateFormatPro extends SimpleDateFormat {


	/**
	 * 默认的日期时间pattern
	 */
	private static String CN_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	/**
	 * 解析日期时间格式的pattern,可以有多个.
	 * <p>
	 * <b style="color:red">注意:是解析. </b>
	 * <p style="color:red">
	 * 因为是多种日期解析pattern 需要注意的是 {@link #parse(String)} 会按照顺序进行解析,无法按照最优匹配原则进行解析,这样可能会损失一定时间精度
	 * </p>
	 * <pre>
	 *     SimpleDateFormatPro format = new SimpleDateFormatPro(Arrays.asList("yyyy-MM-dd","yyyy-MM-dd HH:mm:ss"));
	 *     final String        text   = "2017-12-22 10:59:40";
	 *     // 结果是 : 2017-12-22 00:00:00
	 *     System.err.println(format.format(format.parse(text)));
	 *
	 *     // ----------------------------
	 *
	 *     format = new SimpleDateFormatPro(Arrays.asList("yyyy-MM-dd HH:mm:ss","yyyy-MM-dd"));
	 *     // 结果是 : 2017-12-22 10:59:40
	 *     System.err.println(format.format(format.parse(text)));
	 * </pre>
	 */
	private LinkedHashSet< String > parsePatterns;

	/**
	 * 默认只能解析 : {@link #CN_DATETIME_PATTERN},输出也是按照 {@link #CN_DATETIME_PATTERN} 进行输出
	 */
	public SimpleDateFormatPro () {
		this( CN_DATETIME_PATTERN , Collections.singletonList( CN_DATETIME_PATTERN ) );
	}


	/**
	 * 指定日期解析格式,默认输出格式为 {@link #CN_DATETIME_PATTERN}
	 *
	 * @param parsePatterns {@link #parsePatterns},多重日期解析
	 */
	public SimpleDateFormatPro ( List< String > parsePatterns ) {
		this( CN_DATETIME_PATTERN , parsePatterns );
	}

	/**
	 * 指定格式化输出及日期解析格式
	 * <pre>
	 * 		SimpleDateFormatPro format = new SimpleDateFormatPro("yyyy-MM-dd HH:mm:ss",Arrays.asList("yyyy-MM-dd HH:mm:ss","yyyy-MM-dd")));
	 * </pre>
	 *
	 * @param formatPattern 输出时日期时间格式的pattern,单一格式化输出
	 * @param parsePatterns {@link #parsePatterns},多重日期解析
	 */
	public SimpleDateFormatPro ( String formatPattern , List< String > parsePatterns ) {
		super( formatPattern );
		this.parsePatterns = new LinkedHashSet<>( parsePatterns );
	}

	/**
	 * 解析字符串为 <code>Date</code>
	 *
	 * @param text 待解析的<code>String</code>
	 * @return 如果无法解析则返回 <code>null</code>,<b style="color:red">注意该实现这里不会抛异常</b>
	 */
	@Override
	public Date parse ( String text ) {
		Date date = null;
		for ( String parsePattern : this.parsePatterns ) {
			date = DateUtils.formatStringByStyle( text , parsePattern );
			if ( Objects.nonNull( date ) ) {
				break;
			}
		}
		return date;
	}

}
