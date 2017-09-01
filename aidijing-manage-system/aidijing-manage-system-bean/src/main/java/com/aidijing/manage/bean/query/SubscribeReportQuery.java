package com.aidijing.manage.bean.query;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author : 披荆斩棘
 * @date : 2017/8/7
 */
@Data
@Accessors( chain = true )
public class SubscribeReportQuery implements Serializable {

    private Date startTime;
    private Date endTime;

}
