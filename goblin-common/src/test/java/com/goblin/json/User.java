package com.goblin.json;

/**
 * @author : 披荆斩棘
 * @date : 2017/5/25
 */

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@Accessors( chain = true )
public class User implements Serializable {
    private static final long           serialVersionUID = 1L;
    private              Long           id;
    private              String         name;
    private              String[]       names;
    private              String         username;
    private              List< String > info;
    private              Date           time;
    private              Address        address;
    private              Order          order;

    public User () {
        this.id = 1001L;
        this.name = null;
        this.names = new String[]{ "令狐冲" , "张三" , "大毛" };
        this.info = Arrays.asList( "北京" , "朝阳" , "密云" );
        this.time = new Date();
        this.username = "admin";
        this.address = new Address().setZip( "518000" ).setProvince( "北京" ).setName( "地址" );
        this.order = new Order().setId( 8888L ).setName( "支付宝" );
    }

    @Getter
    @Setter
    @ToString
    @Accessors( chain = true )
    public class Order implements Serializable {
        private Long   id;
        private String name;
    }

    @Getter
    @Setter
    @ToString
    @Accessors( chain = true )
    public class Address implements Serializable {
        private String name;
        private String province;
        private String zip;
    }
}
