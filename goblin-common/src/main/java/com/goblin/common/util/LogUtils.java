package com.goblin.common.util;


import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.*;

/**
 * 不用在每个类中使用 LogManager.getLogger(xxx.class) 的方式了,
 * 直接使用 LogManager.getLogger(),或者用LogUtils.getLogger
 * 使用以上方法,或自动获取当前调用者的类名
 *
 * @author : 披荆斩棘
 * @date : 2017/5/12
 */
public final class LogUtils {

	/**
	 * 返回一个调用这的名称的Logger
	 *
	 * @return The Logger for the calling class.
	 * @throws UnsupportedOperationException if the calling class cannot be determined.
	 */
	public static Logger getLogger () {
		return LogManager.getLogger();
	}


	public static Logger getLogger ( MessageFactory messageFactory ) {
		return LogManager.getLogger( messageFactory );
	}

	public static Logger getCustomizationLogger () {
		return LogManager.getLogger( CustomizationParameterizedMessage.INSTANCE );
	}


	/**
	 * 混淆,未实现
	 *
	 * @param rawString 原字符
	 * @return 混淆后的字符
	 */
	public static String confusion ( String rawString ) {
		if ( StringUtils.isBlank( rawString ) ) {
			return rawString;
		}
		return rawString;
	}


	/**
	 * 默认是沿用 {@link ParameterizedMessageFactory} 的实现.
	 * <p>
	 * 这里可以自己对log输出进行过滤,比如一些敏感的数据,phone,password等进行特殊化输出
	 */
	public static class CustomizationParameterizedMessage extends AbstractMessageFactory {

		private static final CustomizationParameterizedMessage INSTANCE = new CustomizationParameterizedMessage();

		// 邮箱
		// 手机
		// 身份证

		/**
		 * Creates {@link ParameterizedMessage} instances.
		 *
		 * @param message The message pattern.
		 * @param params  The message parameters.
		 * @return The Message.
		 * @see MessageFactory#newMessage(String , Object...)
		 */
		@Override
		public Message newMessage ( final String message , final Object... params ) {
			return new ParameterizedMessage( message , params );
		}

		/**
		 * @since 2.6.1
		 */
		@Override
		public Message newMessage ( final String message , final Object p0 ) {
			return new ParameterizedMessage( message , p0 );
		}

		/**
		 * @since 2.6.1
		 */
		@Override
		public Message newMessage ( final String message , final Object p0 , final Object p1 ) {
			return new ParameterizedMessage( message , p0 , p1 );
		}

		/**
		 * @since 2.6.1
		 */
		@Override
		public Message newMessage ( final String message , final Object p0 , final Object p1 , final Object p2 ) {
			return new ParameterizedMessage( message , p0 , p1 , p2 );
		}

		/**
		 * @since 2.6.1
		 */
		@Override
		public Message newMessage ( final String message ,
									final Object p0 ,
									final Object p1 ,
									final Object p2 ,
									final Object p3 ) {
			return new ParameterizedMessage( message , p0 , p1 , p2 , p3 );
		}

		/**
		 * @since 2.6.1
		 */
		@Override
		public Message newMessage ( final String message ,
									final Object p0 ,
									final Object p1 ,
									final Object p2 ,
									final Object p3 ,
									final Object p4 ) {
			return new ParameterizedMessage( message , p0 , p1 , p2 , p3 , p4 );
		}

		/**
		 * @since 2.6.1
		 */
		@Override
		public Message newMessage ( final String message ,
									final Object p0 ,
									final Object p1 ,
									final Object p2 ,
									final Object p3 ,
									final Object p4 ,
									final Object p5 ) {
			return new ParameterizedMessage( message , p0 , p1 , p2 , p3 , p4 , p5 );
		}

		/**
		 * @since 2.6.1
		 */
		@Override
		public Message newMessage ( final String message ,
									final Object p0 ,
									final Object p1 ,
									final Object p2 ,
									final Object p3 ,
									final Object p4 ,
									final Object p5 ,
									final Object p6 ) {
			return new ParameterizedMessage( message , p0 , p1 , p2 , p3 , p4 , p5 , p6 );
		}

		/**
		 * @since 2.6.1
		 */
		@Override
		public Message newMessage ( final String message ,
									final Object p0 ,
									final Object p1 ,
									final Object p2 ,
									final Object p3 ,
									final Object p4 ,
									final Object p5 ,
									final Object p6 ,
									final Object p7 ) {
			return new ParameterizedMessage( message , p0 , p1 , p2 , p3 , p4 , p5 , p6 , p7 );
		}

		/**
		 * @since 2.6.1
		 */
		@Override
		public Message newMessage ( final String message ,
									final Object p0 ,
									final Object p1 ,
									final Object p2 ,
									final Object p3 ,
									final Object p4 ,
									final Object p5 ,
									final Object p6 ,
									final Object p7 ,
									final Object p8 ) {
			return new ParameterizedMessage( message , p0 , p1 , p2 , p3 , p4 , p5 , p6 , p7 , p8 );
		}

		/**
		 * @since 2.6.1
		 */
		@Override
		public Message newMessage ( final String message ,
									final Object p0 ,
									final Object p1 ,
									final Object p2 ,
									final Object p3 ,
									final Object p4 ,
									final Object p5 ,
									final Object p6 ,
									final Object p7 ,
									final Object p8 ,
									final Object p9 ) {
			return new ParameterizedMessage( message , p0 , p1 , p2 , p3 , p4 , p5 , p6 , p7 , p8 , p9 );
		}

	}


}
