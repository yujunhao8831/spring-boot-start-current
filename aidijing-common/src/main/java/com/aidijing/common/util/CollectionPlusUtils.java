package com.aidijing.common.util;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.ArrayUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author : 披荆斩棘
 * @date : 2017/8/1
 */
public final class CollectionPlusUtils {


    private CollectionPlusUtils () {
    }

    
    public static < K, V > Map< K, V > puts ( Map< K, V > map , Object... keysAndValues ) {
        return putAll( map , keysAndValues );
    }

    public static < K, V > Map< K, V > hashMapPuts ( Object... keysAndValues ) {
        return putAll( new HashMap<>() , keysAndValues );
    }

    public static < K, V > Map< K, V > hashMapExpectedPuts ( int initialCapacity , Object... keysAndValues ) {
        return putAll( Maps.newHashMapWithExpectedSize( initialCapacity ) , keysAndValues );
    }

    public static < K, V > Map< K, V > linkedHashMapPuts ( Object... keysAndValues ) {
        return putAll( new LinkedHashMap<>() , keysAndValues );
    }

    public static < K, V > Map< K, V > linkedHashMapExpectedPuts ( int initialCapacity , Object... keysAndValues ) {
        return putAll( new LinkedHashMap<>( Maps.newHashMapWithExpectedSize( initialCapacity ) ) , keysAndValues );
    }


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
