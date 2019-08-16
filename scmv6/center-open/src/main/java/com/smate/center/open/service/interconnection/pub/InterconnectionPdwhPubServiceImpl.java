package com.smate.center.open.service.interconnection.pub;

import com.smate.center.open.dao.consts.ConstPubTypeDao;
import com.smate.center.open.dao.publication.ConstRefDbDao;
import com.smate.center.open.model.consts.ConstPubType;
import com.smate.center.open.model.publication.ConstRefDb;
import com.smate.center.open.model.publication.PubXmlDocument;
import com.smate.center.open.service.util.PubDetailVoUtil;
import com.smate.core.base.pub.enums.PubConferencePaperTypeEnum;
import com.smate.core.base.pub.util.PubParamUtils;
import com.smate.core.base.pub.vo.PubDetailVO;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.date.DateUtils;
import com.smate.core.base.utils.string.IrisStringUtils;
import com.smate.web.v8pub.dto.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 获取基准库详情服务
 * 
 * @author AiJiangBin
 */
@Service("pdwhPubService")
public class InterconnectionPdwhPubServiceImpl implements InterconnectionIsisPubService {

  public static Map<Integer, String> pubTypeMap = new HashMap<Integer, String>();
  static {
    pubTypeMap.put(1, "奖励");
    pubTypeMap.put(2, "书/著作");
    pubTypeMap.put(3, "会议论文");
    pubTypeMap.put(4, "期刊论文");
    pubTypeMap.put(5, "专利");
    pubTypeMap.put(7, "其他");
    pubTypeMap.put(8, "学位论文");
    pubTypeMap.put(10, "书籍章节");
    pubTypeMap.put(11, "Journal editor");
  }
  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private ConstPubTypeDao constPubTypeDao;
  @Autowired
  private ConstRefDbDao constRefDbDao;
  @Autowired
  private PersonDao personDao;

  @Value("${domainscm}")
  private String domain;
  public static Map<Integer, ConstPubType> constPubTypeMap = new HashMap<Integer, ConstPubType>();

  private void buildOther(Map<String, Object> dataMap, PubDetailVO pubDetailVO) {
    String country_name = pubDetailVO.getCountryName();
    dataMap.put("country_name", country_name);
    String city = pubDetailVO.getCityName();
    dataMap.put("city", city);
    dataMap.put("countries_regions",pubDetailVO.getCountriesRegions());
  }

  /** 已处理 */
  private void buildBookChapter(Map<String, Object> dataMap, BookInfoDTO bookInfo) {
    if(bookInfo == null){
      return ;
    }
    dataMap.put("book_name", bookInfo.getName());
    dataMap.put("series_book", bookInfo.getSeriesName());
    dataMap.put("isbn", bookInfo.getISBN());
    dataMap.put("editors", bookInfo.getEditors());

    dataMap.put("pub_house", bookInfo.getPublisher());
    dataMap.put("chapter_no", bookInfo.getChapterNo());
    dataMap.put("start_page", bookInfo.getPageNumber());
    dataMap.put("isbn", bookInfo.getISBN());
  }

  private void buildPatent(Map<String, Object> dataMap, PatentInfoDTO patentInfo) {
    if(patentInfo == null){
      return ;
    }
    String patentStatus = "";
    // <!-- 专利状态：0-申请,1-授权 -->
    if (patentInfo.getStatus() == 1) {
      patentStatus = "授权";
    } else if (patentInfo.getStatus() == 0) {
      patentStatus = "申请";
    }
    dataMap.put("patent_status", patentStatus);
    // 这三个字段，只放申请时间
    dataMap.put("publish_year", PubDetailVoUtil.parseDateForYear(patentInfo.getApplicationDate()));
    dataMap.put("publish_month", PubDetailVoUtil.parseDateForMonth(patentInfo.getApplicationDate()));
    dataMap.put("publish_day", PubDetailVoUtil.parseDateForDay(patentInfo.getApplicationDate()));
    dataMap.put("apply_man", patentInfo.getApplier());
    dataMap.put("license_unit", patentInfo.getIssuingAuthority());
    dataMap.put("ch_patent_type", PubParamUtils.buildPatentTypeDesc(patentInfo.getType()));
    dataMap.put("cpc_num", patentInfo.getCPC());
    dataMap.put("open_num", patentInfo.getPublicationOpenNo());
    dataMap.put("ipc_num", patentInfo.getIPC());
    dataMap.put("money", patentInfo.getPrice());
    dataMap.put("patent_num", patentInfo.getApplicationNo());
    dataMap.put("patent_name", patentInfo.getArea().getValue());
    dataMap.put("con_status_value", patentInfo.getTransitionStatus());
    dataMap.put("effect_start_year", PubDetailVoUtil.parseDateForYear(patentInfo.getStartDate()));
    dataMap.put("effect_start_month", PubDetailVoUtil.parseDateForMonth(patentInfo.getStartDate()));
    dataMap.put("effect_start_day", PubDetailVoUtil.parseDateForDay(patentInfo.getStartDate()));
    dataMap.put("effect_end_year", PubDetailVoUtil.parseDateForYear(patentInfo.getEndDate()));
    dataMap.put("effect_end_month", PubDetailVoUtil.parseDateForMonth(patentInfo.getEndDate()));
    dataMap.put("effect_end_day", PubDetailVoUtil.parseDateForDay(patentInfo.getEndDate()));


  }

  private void buildJournal(Map<String, Object> dataMap, JournalInfoDTO journalInfo) {
    if(journalInfo == null){
      return ;
    }
    // 期刊publish 年月日 可能是取这个节点的值accept_year
    String publicStatus = "";
    if ("P".equalsIgnoreCase(journalInfo.getPublishStatus())) {
      publicStatus = "已发表";
    } else if ("A".equalsIgnoreCase(journalInfo.getPublishStatus())) {
      publicStatus = "已接收未发表";
    }
    dataMap.put("public_status", publicStatus); // A 接受未发表 P 已发表
    dataMap.put("volume", journalInfo.getVolumeNo()); // 卷号
    dataMap.put("issue", journalInfo.getIssue()); // 期号
    dataMap.put("include_start", ""); // 1 包含
    String pageNumber = journalInfo.getPageNumber();
    if (StringUtils.isNotBlank(pageNumber)) {
      dataMap.put("begin_num", pageNumber.split("-")[0]);
    } else {
      dataMap.put("begin_num", "");
    }
    dataMap.put("pages", pageNumber);
    dataMap.put("issn", journalInfo.getISSN());
    dataMap.put("journal_name", journalInfo.getName());
  }

  /**
   * 处理
   * 
   * @param dataMap
   * @param conferencePaper
   */
  private void buildConf(Map<String, Object> dataMap, ConferencePaperDTO conferencePaper) {
    if(conferencePaper == null){
      return ;
    }
    dataMap.put("conf_name", conferencePaper.getName());
    // 论文类别 pub_conf_paper/@paper_type
    PubConferencePaperTypeEnum paperType = conferencePaper.getPaperType();
    if (paperType == null) {
      dataMap.put("conf_type", "");
    } else if (paperType == PubConferencePaperTypeEnum.INVITED) {
      dataMap.put("conf_type", "A");
    } else if (paperType == PubConferencePaperTypeEnum.GROUP) {
      dataMap.put("conf_type", "E");
    } else if (paperType == PubConferencePaperTypeEnum.POSTER) {
      // SCM-21104 2018-11-07 调整
      dataMap.put("conf_type", "C");
    } else {
      dataMap.put("conf_type", "");
    }
    dataMap.put("conf_start_year", PubDetailVoUtil.parseDateForYear(conferencePaper.getStartDate()));
    dataMap.put("conf_start_month", PubDetailVoUtil.parseDateForMonth(conferencePaper.getStartDate()));
    dataMap.put("conf_start_day", PubDetailVoUtil.parseDateForDay(conferencePaper.getStartDate()));
    dataMap.put("conf_end_year", PubDetailVoUtil.parseDateForYear(conferencePaper.getEndDate()));
    dataMap.put("conf_end_month", PubDetailVoUtil.parseDateForMonth(conferencePaper.getEndDate()));
    dataMap.put("conf_end_day", PubDetailVoUtil.parseDateForDay(conferencePaper.getEndDate()));
    dataMap.put("conf_org", conferencePaper.getOrganizer());
    String pageNumber = conferencePaper.getPageNumber();
    if (StringUtils.isNotBlank(pageNumber)) {
      dataMap.put("begin_num", pageNumber.split("-")[0]);
    } else {
      dataMap.put("begin_num", "");
    }
    dataMap.put("pages", pageNumber);
    // 论文类型pub_conf_paper@category_name
    dataMap.put("paper_type", conferencePaper.getPaperType() != null ? conferencePaper.getPaperType().getValue() : "");
    dataMap.put("article_no", "");
  }

  private void buildBook(Map<String, Object> dataMap, BookInfoDTO bookInfo) {
    if(bookInfo == null){
      return ;
    }
    String language = "中文";
    dataMap.put("language", language);
    // 1--已出版 0 待出版 -- 已去除-2018
    dataMap.put("publication_status", "");
    dataMap.put("pub_house", bookInfo.getPublisher());
    dataMap.put("t_word",
        (bookInfo.getTotalWords() == null || bookInfo.getTotalWords() == 0) ? "" : bookInfo.getTotalWords());
    dataMap.put("isbn", bookInfo.getISBN());
    dataMap.put("series_book", bookInfo.getSeriesName());
    dataMap.put("editors", bookInfo.getEditors());
    dataMap.put("pages", bookInfo.getShowTotalPageOrPage());
  }

  /**
   * 已处理
   * 
   * @param dataMap
   * @param awardsInfo
   */
  private void buildAward(Map<String, Object> dataMap, AwardsInfoDTO awardsInfo) {
    if(awardsInfo == null){
      return ;
    }
    dataMap.put("award_type_name", awardsInfo.getCategory());
    dataMap.put("award_grade_name", awardsInfo.getGrade());
    dataMap.put("prize_org", awardsInfo.getIssuingAuthority());
    // 证书编号
    dataMap.put("zs_number", awardsInfo.getCertificateNo());
  }

  /**
   * 判断是否认证
   * 
   * @param dataMap
   */
  private void isOrNotAuthenticated(Map<String, Object> dataMap, PubXmlDocument pubXmlDoc) {
    String sourceDbCode = pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "source_db_code");
    String dbId = "-1";
    if (StringUtils.isNotBlank(sourceDbCode)) {
      try {
        ConstRefDb constRefDb = constRefDbDao.getConstRefDbByCode(sourceDbCode);
        dbId = constRefDb.getId().toString();
      } catch (Exception e) {
      }
    }
    String authenticated = "0";
    if (StringUtils.isNoneBlank(dbId) && NumberUtils.isNumber(dbId)) {
      if (NumberUtils.toInt(dbId) > 0) {
        authenticated = "1";
      }
    }
    dataMap.put("authenticated", authenticated);

  }



  /**
   * 获取成果更新时间
   * 
   * @param updateDate
   */
  private String buildFormateTime(Date updateDate) {
    String updateDateStr = "";
    if (updateDate != null) {
      updateDateStr = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(updateDate);
    }
    return updateDateStr;
  }

  @Override
  public void fillCommonElement(Element pubElement, String pubXml) {

  }

  /** 解析基准库的成果 */
  @Override
  public Map<String, Object> parsePdwhXmlXmlToMap1(PubDetailVO pubDetailVO, Long ownPsnId) throws Exception {
    Map<String, Object> dataMap = new HashMap<String, Object>();
    if (pubDetailVO != null) {
      Long pubId = pubDetailVO.getPubId();
      int pubType = pubDetailVO.getPubType();
      dataMap.put("status",pubDetailVO.getStatus() == null ?0:pubDetailVO.getStatus());
      dataMap.put("pdwh_pub_id", pubId);
      if(pubDetailVO.getStatus() != null && pubDetailVO.getStatus() == 1){
        return dataMap;
      }
      dataMap.put("pub_type_id", pubType);
      ConstPubType constPubType = constPubTypeDao.get(pubType);
      dataMap.put("zh_pub_type_name", constPubType.getZhName());
      dataMap.put("en_pub_type_name", constPubType.getEnName());
      dataMap.put("zh_title", pubDetailVO.getTitle());
      dataMap.put("en_title", pubDetailVO.getTitle());
      dataMap.put("zh_source", pubDetailVO.getBriefDesc());
      dataMap.put("en_source", pubDetailVO.getBriefDesc());
      dataMap.put("authors_name", pubDetailVO.getAuthorNames());
      dataMap.put("update_mark", pubDetailVO.getUpdateMark() != null ? pubDetailVO.getUpdateMark() : "");
      dataMap.put("zh_keywords", IrisStringUtils.subMaxLengthString(pubDetailVO.getKeywords(), 200));
      dataMap.put("en_keywords", IrisStringUtils.subMaxLengthString(pubDetailVO.getKeywords(), 200));
      dataMap.put("zh_abstract", pubDetailVO.getSummary());
      dataMap.put("en_abstract", pubDetailVO.getSummary());
      dataMap.put("doi", pubDetailVO.getDoi());
      dataMap.put("doi_url",
          StringUtils.isNotBlank(pubDetailVO.getDoi()) ? "http://dx.doi.org/" + pubDetailVO.getDoi() : "");

      dataMap.put("publish_year", PubDetailVoUtil.parseDateForYear(pubDetailVO.getPublishDate()));
      dataMap.put("publish_month", PubDetailVoUtil.parseDateForMonth(pubDetailVO.getPublishDate()));
      dataMap.put("publish_day", PubDetailVoUtil.parseDateForDay(pubDetailVO.getPublishDate()));

      fillFullTextUrl(pubId, dataMap, pubDetailVO); // 全文图片 ，是否全文 ，全文链接
      // 收录情况sci
      PubDetailVoUtil.fillListInfoAddSrcId(pubDetailVO, dataMap);
      PubDetailVoUtil.fillIsOwnerByPubDetailVO(dataMap, pubDetailVO); // 填充 作者 和 owner(dataMap, pubDetailVO);
      PubDetailVoUtil.fillCreateTime(pubDetailVO, dataMap);
      // 是否认证 0==不是 ， 1==是
      PubDetailVoUtil.isOrNotAuthenticated(dataMap, pubDetailVO); // 是否认证
      // 和强哥确认，取isi库的引用次数，有就取值，没有就为空 dbid==99为isi的
      dataMap.put("cited_times", pubDetailVO.getCitations());
      dataMap.put("pub_detail_param", pubDetailVO.getPubIndexUrl());
      dataMap.put("product_mark", pubDetailVO.getFundInfo());
      dataMap.put("pub_update_date", buildFormateTime(pubDetailVO.getGmtModified()));
      dataMap.put("source_db_id", pubDetailVO.getSourceDbId());
      String language = "中文";
      dataMap.put("language", language);
      // remark字段返回空 SCM-19851
      dataMap.put("remark", "");
      PubTypeInfoDTO pubTypeInfo = pubDetailVO.getPubTypeInfo();
      // 生成数据没有1 ， 2
      switch (pubType) {
        case 1:
          // 奖励
          buildAward(dataMap, (AwardsInfoDTO) pubTypeInfo);
          break;
        case 2:
          // 书 著作
          buildBook(dataMap, (BookInfoDTO) pubTypeInfo);
          break;
        case 3:
          // 会议
          buildConf(dataMap, (ConferencePaperDTO) pubTypeInfo);
          break;
        case 4:
          // 期刊
          buildJournal(dataMap, (JournalInfoDTO) pubTypeInfo);
          dataMap.put("impact_factors", pubDetailVO.getImpactFactors());
          break;
        case 5:
          // 专利
          buildPatent(dataMap, (PatentInfoDTO) pubTypeInfo);
          break;
        case 10:
          // 书籍章节
          buildBookChapter(dataMap, (BookInfoDTO) pubTypeInfo);
          break;
        default:// 其他
          ;
          break;
      }
      // 国家 城市
      buildOther(dataMap, pubDetailVO);
    }
    return dataMap;

  }

  /**
   * @param pubXmlDoc
   * @param arrDate
   */
  private void parsePublishDate(PubXmlDocument pubXmlDoc, String[] arrDate) {
    String publishDateXml = pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "pubyear");
    if (StringUtils.isBlank(publishDateXml)) {
      publishDateXml = pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_date");
    }

    Date parseDate = DateUtils.parseStringToDate(publishDateXml);
    if (parseDate != null) {
      Calendar instance = Calendar.getInstance();
      instance.setTime(parseDate);
      switch (publishDateXml.length()) {
        case 4:
          arrDate[0] = instance.get(Calendar.YEAR) + "";
          arrDate[1] = "";
          arrDate[2] = "";
          break;
        case 7:
          arrDate[0] = instance.get(Calendar.YEAR) + "";
          arrDate[1] = instance.get(Calendar.MONTH) + 1 + "";
          arrDate[2] = "";
          break;
        default:
          arrDate[0] = instance.get(Calendar.YEAR) + "";
          arrDate[1] = instance.get(Calendar.MONTH) + 1 + "";
          arrDate[2] = instance.get(Calendar.DAY_OF_MONTH) + "";
          break;
      }
    } else {
      arrDate[0] = "";
      arrDate[1] = "";
      arrDate[2] = "";
    }
  }

  /**
   * @param arrDate
   */
  private void parsePatentPublishDate(String publishDateXml, String[] arrDate) {

    Date parseDate = DateUtils.parseStringToDate(publishDateXml);
    if (parseDate != null) {
      Calendar instance = Calendar.getInstance();
      instance.setTime(parseDate);
      switch (publishDateXml.length()) {
        case 4:
          arrDate[0] = instance.get(Calendar.YEAR) + "";
          arrDate[1] = "";
          arrDate[2] = "";
          break;
        case 7:
          arrDate[0] = instance.get(Calendar.YEAR) + "";
          arrDate[1] = instance.get(Calendar.MONTH) + 1 + "";
          arrDate[2] = "";
          break;
        default:
          arrDate[0] = instance.get(Calendar.YEAR) + "";
          arrDate[1] = instance.get(Calendar.MONTH) + 1 + "";
          arrDate[2] = instance.get(Calendar.DAY_OF_MONTH) + "";
          break;
      }
    } else {
      arrDate[0] = "";
      arrDate[1] = "";
      arrDate[2] = "";
    }
  }


  @Override
  public Map<String, Object> parseXmlToMap1(PubDetailVO pubDetailVO) throws Exception {
    return null;
  }

  /**
   * 把日期分隔成年、月、日.
   * 
   * @param doc XmlDocument
   * @param xpath xml元素路径
   * @param attrName 属性名
   * @param pattern 格式
   * @return String[]
   */
  public static String[] splitDateYearMonth(PubXmlDocument doc, String xpath, String attrName, String pattern) {
    String date = doc.getXmlNodeAttribute(xpath, attrName);
    if ("".equals(date)) {
      return new String[] {"", "", ""};
    }

    return getDateYearMonth(pattern, date);
  }

  /**
   * @param pattern 格式
   * @param date 日期字符串
   * @return String[]
   */
  public static String[] getDateYearMonth(String pattern, String date) {

    if (date == null || "".equals(date)) {
      return new String[] {"", "", ""};
    }

    date = date.replaceAll("\\s+", "");
    String year = "", month = "", day = "";
    if (PubXmlConstants.CHS_DATE_PATTERN.equalsIgnoreCase(pattern)) {
      String[] temp = date.split("[-/]");
      year = "0".equals(temp[0]) ? "" : temp[0];
      if (temp.length >= 2) {
        month = "0".equals(temp[1]) ? "" : temp[1];
      }
      if (temp.length >= 3) {
        day = "0".equals(temp[2]) ? "" : temp[2];
      }
    } else if (PubXmlConstants.ENG_DATE_PATTERN.equalsIgnoreCase(pattern)) {
      String[] temp = date.split("[-/]");
      if (temp.length == 1) {
        year = "0".equals(temp[0]) ? "" : temp[0];
      } else if (temp.length <= 2) {
        year = "0".equals(temp[1]) ? "" : temp[1];
        month = "0".equals(temp[0]) ? "" : temp[0];
      } else if (temp.length >= 3) {
        day = "0".equals(temp[0]) ? "" : temp[0];
        month = "0".equals(temp[1]) ? "" : temp[1];
        year = "0".equals(temp[2]) ? "" : temp[2];
      }
    } else if (PubXmlConstants.PATENT_CHS_DATE_PATTERN.equalsIgnoreCase(pattern)) {
      String[] temp = date.split("[\\.\\-/]");
      if (temp.length == 1) {
        year = "0".equals(temp[0]) ? "" : temp[0];
      } else if (temp.length <= 2) {
        year = "0".equals(temp[0]) ? "" : temp[0];
        month = "0".equals(temp[1]) ? "" : temp[1];
      } else if (temp.length >= 3) {
        day = "0".equals(temp[2]) ? "" : temp[2];
        month = "0".equals(temp[1]) ? "" : temp[1];
        year = "0".equals(temp[0]) ? "" : temp[0];
      }
    }
    year = XmlUtil.changeSBCChar(year);
    month = XmlUtil.changeSBCChar(month);
    day = XmlUtil.changeSBCChar(day);

    try {
      int t = Integer.parseInt(year);
      if (t < 1970 || t > 2039) {
        year = "";
        month = "";
        day = "";
      } else {
        try {
          int m = Integer.parseInt(month);
          if (m < 1 || m > 12) {
            month = "";
            day = "";
          } else {
            try {
              int d = Integer.parseInt(day);
              if (d < 1 || d > 31) {
                day = "";
              }
            } catch (Exception e) {
              day = "";
            }
          }
        } catch (Exception e) {
          month = "";
          day = "";
        }
      }
    } catch (Exception e) {
      year = "";
      month = "";
      day = "";
    }

    if (month.startsWith("0")) {
      month = month.substring(1);
    }
    if (day.startsWith("0")) {
      day = day.substring(1);
    }

    return new String[] {year, month, day};
  }


  /**
   * 全文
   * 
   * @param dataMap
   */
  private void fillFullTextUrl(Long pubId, Map<String, Object> dataMap, PubDetailVO pubDetailVO) {

    if (pubDetailVO.getFullText() != null) {
      dataMap.put("has_full_text", "1");
      dataMap.put("full_text_img_url", pubDetailVO.getFullText().getThumbnailPath());
    } else {
      dataMap.put("has_full_text", "0");
      dataMap.put("full_text_img_url", "");
    }
    // 全文链接
    dataMap.put("full_link", pubDetailVO.getSrcFulltextUrl());

  }
}
