package com.smate.web.v8pub.service.handler.assembly.pubtypebean;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.smate.core.base.pub.enums.PublicationTypeEnum;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.pub.BriefFormatter;
import com.smate.web.v8pub.dom.BookInfoBean;
import com.smate.web.v8pub.dom.PubTypeInfoBean;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.region.ConstRegionService;
import com.smate.web.v8pub.utils.DataFormatUtils;
import com.smate.web.v8pub.utils.PubLocaleUtils;

/**
 * 成果类别 1.书/著作 BOOK 2.书籍章节 BOOK_CHAPTER 构建成果详情的类别对象
 * 
 * @author YJ
 * 
 *         2018年7月31日
 */
@Transactional(rollbackFor = Exception.class)
public class ASBookInfoSaveImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private ConstRegionService constRegionService;

  @Override
  public void checkSourcesParameter(PubDTO pub) throws PubHandlerAssemblyException {

  }

  @Override
  public void checkRebuildParameter(PubDTO pub) throws PubHandlerAssemblyException {}

  private PubTypeInfoBean constructParams(PubDTO pub) {
    BookInfoBean a = JacksonUtils.jsonObject(pub.pubTypeInfo.toJSONString(), BookInfoBean.class);
    if (a != null) {
      a.setISBN(StringUtils.substring(a.getISBN(), 0, 80));
      a.setPageNumber(StringUtils.substring(a.getPageNumber(), 0, 100));
      a.setName(StringUtils.substring(a.getName(), 0, 500));
      a.setSeriesName((StringUtils.substring(a.getSeriesName(), 0, 1000)));
      a.setEditors(XmlUtil.subStr500char(a.getEditors()));
      a.setChapterNo(StringUtils.substring(a.getChapterNo(), 0, 40));
      a.setPublisher(StringUtils.substring(a.getPublisher(), 0, 500));
    }
    return a;
  }

  @Override
  public Map<String, String> excute(PubDTO pub) throws PubHandlerAssemblyException {
    // 构建pubTypeInfoBean
    if (pub.pubType.intValue() == PublicationTypeEnum.BOOK
        || pub.pubType.intValue() == PublicationTypeEnum.BOOK_CHAPTER) {
      try {
        pub.pubTypeInfoBean = constructParams(pub);

      } catch (Exception e) {
        logger.error("构建书籍类别对象失败，pubTypeInfo={}", pub.pubTypeInfo, e);
        throw new PubHandlerAssemblyException(this.getClass().getSimpleName() + "构建书籍类别对象失败！", e);
      }
      // 构建briefDesc参数
      String briefDesc = "";
      try {
        Map<String, String> result = null;
        if (pub.pubType.intValue() == PublicationTypeEnum.BOOK) {
          // 书籍著作
          result = buildBookData(pub);
          // 根据书名去判断中英文，而不是章节名，因为章节名存在数字的情况
          BriefFormatter formatter = new BriefFormatter(PubLocaleUtils.getLocale(result.get("NAME")), result);
          briefDesc = formatter.format(BriefFormatter.BOOKBRIEF_PATTERN);
        }
        if (pub.pubType.intValue() == PublicationTypeEnum.BOOK_CHAPTER) {
          // 书籍章节
          result = buildChpaterData(pub);
          BriefFormatter formatter = new BriefFormatter(PubLocaleUtils.getLocale(pub.title), result);
          briefDesc = formatter.format(BriefFormatter.BOOKCHPATERBRIEF_PATTERN);
        }
        pub.briefDesc = briefDesc;
      } catch (Exception e) {
        // 不抛出异常
        logger.error("书籍成果构建briefDesc参数失败，pubTypeInfo={}", pub.pubTypeInfo, e);
        throw new PubHandlerAssemblyException(this.getClass().getSimpleName() + "书籍成果构建briefDesc参数失败！", e);
      }
    }

    return null;
  }

  /**
   * 构建书籍/著作数据
   * 
   * @param pub
   * @return
   */
  private Map<String, String> buildBookData(PubDTO pub) {
    BookInfoBean a = (BookInfoBean) pub.pubTypeInfoBean;
    Map<String, String> map = new HashMap<String, String>();
    if (a != null) {
      map.put("publisher".toUpperCase(), a.getPublisher());
      if (a.getTotalPages() == null || a.getTotalPages() == 0) {
        map.put("total_pages".toUpperCase(), "");
      } else {
        map.put("total_pages".toUpperCase(), a.getTotalPages() + "");
      }
      map.put("publish_date".toUpperCase(), DataFormatUtils.parseDate(pub.publishDate, "-"));
      map.put("language".toUpperCase(), a.getLanguage());
    }
    return map;
  }

  /**
   * 构建书籍章节数据
   *
   * @param pub
   * @return
   */
  private Map<String, String> buildChpaterData(PubDTO pub) {
    Map<String, String> countryInfo = null;
    BookInfoBean a = (BookInfoBean) pub.pubTypeInfoBean;
    Map<String, String> map = new HashMap<String, String>();
    if (a != null) {
      map.put("name".toUpperCase(), a.getName());
      map.put("editors".toUpperCase(), a.getEditors());
      map.put("publisher".toUpperCase(), a.getPublisher());
      if (StringUtils.isNotEmpty(a.getPageNumber())) {
        map.put("page_number".toUpperCase(), a.getPageNumber());
      }
      if (!NumberUtils.isNullOrZero(pub.countryId)) {
        countryInfo = constRegionService.buildCountryAndCityName(pub.countryId, PubLocaleUtils.getLocale(a.getName()));
      }
      map.put("country_name".toUpperCase(),
          !CollectionUtils.isEmpty(countryInfo) ? countryInfo.get("countryName") : "");
      map.put("publish_date".toUpperCase(), DataFormatUtils.parseDate(pub.publishDate, "-"));
    }
    return map;
  }

}
