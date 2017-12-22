# Spring Boot 基础骨架
https://yujunhao8831.github.io/

算是个人的记录,几年来做项目是会遇到的问题,这里会得到解决.当然还是有很多不足

没有整合消息队列,不想东西太多了,想要整合可以看 https://github.com/spring-projects/spring-amqp-samples

分布式服务之间调用,这里没有用到,后续会用Spring Cloud解决.

项目可以打成jar部署,也可以打成war部署,默认使用jar

``` xml
<groupId>com.aidijing</groupId>
    <artifactId>aidijing-restful-client</artifactId>
    <!--<packaging>war</packaging>-->
<packaging>jar</packaging>
```

也可以用Docker部署,要求
 + 本地含有Docker,启动状态
 + Docker包含JDK1.8,如没有安装即可
 ``` shell
 docker pull java
 ```
 
maven打包命令 : mvn clean package docker:build
 
docker运行命令 : docker run -p 8080:8080 -t com.aidijing/aidijing-restful-client # (默认情况打包后的docker镜像名称是这样)
 


项目所用技术
 + Spring 
 + Spring Boot
 + Spring MVC
 + Spring Session(已不使用,项目无状态)
 + Spring Security
 + Spring Cache
 + Spring Boot Admin
 + Hibernate Validator
 + Mybatis Plus 
 + Redis 
 + Swagger(已废弃)
 + Pagehelper
 + Druid
 + Log4j2
 + WebSocket
 + JWT(JSON Web Tokens)
 + WebSocket

项目包含了
 + 分布式锁
 + 分布式唯一code构建
 + 分布式session共享(已不使用,项目无状态)
 + 异步处理
 + Aop缓存
 + 基础CRUD
 + 物理分页
 + 异步日志,多线程下用户的会话跟踪
 + Swagger restful api
 + 异常统一处理
 + Cors解决跨域
 + 注入攻击拦截器
 + 多环境配置
 + 基本工具类
 + jwt认证
 + Spring Security基础权限管理
 + 细粒度权限控制(粒度控制到字段)
 + 验证码
 + Spring Boot Admin 管理和监视
 + 极简分布式任务调度
 + WebSocket 简单示例
 
 
要求 : 
 + JDK 1.8 
 + redis 默认使用 127.0.0.1:6379
 + mysql root/root 127.0.0.1:3306 数据库名称 : blog,基础sql见init.sql
 + lombok 插件(eclipse IntelliJ IDEA),不然项目可能会报错,但是不影响运行
 
## aidijing-parent
+ 版本管理

## aidijing-common 
+ 工具

## aidijing-service
**pom.xml**中所有的依赖都是
``` xml
<scope>provided</scope>
```




+ 服务接口


## aidijing-dao
**pom.xml**中所有的依赖都是
``` xml
<scope>provided</scope>
```
+ dao与数据库交互

## aidijing-basic-config
+ 基础配置(暂时未用到)

## aidijing-restful-client
+ 控制器,发布http restful接口


## 项目无状态
不要使用session(项目也禁用了)
``` java
com.aidijing.ContextUtils
```
用户相关数据从 ContextUtils 中获取
## 数据交互
``` java
org.springframework.http.ResponseEntity
```
使用ResponseEntity对数据进行封装
## 代码生成器
```
aidijing-generator
|---src
|   |---test
         |---com
              |---aidijing
                   |---generator
                       |---...
```
### 注意,所有状态,类型等字段,请使用枚举

    比如 : String resourceType(API:接口,MENU:菜单,BUTTON:按钮)
    这样的字段在实体中使用枚举类型

#### 创建枚举
相应的枚举,注释必须完整
注:
+ 枚举请实现 BaseEnumInterface 接口,方便之后操作
+ 枚举放入domain.enum包下

``` java
/**
 * 资源类型(API:接口,MENU:菜单,BUTTON:按钮)
 * <p>
 * 对应字段 {@link com.aidijing.domain.PermissionResource#resourceType}
 */
@Getter
public enum ResourceType implements BaseEnumInterface< ResourceType > {

    API( "API", "接口" ),
    MENU( "MENU", "菜单" ),
    BUTTON( "BUTTON", "按钮" );

    /** code **/
    private String code;
    /** 注释 **/
    private String comment;

    ResourceType ( String code, String comment ) {
        this.code = code;
        this.comment = comment;
    }

    @Override
    public boolean isEnumCode ( final String inputCode ) {
        return Objects.nonNull( getEnum( inputCode ) );
    }

    @Override
    public boolean isNotEnumCode ( final String inputCode ) {
        return ! isEnumCode( inputCode );
    }

    @Override
    public String getCodeComment ( final String inputCode ) {
        if ( Objects.isNull( inputCode ) ) {
            return null;
        }
        for ( ResourceType value : ResourceType.values() ) {
            if ( value.getCode().equals( inputCode ) ) {
                return value.getComment();
            }
        }
        return null;
    }

    @Override
    public ResourceType getEnum ( final String inputCode ) {
        if ( Objects.isNull( inputCode ) ) {
            return null;
        }
        for ( ResourceType thisEnum : ResourceType.values() ) {
            if ( thisEnum.getCode().equals( inputCode ) ) {
                return thisEnum;
            }
        }
        return null;
    }
}
```

#### 实体中声明
``` java
public class PermissionResource {
    private Long id;
    private ResourceType resourceType;
    // ... ... 
}
``` 


#### 构建枚举Handler
```
aidijing-generator
|---src
|   |---test
         |---com
              |---aidijing
                   |---generator
                       |---...
```
## 使用 mybatis plus
文档 http://mp.baomidou.com

+ 注意 : 分页不适用mybatis plus的分页,使用PageHelper,mybatis plus分页对多表查询的分页支持不够

## 查询分页(PageHelper)
https://pagehelper.github.io

示例 : 
``` java
// controller
@GetMapping
public ResponseEntity< PageInfo > listPage ( PageRowBounds pageRowBounds ) {
    return ResponseEntityPro.ok(userService.listPage( pageRowBounds ) );
}

// service
public PageInfo listPage ( PageRowBounds pageRowBounds ) {
    PageHelper.startPage( pageRowBounds.getOffset(), pageRowBounds.getLimit() );
    return new PageInfo( super.selectList( null ) );
}
```

## api返回指定/排除字段

示例 : 
``` java
// 排除指定字段
@GetMapping
public ResponseEntity< PageInfo > listPage ( PageRowBounds pageRowBounds ) {
    return ResponseEntityPro.ok(userService.listPage( pageRowBounds ) ,"-password,-realName" );
}

// 只返回指定字段
@GetMapping
public ResponseEntity< PageInfo > listPage ( PageRowBounds pageRowBounds ) {
    return ResponseEntityPro.ok(userService.listPage( pageRowBounds ) ,"password,realName" );
}

```

## api权限返回指定/排除字段

```
# 见 resource_api_uri_show_fields 字段
SELECT * FROM manage_role_permission_resource;
```

## 流程控制
接口对内使用Exception处理 AssertUtils

接口对外使用if else流程控制
