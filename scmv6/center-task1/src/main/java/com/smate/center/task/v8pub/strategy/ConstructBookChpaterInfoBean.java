package com.smate.center.task.v8pub.strategy;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.smate.center.task.service.sns.quartz.ConstRegionService;
import com.smate.center.task.utils.DataFormatUtils;
import com.smate.center.task.utils.data.PubLocaleUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.pub.BriefFormatter;
import com.smate.web.v8pub.dom.BookInfoBean;
import com.smate.web.v8pub.dom.PubTypeInfoBean;

/**
 * 构建书籍章节
 * 
 * @author YJ
 *
 *         2018年8月15日
 */
public class ConstructBookChpaterInfoBean implements PubTypeInfoDriver {

  private Logger logger = LoggerFactory.getLogger(getClass());


  @Autowired
  private ConstRegionService constRegionService;

  @Override
  public String constructBriefDesc(PubTypeInfoBean typeInfo, Locale locale, Long countryId, String publishDate) {
    String briefDesc = "";
    try {
      Map<String, String> result = null;
      // 书籍章节
      result = buildChpaterData(typeInfo, countryId, publishDate);
      BriefFormatter formatter = new BriefFormatter(locale, result);
      briefDesc = formatter.format(BriefFormatter.BOOKCHPATERBRIEF_PATTERN);
    } catch (Exception e) {
      // 不抛出异常
      logger.error("书籍成果构建briefDesc参数失败，typeInfo={}", typeInfo, e);
    }
    return briefDesc;
  }

  /**
   * 构建书籍章节数据
   * 
   * @param pub
   * @return
   * @throws Exception
   */
  private Map<String, String> buildChpaterData(PubTypeInfoBean typeInfo, Long countryId, String publishDate)
      throws Exception {
    Map<String, String> countryInfo = null;
    BookInfoBean a = (BookInfoBean) typeInfo;
    Map<String, String> map = new HashMap<String, String>();
    if (a != null) {
      map.put("name".toUpperCase(), a.getName());
      map.put("editors".toUpperCase(), a.getEditors());
      map.put("publisher".toUpperCase(), a.getPublisher());
      if (StringUtils.isNotBlank(a.getPageNumber())) {
        map.put("page_number".toUpperCase(), a.getPageNumber());
      }
      if (!NumberUtils.isNullOrZero(countryId)) {
        countryInfo = constRegionService.buildCountryAndCityName(countryId, PubLocaleUtils.getLocale(a.getName()));
      }
      map.put("country_name".toUpperCase(),
          !CollectionUtils.isEmpty(countryInfo) ? countryInfo.get("countryName") : "");
    }
    map.put("publish_date".toUpperCase(), DataFormatUtils.parseDate(publishDate, "-"));
    return map;
  }

}
