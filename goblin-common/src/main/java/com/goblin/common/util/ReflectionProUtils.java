package com.goblin.common.util;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.reflections.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * 反射工具
 *
 * @author : 披荆斩棘
 * @date : 2017/7/26
 * @see <a href="https://github.com/ronmamo/reflections" >document</a>
 */
public final class ReflectionProUtils extends ReflectionUtils {

	private final static String GETTERS_PREFIX = "get";


	/**
	 * 反射调用对象字段的Getters方法,结果定制输出成字符串
	 * <p>
	 * 只适用于个别业务场景
	 *
	 * @param object
	 * @param fieldName
	 * @return 结果定制化
	 */
	public static String invokeFieldGettersMethodToCustomizeString ( Object object , String fieldName ) {
		final Object methodResult = invokeFieldGettersMethod( object , fieldName );
		if ( Objects.isNull( methodResult ) ) {
			return StringUtils.EMPTY;
		}
		if ( methodResult.getClass().isEnum() ) {
			return invokeFieldGettersMethod( methodResult , "comment" ).toString();
		} else if ( methodResult instanceof Date ) {
			return DateUtils.formatDateByStyle( ( Date ) methodResult );
		}
		return methodResult.toString();
	}


	///////////////////////////////////////////////////////////////////////////
	// 下面为通用方法
	///////////////////////////////////////////////////////////////////////////


	public static Set< Method > getMethods ( Class< ? > clazz , final Class< ? extends Annotation > annotation ) {
		return getMethods( clazz , withAnnotation( annotation ) );
	}

	/**
	 * {@link #invokeFieldGettersMethod(Object , Method)}
	 */
	public static Object invokeFieldGettersMethod ( Object object , String fieldName ) {
		final Method gettersMethod = getFieldGettersMethod( object.getClass() , fieldName );
		if ( null == gettersMethod ) {
			return null;
		}
		return invoke( object , gettersMethod );
	}

	/**
	 * 反射调用对象字段的Getters方法
	 *
	 * @param object        : 该对象
	 * @param gettersMethod : Getters方法
	 * @return 该对象字段Getters方法结果 , 如果反射异常调用则返回 <code>null</code>
	 */
	public static Object invokeFieldGettersMethod ( Object object , Method gettersMethod ) {
		if ( null == object ) {
			return null;
		}
		return invoke( object , gettersMethod );
	}

	private static Object invoke ( Object object , Method gettersMethod ) {
		try {
			return gettersMethod.invoke( object );
		} catch ( IllegalAccessException | InvocationTargetException e ) {
			LogUtils.getLogger().error( e.getMessage() , e );
			return null;
		}
	}

	/**
	 * 反射得到对象字段所有Getters方法(这些字段的Getters方法,一定是 <code>public</code> 无参数的,get开头的)
	 *
	 * @param clazz : 对象
	 * @return 公开没有参数的get方法 , 如果反射不到则返回null
	 */
	public static Set< Method > getAllGettersMethods ( Class< ? > clazz ) {
		if ( null == clazz ) {
			return null;
		}
		final Set< Field > fields = getAllFields( clazz );
		if ( CollectionUtils.isEmpty( fields ) ) {
			return null;
		}
		Set< Method > methods = new HashSet<>( fields.size() );
		for ( Field field : fields ) {
			final Method gettersMethod = getFieldGettersMethod( clazz , field.getName() );
			if ( null == gettersMethod ) {
				continue;
			}
			methods.add( gettersMethod );
		}
		return methods;
	}


	/**
	 * 反射得到对象'指定'字段Getters方法(该方法一定是 <code>public</code> 无参数的,get开头的)
	 *
	 * @param clazz     : 对象
	 * @param fieldName : 对象字段
	 * @return 公开没有参数的get方法 , 如果反射不到则返回<code>null</code>
	 */
	public static Method getFieldGettersMethod ( Class< ? > clazz , String fieldName ) {
		if ( null == clazz || StringUtils.isEmpty( fieldName ) ) {
			return null;
		}
		return getPublicNoParametersGettersMethod( clazz , fieldName );
	}

	/**
	 * 反射得到公开的没有参数的get方法
	 *
	 * @param clazz      对象
	 * @param fieldName  对象字段
	 * @return 公开没有参数的get方法 , 如果反射不到则返回<code>null</code>
	 */
	private static Method getPublicNoParametersGettersMethod ( Class< ? > clazz , String fieldName ) {
		// getAllMethods 该方法会把父类的也进行反射
		return getAllMethods( clazz ,
							  // 公开的方法
							  withModifier( Modifier.PUBLIC ) ,
							  // getXXX 名称开通
							  withName( buildGetters( fieldName ) ) ,
							  // 没有参数
							  withParametersCount( 0 ) )
			.parallelStream().findFirst().orElse( null );
	}

	/**
	 * 构建字段Getters方法名称
	 *
	 * @param fieldName 字段
	 * @return <code>getFieldName</code>
	 */
	private static String buildGetters ( String fieldName ) {
		return GETTERS_PREFIX + StringProUtils.firstCharToUpperCase( fieldName );
	}


}










