package com.aidijing.common.util;

import com.google.common.collect.Maps;
import com.aidijing.common.annotation.ExportFiledComment;
import org.apache.commons.lang3.StringUtils;
import org.reflections.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Set;

/**
 * 反射工具
 *
 * @author : 披荆斩棘
 * @date : 2017/7/26
 * @see "https://github.com/ronmamo/reflections"
 */
public final class ReflectionPlusUtils extends ReflectionUtils {

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


	/**
	 * 获取字段属性说明
	 * 
	 * @param clazz
	 * @return
	 */
	public static LinkedHashMap<String, String> exportFiledComment(Class<?> clazz) {
		Set<Field> fields = getAllFields(clazz, withAnnotation(ExportFiledComment.class));
		LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>( Maps.newHashMapWithExpectedSize( fields.size()));
		fields.forEach(field -> {
			String filedComment = field.getAnnotation(ExportFiledComment.class).value();
			if ( StringUtils.isEmpty( filedComment)) {
				filedComment = field.getName();
			}
			linkedHashMap.put(field.getName(), filedComment);
		});
		return linkedHashMap;
	}


	/**
	 * 获取字段属性说明
	 * 
	 * @param clazz
	 * @param keyPrefix 字段前缀
	 * @param valuePrefix 值的前缀
	 * @return
	 */
	public static LinkedHashMap<String, String> exportFiledComment(Class<?> clazz, String keyPrefix,
			String valuePrefix) {
		Set<Field> fields = getAllFields(clazz, withAnnotation(ExportFiledComment.class));
		LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>( Maps.newHashMapWithExpectedSize( fields.size()));
		fields.forEach(field -> {
			String filedComment = field.getAnnotation(ExportFiledComment.class).value();
			if ( StringUtils.isEmpty( filedComment)) {
				filedComment = field.getName();
			}
			linkedHashMap.put(keyPrefix + field.getName(), valuePrefix + filedComment);
		});
		return linkedHashMap;
	}


    ///////////////////////////////////////////////////////////////////////////
    // 下面为通用方法
    ///////////////////////////////////////////////////////////////////////////


    public static Set<Method> getMethods ( Class< ? > clazz , final Class<? extends Annotation> annotation ) {
        return getMethods( clazz,withAnnotation( annotation ) );
    }

    /**
     * 反射调用对象字段的Getters方法
     *
     * @param object    : 该对象
     * @param fieldName : 该对象字段
     * @return 该对象字段Getters方法结果
     */
    public static Object invokeFieldGettersMethod ( Object object , String fieldName ) {
        if ( Objects.isNull( object ) ) {
            return null;
        }
        final Method gettersMethod = getFieldGettersMethod( object.getClass() , fieldName );
        if ( Objects.isNull( gettersMethod ) ) {
            return null;
        }
        try {
            return gettersMethod.invoke( object );
        } catch ( IllegalAccessException | InvocationTargetException e ) {
            LogUtils.getLogger().error( e );
            return null;
        }
    }


    /**
     * 反射得到对象字段Getters方法
     *
     * @param clazz     : 对象
     * @param fieldName : 对象字段
     * @return <code>getFieldName</code>方法
     */
    public static Method getFieldGettersMethod ( Class< ? > clazz , String fieldName ) {
        if ( Objects.isNull( clazz ) || StringUtils.isEmpty( fieldName ) ) {
            return null;
        }
        final Set< Method > fieldNameGettersMethods = getMethods( clazz ,
                                                                  withModifier( Modifier.PUBLIC ) ,
                                                                  withName( buildGetters( fieldName ) ) ,
                                                                  withParametersCount( 0 ) );
        return fieldNameGettersMethods.parallelStream().findFirst().orElse( null );
    }

    /**
     * 构建字段Getters方法名称
     *
     * @param fieldName 字段
     * @return <code>getFieldName</code>
     */
    private static String buildGetters ( String fieldName ) {
        return GETTERS_PREFIX + StringPrivateUtils.firstCharToUpperCase( fieldName );
    }


}










