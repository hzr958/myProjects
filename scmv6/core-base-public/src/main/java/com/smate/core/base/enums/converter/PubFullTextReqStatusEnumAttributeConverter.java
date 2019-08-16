package com.smate.core.base.enums.converter;

import javax.persistence.Converter;

import com.smate.core.base.enums.converter.AbstractCustomEnumAttributeConverter;
import com.smate.core.base.pub.enums.PubFullTextReqStatusEnum;

/**
 * 全文请求记录状态枚举类型转换器
 * 
 * @author houchuanjie
 * @date 2017年7月26日
 */
@Converter(autoApply = true)
public class PubFullTextReqStatusEnumAttributeConverter
    extends AbstractCustomEnumAttributeConverter<PubFullTextReqStatusEnum, Integer> {

}
