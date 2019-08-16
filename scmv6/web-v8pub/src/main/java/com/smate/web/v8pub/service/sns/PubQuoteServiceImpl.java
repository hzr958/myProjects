package com.smate.web.v8pub.service.sns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.util.HtmlUtils;

import com.smate.core.base.pub.enums.PublicationTypeEnum;
import com.smate.core.base.pub.model.PubMemberPO;
import com.smate.core.base.pub.util.PubDetailVoUtil;
import com.smate.web.v8pub.dao.sns.PubSnsDAO;
import com.smate.web.v8pub.dao.sns.QuoteTemplateInfoDao;
import com.smate.web.v8pub.dom.AwardsInfoBean;
import com.smate.web.v8pub.dom.BookInfoBean;
import com.smate.web.v8pub.dom.ConferencePaperBean;
import com.smate.web.v8pub.dom.JournalInfoBean;
import com.smate.web.v8pub.dom.PatentInfoBean;
import com.smate.web.v8pub.dom.PubDetailDOM;
import com.smate.web.v8pub.dom.ThesisInfoBean;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.sns.QuoteTemplateInfo;
import com.smate.web.v8pub.service.region.ConstRegionService;
import com.smate.web.v8pub.utils.PubLocaleUtils;
import com.smate.web.v8pub.utils.PubMemberNameUtils;
import com.smate.web.v8pub.vo.PubQuoteVO;

import freemarker.template.Configuration;

/**
 * 成果引用服务
 * 
 * @author lhd
 */
@Service("pubQuoteService")
@Transactional(rollbackFor = Exception.class)
public class PubQuoteServiceImpl implements PubQuoteService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private final String encoding = "utf-8";

  @Autowired
  private PubSnsDAO pubSnsDAO;
  @Autowired
  private PubMemberService pubMemberService;
  @Autowired
  private QuoteTemplateInfoDao quoteTemplateInfoDao;
  @Autowired
  private PubSnsDetailService pubSnsDetailService;

  @Autowired
  private ConstRegionService constRegionService;

  @Resource(name = "quoteFreemarkerConfiguration")
  private Configuration quoteFreemarkerConfiguration;

  @Override
  public List<PubQuoteVO> findPubQuote(Long pubId) throws ServiceException {
    try {

      List<PubQuoteVO> pubQuoteList = new ArrayList<PubQuoteVO>();
      PubDetailDOM pubDetail = pubSnsDetailService.get(pubId);
      if (pubDetail == null) {
        return pubQuoteList;
      }
      List<QuoteTemplateInfo> quoteList =
          this.quoteTemplateInfoDao.findPubTypeTemplate(QuoteTemplateInfo.TYPE_PUB, pubDetail.getPubType());
      if (CollectionUtils.isEmpty(quoteList)) {
        logger.warn("成果引用模板，没有找到pubType=" + pubDetail.getPubType() + "的成果模板,将查找成果的默认模板！");
        quoteList = this.quoteTemplateInfoDao.findTypeTemplate(QuoteTemplateInfo.TYPE_PUB);
      }
      if (CollectionUtils.isEmpty(quoteList)) {
        logger.error("成果引用模板没有数据，请检查表QUOTE_TEMPLATE_INFO，type=" + QuoteTemplateInfo.TYPE_PUB + ",pubType="
            + pubDetail.getPubType());
        throw new ServiceException("成果引用模板没有数据，请检查表QUOTE_TEMPLATE_INFO，type=" + QuoteTemplateInfo.TYPE_PUB + ",pubType="
            + pubDetail.getPubType());
      }

      Map<String, Object> paramMap = this.getTempParams(pubDetail);
      for (QuoteTemplateInfo templateInfo : quoteList) {
        String quoteContent = FreeMarkerTemplateUtils.processTemplateIntoString(
            quoteFreemarkerConfiguration.getTemplate(
                templateInfo.getTemplateName() + "_" + LocaleContextHolder.getLocale().toString() + ".ftl", encoding),
            paramMap);
        String quoteName = "";
        if ("en".equalsIgnoreCase(LocaleContextHolder.getLocale().getLanguage())) {
          quoteName = templateInfo.getQuoteEnName();
        } else {
          quoteName = templateInfo.getQuoteZhName();
        }
        PubQuoteVO pubQuote = new PubQuoteVO();
        pubQuote.setQuoteName(quoteName);
        pubQuote.setQuoteContent(quoteContent);
        pubQuoteList.add(pubQuote);
      }
      return pubQuoteList;
    } catch (Exception e) {
      logger.error("生成成果引用模板出现错误，pubId=" + pubId, e);
      throw new ServiceException("生成成果引用模板出现错误，pubId=" + pubId, e);
    }
  }

  /**
   * 获取模板参数
   * 
   * @param pubDetail
   * @return
   * @throws Exception
   */
  private Map<String, Object> getTempParams(PubDetailDOM pubDetail) throws Exception {
    Integer pubType = pubDetail.getPubType();
    if (pubType == null) {
      logger.error("该成果的pubType为空，请检查数据。pubId=" + pubDetail.getPubId());
      throw new ServiceException("该成果的pubType为空，请检查数据。pubId=" + pubDetail.getPubId());
    }

    switch (pubType.intValue()) {
      case PublicationTypeEnum.AWARD:
        return this.getAwardParams(pubDetail);
      case PublicationTypeEnum.BOOK:
        return this.getBookParams(pubDetail);
      case PublicationTypeEnum.CONFERENCE_PAPER:
        return this.getConferenceParams(pubDetail);
      case PublicationTypeEnum.JOURNAL_ARTICLE:
        return this.getJournalParams(pubDetail);
      case PublicationTypeEnum.PATENT:
        return this.getPatentParams(pubDetail);
      case PublicationTypeEnum.THESIS:
        return this.getThesisParams(pubDetail);
      case PublicationTypeEnum.BOOK_CHAPTER:
        return this.getChapterParams(pubDetail);
      default:
        return this.getOtherParams(pubDetail);
    }
  }

  /**
   * 获取奖励类型的参数
   * 
   * @param pubDetail
   * @return
   * @throws ServiceException
   */
  private Map<String, Object> getAwardParams(PubDetailDOM pubDetail) throws Exception {
    Map<String, Object> paramMap = new HashMap<String, Object>();
    paramMap = this.getPubMamberName(pubDetail.getPubId(), paramMap);
    // 获取奖励类别，等级，颁奖机构
    AwardsInfoBean infoBean = new AwardsInfoBean();
    if (pubDetail.getTypeInfo() != null) {
      infoBean = (AwardsInfoBean) pubDetail.getTypeInfo();
    }
    paramMap.put("awardGrade", getNullString(infoBean.getGrade()));
    paramMap.put("awardCategory", getNullString(infoBean.getCategory()));
    paramMap.put("issueInsName", getNullString(infoBean.getIssuingAuthority()));
    // 获取科研之友格式
    paramMap = this.getSmateQuote(pubDetail, paramMap);
    if (StringUtils.isNotBlank(infoBean.getAwardDate())) {
      String publishYear = PubDetailVoUtil.parseDateForYear(infoBean.getAwardDate());
      paramMap.put("publishYear", StringUtils.isNotBlank(publishYear) ? Integer.parseInt(publishYear) : null);
    }
    return paramMap;
  }

  /**
   * 获取书籍章节类型参数
   * 
   * @param pubDetail
   * @return
   * @throws Exception
   */
  private Map<String, Object> getBookParams(PubDetailDOM pubDetail) throws Exception {
    Map<String, Object> paramMap = new HashMap<String, Object>();
    paramMap = this.getPubMamberName(pubDetail.getPubId(), paramMap);
    // 获取科研之友格式
    paramMap = this.getSmateQuote(pubDetail, paramMap);
    Locale locale = PubLocaleUtils.getLocale(pubDetail.getTitle());
    Map countryInfo = constRegionService.buildCountryAndCityName(pubDetail.getCountryId(), locale);
    if (countryInfo.get("cityName") != null) {
      paramMap.put("cityName", countryInfo.get("cityName"));
    }
    BookInfoBean infoBean = new BookInfoBean();
    if (pubDetail.getTypeInfo() != null) {
      infoBean = (BookInfoBean) pubDetail.getTypeInfo();
    }
    paramMap.put("pageNumber", getNullString(infoBean.getPageNumber()));
    paramMap.put("publisher", getNullString(infoBean.getPublisher()));
    // 获取科研之友格式
    paramMap = this.getSmateQuote(pubDetail, paramMap);
    // 获取GBT7714-2015格式
    paramMap = this.getGBTQuote(pubDetail, paramMap);
    return paramMap;
  }

  /**
   * 获取会议论文类型参数
   * 
   * @param pubDetail
   * @return
   * @throws Exception
   */
  private Map<String, Object> getConferenceParams(PubDetailDOM pubDetail) throws Exception {
    Map<String, Object> paramMap = new HashMap<String, Object>();
    ConferencePaperBean infoBean = new ConferencePaperBean();
    if (pubDetail.getTypeInfo() != null) {
      infoBean = (ConferencePaperBean) pubDetail.getTypeInfo();
    }
    // 作者信息
    paramMap = this.getPubMamberName(pubDetail.getPubId(), paramMap);
    paramMap.put("pageNumber", getNullString(infoBean.getPageNumber()));
    paramMap.put("confName", getNullString(infoBean.getName()));
    Locale locale = PubLocaleUtils.getLocale(pubDetail.getTitle());
    Map countryInfo = constRegionService.buildCountryAndCityName(pubDetail.getCountryId(), locale);
    if (countryInfo.get("cityName") != null) {
      paramMap.put("cityName", countryInfo.get("cityName"));
    }
    if (StringUtils.isNotBlank(pubDetail.getPublishDate())) {
      String publishMonth = PubDetailVoUtil.parseDateForMonth(pubDetail.getPublishDate());
      paramMap.put("publishMonth", StringUtils.isNotBlank(publishMonth) ? Integer.parseInt(publishMonth) : null);
    }
    if (StringUtils.isNotBlank(infoBean.getOrganizer())) {
      paramMap.put("confOrganizer", infoBean.getOrganizer());
    }

    // 获取科研之友格式
    paramMap = this.getSmateQuote(pubDetail, paramMap);
    // 获取GBT7714-2015格式
    paramMap = this.getGBTQuote(pubDetail, paramMap);
    return paramMap;
  }

  /**
   * 期刊类型参数
   * 
   * @param pubDetail
   * @return
   * @throws Exception
   */
  private Map<String, Object> getJournalParams(PubDetailDOM pubDetail) throws Exception {
    Map<String, Object> paramMap = new HashMap<String, Object>();
    JournalInfoBean infoBean = new JournalInfoBean();
    if (pubDetail.getTypeInfo() != null) {
      infoBean = (JournalInfoBean) pubDetail.getTypeInfo();
    }
    // 作者信息
    paramMap = this.getPubMamberName(pubDetail.getPubId(), paramMap);
    paramMap.put("pageNumber", getNullString(infoBean.getPageNumber()));
    paramMap.put("volume", getNullString(infoBean.getVolumeNo()));
    paramMap.put("issue", getNullString(infoBean.getIssue()));
    paramMap.put("journalName", getNullString(infoBean.getName()));
    // 获取科研之友格式
    paramMap = this.getSmateQuote(pubDetail, paramMap);
    // 获取GBT7714-2015格式
    paramMap = this.getGBTQuote(pubDetail, paramMap);
    return paramMap;
  }

  /**
   * 专利类型参数
   * 
   * @param pubDetail
   * @return
   * @throws Exception
   */
  private Map<String, Object> getPatentParams(PubDetailDOM pubDetail) throws Exception {
    Map<String, Object> paramMap = new HashMap<String, Object>();
    // 作者信息
    paramMap = this.getPubMamberName(pubDetail.getPubId(), paramMap);
    PatentInfoBean infoBean = new PatentInfoBean();
    if (pubDetail.getTypeInfo() != null) {
      infoBean = (PatentInfoBean) pubDetail.getTypeInfo();
    }
    String patentNo = infoBean.getApplicationNo();
    if (StringUtils.isBlank(patentNo)) {
      patentNo = infoBean.getPublicationOpenNo();
    }
    paramMap.put("patentNo", getNullString(patentNo));
    // 获取科研之友格式
    paramMap = this.getSmateQuote(pubDetail, paramMap);
    // 获取GBT7714-2015格式
    paramMap = this.getGBTQuote(pubDetail, paramMap);
    return paramMap;
  }

  /**
   * 学位论文类型参数
   * 
   * @param pubDetail
   * @return
   * @throws Exception
   */
  private Map<String, Object> getThesisParams(PubDetailDOM pubDetail) throws Exception {
    Map<String, Object> paramMap = new HashMap<String, Object>();
    ThesisInfoBean infoBean = new ThesisInfoBean();
    if (pubDetail.getTypeInfo() != null) {
      infoBean = (ThesisInfoBean) pubDetail.getTypeInfo();
    }
    // 作者信息
    paramMap = this.getPubMamberName(pubDetail.getPubId(), paramMap);
    paramMap.put("programeName", getNullString(infoBean.getDegree().getZhDescription()));
    paramMap.put("department", getNullString(infoBean.getDepartment()));
    paramMap.put("issue_org", getNullString(infoBean.getIssuingAuthority()));
    Locale locale = PubLocaleUtils.getLocale(pubDetail.getTitle());
    Map countryInfo = constRegionService.buildCountryAndCityName(pubDetail.getCountryId(), locale);
    if (StringUtils.isNotBlank(countryInfo.get("countryName").toString())) {
      paramMap.put("countryName", countryInfo.get("countryName"));
    }
    if (StringUtils.isNotBlank(infoBean.getDefenseDate())) {
      String publishYear = PubDetailVoUtil.parseDateForYear(infoBean.getDefenseDate());
      paramMap.put("publishYear", StringUtils.isNotBlank(publishYear) ? Integer.parseInt(publishYear) : null);
      String publishMonth = PubDetailVoUtil.parseDateForMonth(infoBean.getDefenseDate());
      paramMap.put("publishMonth", StringUtils.isNotBlank(publishMonth) ? Integer.parseInt(publishMonth) : null);
    }

    // 获取科研之友格式
    paramMap = this.getSmateQuote(pubDetail, paramMap);
    // 获取GBT7714-2015格式
    paramMap = this.getGBTQuote(pubDetail, paramMap);
    return paramMap;
  }

  /**
   * 书籍章节类型参数
   * 
   * @param pubDetail
   * @return
   * @throws Exception
   */
  private Map<String, Object> getChapterParams(PubDetailDOM pubDetail) throws Exception {
    Map<String, Object> paramMap = new HashMap<String, Object>();
    BookInfoBean infoBean = new BookInfoBean();
    if (pubDetail.getTypeInfo() != null) {
      infoBean = (BookInfoBean) pubDetail.getTypeInfo();
    }
    // 作者信息
    paramMap = this.getPubMamberName(pubDetail.getPubId(), paramMap);
    paramMap.put("pageNumber", getNullString(infoBean.getPageNumber()));
    Locale locale = PubLocaleUtils.getLocale(pubDetail.getTitle());
    Map countryInfo = constRegionService.buildCountryAndCityName(pubDetail.getCountryId(), locale);
    if (countryInfo.get("cityName") != null) {
      paramMap.put("countryName", String.valueOf(countryInfo.get("cityName")));
    } else {
      paramMap.put("countryName", String.valueOf(countryInfo.get("countryName")));
    }
    if (StringUtils.isNotBlank(pubDetail.getPublishDate())) {
      paramMap.put("publishYear", Integer.valueOf(PubDetailVoUtil.parseDateForYear(pubDetail.getPublishDate())));
    }
    paramMap.put("bookTitle", getNullString(infoBean.getName()));
    paramMap.put("editors", getNullString(infoBean.getEditors()));
    paramMap.put("publisher", getNullString(infoBean.getPublisher()));
    // 获取科研之友格式
    paramMap = this.getSmateQuote(pubDetail, paramMap);
    // 获取GBT7714-2015格式
    paramMap = this.getGBTQuote(pubDetail, paramMap);
    return paramMap;
  }

  private Map<String, Object> getOtherParams(PubDetailDOM pubDetail) throws Exception {
    Map<String, Object> paramMap = new HashMap<String, Object>();
    paramMap = this.getSmateQuote(pubDetail, paramMap);
    return paramMap;
  }

  /**
   * 获取成果成员名字
   * 
   * @param pubId
   * @param paramMap
   * @return
   * @throws ServiceException
   */
  private Map<String, Object> getPubMamberName(Long pubId, Map<String, Object> paramMap) throws Exception {
    List<PubMemberPO> pubMemberList = this.pubMemberService.findByPubId(pubId);
    String pubMemberMLAname = PubMemberNameUtils.getMLAname(pubMemberList);
    paramMap.put("pubMemberMLAname", getNullString(pubMemberMLAname));
    String pubMemberAPAname = PubMemberNameUtils.getAPAname(pubMemberList);
    paramMap.put("pubMemberAPAname", getNullString(pubMemberAPAname));
    String pubMemberChicagoName = PubMemberNameUtils.getChicagoName(pubMemberList);
    paramMap.put("pubMemberChicagoName", getNullString(pubMemberChicagoName));
    return paramMap;
  }

  /**
   * 获取科研之友格式
   * 
   * @param pubDetail
   * @param paramMap
   * @return
   */
  private Map<String, Object> getSmateQuote(PubDetailDOM pubDetail, Map<String, Object> paramMap) {
    String title = pubDetail.getTitle();
    paramMap.put("title", getNullString(title));
    if (StringUtils.isNotBlank(pubDetail.getPublishDate())) {
      paramMap.put("publishYear", Integer.valueOf(PubDetailVoUtil.parseDateForYear(pubDetail.getPublishDate())));
    }
    String authorsName = pubDetail.getAuthorNames();
    if (StringUtils.isNotBlank(authorsName)) {
      authorsName = authorsName.replace("<strong>", "").replace("</strong>", "").replace("*", "") + ", ";
    }
    authorsName = this.getEmptyString(authorsName);
    authorsName = HtmlUtils.htmlEscape(authorsName);
    String brefDesc = this.getEmptyString(pubDetail.getBriefDesc());
    if (StringUtils.isNotBlank(brefDesc)) {
      brefDesc = brefDesc.replace('.', '-') + ".";
    }
    // 作者信息
    if (StringUtils.isNotBlank(title)) {
      if (StringUtils.isNotBlank(brefDesc)) {
        title = title + ", ";
      } else {
        title = title + ".";
      }
    }

    // 专利-公告日期或公开日期
    String publishDate = "";
    if (StringUtils.isNotBlank(pubDetail.getPublishDate())) {
      publishDate = pubDetail.getPublishDate().toString().replace(".", "-");
      publishDate += ", ";
    }
    title = this.getEmptyString(title);
    paramMap.put("zhSmateQueto", authorsName + title + brefDesc);
    paramMap.put("enSmateQueto", authorsName + title + brefDesc);
    return paramMap;
  }

  /**
   * 获取GBT7714-2015格式
   * 
   * @author lhd
   * @param pubDetail
   * @param paramMap
   * @return
   */
  private Map<String, Object> getGBTQuote(PubDetailDOM pubDetail, Map<String, Object> paramMap) {
    // 公共部分=====成果作者名, title(题名)=========================start
    List<PubMemberPO> pubMemberList;
    String pubMemberGBTname = "";
    boolean isPublisherNotNull = StringUtils.isNotBlank((String) paramMap.get("publisher"));
    try {
      pubMemberList = this.pubMemberService.findByPubId(pubDetail.getPubId());
      pubMemberGBTname = PubMemberNameUtils.getGBTname(pubMemberList);
    } catch (Exception e1) {
      logger.error("获取GBT7714-2015格式成果作者名出错：pubId={}", pubDetail.getPubId(), e1);
    }
    String title = pubDetail.getTitle();
    // ==========================公共部分========================end
    if (pubDetail.getPubType() == 5) {// 专利
      PatentInfoBean infoBean = (PatentInfoBean) pubDetail.getTypeInfo();
      if (StringUtils.isNotBlank(infoBean.getApplicationDate())) {
        String publishYear = PubDetailVoUtil.parseDateForYear(infoBean.getApplicationDate());
        String publishMonth = PubDetailVoUtil.parseDateForMonth(infoBean.getApplicationDate());
        String publishDay = PubDetailVoUtil.parseDateForDay(infoBean.getApplicationDate());
        paramMap.put("publishYear", StringUtils.isNotBlank(publishYear) ? Integer.parseInt(publishYear) : null);
        paramMap.put("publishMonth", StringUtils.isNotBlank(publishMonth) ? Integer.parseInt(publishMonth) : null);
        paramMap.put("publishDay", StringUtils.isNotBlank(publishDay) ? Integer.parseInt(publishDay) : null);
      }
      try {
        // 专利-专利题名:专利号[文献类型标识/文献载体标识].
        String pn = "";
        if (paramMap.get("patentNo") != null) {
          if (StringUtils.isNotBlank(title)) {
            pn = ":" + paramMap.get("patentNo") + "[P]. ";
          } else {
            pn = paramMap.get("patentNo") + "[P]. ";
          }
        } else {
          pn = "[P]. ";
        }
        // 专利-公告日期或公开日期
        String publishDate = "";
        if (StringUtils.isNotBlank(infoBean.getApplicationDate())) {
          publishDate = infoBean.getApplicationDate().toString().replace(".", "-");
          publishDate += ". ";
        }
        paramMap.put("zhGBTQueto", pubMemberGBTname + title + pn + publishDate);
        paramMap.put("enGBTQueto", pubMemberGBTname + title + pn + publishDate);
      } catch (Exception e) {
        logger.error("获取GBT7714-2015格式-专利-出错,pubId= " + pubDetail.getPubId(), e);
      }
    } else if (pubDetail.getPubType() == 4) {// 期刊论文
      try {
        // 标题
        if (StringUtils.isNotBlank(title)) {
          title = title + "[J]. ";
        }
        // 期刊名
        String jName = "";
        if (paramMap.get("journalName") != null) {
          jName = paramMap.get("journalName") + ". ";
        }
        // 年份, 卷(期):页码
        String pvi = "";
        if (paramMap.get("publishYear") != null) {// 年
          pvi = paramMap.get("publishYear") + "";
        }
        if (paramMap.get("volume") != null && paramMap.get("volume") != "") {// 卷
          if (StringUtils.isNotBlank(pvi)) {
            pvi += "," + paramMap.get("volume");
          } else {
            pvi += paramMap.get("volume");
          }
        }
        if (paramMap.get("issue") != null) {// 期
          if (StringUtils.isNotBlank(pvi)) {
            pvi += ",(" + paramMap.get("issue") + ")";
          } else {
            pvi += "(" + paramMap.get("issue") + ")";
          }
        }
        if (StringUtils.isNotBlank(getEmptyString((String) paramMap.get("pageNumber")))) {
          pvi += ":" + paramMap.get("pageNumber");
        }
        if (StringUtils.isNotBlank(pvi)) {
          pvi = pvi + ". ";
        }
        paramMap.put("zhGBTQueto", pubMemberGBTname + title + jName + pvi);
        paramMap.put("enGBTQueto", pubMemberGBTname + title + jName + pvi);
      } catch (Exception e) {
        logger.error("获取GBT7714-2015格式-期刊论文-出错,pubId= " + pubDetail.getPubId(), e);
      }
    } else if (pubDetail.getPubType() == 2) {// 书/著作
      try {
        if (StringUtils.isNotBlank(title)) {
          title = title + "[M]. ";
        }
        String source = "";
        boolean isCityNameNotNull = StringUtils.isNotBlank((String) paramMap.get("cityName"));

        if (isCityNameNotNull && isPublisherNotNull) {// 城市,出版社都有
          source = paramMap.get("cityName") + ":" + paramMap.get("publisher") + ". ";
        } else if (isCityNameNotNull && !isPublisherNotNull) {// 只有城市
          source = paramMap.get("cityName") + ". ";
        } else if (!isCityNameNotNull && isPublisherNotNull) {// 只有出版社
          source = paramMap.get("publisher") + ". ";
        }
        String pyear = "";
        if (paramMap.get("publishYear") != null) {
          pyear = paramMap.get("publishYear") + ". ";
        }
        paramMap.put("zhGBTQueto", pubMemberGBTname + title + source + pyear);
        paramMap.put("enGBTQueto", pubMemberGBTname + title + source + pyear);
      } catch (Exception e) {
        logger.error("获取GBT7714-2015格式-书/著作-出错,pubId= " + pubDetail.getPubId(), e);
      }
    } else if (pubDetail.getPubType() == 8) {// 学位论文
      try {
        if (StringUtils.isNotBlank(title)) {
          title += "[D]. ";
        }
        String issue_org = "";
        if (paramMap.get("issue_org") != null) {
          issue_org = paramMap.get("issue_org") + ". ";
        }
        String pyear = "";
        if (paramMap.get("publishYear") != null) {
          pyear = paramMap.get("publishYear") + ". ";
        }
        paramMap.put("zhGBTQueto", pubMemberGBTname + title + issue_org + pyear);
        paramMap.put("enGBTQueto", pubMemberGBTname + title + issue_org + pyear);
      } catch (Exception e) {
        logger.error("获取GBT7714-2015格式-学位论文-出错,pubId= " + pubDetail.getPubId(), e);
      }
    } else if (pubDetail.getPubType() == 10) {// 书籍章节
      try {
        if (StringUtils.isNotBlank(title)) {
          title += "[M]";
        }
        if (paramMap.get("editors") != null) {// 编辑.
          title += "//" + paramMap.get("editors") + ". ";
        } else {
          title += ". ";
        }
        String book_title = (String) paramMap.get("bookTitle");// 书名.
        if (book_title == null) {
          book_title = "";
        }
        if (StringUtils.isNotBlank(book_title)) {// 书名.
          book_title += ". ";
        }
        String city_publisher = "";
        // 之前的写法出现问题 所以把所有判断为空的都改了一下
        boolean isCountryNameNotNull = StringUtils.isNotBlank((String) paramMap.get("countryName"));
        if (isCountryNameNotNull && isPublisherNotNull) {
          city_publisher = paramMap.get("countryName") + ":" + paramMap.get("publisher");
        } else if (isCountryNameNotNull && !isPublisherNotNull) {
          city_publisher = paramMap.get("countryName").toString();
        } else if (!isCountryNameNotNull && isPublisherNotNull) {
          city_publisher = paramMap.get("publisher").toString();
        }
        if (StringUtils.isNotBlank(city_publisher)) {
          city_publisher += ". ";
        }
        // 年份:页码
        String pse = "";
        if (paramMap.get("publishYear") != null) {
          pse = paramMap.get("publishYear") + "";
        }
        if (StringUtils.isNotBlank((String) paramMap.get("pageNumber"))) {
          pse += ":" + paramMap.get("pageNumber");
        }
        if (StringUtils.isNotBlank(pse)) {
          pse += ". ";
        }
        paramMap.put("zhGBTQueto", pubMemberGBTname + title + book_title + city_publisher + pse);
        paramMap.put("enGBTQueto", pubMemberGBTname + title + book_title + city_publisher + pse);
      } catch (Exception e) {
        logger.error("获取GBT7714-2015格式-书籍章节-出错,pubId= " + pubDetail.getPubId(), e);
      }
    } else if (pubDetail.getPubType() == 3) {// 会议论文
      try {
        // 标题
        if (StringUtils.isNotBlank(pubDetail.getTitle())) {
          title += "[C]";
        }
        if (paramMap.get("confName") != null) {
          title += "//" + paramMap.get("confName");
        }
        if (StringUtils.isNotBlank(pubDetail.getTitle())) {
          title += ". ";
        }
        // 城市:会议组织者
        // 城市:会议组织者
        String conf_city = "";
        boolean isCityNameNotNull = StringUtils.isNotBlank((String) paramMap.get("cityName"));
        boolean isConfOrganizerNotNull = StringUtils.isNotBlank((String) paramMap.get("conf_organizer"));
        if (isCityNameNotNull && isConfOrganizerNotNull) {// 两者都有
          conf_city = paramMap.get("cityName") + ":" + paramMap.get("conf_organizer") + ". ";
        } else if (isCityNameNotNull && !isConfOrganizerNotNull) {// 只有城市
          conf_city = paramMap.get("cityName") + ". ";
        } else if (!isCityNameNotNull && isConfOrganizerNotNull) {// 只有会议组织者
          conf_city = paramMap.get("conf_organizer") + ". ";
        }
        // 年份:页码s
        String pse = "";
        if (paramMap.get("publishYear") != null) {
          pse = paramMap.get("publishYear") + "";
        }
        if (StringUtils.isNotBlank((String) paramMap.get("pageNumber"))) {
          pse += ":" + paramMap.get("pageNumber");
        }
        if (StringUtils.isNotBlank(pse)) {
          pse += ". ";
        }
        paramMap.put("zhGBTQueto", pubMemberGBTname + title + conf_city + pse);
        paramMap.put("enGBTQueto", pubMemberGBTname + title + conf_city + pse);
      } catch (Exception e) {
        logger.error("获取GBT7714-2015格式-会议论文-出错,pubId= " + pubDetail.getPubId(), e);
      }
    }
    return paramMap;
  }

  /**
   * 方便freemaker判断空字串串 例如，zhTitle为空的话，显示enTitle <#if zhTitle?exists>${zhTitle}.<#else><#if
   * enTitle?exists>${enTitle}.</#if></#if> 但是这样写的是如果zhTitle为空字符串，而不是null，则页面会显示空白
   * 
   * @param str
   * @return
   */
  private String getNullString(String str) {
    if (StringUtils.isBlank(str)) {
      return null;
    } else {
      return str;
    }
  }

  /**
   * 获取空字符串
   * 
   * @param str
   * @return
   */
  private String getEmptyString(String str) {
    if (StringUtils.isBlank(str)) {
      return "";
    } else {
      return str;
    }
  }

  /**
   * 获取对应的语言版本
   * 
   * @param locale
   * @param zhStr
   * @param enStr
   * @return
   */
  private String getLocaleString(Locale locale, String zhStr, String enStr) {
    if ("en".equals(locale.getLanguage())) {
      if (StringUtils.isBlank(enStr)) {
        return zhStr;
      }
      return enStr;
    } else {
      if (StringUtils.isBlank(zhStr)) {
        return enStr;
      }
      return zhStr;
    }
  }
}
