package com.aidijing.json;

import com.aidijing.common.ResponseEntity;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.gson.reflect.TypeToken;
import com.aidijing.common.util.JsonUtils;
import org.junit.Test;

/**
 * @author : 披荆斩棘
 * @date : 2017/5/25
 */
public class JacksonAndGsonTest {


    @Test
    public void serializableTest () throws Exception {
        final String jscksonJsonValue = JsonUtils.toJson( new User() );
        System.err.println( "jscksonJsonValue = " + jscksonJsonValue );
        final User user = JsonUtils.jsonToType( jscksonJsonValue, User.class );
        System.err.println( "user = " + user ); // 反序列失败
        final String gsonJsonValue = JsonUtils.getGson().toJson( new User() );
        System.err.println( "gsonJsonValue = " + gsonJsonValue );
        final User user2 = JsonUtils.getGson().fromJson( gsonJsonValue, User.class );
        System.err.println( "user2 = " + user2 );
        final User user3 = JsonUtils.getGson().fromJson( jscksonJsonValue, User.class );
        System.err.println( "user3 = " + user3 );
    }

    @Test
    public void jacksonSerializableTest () throws Exception {
        final ResponseEntity< User > responseEntity = ResponseEntity.empty().setResponseContent( new User() );
        final String                 jscksonJsonValue = JsonUtils.toJson( responseEntity );
        System.err.println( "jscksonJsonValue = " + jscksonJsonValue );

        final ResponseEntity< User > result = JsonUtils.jsonToType(
                jscksonJsonValue,
                new com.fasterxml.jackson.core.type.TypeReference< ResponseEntity< User > >() {
                }
        );
        System.err.println( "result = " + result );
        final User user = result.getResponseContent();
        System.err.println( "result.content = " + user );

    }

    @Test
    public void gsonSerializableTest () throws Exception {

        final ResponseEntity< User > responseEntity = ResponseEntity.empty().setResponseContent( new User() );
        final String                 gsonJsonValue  = JsonUtils.getGson().toJson( responseEntity );
        System.err.println( "gsonJsonValue = " + gsonJsonValue );


        final ResponseEntity< User > result = JsonUtils.getGson().fromJson(
                gsonJsonValue,
                new TypeToken< ResponseEntity< User > >() {
                }.getType()
        );
        System.err.println( "result = " + result );
        final User user = result.getResponseContent();
        System.err.println( "result.content = " + user );

    }


    @Test
    public void fastjsonSerializableTest () throws Exception {
        final ResponseEntity< User > responseEntity = ResponseEntity.empty().setResponseContent( new User() );
        final String                 fastjsonValue  = JSON.toJSONString( responseEntity );
        System.err.println( "fastjsonValue = " + fastjsonValue );
        final ResponseEntity< User > result = JSON.parseObject(
                fastjsonValue,
                new TypeReference< ResponseEntity< User > >() {
                }
        );
        System.err.println( "result = " + result );
        final User user = result.getResponseContent();
        System.err.println( "result.content = " + user );

    }


}
