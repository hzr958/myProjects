package com.smate.center.open.service.interconnection.pub;

import com.smate.center.open.dao.consts.ConstPubTypeDao;
import com.smate.center.open.model.consts.ConstPubType;
import com.smate.center.open.service.util.PubDetailVoUtil;
import com.smate.core.base.pub.enums.PubConferencePaperTypeEnum;
import com.smate.core.base.pub.util.PubParamUtils;
import com.smate.core.base.pub.vo.PubDetailVO;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.web.v8pub.dto.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * u83nu2n0 服务调用的成果接口
 * 
 * @author Ai Jiangbin
 * 
 * @creation 2017年10月30日
 */
@Service("oldIsisPubService")
public class OldIsisPubServiceImpl implements OldIsisPubService {

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


  @Value("${domainscm}")
  private String domain;
  public static Map<Integer, ConstPubType> constPubTypeMap = new HashMap<Integer, ConstPubType>();

  public Map<String, Object> parseXmlToMap(PubDetailVO pubDetailVO) throws Exception {
    Map<String, Object> dataMap = new HashMap<String, Object>();
    if (pubDetailVO != null) {
      Long pubId = pubDetailVO.getPubId();
      int pubType = pubDetailVO.getPubType();
      dataMap.put("pub_id", pubId.toString());
      dataMap.put("hcp", pubDetailVO.getHCP() == 1);
      dataMap.put("pub_type_id", pubType);
      fillPubType("", "", dataMap, pubType);
      dataMap.put("zh_title", PubDetailVoUtil.escapeXMLStr(pubDetailVO.getTitle()));
      dataMap.put("en_title", PubDetailVoUtil.escapeXMLStr(pubDetailVO.getTitle()));
      dataMap.put("zh_source", PubDetailVoUtil.escapeXMLStr(pubDetailVO.getBriefDesc()));
      dataMap.put("en_source", PubDetailVoUtil.escapeXMLStr(pubDetailVO.getBriefDesc()));
      dataMap.put("authors_name", PubDetailVoUtil.escapeXMLStr(pubDetailVO.getAuthorNames()));

      dataMap.put("publish_year", PubDetailVoUtil.parseDateForYear(pubDetailVO.getPublishDate()));
      dataMap.put("publish_month", PubDetailVoUtil.parseDateForMonth(pubDetailVO.getPublishDate()));
      dataMap.put("publish_day", PubDetailVoUtil.parseDateForDay(pubDetailVO.getPublishDate()));

      fillFullTextUrl(pubId, dataMap, pubDetailVO); // 全文图片 ，是否全文 ，全文链接
      PubDetailVoUtil.fillListInfoForUnion(pubDetailVO, dataMap);
      PubDetailVoUtil.fillIsOwnerWithAuhtors(dataMap, pubDetailVO); // 填充 作者 和 owner(dataMap, pubDetailVO); // 填充 作者 和
                                                                    // owner
      // 默认为空，只有基准库成果变成sns成果才会有创建时间
      PubDetailVoUtil.fillCreateTime(pubDetailVO, dataMap);
      PubDetailVoUtil.isOrNotAuthenticated(dataMap, pubDetailVO); // 是否认证
      dataMap.put("cited_times", pubDetailVO.getCitations());
      int random = (int) (Math.random() * 10000);
      String pub_detail_param = ServiceUtil.encodeToDes3(String.valueOf(random) + "|" + pubId.toString());
      dataMap.put("pub_detail_param", pub_detail_param);
      dataMap.put("product_mark", PubDetailVoUtil.escapeXMLStr(pubDetailVO.getFundInfo()));
      dataMap.put("remark", "");
      dataMap.put("language", "");
      PubTypeInfoDTO pubTypeInfo = pubDetailVO.getPubTypeInfo();
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
          dataMap.put("doi", pubDetailVO.getDoi());
          break;
        case 4:
          // 期刊
          buildJournal(dataMap, (JournalInfoDTO) pubTypeInfo);
          dataMap.put("doi", pubDetailVO.getDoi());
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



  private void buildOther(Map<String, Object> dataMap, PubDetailVO pubDetailVO) {
    String country_name = pubDetailVO.getCountryName();
    dataMap.put("country_name", country_name);
    String city = pubDetailVO.getCityName();
    dataMap.put("city", city);
  }

  private void buildBookChapter(Map<String, Object> dataMap, BookInfoDTO bookInfo) {
    dataMap.put("book_name", PubDetailVoUtil.escapeXMLStr(bookInfo.getName()));
    dataMap.put("series_book", PubDetailVoUtil.escapeXMLStr(bookInfo.getSeriesName()));
    dataMap.put("isbn", PubDetailVoUtil.escapeXMLStr(bookInfo.getISBN()));
    dataMap.put("editors", PubDetailVoUtil.escapeXMLStr(bookInfo.getEditors()));

    dataMap.put("pub_house", PubDetailVoUtil.escapeXMLStr(bookInfo.getPublisher()));
  }

  private void buildPatent(Map<String, Object> dataMap, PatentInfoDTO patentInfo) {


    String patentStatus = "";
    // <!-- 专利状态：0-申请,1-授权 -->
    if (patentInfo.getStatus() == 1) {
      patentStatus = "授权";
      // 这三个字段，授权生效日期
      dataMap.put("publish_year", PubDetailVoUtil.parseDateForYear(patentInfo.getStartDate()));
      dataMap.put("publish_month", PubDetailVoUtil.parseDateForMonth(patentInfo.getStartDate()));
      dataMap.put("publish_day", PubDetailVoUtil.parseDateForDay(patentInfo.getStartDate()));
      dataMap.put("apply_man", patentInfo.getPatentee());
      dataMap.put("patent_num", patentInfo.getApplicationNo());
    } else if (patentInfo.getStatus() == 0) {
      patentStatus = "申请";
      dataMap.put("apply_man", patentInfo.getApplier());
      dataMap.put("patent_num", patentInfo.getApplicationNo());
      // 这三个字段，只放申请时间
      dataMap.put("publish_year", PubDetailVoUtil.parseDateForYear(patentInfo.getApplicationDate()));
      dataMap.put("publish_month", PubDetailVoUtil.parseDateForMonth(patentInfo.getApplicationDate()));
      dataMap.put("publish_day", PubDetailVoUtil.parseDateForDay(patentInfo.getApplicationDate()));
    }
    dataMap.put("patent_status", patentStatus);



    dataMap.put("license_unit", PubDetailVoUtil.escapeXMLStr(patentInfo.getIssuingAuthority()));

    dataMap.put("ch_patent_type", PubParamUtils.buildPatentTypeDesc(patentInfo.getType()));


  }

  private void buildJournal(Map<String, Object> dataMap, JournalInfoDTO journalInfo) {
    String publicStatus = "";
    if ("P".equalsIgnoreCase(journalInfo.getPublishStatus())) {
      publicStatus = "已发表";
    } else if ("A".equalsIgnoreCase(journalInfo.getPublishStatus())) {
      publicStatus = "已接收未发表";
    }
    dataMap.put("public_status", publicStatus); // A 接受未发表 P 已发表
    dataMap.put("volume", journalInfo.getVolumeNo()); // 卷号
    dataMap.put("issue", journalInfo.getIssue()); // 期号
    String includeStart = "否";
    dataMap.put("include_start", includeStart); // 1 包含
    dataMap.put("begin_num", journalInfo.getPageNumber());
    dataMap.put("journal_name", PubDetailVoUtil.escapeXMLStr(journalInfo.getName()));
  }

  private void buildConf(Map<String, Object> dataMap, ConferencePaperDTO conferencePaper) {

    dataMap.put("conf_name", PubDetailVoUtil.escapeXMLStr(conferencePaper.getName()));

    // 论文类别 pub_conf_paper/@paper_type
    // <!-- 会议类型：A-特邀报告,E-分组报告 ,P--墙报展示-->
    PubConferencePaperTypeEnum paperType = conferencePaper.getPaperType();
    if (paperType == null) {
      dataMap.put("conf_type", "");
    } else if (paperType == PubConferencePaperTypeEnum.INVITED) {
      dataMap.put("conf_type", "A");
    } else if (paperType == PubConferencePaperTypeEnum.GROUP) {
      dataMap.put("conf_type", "E");
    } else if (paperType == PubConferencePaperTypeEnum.POSTER) {
      dataMap.put("conf_type", "P");
    }
    dataMap.put("conf_org", PubDetailVoUtil.escapeXMLStr(conferencePaper.getOrganizer()));
    dataMap.put("conf_start_year", PubDetailVoUtil.parseDateForYear(conferencePaper.getStartDate()));
    dataMap.put("conf_start_month", PubDetailVoUtil.parseDateForMonth(conferencePaper.getStartDate()));
    dataMap.put("conf_start_day", PubDetailVoUtil.parseDateForDay(conferencePaper.getStartDate()));
    dataMap.put("conf_end_year", PubDetailVoUtil.parseDateForYear(conferencePaper.getEndDate()));
    dataMap.put("conf_end_month", PubDetailVoUtil.parseDateForMonth(conferencePaper.getEndDate()));
    dataMap.put("conf_end_day", PubDetailVoUtil.parseDateForDay(conferencePaper.getEndDate()));
    dataMap.put("begin_num", conferencePaper.getPageNumber());
    // 论文类型pub_conf_paper@category_name
    dataMap.put("paper_type", conferencePaper.getPaperType() != null ? conferencePaper.getPaperType().getValue() : "");
    dataMap.put("article_no", "");
  }

  private void buildBook(Map<String, Object> dataMap, BookInfoDTO bookInfo) {
    dataMap.put("language", bookInfo.getLanguage() != null ? bookInfo.getLanguage() : "");
    // 1--已出版 0 待出版已去除-2018
    String publicationStatus = "";
    dataMap.put("publication_status", publicationStatus);
    dataMap.put("pub_house", PubDetailVoUtil.escapeXMLStr(bookInfo.getPublisher()));
    dataMap.put("t_word", bookInfo.getTotalWords());
    dataMap.put("isbn", bookInfo.getISBN());
  }

  private void buildAward(Map<String, Object> dataMap, AwardsInfoDTO awardsInfo) {
    dataMap.put("award_type_name", PubDetailVoUtil.escapeXMLStr(awardsInfo.getCategory()));
    dataMap.put("award_grade_name", PubDetailVoUtil.escapeXMLStr(awardsInfo.getGrade()));
    dataMap.put("prize_org", PubDetailVoUtil.escapeXMLStr(awardsInfo.getIssuingAuthority()));
  }

  /**
   * 得到成果类型
   * 
   * @return
   */
  public void fillPubType(String zhPubType, String enPubType, Map<String, Object> dataMap, int pubType) {
    if (StringUtils.isNotBlank(zhPubType) && StringUtils.isNoneBlank(enPubType)) {
      dataMap.put("zh_pub_type_name", zhPubType);
      dataMap.put("en_pub_type_name", enPubType);
    } else {
      if (constPubTypeMap.size() == 0) {
        List<ConstPubType> constPubTypes = constPubTypeDao.getAll();
        if (constPubTypes != null && constPubTypes.size() > 0) {
          for (ConstPubType constPubType : constPubTypes) {
            Integer id = constPubType.getId();
            constPubTypeMap.put(id, constPubType);
          }
          surePubType(dataMap, pubType);
        }
      } else {
        surePubType(dataMap, pubType);
      }
    }
  }

  /**
   * 确定成果类型
   * 
   * @param dataMap
   */
  private void surePubType(Map<String, Object> dataMap, int pubType) {
    ConstPubType constPubType = constPubTypeMap.get(pubType);
    dataMap.put("zh_pub_type_name", constPubType.getZhName());
    dataMap.put("en_pub_type_name", constPubType.getEnName());
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
    dataMap.put("full_link", pubDetailVO.getSrcFulltextUrl() == null ? "" : pubDetailVO.getSrcFulltextUrl());

  }



  public static void main(String[] args) {
    //String s =translationStr("<strong>Micha>Micha\u0002l Bacz Baczy&acute; nski and Wanda Niemyska</strong>");
    //System.out.println(s);
  }


}
