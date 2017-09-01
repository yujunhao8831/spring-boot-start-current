package com.aidijing.manage.bean.query;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author : 披荆斩棘
 * @date : 2017/7/6
 */
@Data
@Accessors(chain = true)
public class RoleConfigQuery implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -8546744061965308022L;
    private Long id;
    private Long roleId;

    /**
     * 一般为具体功能或业务标志
     */
    private String configKey;

    /**
     * 具体配置项，格式可以是字符串也可以是json，根据业务规则自定义
     */
    private String configValue;

}
