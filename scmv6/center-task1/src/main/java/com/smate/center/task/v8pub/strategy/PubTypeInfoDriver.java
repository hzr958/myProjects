package com.smate.center.task.v8pub.strategy;

import java.util.Locale;
import com.smate.web.v8pub.dom.PubTypeInfoBean;

/**
 * 成果类型数据对象策略
 * 
 * @author YJ
 *
 *         2018年8月15日
 */
public interface PubTypeInfoDriver {

  /**
   * 构造briefDesc参数
   * 
   * @param typeInfo
   * @return
   */
  String constructBriefDesc(PubTypeInfoBean typeInfo, Locale locale, Long countryId, String publishDate)
      throws Exception;

}
