package com.goblin.common.util;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.ArrayUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author : 披荆斩棘
 * @date : 2017/8/1
 */
public final class CollectionProUtils {


    private CollectionProUtils () {
    }

	/**
	 * 示例
	 * <pre>
	 *     CollectionProUtils.puts(new HashMap<>(),"username","admin","password","123456")       = {username=admin, password=123456}
	 *     CollectionProUtils.puts(new LinkedHashMap<>(),"username","admin","password","123456") = {username=admin, password=123456}
	 * </pre>
	 *
	 * @param map             Map 类型
	 * @param keysAndValues  键值对
	 * @param <K>             key 类型
	 * @param <V>             value 类型
	 * @return 指定类型的<code>Map</code>
	 */
	public static < K, V > Map< K, V > puts ( Map< K, V > map , Object... keysAndValues ) {
		return putAll( map , keysAndValues );
	}

	/**
	 * 示例
	 * <pre>
	 *     CollectionProUtils.hashMapPuts("username","admin","password","123456")       = {username=admin, password=123456}
	 * </pre>
	 *
	 * @param keysAndValues  键值对
	 * @param <K>             key 类型
	 * @param <V>             value 类型
	 * @return <code>HashMap</code>
	 */
	@SuppressWarnings("unchecked")
	public static < K, V > Map< K, V > hashMapPuts ( Object... keysAndValues ) {
		return (Map< K, V >) putAll( new HashMap<>() , keysAndValues );
	}

	/**
	 * 示例
	 * <pre>
	 *     CollectionProUtils.hashMapExpectedPuts(2,"username","admin","password","123456")       = {username=admin, password=123456}
	 * </pre>
	 *
	 * @param keysAndValues  键值对
	 * @param <K>             key 类型
	 * @param <V>             value 类型
	 * @return <code>HashMap</code>,指定容器的预期大小
	 */
	@SuppressWarnings("unchecked")
	public static < K, V > Map< K, V > hashMapExpectedPuts ( int initialCapacity , Object... keysAndValues ) {
		return (Map< K, V >) putAll( Maps.newHashMapWithExpectedSize( initialCapacity ) , keysAndValues );
	}

	/**
	 * {@link #hashMapPuts(Object...)}
	 * @return {@link LinkedHashMap}
	 */
	@SuppressWarnings("unchecked")
	public static < K, V > Map< K, V > linkedHashMapPuts ( Object... keysAndValues ) {
		return (Map< K, V >) putAll( new LinkedHashMap<>() , keysAndValues );
	}

	/**
	 * {@link #hashMapExpectedPuts(int, Object...)}
	 * @return {@link LinkedHashMap}
	 */
	@SuppressWarnings("unchecked")
	public static < K, V > Map< K, V > linkedHashMapExpectedPuts ( int initialCapacity , Object... keysAndValues ) {
		return (Map< K, V >) putAll( new LinkedHashMap<>( Maps.newHashMapWithExpectedSize( initialCapacity ) ) , keysAndValues );
	}

	@SuppressWarnings("unchecked")
	private static < K, V > Map< K, V > putAll ( Map< K, V > map , Object... keysAndValues ) {
		if ( ArrayUtils.isNotEmpty( keysAndValues ) ) {
			for ( int i = 0 ; i < keysAndValues.length ; i += 2 ) {
				if ( keysAndValues.length > ( i + 1 ) ) {
					map.put( ( K ) keysAndValues[i] , ( V ) keysAndValues[i + 1] );
				}
			}
		}
		return map;
	}


}
