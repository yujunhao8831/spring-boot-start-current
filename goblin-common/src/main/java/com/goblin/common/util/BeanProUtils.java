package com.goblin.common.util;

import org.apache.commons.collections4.CollectionUtils;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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


}
