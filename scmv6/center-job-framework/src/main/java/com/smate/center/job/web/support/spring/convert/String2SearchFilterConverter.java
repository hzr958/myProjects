package com.smate.center.job.web.support.spring.convert;

import com.smate.center.job.web.support.jqgrid.SearchFilter;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.string.StringUtils;
import org.springframework.core.convert.converter.Converter;

/**
 * Spring的值转换器，用于将页面json字符串filters转换为JqGrid查询过滤条件模型类对象
 *
 * @author Created by hcj
 * @date 2018/07/03 9:26
 */
public class String2SearchFilterConverter implements Converter<String, SearchFilter> {

  @Override
  public SearchFilter convert(String source) {
    return StringUtils.isBlank(source) ? null : JacksonUtils.jsonObject(source, SearchFilter.class);
  }
}
