package com.smate.sie.center.task.pdwh.json.pubtype.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.LocaleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.single.enums.pub.PublicationEnterFormEnum;
import com.smate.core.base.pub.enums.PublicationTypeEnum;
import com.smate.core.base.utils.dao.consts.Sie6ConstDictionaryDao;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.string.StringUtils;
import com.smate.sie.center.task.pdwh.brief.BriefDriverFactory;
import com.smate.sie.center.task.pdwh.brief.IBriefDriver;
import com.smate.sie.center.task.pdwh.json.service.process.PubHandlerProcessService;
import com.smate.sie.core.base.utils.date.DateUtils;
import com.smate.sie.core.base.utils.json.pub.BriefFormatter;
import com.smate.sie.core.base.utils.pub.dom.BookInfoBean;
import com.smate.sie.core.base.utils.pub.dom.PubTypeInfoBean;
import com.smate.sie.core.base.utils.pub.exception.PubHandlerProcessException;
import com.smate.sie.core.base.utils.pub.service.PubJsonDTO;

/**
 * 著作/书籍章节 构建成果详情的类别对象
 * 
 * @author ZSJ
 *
 * @date 2019年1月31日
 */
@Transactional(rollbackFor = Exception.class)
public class PubBookInfoDealServiceImpl implements PubHandlerProcessService {

  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private BriefDriverFactory briefDriverFactory;
  @Autowired
  private Sie6ConstDictionaryDao sie6ConstDictionaryDao;

  private PubTypeInfoBean constructParams(PubJsonDTO pub) throws IOException {
    BookInfoBean a = JacksonUtils.jsonObject(pub.pubTypeInfo.toJSONString(), BookInfoBean.class);
    if (a != null) {
      Map<String, String> map = JacksonUtils.json2Map(pub.pubTypeInfo.toJSONString());
      // 文件导入时，JacksonUtils.jsonObject(pub.pubTypeInfo.toJSONString(),
      // BookInfoBean.class) 转为bean时isbn值会丢失
      String isbn = a.getISBN();
      if (StringUtils.isBlank(isbn)) {
        isbn = map.get("iSBN");
      }
      a.setISBN(StringUtils.substring(isbn, 0, 20));
      a.setName(StringUtils.substring(a.getName(), 0, 100)); // 书名
      a.setPublisher(StringUtils.substring(a.getPublisher(), 0, 100));
      if (pub.pubTypeCode == 2) {
        if ("1".equals(a.getLanguageCode())) {
          a.setLanguageName("中文");
        } else {
          a.setLanguageName("外文");
        }
        if ("1".equals(a.getPublishStatusCode())) {
          a.setPublishStatusName("已出版");
        } else {
          a.setPublishStatusName("待出版");
        }
        if (StringUtils.isNotBlank(a.getTypeCode())) {
          String name = sie6ConstDictionaryDao.findZhNameByCategoryAndCode("pub_book_type", a.getTypeCode());
          a.setTypeName(name);
        }
      }
    }
    return a;
  }

  @Override
  public void checkSourcesParameter(PubJsonDTO pub) throws PubHandlerProcessException {
    // TODO Auto-generated method stub

  }

  @Override
  public void checkRebuildParameter(PubJsonDTO pub) throws PubHandlerProcessException {
    // TODO Auto-generated method stub

  }

  @Override
  public Map<String, Object> excute(PubJsonDTO pub) throws PubHandlerProcessException {
    Map<String, Object> resultMap = new HashMap<String, Object>();
    // 构建pubTypeInfoBean
    if (pub.pubTypeCode.intValue() == PublicationTypeEnum.BOOK
        || pub.pubTypeCode.intValue() == PublicationTypeEnum.BOOK_CHAPTER) {
      try {
        pub.pubTypeInfoBean = constructParams(pub);

      } catch (Exception e) {
        logger.error("构建书籍类别对象失败，pubTypeInfo={}", pub.pubTypeInfo, e);
        throw new PubHandlerProcessException(this.getClass().getSimpleName() + "构建书籍类别对象失败！", e);
      }
      // 文件导入，在预览页面就已经构造好来源，故这里做个判断，避免重复工作，
      if (!pub.isImport) {
        try {
          IBriefDriver briefDriver = null;
          Map<String, String> result = null;
          if (pub.pubTypeCode.intValue() == PublicationTypeEnum.BOOK) {
            // 书籍著作
            result = buildBookData(pub);
            briefDriver = briefDriverFactory.getDriver(PublicationEnterFormEnum.SCHOLAR, pub.pubTypeCode);
          }
          if (pub.pubTypeCode.intValue() == PublicationTypeEnum.BOOK_CHAPTER) {
            // 书籍章节
            result = buildChpaterData(pub);
            briefDriver = briefDriverFactory.getDriver(PublicationEnterFormEnum.SCHOLAR, pub.pubTypeCode);
          }
          String pattern = briefDriver.getPattern();
          BriefFormatter formatter = new BriefFormatter(LocaleUtils.toLocale("zh_CN"), result);
          String briefDesc = formatter.format(pattern);
          pub.briefDesc = briefDesc;
        } catch (Exception e) {
          // 不抛出异常
          logger.error("书籍成果构建briefDesc参数失败，pubTypeInfo={}", pub.pubTypeInfo, e);
          throw new PubHandlerProcessException(this.getClass().getSimpleName() + "书籍成果构建briefDesc参数失败！", e);
        }
      }
    }
    return resultMap;
  }

  /**
   * 构建著作数据
   * 
   * @param pub
   * @return
   */
  private Map<String, String> buildBookData(PubJsonDTO pub) {
    BookInfoBean a = (BookInfoBean) pub.pubTypeInfoBean;
    Map<String, String> map = new HashMap<String, String>();
    if (a != null) {
      map.put("publisher".toUpperCase(), a.getPublisher());
      String publishDate = pub.publishDate;
      if (StringUtils.isNotEmpty(publishDate)) {
        String[] dates = DateUtils.splitToYearMothDayByStrUseToSie(pub.publishDate);
        map.put("publish_date".toUpperCase(), DateUtils.parseDate(dates[0], dates[1], dates[2], "-"));
      }
      map.put("language".toUpperCase(), a.getLanguageName());
    }
    return map;
  }

  /**
   * 构建书籍章节数据
   *
   * @param pub
   * @return
   */
  private Map<String, String> buildChpaterData(PubJsonDTO pub) {
    BookInfoBean a = (BookInfoBean) pub.pubTypeInfoBean;
    Map<String, String> map = new HashMap<String, String>();
    if (a != null) {
      map.put("book_title".toUpperCase(), a.getName());
      map.put("publisher".toUpperCase(), a.getPublisher());
      if (StringUtils.isNotEmpty(a.getStartPage()) || StringUtils.isNotEmpty(a.getEndPage())) {
        map.put("start_page".toUpperCase(), a.getStartPage());
        map.put("end_page".toUpperCase(), a.getEndPage());
      } else {
        map.put("start_page".toUpperCase(), a.getArticleNo());
      }
      String publishDate = pub.publishDate;
      if (StringUtils.isNotEmpty(publishDate)) {
        String[] dates = DateUtils.splitToYearMothDayByStrUseToSie(pub.publishDate);
        map.put("publish_date".toUpperCase(), DateUtils.parseDate(dates[0], dates[1], dates[2], "-"));
      }
    }
    return map;
  }

  @Override
  public Integer getPubType() {
    return PublicationTypeEnum.BOOK; // 包括书籍章节
  }

}
