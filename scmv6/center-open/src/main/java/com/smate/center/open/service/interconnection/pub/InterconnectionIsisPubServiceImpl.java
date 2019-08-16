package com.smate.center.open.service.interconnection.pub;

import com.smate.center.open.dao.consts.ConstPubTypeDao;
import com.smate.center.open.dao.data.OpenUserUnionDao;
import com.smate.center.open.model.consts.ConstPubType;
import com.smate.center.open.service.util.PubDetailVoUtil;
import com.smate.core.base.pub.enums.PubConferencePaperTypeEnum;
import com.smate.core.base.pub.po.dao.PubPdwhSnsRelationDAO;
import com.smate.core.base.pub.util.PubParamUtils;
import com.smate.core.base.pub.vo.PubDetailVO;
import com.smate.core.base.utils.string.IrisStringUtils;
import com.smate.web.v8pub.dto.*;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 获取Isis成果服务
 * 
 * @author AiJiangBin
 */
@Service("isisPubService")
public class InterconnectionIsisPubServiceImpl implements InterconnectionIsisPubService {

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
  private OpenUserUnionDao openUserUnionDao;
  @Autowired
  private PubPdwhSnsRelationDAO pubPdwhSnsRelationDao;

  @Value("${domainscm}")
  private String domain;
  public static Map<Integer, ConstPubType> constPubTypeMap = new HashMap<Integer, ConstPubType>();

  public Map<String, Object> parseXmlToMap1(PubDetailVO pubDetailVO) throws Exception {
    Map<String, Object> dataMap = new HashMap<String, Object>();
    if (pubDetailVO != null) {
      Long pubId = pubDetailVO.getPubId();
      int pubType = pubDetailVO.getPubType();
      dataMap.put("status",pubDetailVO.getStatus() == null ?0:pubDetailVO.getStatus());
      dataMap.put("pub_id", pubId.toString());
      if(pubDetailVO.getStatus() != null && pubDetailVO.getStatus() == 1){
        return dataMap;
      }
      Long pdwhPubId = pubPdwhSnsRelationDao.getPdwhPubIdBySnsPubId(pubId);
      dataMap.put("pdwh_pub_id", pdwhPubId == null ? "" : pdwhPubId);
      dataMap.put("pub_type_id", pubType);
      fillPubType("", "", dataMap, pubType);
      dataMap.put("zh_title", pubDetailVO.getTitle());
      dataMap.put("en_title", pubDetailVO.getTitle());
      dataMap.put("zh_abstract", pubDetailVO.getSummary());
      dataMap.put("en_abstract", pubDetailVO.getSummary());
      dataMap.put("zh_keywords", IrisStringUtils.subMaxLengthString(pubDetailVO.getKeywords(), 200));
      dataMap.put("en_keywords", IrisStringUtils.subMaxLengthString(pubDetailVO.getKeywords(), 200));
      dataMap.put("zh_source", pubDetailVO.getBriefDesc());
      dataMap.put("en_source", pubDetailVO.getBriefDesc());
      dataMap.put("authors_name", pubDetailVO.getAuthorNames());

      dataMap.put("publish_year", PubDetailVoUtil.parseDateForYear(pubDetailVO.getPublishDate()));
      dataMap.put("publish_month", PubDetailVoUtil.parseDateForMonth(pubDetailVO.getPublishDate()));
      dataMap.put("publish_day", PubDetailVoUtil.parseDateForDay(pubDetailVO.getPublishDate()));
      dataMap.put("doi", pubDetailVO.getDoi());
      dataMap.put("doi_url",
          StringUtils.isNotBlank(pubDetailVO.getDoi()) ? "http://dx.doi.org/" + pubDetailVO.getDoi() : "");

      fillFullTextUrl(pubId, dataMap, pubDetailVO); // 全文图片 ，是否全文 ，全文链接
      // 填充 list_info
      PubDetailVoUtil.fillListInfoAddSrcId(pubDetailVO, dataMap);
      PubDetailVoUtil.fillIsOwnerByPubDetailVO(dataMap, pubDetailVO); // 填充
      // 作者
      // 和
      // owner(dataMap,
      // pubDetailVO);
      // //
      // 填充
      // 作者
      // 和
      // owner
      PubDetailVoUtil.fillCreateTime(pubDetailVO, dataMap);
      PubDetailVoUtil.isOrNotAuthenticated(dataMap, pubDetailVO); // 是否认证
      dataMap.put("cited_times", pubDetailVO.getCitations());

      dataMap.put("pub_detail_param", pubDetailVO.getPubIndexUrl());
      dataMap.put("product_mark", pubDetailVO.getFundInfo());
      dataMap.put("update_mark", pubDetailVO.getUpdateMark() != null ? pubDetailVO.getUpdateMark() : "");
      String format = "";
      if (pubDetailVO.getGmtModified() != null) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        format = sf.format(pubDetailVO.getGmtModified());
      }
      dataMap.put("pub_update_date", format);
      Long openId = openUserUnionDao.getOpenIdByPsnId(pubDetailVO.getPubOwnerPsnId());
      dataMap.put("pub_owner_openid", openId == null ? "" : openId);
      String language = "";
      dataMap.put("language", language);
      dataMap.put("source_db_id", pubDetailVO.getSourceDbId());
      dataMap.put("remark", pubDetailVO.getRemarks());
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
          // dataMap.put("article_no", pubDetailVO.getar);
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
        case 12:
          // 标准
          buildStandardInfo(dataMap, (StandardInfoDTO) pubTypeInfo);
          break;
        case 13:
          // 软件著作权
          buildSoftwareCopyright(dataMap, (SoftwareCopyrightDTO) pubTypeInfo);
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


  private void buildStandardInfo(Map<String, Object> dataMap, StandardInfoDTO info) {
    if(info == null){
      return ;
    }
    //  标准号  公布机构  归口单位  标准类型
    dataMap.put("standard_no", com.smate.core.base.utils.string.StringUtils.defaultString(info.getStandardNo(),""));
    dataMap.put("publish_agency", com.smate.core.base.utils.string.StringUtils.defaultString(info.getPublishAgency()));
    dataMap.put("technical_committees", com.smate.core.base.utils.string.StringUtils.defaultString(info.getTechnicalCommittees()));
    dataMap.put("standard_type",info.getType() == null ? "" :info.getType().getValue());

  }
  private void buildSoftwareCopyright(Map<String, Object> dataMap, SoftwareCopyrightDTO info) {
    if(info == null){
      return ;
    }
    //  登记号  权利获得方式  权利范围
    dataMap.put("register_no", com.smate.core.base.utils.string.StringUtils.defaultString(info.getRegisterNo(),""));
    dataMap.put("sc_acquisition_type",info.getAcquisitionType() == null ? "" : info.getAcquisitionType().getValue());
    dataMap.put("sc_scope_type", info.getScopeType() == null ? "" : info.getScopeType().getValue());

  }

  private void buildOther(Map<String, Object> dataMap, PubDetailVO pubDetailVO) {
    String country_name = pubDetailVO.getCountryName();
    dataMap.put("country_name", country_name);
    String city = pubDetailVO.getCityName();
    dataMap.put("city", city);
    dataMap.put("countries_regions",pubDetailVO.getCountriesRegions());
  }

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
      dataMap.put("apply_man", patentInfo.getPatentee());
    } else if (patentInfo.getStatus() == 0) {
      patentStatus = "申请";
      dataMap.put("apply_man", patentInfo.getApplier());
    }
    dataMap.put("patent_status", patentStatus);

    // 这三个字段，只放申请时间
    dataMap.put("publish_year", PubDetailVoUtil.parseDateForYear(patentInfo.getApplicationDate()));
    dataMap.put("publish_month", PubDetailVoUtil.parseDateForMonth(patentInfo.getApplicationDate()));
    dataMap.put("publish_day", PubDetailVoUtil.parseDateForDay(patentInfo.getApplicationDate()));
    dataMap.put("license_unit", patentInfo.getIssuingAuthority());

    dataMap.put("ch_patent_type", PubParamUtils.buildPatentTypeDesc(patentInfo.getType()));
    dataMap.put("patent_num", patentInfo.getApplicationNo());
    dataMap.put("patent_name", patentInfo.getArea().getValue());
    dataMap.put("cpc_num", patentInfo.getCPC());
    dataMap.put("open_num", patentInfo.getPublicationOpenNo());
    dataMap.put("ipc_num", patentInfo.getIPC());
    //
    dataMap.put("money", patentInfo.getPrice());
    dataMap.put("con_status_value", patentInfo.getTransitionStatus());
    // 授权日期effect_start_year effect_start_month effect_start_day
    // effect_end_year effect_end_month effect_end_day
    dataMap.put("effect_start_year", PubDetailVoUtil.parseDateForYear(patentInfo.getStartDate()));
    dataMap.put("effect_start_month", PubDetailVoUtil.parseDateForMonth(patentInfo.getStartDate()));
    dataMap.put("effect_start_day", PubDetailVoUtil.parseDateForDay(patentInfo.getStartDate()));

    dataMap.put("effect_end_year", PubDetailVoUtil.parseDateForYear(patentInfo.getEndDate()));
    dataMap.put("effect_end_month", PubDetailVoUtil.parseDateForMonth(patentInfo.getEndDate()));
    dataMap.put("effect_end_day", PubDetailVoUtil.parseDateForDay(patentInfo.getEndDate()));

  }

  private void buildJournal(Map<String, Object> dataMap, JournalInfoDTO journalInfo) {
    // 接收状态，年月日
    // dataMap.put("accept_year",
    // PubDetailVoUtil.parseDateForYear(journalInfo.getAcceptDate()));
    // dataMap.put("accept_month",
    // PubDetailVoUtil.parseDateForMonth(journalInfo.getAcceptDate()));
    // dataMap.put("accept_day",
    // PubDetailVoUtil.parseDateForDay(journalInfo.getAcceptDate())); //
    // 发表状态，描述如：冬季
    if(journalInfo == null){
      return ;
    }
    dataMap.put("pub_date_desc", "");
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

  private void buildConf(Map<String, Object> dataMap, ConferencePaperDTO conferencePaper) {
    if(conferencePaper == null){
      return ;
    }
    dataMap.put("conf_name", conferencePaper.getName());
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
      // SCM-21104 2018-11-07 调整
      dataMap.put("conf_type", "C");
    } else {
      dataMap.put("conf_type", "");
    }
    dataMap.put("conf_org", conferencePaper.getOrganizer());
    dataMap.put("conf_start_year", PubDetailVoUtil.parseDateForYear(conferencePaper.getStartDate()));
    dataMap.put("conf_start_month", PubDetailVoUtil.parseDateForMonth(conferencePaper.getStartDate()));
    dataMap.put("conf_start_day", PubDetailVoUtil.parseDateForDay(conferencePaper.getStartDate()));
    dataMap.put("conf_end_year", PubDetailVoUtil.parseDateForYear(conferencePaper.getEndDate()));
    dataMap.put("conf_end_month", PubDetailVoUtil.parseDateForMonth(conferencePaper.getEndDate()));
    dataMap.put("conf_end_day", PubDetailVoUtil.parseDateForDay(conferencePaper.getEndDate()));
    String pageNumber = conferencePaper.getPageNumber();
    if (StringUtils.isNotBlank(pageNumber)) {
      dataMap.put("begin_num", pageNumber.split("-")[0]);
    } else {
      dataMap.put("begin_num", "");
    }
    dataMap.put("pages", pageNumber);
    // 论文类型pub_conf_paper@category_name
    dataMap.put("paper_type", conferencePaper.getPaperType() != null ? conferencePaper.getPaperType().getValue() : "");
  }

  private void buildBook(Map<String, Object> dataMap, BookInfoDTO bookInfo) {
    // 1--已出版 0 待出版
    // 已去除-2018
    if(bookInfo == null){
      return ;
    }
    dataMap.put("publication_status", "");
    dataMap.put("pub_house", bookInfo.getPublisher());
    dataMap.put("t_word",
        (bookInfo.getTotalWords() == null || bookInfo.getTotalWords() == 0) ? "" : bookInfo.getTotalWords().toString());
    dataMap.put("isbn", bookInfo.getISBN());
    dataMap.put("language", bookInfo.getLanguage());
    dataMap.put("series_book", bookInfo.getSeriesName());
    dataMap.put("editors", bookInfo.getEditors());
    dataMap.put("pages", bookInfo.getShowTotalPageOrPage());
  }

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
    dataMap.put("full_link", pubDetailVO.getSrcFulltextUrl());

  }

  public static void main(String[] args) throws ParseException {
    String time = "2016-07-19 14:03:05";
    time = time.replaceAll("-", "/");

    System.out.println("1,2,3,4,5,".substring(0, "1,2,3,4,5,".length() - 1));
  }

  @Override
  public void fillCommonElement(Element pubElement, String pubXml) {

  }

  @Override
  public Map<String, Object> parsePdwhXmlXmlToMap1(PubDetailVO pubDetailVO, Long ownPsnId) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

}
