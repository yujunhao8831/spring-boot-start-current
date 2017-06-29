package com.aidijing.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * @author : 披荆斩棘
 * @date : 2017/5/10
 */
@Configuration
@ImportResource( "classpath:applicationContext-tx.xml" )
public class TransactionConfig {
    // 声明式事务未转化为JavaConfig配置方式,后续需要修改 
}
