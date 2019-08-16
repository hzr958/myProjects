package com.smate.center.task.v8pub.strategy;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.smate.center.task.service.sns.quartz.ConstRegionService;
import com.smate.center.task.utils.DataFormatUtils;
import com.smate.core.base.pub.enums.PubThesisDegreeEnum;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.pub.BriefFormatter;
import com.smate.web.v8pub.dom.PubTypeInfoBean;
import com.smate.web.v8pub.dom.ThesisInfoBean;

/**
 * 构建学位
 * 
 * @author YJ
 *
 *         2018年8月15日
 */
public class ConstructThesisInfoBean implements PubTypeInfoDriver {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private ConstRegionService constRegionService;

  @Override
  public String constructBriefDesc(PubTypeInfoBean typeInfo, Locale locale, Long countryId, String publishDate)
      throws Exception {
    String briefDesc = "";
    try {
      Map<String, String> result = buildData(typeInfo, locale, countryId, publishDate);
      BriefFormatter formatter = new BriefFormatter(locale, result);
      briefDesc = formatter.format(BriefFormatter.THEISBRIEF_PATTERN);
    } catch (Exception e) {
      logger.error("学位成果构建briefDesc参数失败，typeInfo={}", typeInfo, e);
    }
    return briefDesc;
  }

  private Map<String, String> buildData(PubTypeInfoBean typeInfo, Locale locale, Long countryId, String publishDate)
      throws Exception {
    Map<String, String> countryInfo = null;
    ThesisInfoBean a = (ThesisInfoBean) typeInfo;
    Map<String, String> map = new HashMap<String, String>();
    if (a != null) {
      map.put("department".toUpperCase(), a.getDepartment());
      map.put("degree".toUpperCase(), buildDegree(a.getDegree(), locale));
      map.put("issuing_authority".toUpperCase(), a.getIssuingAuthority());
      if (!NumberUtils.isNullOrZero(countryId)) {
        countryInfo = constRegionService.buildCountryAndCityName(countryId, locale);
      }
      map.put("country_name".toUpperCase(),
          !CollectionUtils.isEmpty(countryInfo) ? countryInfo.get("countryName") : "");
    }
    map.put("publish_date".toUpperCase(), DataFormatUtils.parseDate(a.getDefenseDate(), "-"));
    return map;
  }

  private String buildDegree(PubThesisDegreeEnum degree, Locale locale) {
    if (Locale.CHINA.equals(locale)) {
      return degree.getZhDescription();
    } else {
      return degree.getEnDescription();
    }
  }

}
