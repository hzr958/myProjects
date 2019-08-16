package com.smate.center.task.v8pub.strategy;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smate.center.task.utils.DataFormatUtils;
import com.smate.core.base.utils.pub.BriefFormatter;
import com.smate.web.v8pub.dom.AwardsInfoBean;
import com.smate.web.v8pub.dom.PubTypeInfoBean;

/**
 * 构建奖励
 * 
 * @author YJ
 *
 *         2018年8月15日
 */
public class ConstructAwardsInfoBean implements PubTypeInfoDriver {

  private Logger logger = LoggerFactory.getLogger(getClass());

  private Map<String, String> buildData(PubTypeInfoBean typeInfo) {
    AwardsInfoBean a = (AwardsInfoBean) typeInfo;
    Map<String, String> map = new HashMap<String, String>();
    if (a != null) {
      map.put("issuing_authority".toUpperCase(), a.getIssuingAuthority());
      map.put("category".toUpperCase(), a.getCategory());
      map.put("grade".toUpperCase(), a.getGrade());
      // 这里的日期格式必须是以 - 隔开的
      map.put("award_date".toUpperCase(), DataFormatUtils.parseDate(a.getAwardDate(), "-"));
    }
    return map;
  }

  @Override
  public String constructBriefDesc(PubTypeInfoBean typeInfo, Locale locale, Long countryId, String publishDate)
      throws Exception {
    String briefDesc = "";
    try {
      Map<String, String> result = buildData(typeInfo);
      BriefFormatter formatter = new BriefFormatter(locale, result);
      briefDesc = formatter.format(BriefFormatter.AWARDBRIEF_PATTERN);
    } catch (Exception e) {
      logger.error("奖励成果构建briefDesc参数失败，pubTypeInfo={}", typeInfo, e);
    }
    return briefDesc;
  }

}
