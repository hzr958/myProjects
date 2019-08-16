package com.smate.center.task.v8pub.strategy;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smate.center.task.utils.DataFormatUtils;
import com.smate.core.base.utils.pub.BriefFormatter;
import com.smate.web.v8pub.dom.JournalInfoBean;
import com.smate.web.v8pub.dom.PubTypeInfoBean;

/**
 * 构建期刊
 * 
 * @author YJ
 *
 *         2018年8月15日
 */
public class ConstructJournalInfoBean implements PubTypeInfoDriver {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public String constructBriefDesc(PubTypeInfoBean typeInfo, Locale locale, Long countryId, String publishDate)
      throws Exception {
    String briefDesc = "";
    try {
      Map<String, String> result = buildData(typeInfo, publishDate);
      BriefFormatter formatter = new BriefFormatter(locale, result);
      briefDesc = formatter.format(BriefFormatter.JOURNALBRIEF_PATTERN);
    } catch (Exception e) {
      logger.error("会议成果构建briefDesc参数失败，typeInfo={}", typeInfo, e);
    }
    return briefDesc;
  }

  private Map<String, String> buildData(PubTypeInfoBean typeInfo, String publishDate) {
    JournalInfoBean a = (JournalInfoBean) typeInfo;
    Map<String, String> map = new HashMap<String, String>();
    if (a != null) {
      map.put("name".toUpperCase(), a.getName());
      map.put("volume_no".toUpperCase(), a.getVolumeNo());
      map.put("issue".toUpperCase(), a.getIssue());
      if (StringUtils.isNotBlank(a.getPageNumber())) {
        map.put("page_number".toUpperCase(), a.getPageNumber());
      }
    }
    map.put("publish_date".toUpperCase(), DataFormatUtils.parseDate(publishDate, "-"));
    return map;
  }

}
