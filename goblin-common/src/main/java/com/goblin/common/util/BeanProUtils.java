package com.goblin.common.util;

import org.apache.commons.beanutils.BeanUtilsBean2;
import org.apache.commons.collections4.CollectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static com.goblin.common.util.ReflectionProUtils.invokeFieldGettersMethod;
import static org.reflections.ReflectionUtils.getAllFields;

/**
 * @author : 披荆斩棘
 * @date : 2017/12/12
 */
public class BeanProUtils {

	/**
	 * bean转换成Map
	 *
	 * @param bean 要转换的Bean
	 * @return 返回bean字段getters方法的全部属性 , 字段名为key , 其属性为value的<code>Map</code>
	 */
	public static Map< String, Object > toMap ( Object bean ) {
		final Set< Field > fields = getAllFields( bean.getClass() );
		if ( CollectionUtils.isEmpty( fields ) ) {
			return Collections.emptyMap();
		}
		Map< String, Object > map = new HashMap<>();
		for ( Field field : fields ) {
			final Object result = invokeFieldGettersMethod( bean , field.getName() );
			if ( null == result ) {
				continue;
			}
			map.put( field.getName() , result );
		}
		return map;
	}


	/**
	 * bean转换成Map{@code <String,String>}
	 *
	 * <b style="color:red">注意,转换过程中,value直接取toString()方法</b>
	 *
	 * @param bean 要转换的Bean
	 * @return 返回bean字段getters方法的全部属性 , 字段名为key , 其属性为value的<code>Map</code>
	 */
	public static Map< String, String > toStringMap( Object bean ) {
		final Map< String, Object > map = toMap( bean );
		Map<String,String> stringMap = new HashMap<>( map.size() );
		map.forEach( ( key , value ) -> stringMap.put( key, Objects.toString( value , null ) ) );
		return stringMap;
	}

	/**
	 * 拷贝属性
	 *
	 * @param sources     源
	 * @param targetClass 目标 class
	 * @param <T> 目标类型
	 * @return 目标集合
	 */
	public static < T > List< T > copyPropertiesToList ( List< ? > sources , Class<T> targetClass) {
		AssertUtils.isTrue( CollectionUtils.isEmpty( sources ), "sources is null" );
		AssertUtils.isTrue( targetClass == null, "targetClass is null" );
		List< T > targets = new ArrayList<>( sources.size() );
		for ( Object source : sources ) {
			T target;
			try {
				target = targetClass.newInstance();
				BeanUtilsBean2.getInstance().copyProperties( target , source );
			} catch ( InstantiationException | IllegalAccessException | InvocationTargetException e ) {
				throw new RuntimeException( e.getMessage() , e );
			}
			targets.add( target );
		}
		return targets;
	}


}
