package com.smate.center.task.v8pub.strategy;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smate.center.task.utils.DataFormatUtils;
import com.smate.center.task.utils.data.PubLocaleUtils;
import com.smate.core.base.utils.pub.BriefFormatter;
import com.smate.web.v8pub.dom.BookInfoBean;
import com.smate.web.v8pub.dom.PubTypeInfoBean;

/**
 * 构建书籍著作
 * 
 * @author YJ
 *
 *         2018年8月15日
 */
public class ConstructBookInfoBean implements PubTypeInfoDriver {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public String constructBriefDesc(PubTypeInfoBean typeInfo, Locale locale, Long countryId, String publishDate) {
    String briefDesc = "";
    try {
      Map<String, String> result = buildBookData(typeInfo, publishDate);
      // 根据书名去判断中英文，而不是章节名，因为章节名存在数字的情况
      BriefFormatter formatter = new BriefFormatter(PubLocaleUtils.getLocale(result.get("NAME")), result);
      briefDesc = formatter.format(BriefFormatter.BOOKBRIEF_PATTERN);
    } catch (Exception e) {
      // 不抛出异常
      logger.error("书籍成果构建briefDesc参数失败，typeInfo={}", typeInfo, e);
    }
    return briefDesc;
  }

  /**
   * 构建书籍/著作数据
   * 
   * @param pub
   * @return
   */
  private Map<String, String> buildBookData(PubTypeInfoBean typeInfo, String publishDate) {
    BookInfoBean a = (BookInfoBean) typeInfo;
    Map<String, String> map = new HashMap<String, String>();
    if (a != null) {
      map.put("publisher".toUpperCase(), a.getPublisher());
      if (a.getTotalPages() == null || a.getTotalPages() == 0) {
        map.put("total_pages".toUpperCase(), "");
      } else {
        map.put("total_pages".toUpperCase(), a.getTotalPages() + "");
      }
      map.put("language".toUpperCase(), a.getLanguage());
    }
    map.put("publish_date".toUpperCase(), DataFormatUtils.parseDate(publishDate, "-"));
    return map;
  }

}
