package com.smate.center.task.v8pub.strategy;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.smate.center.task.service.sns.quartz.ConstRegionService;
import com.smate.center.task.utils.DataFormatUtils;
import com.smate.core.base.pub.enums.PubConferencePaperTypeEnum;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.pub.BriefFormatter;
import com.smate.web.v8pub.dom.ConferencePaperBean;
import com.smate.web.v8pub.dom.PubTypeInfoBean;

/**
 * 构建会议
 * 
 * @author YJ
 *
 *         2018年8月15日
 */
public class ConstructConferencePaperBean implements PubTypeInfoDriver {

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
      briefDesc = formatter.format(BriefFormatter.CONFPAPERBRIEF_PATTERN);
    } catch (Exception e) {
      // 不抛出异常
      logger.error("会议成果构建briefDesc参数失败，typeInfo={}", typeInfo, e);
    }
    return briefDesc;
  }

  private Map<String, String> buildData(PubTypeInfoBean typeInfo, Locale locale, Long countryId, String publishDate)
      throws Exception {
    Map<String, String> countryInfo = null;
    ConferencePaperBean a = (ConferencePaperBean) typeInfo;
    Map<String, String> map = new HashMap<String, String>();
    if (a != null) {
      map.put("conf_name".toUpperCase(), a.getName());
      // 这里的日期格式必须是以 / 隔开的
      map.put("start_date".toUpperCase(), DataFormatUtils.parseDate(a.getStartDate(), "/"));
      map.put("end_date".toUpperCase(), DataFormatUtils.parseDate(a.getEndDate(), "/"));
      if (StringUtils.isNotBlank(a.getPageNumber())) {
        map.put("page_number".toUpperCase(), a.getPageNumber());
      }
      if (!NumberUtils.isNullOrZero(countryId)) {
        countryInfo = constRegionService.buildCountryAndCityName(countryId, locale);
      }
      map.put("city".toUpperCase(), CollectionUtils.isEmpty(countryInfo) ? "" : countryInfo.get("cityName"));
      map.put("confType".toUpperCase(), getPaperType(locale, a.getPaperType()));
    }
    // 这里的日期格式必须是以 - 隔开的
    map.put("publish_date".toUpperCase(), DataFormatUtils.parseDate(publishDate, "-"));
    return map;
  }

  /**
   * 获取会议类别名称
   * 
   * @param locale
   * @param paperType
   * @return
   */
  private String getPaperType(Locale locale, PubConferencePaperTypeEnum paperType) {
    if (Locale.CHINA.equals(locale)) {
      return paperType.getZhDescription();
    } else {
      return paperType.getEnDescription();
    }
  }

}
