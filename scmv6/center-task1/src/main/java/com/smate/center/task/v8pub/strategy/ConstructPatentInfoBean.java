package com.smate.center.task.v8pub.strategy;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smate.center.task.utils.DataFormatUtils;
import com.smate.core.base.utils.pub.BriefFormatter;
import com.smate.web.v8pub.dom.PatentInfoBean;
import com.smate.web.v8pub.dom.PubTypeInfoBean;

/**
 * 专利
 * 
 * @author YJ
 *
 *         2018年8月15日
 */
public class ConstructPatentInfoBean implements PubTypeInfoDriver {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public String constructBriefDesc(PubTypeInfoBean typeInfo, Locale locale, Long countryId, String publishDate)
      throws Exception {
    String briefDesc = "";
    try {
      Map<String, String> result = buildData(typeInfo, locale, countryId, publishDate);
      BriefFormatter formatter = new BriefFormatter(locale, result);
      briefDesc = formatter.format(BriefFormatter.PATENTBRIEF_PATTERN);
    } catch (Exception e) {
      // 不抛出异常
      logger.error("专利成果构建briefDesc参数失败，typeInfo={}", typeInfo, e);
    }
    return briefDesc;
  }

  private Map<String, String> buildData(PubTypeInfoBean typeInfo, Locale locale, Long countryId, String publishDate)
      throws Exception {
    PatentInfoBean a = (PatentInfoBean) typeInfo;
    Map<String, String> map = new HashMap<String, String>();
    if (a != null) {
      if (a.getStatus() != null && a.getStatus() == 0) {
        // 申请状态
        map.put("start_date".toUpperCase(), DataFormatUtils.parseDate(a.getApplicationDate(), "-"));
      } else {
        map.put("start_date".toUpperCase(), DataFormatUtils.parseDate(a.getStartDate(), "-"));
      }
      // 签发机构/发证单位
      map.put("issuing_authority".toUpperCase(), a.getIssuingAuthority());
      map.put("application_no".toUpperCase(), a.getApplicationNo());
      map.put("country_name".toUpperCase(), "");
    }
    return map;
  }

}
