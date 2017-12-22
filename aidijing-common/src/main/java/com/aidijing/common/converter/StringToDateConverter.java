package com.aidijing.common.converter;

import com.aidijing.common.util.DateFormatStyle;
import com.aidijing.common.util.DateUtils;
import org.springframework.core.convert.converter.Converter;

import java.util.Date;
import java.util.Objects;

/**
 * @author : 披荆斩棘
 * @date : 17/7/17
 */
public class StringToDateConverter implements Converter< String, Date > {

    @Override
    public Date convert ( String source ) {
		Date date = null;
        for ( DateFormatStyle formatStyle : DateFormatStyle.values() ) {
			date = DateUtils.formatStringByStyle(source.trim(), formatStyle.getDateStyle());
			if ( Objects.nonNull(date) ) {
				break;
			}
        }
        return date;
    }
}
