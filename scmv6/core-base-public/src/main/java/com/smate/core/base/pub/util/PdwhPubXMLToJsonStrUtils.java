package com.smate.core.base.pub.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.web.util.HtmlUtils;

import com.smate.core.base.pub.enums.PubBookTypeEnum;
import com.smate.core.base.pub.enums.PubConferencePaperTypeEnum;
import com.smate.core.base.pub.enums.PubPatentAreaEnum;
import com.smate.core.base.pub.enums.PubPatentTransitionStatusEnum;
import com.smate.core.base.pub.enums.PubSCAcquisitionTypeEnum;
import com.smate.core.base.pub.enums.PubSCScopeTypeEnum;
import com.smate.core.base.pub.enums.PubStandardTypeEnum;
import com.smate.core.base.pub.enums.PubThesisDegreeEnum;
import com.smate.core.base.pub.enums.PublicationTypeEnum;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.date.DateStringSplitFormateUtil;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.web.v8pub.dto.AwardsInfoDTO;
import com.smate.web.v8pub.dto.BookInfoDTO;
import com.smate.web.v8pub.dto.ConferencePaperDTO;
import com.smate.web.v8pub.dto.JournalInfoDTO;
import com.smate.web.v8pub.dto.MemberInsDTO;
import com.smate.web.v8pub.dto.PatentInfoDTO;
import com.smate.web.v8pub.dto.PubMemberDTO;
import com.smate.web.v8pub.dto.PubSituationDTO;
import com.smate.web.v8pub.dto.SoftwareCopyrightDTO;
import com.smate.web.v8pub.dto.StandardInfoDTO;
import com.smate.web.v8pub.dto.ThesisInfoDTO;

/**
 * pdwh成果xml转json number 类型的结果，空值都返回0 ；
 * 
 * @author aijiangbin
 * @date 2018年7月11日
 */
public class PdwhPubXMLToJsonStrUtils {

  /**
   * pdwh成果xml转Json
   * 
   * @param pubXml
   * @return
   * @throws Exception
   */
  public static String dealWithXML(String pubXml) {
    Map<String, Object> result = dealWithXMLToMap(pubXml);
    String jsonData = JacksonUtils.mapToJsonStr(result);
    return jsonData;
  }

  /**
   * 检索插件获取的成果xml转Json
   * 
   * @param pubXml 成果xml
   * @param isChina 当前环境是否是中文环境
   * @return
   * @throws Exception
   */
  public static String dealWithXML(String pubXml, boolean isChina) {
    Map<String, Object> result = dealWithXMLToMap(pubXml, isChina);
    return JacksonUtils.mapToJsonStr(result);
  }

  /**
   * pdwh成果xml转Map
   * 
   * @param pubXml
   * @return
   * @throws Exception
   */
  public static Map<String, Object> dealWithXMLToMap(String pubXml) {
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      if (StringUtils.isBlank(pubXml)) {
        return result;
      }
      Document document = DocumentHelper.parseText(pubXml);
      boolean isChina = isChina(document);
      result = dealWithXMLToMap(pubXml, isChina);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }

  /**
   * 检索插件获取的成果xml转Map
   * 
   * @param pubXml 成果xml
   * @param isChina 当前环境是否是中文环境
   * @return
   * @throws Exception
   */
  public static Map<String, Object> dealWithXMLToMap(String pubXml, boolean isChina) {
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      if (StringUtils.isBlank(pubXml)) {
        return result;
      }
      Document document = DocumentHelper.parseText(pubXml);
      Element root = document.getRootElement();
      Element one = root.element("publication");
      if (null == one) {
        return null;
      }
      // 成果基本信息
      result = dealPubBase(one, isChina);

      // 成员信息
      List<PubMemberDTO> pubMembersMapList = dealPubMembers(root);
      result.put("members", pubMembersMapList);

      // 收录信息
      List<PubSituationDTO> pubInfoList = dealpubList(one);
      result.put("situations", pubInfoList);

      int pubType = Integer.parseInt(result.get("pubType").toString());
      Map<String, Object> typeInfo = buildPubTypeInfo(pubXml, pubType, one, isChina);
      result.put("pubTypeInfo", typeInfo);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }

  private static String getSourceDbCode(Element one) {
    String sourceDbCode = one.attributeValue("source_db_code");
    if (StringUtils.isBlank(sourceDbCode)) {
      sourceDbCode = one.attributeValue("source");
    }
    sourceDbCode = StringUtils.trimToEmpty(sourceDbCode);
    if (sourceDbCode.equalsIgnoreCase("SCI") || sourceDbCode.equalsIgnoreCase("SCIE")) {
      sourceDbCode = "SCIE";
    }
    return sourceDbCode;
  }

  public static Map<String, Object> buildPubTypeInfo(String pubXml, Integer pubType, Element publication,
      boolean isChina) {
    Map<String, Object> typeInfo = new HashMap<>();
    try {
      if (publication == null) {
        // 为null则通过传入的pubXml进行解析
        if (StringUtils.isBlank(pubXml)) {
          return typeInfo;
        }
        Document document = DocumentHelper.parseText(pubXml);
        Element root = document.getRootElement();
        publication = root.element("publication");
        if (null == publication) {
          return typeInfo;
        }
      }
      switch (pubType) {
        case PublicationTypeEnum.AWARD:
          typeInfo = dealPubAward(publication);
          break;
        case PublicationTypeEnum.BOOK:
        case PublicationTypeEnum.BOOK_CHAPTER:
          typeInfo = dealPubBook(publication, isChina);
          break;
        case PublicationTypeEnum.CONFERENCE_PAPER:
          typeInfo = dealPubConfPaper(publication);
          break;
        case PublicationTypeEnum.JOURNAL_ARTICLE:
          typeInfo = dealPubJournal(publication);
          break;
        case PublicationTypeEnum.PATENT:
          typeInfo = dealPubPatent(publication);
          break;
        case PublicationTypeEnum.THESIS:
          typeInfo = dealPubThesis(publication);
          break;
        case PublicationTypeEnum.OTHERS:
          typeInfo = dealpubOther(publication);
          break;
        case PublicationTypeEnum.SOFTWARE_COPYRIGHT:
          typeInfo = dealPubSoftwareCopyright(publication);
          break;
        case PublicationTypeEnum.STANDARD:
          typeInfo = dealPubStandard(publication);
          break;
        default:
          typeInfo = dealpubOther(publication);
          break;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return typeInfo;
  }

  private static String getCitedUrl(Element one) {
    String citedUrl = StringUtils.defaultString(one.attributeValue("cite_record_url"));
    String sourceDbCode = getSourceDbCode(one);
    if (StringUtils.isNotBlank(sourceDbCode) && StringUtils.isBlank(citedUrl)) {
      citedUrl = StringUtils.defaultString(one.attributeValue(sourceDbCode.toLowerCase() + "_cite_record_url"));
      if (StringUtils.isBlank(citedUrl)) {
        citedUrl = StringUtils.defaultString(one.attributeValue("tmpcite_record_url"));
      }
    }
    citedUrl = StringUtils.trimToEmpty(citedUrl);
    return citedUrl;
  }

  private static String getPublishDate(Element one, Integer pubType) {
    String publishDate = "";
    if (pubType == PublicationTypeEnum.STANDARD) {
      // 标准，将发布日期，填充至出版日期中
      publishDate = StringUtils.defaultString(one.attributeValue("lblPubDate"));
    } else {
      publishDate = StringUtils.defaultString(one.attributeValue("publish_date"));
      if (StringUtils.isBlank(publishDate)) {
        publishDate = StringUtils.defaultString(one.attributeValue("pubyear"));
      }
    }

    Map<String, String> publishDateMap = DateStringSplitFormateUtil.split(publishDate);
    publishDate = publishDateMap.get("fomate_date");
    return StringUtils.trimToEmpty(publishDate);
  }

  /**
   * 解析成果列表信息
   * 
   * @param one
   * @return
   */
  private static List<PubSituationDTO> dealpubList(Element one) throws Exception {
    List<PubSituationDTO> list = new ArrayList<>();
    PubSituationDTO pubSituationDTO = null;
    // 当前的成果的文献库
    String libraryName = getSourceDbCode(one);
    String srcId = one.attributeValue("source_id");
    srcId = StringUtils.trimToEmpty(srcId);
    String srcUrl = getSourceUrl(one);
    if (StringUtils.isNotBlank(libraryName)) {
      pubSituationDTO = new PubSituationDTO();
      if (libraryName.equalsIgnoreCase("SCI") || libraryName.equalsIgnoreCase("SCIE")) {
        libraryName = "SCIE";
      }
      pubSituationDTO.setLibraryName(libraryName);
      pubSituationDTO.setSitStatus(true);
      pubSituationDTO.setSitOriginStatus(false);
      pubSituationDTO.setSrcId(srcId);
      pubSituationDTO.setSrcUrl(srcUrl);
      list.add(pubSituationDTO);
    }

    // 对citations_index字段进行拆分
    String citationIndex = one.attributeValue("citation_index");
    citationIndex = StringUtils.trimToEmpty(citationIndex);
    if (StringUtils.isNotBlank(citationIndex)) {
      String[] citationIndexs = citationIndex.split(",|;");
      for (String index : citationIndexs) {
        if (index.equalsIgnoreCase("SCI") || index.equalsIgnoreCase("SCIE")) {
          index = "SCIE";
        }
        if (StringUtils.isNotBlank(index) && !libraryName.equalsIgnoreCase(index)) {
          pubSituationDTO = new PubSituationDTO();
          pubSituationDTO.setLibraryName(index);
          pubSituationDTO.setSitStatus(true);
          pubSituationDTO.setSitOriginStatus(false);
          list.add(pubSituationDTO);
        }
      }
    }
    return list;
  }

  private static String getSourceUrl(Element one) {
    String srcUrl = one.attributeValue("source_url");
    if (StringUtils.isBlank(srcUrl)) {
      srcUrl = one.attributeValue("tmpsource_url");
    }
    srcUrl = StringUtils.isBlank(srcUrl) ? "" : srcUrl;
    srcUrl = StringUtils.trimToEmpty(srcUrl);
    return srcUrl;
  }

  /**
   * 解析标准类型成果
   * 
   * @param publication
   * @return
   */
  private static Map<String, Object> dealPubStandard(Element one) {
    StandardInfoDTO standardInfoDTO = new StandardInfoDTO();

    // 标准号
    String standardNo = StringUtils.defaultString(one.attributeValue("lblStdNo"));
    standardInfoDTO.setStandardNo(standardNo);

    // 作废日期
    String obsoleteDate = StringUtils.defaultString(one.attributeValue("lblEndDate"));
    Map<String, String> dateMap1 = DateStringSplitFormateUtil.split(obsoleteDate);
    standardInfoDTO.setObsoleteDate(dateMap1.get("fomate_date"));

    // 实施日期
    String implementDate = StringUtils.defaultString(one.attributeValue("lblActDate"));
    Map<String, String> dateMap2 = DateStringSplitFormateUtil.split(implementDate);
    standardInfoDTO.setImplementDate(dateMap2.get("fomate_date"));

    // ICS分类
    String icsNo = StringUtils.defaultString(one.attributeValue("lblIcs"));
    standardInfoDTO.setIcsNo(icsNo);

    // 中标字段
    String domainNo = StringUtils.defaultString(one.attributeValue("lblCnClass"));
    standardInfoDTO.setDomainNo(domainNo);

    // 标准类型
    String drpType = StringUtils.defaultString(one.attributeValue("drpType"));
    PubStandardTypeEnum standardType = buildStandardType(drpType, standardNo);
    standardInfoDTO.setType(standardType);

    // 起草单位
    String lblDrafter = StringUtils.defaultString(one.attributeValue("lblDrafter"));
    if (standardType.equals(PubStandardTypeEnum.INTERNATIONAL)) {
      // 标准类型是国际，那么填入公布机构
      standardInfoDTO.setPublishAgency(lblDrafter);
    } else {
      // 填入归口单位
      standardInfoDTO.setTechnicalCommittees(lblDrafter);
    }

    String jsonStr = JacksonUtils.jsonObjectSerializer(standardInfoDTO);
    Map<String, Object> map = JacksonUtils.json2HashMap(jsonStr);
    return map;
  }

  /**
   * standardNo 标准号（GB 为强制标准，GB/T为推荐标准）
   * 
   * @param type
   * @param standardNo
   * @return
   */
  private static PubStandardTypeEnum buildStandardType(String drpType, String standardNo) {
    String tType = StringUtils.substring(standardNo, 0, standardNo.indexOf(" "));
    if (StringUtils.isBlank(tType)) {
      return PubStandardTypeEnum.OTHER;
    }
    if (StringUtils.isBlank(drpType)) {
      return PubStandardTypeEnum.OTHER;
    }
    drpType = drpType.replace("中国", "");
    if (drpType.equals("国家标准") || drpType.equals("行业标准")) {
      drpType += tType.contains("/T") ? "（推荐）" : "（强制）";
    }
    return PubStandardTypeEnum.parse(drpType);
  }

  public static void main(String[] args) {
    System.out.println(buildStandardType("中国行业标准", "AQ/T 2060-2016"));
  }

  /**
   * 解析软件著作权成果
   * 
   * @param publication
   * @return
   */
  private static Map<String, Object> dealPubSoftwareCopyright(Element one) {
    SoftwareCopyrightDTO softwareCopyrightDTO = new SoftwareCopyrightDTO();

    // 获得方式 -- 目前字段从xml中无法获取
    String acquisitionType = StringUtils.defaultString(one.attributeValue("acquisition_type"));
    softwareCopyrightDTO.setAcquisitionType(buildAcquisitionType(acquisitionType));

    // 权利范围 -- 目前字段从xml中无法获取
    String scopeType = StringUtils.defaultString(one.attributeValue("scope_type"));
    softwareCopyrightDTO.setScopeType(buildScopeType(scopeType));

    // 登记号
    String registerNo = StringUtils.defaultString(one.attributeValue("registration_number"));
    softwareCopyrightDTO.setRegisterNo(registerNo);

    // 登记日期
    String registerDate = StringUtils.defaultString(one.attributeValue("date_of_registration"));
    Map<String, String> dateMap = DateStringSplitFormateUtil.split(registerDate);
    softwareCopyrightDTO.setRegisterDate(dateMap.get("fomate_date"));

    // 公示日期
    String publicityDate = StringUtils.defaultString(one.attributeValue("date_of_publicity"));
    Map<String, String> dateMap1 = DateStringSplitFormateUtil.split(publicityDate);
    softwareCopyrightDTO.setPublicityDate(dateMap1.get("fomate_date"));

    // 首次发表日期
    String firstPublishDate = StringUtils.defaultString(one.attributeValue("first_publication_date"));
    firstPublishDate = StringUtils.replace(firstPublishDate, "未发表", "");
    Map<String, String> dateMap2 = DateStringSplitFormateUtil.split(firstPublishDate);
    softwareCopyrightDTO.setFirstPublishDate(dateMap2.get("fomate_date"));

    // 分类号
    String categoryNo = StringUtils.defaultString(one.attributeValue("journal_category_no"));
    softwareCopyrightDTO.setCategoryNo(categoryNo);

    String jsonStr = JacksonUtils.jsonObjectSerializer(softwareCopyrightDTO);
    Map<String, Object> map = JacksonUtils.json2HashMap(jsonStr);
    return map;
  }


  private static PubSCScopeTypeEnum buildScopeType(String scopeType) {
    return PubSCScopeTypeEnum.parse(scopeType);
  }

  private static PubSCAcquisitionTypeEnum buildAcquisitionType(String acquisitionType) {
    return PubSCAcquisitionTypeEnum.parse(acquisitionType);
  }

  /**
   * 解析成果其他信息
   * 
   * @param one
   * @return
   */
  private static Map<String, Object> dealpubOther(Element one) throws Exception {
    return null;
  }

  /**
   * 解析XX类型成果信息
   * 
   * @param one
   * @return
   */
  private static Map<String, Object> dealPubThesis(Element one) throws Exception {
    ThesisInfoDTO thesisInfoDTO = new ThesisInfoDTO();
    // 学位
    String thesis_programme = StringUtils.defaultString(one.attributeValue("thesis_programme"));
    thesis_programme = StringUtils.trimToEmpty(thesis_programme);
    thesisInfoDTO.setDegree(buildDegree(thesis_programme));
    // 颁发单位
    String issuingAuthority = StringUtils.defaultString(one.attributeValue("thesis_ins_name"));
    issuingAuthority = StringUtils.trimToEmpty(issuingAuthority);
    thesisInfoDTO.setIssuingAuthority(issuingAuthority);
    // 部门
    String department = StringUtils.defaultString(one.attributeValue("thesis_dept_name"));
    department = StringUtils.trimToEmpty(department);
    thesisInfoDTO.setDepartment(department);
    // 答辩日期
    String defenseDate = getPublishDate(one, 8);
    thesisInfoDTO.setDefenseDate(defenseDate);

    String jsonStr = JacksonUtils.jsonObjectSerializer(thesisInfoDTO);
    Map<String, Object> map = JacksonUtils.json2HashMap(jsonStr);
    return map;
  }

  private static PubThesisDegreeEnum buildDegree(String thesis_programme) {
    if (StringUtils.isBlank(thesis_programme)) {
      return PubThesisDegreeEnum.OTHER;
    }
    if ("硕士".equalsIgnoreCase(thesis_programme)) {
      return PubThesisDegreeEnum.MASTER;
    }
    if ("博士".equalsIgnoreCase(thesis_programme)) {
      return PubThesisDegreeEnum.DOCTOR;
    }
    if ("其他".equalsIgnoreCase(thesis_programme)) {
      return PubThesisDegreeEnum.OTHER;
    }
    return PubThesisDegreeEnum.OTHER;
  }

  /**
   * 解析专利类型成果信息
   * 
   * @param one
   * @return
   */
  private static Map<String, Object> dealPubPatent(Element one) throws Exception {
    PatentInfoDTO patentInfoDTO = new PatentInfoDTO();

    // 申请(专利)号
    String patent_no = StringUtils.defaultString(one.attributeValue("patent_no"));
    if (StringUtils.isBlank(patent_no)) {
      patent_no = StringUtils.defaultString(one.attributeValue("patent_reg_no"));
    }
    patent_no = StringUtils.trimToEmpty(patent_no);
    patentInfoDTO.setApplicationNo(patent_no);

    // 公开（公告）号
    String patent_open_no = StringUtils.defaultString(one.attributeValue("patent_open_no"));
    patent_open_no = StringUtils.trimToEmpty(patent_open_no);
    patentInfoDTO.setPublicationOpenNo(patent_open_no);

    // 主分类号
    String IPC = StringUtils.defaultString(one.attributeValue("patent_main_category_no"));
    if (StringUtils.isBlank(IPC)) {
      IPC = StringUtils.defaultString(one.attributeValue("patent_category_no"));
    }
    IPC = StringUtils.trimToEmpty(IPC);
    patentInfoDTO.setIPC(IPC);

    // CPC号
    String CPC = StringUtils.defaultString(one.attributeValue("cpc_no"));
    patentInfoDTO.setCPC(CPC);

    // 专利类别 数字 51 52
    String patent_category = StringUtils.defaultString(one.attributeValue("patent_category"));
    patent_category = StringUtils.trimToEmpty(patent_category);
    patentInfoDTO.setType(patent_category);

    // 生效开始日期
    String effective_start_date = StringUtils.defaultString(one.attributeValue("effective_start_date"));
    effective_start_date = StringUtils.trimToEmpty(effective_start_date);
    effective_start_date = effective_start_date.replace("/", "-").replace(".", "-");
    patentInfoDTO.setStartDate(effective_start_date);

    // 生效截止日期
    String endDate = StringUtils.defaultString(one.attributeValue("effective_end_date"));
    if (StringUtils.isBlank(endDate)) {
      endDate = StringUtils.defaultString(one.attributeValue("end_date"));
    }
    endDate = StringUtils.trimToEmpty(endDate);
    endDate = endDate.replace("/", "-").replace(".", "-");
    patentInfoDTO.setEndDate(endDate);

    // 发证单位
    String issuingAuthority = StringUtils.defaultString(one.attributeValue("patent_agent_org"));
    issuingAuthority = StringUtils.trimToEmpty(issuingAuthority);

    // 专利地区
    PubPatentAreaEnum area = buildArea(patent_open_no);
    patentInfoDTO.setArea(area);
    patentInfoDTO.setIssuingAuthority(buildIssuingAuthority(area));

    // 专利授权人/申请人
    String applier = StringUtils.defaultString(one.attributeValue("organization"));
    applier = StringUtils.trimToEmpty(applier);

    // 专利公开日
    String patent_issue_date = StringUtils.defaultString(one.attributeValue("patent_issue_date"));
    patent_issue_date = StringUtils.trimToEmpty(patent_issue_date);
    patent_issue_date = patent_issue_date.replace("/", "-").replace(".", "-");

    // 申请日期-xml中的start_date
    String start_date = StringUtils.defaultString(one.attributeValue("start_date"));
    start_date = StringUtils.trimToEmpty(start_date);
    start_date = start_date.replace("/", "-").replace(".", "-");

    // 专利状态(0申请，1授权)
    if (StringUtils.isNotBlank(effective_start_date)) {
      patentInfoDTO.setStatus(1);
      patentInfoDTO.setPatentee(applier);
      patentInfoDTO.setApplicationDate(patent_issue_date);
    } else {
      patentInfoDTO.setStatus(0);
      patentInfoDTO.setApplier(applier);
      patentInfoDTO.setApplicationDate(start_date);
    }

    // 专利转化状态
    String transitionStatus = StringUtils.defaultString(one.attributeValue("patent_transition_status"));
    transitionStatus = StringUtils.trimToEmpty(transitionStatus);
    patentInfoDTO.setTransitionStatus(buildTransitionStatus(transitionStatus));
    // 交易金额 amount
    String price = StringUtils.defaultString(one.attributeValue("amount"));
    if (StringUtils.isBlank(price)) {
      price = StringUtils.defaultString(one.attributeValue("patent_price"));
    }
    price = StringUtils.trimToEmpty(price);
    patentInfoDTO.setPrice(price);

    String jsonStr = JacksonUtils.jsonObjectSerializer(patentInfoDTO);
    Map<String, Object> map = JacksonUtils.json2HashMap(jsonStr);
    return map;
  }

  private static String buildIssuingAuthority(PubPatentAreaEnum area) {
    String flag = area.getValue();
    switch (flag) {
      case "CHINA":
        return "国家知识产权局(SIPO)";
      case "USA":
        return "United States Patent and Trademark Office(USPTO)";
      case "EUROPE":
        return "European Patent Office (EPO)";
      case "JAPAN":
        return "Japan Patent Office(JPO)";
      case "WIPO":
        return "World Intellectual property organization";
      case "OTHER":
        return "";
    }
    return "";
  }

  /**
   * 通过申请号前两位判断 EP 欧州 US 美国 JP 日本 CN 中国 WO WIPO
   * 
   * @param patent_no
   * @return
   */
  private static PubPatentAreaEnum buildArea(String patent_open_no) {
    if (StringUtils.isBlank(patent_open_no)) {
      return PubPatentAreaEnum.OTHER;
    }
    patent_open_no = patent_open_no.toUpperCase();
    if (patent_open_no.contains("US")) {
      return PubPatentAreaEnum.USA;
    }
    if (patent_open_no.contains("WO")) {
      return PubPatentAreaEnum.WIPO;
    }
    if (patent_open_no.contains("EP")) {
      return PubPatentAreaEnum.EUROPE;
    }
    if (patent_open_no.contains("JP")) {
      return PubPatentAreaEnum.JAPAN;
    }
    if (patent_open_no.contains("CN")) {
      return PubPatentAreaEnum.CHINA;
    }
    return PubPatentAreaEnum.OTHER;
  }

  private static PubPatentTransitionStatusEnum buildTransitionStatus(String transitionStatus) {
    if (StringUtils.isBlank(transitionStatus)) {
      return PubPatentTransitionStatusEnum.NONE;
    }
    if ("assigned".equalsIgnoreCase(transitionStatus)) {
      return PubPatentTransitionStatusEnum.TRANSFER;
    }
    if ("licensed".equalsIgnoreCase(transitionStatus)) {
      return PubPatentTransitionStatusEnum.LICENCE;
    }
    if ("evaluation_investment".equalsIgnoreCase(transitionStatus)) {
      return PubPatentTransitionStatusEnum.INVESTMENT;
    }
    if ("none".equalsIgnoreCase(transitionStatus)) {
      return PubPatentTransitionStatusEnum.NONE;
    }
    return PubPatentTransitionStatusEnum.NONE;
  }

  /**
   * 解析期刊类型成果信息
   * 
   * @param one
   * @return
   */
  private static Map<String, Object> dealPubJournal(Element one) throws Exception {
    JournalInfoDTO journalInfoDTO = new JournalInfoDTO();
    // 期刊ID
    String jid = StringUtils.defaultString(one.attributeValue("jid"));
    if (StringUtils.isBlank(jid)) {
      journalInfoDTO.setJid(null);
    } else {
      journalInfoDTO.setJid(NumberUtils.toLong(jid));
    }
    String original = StringUtils.defaultString(one.attributeValue("original"));
    original = HtmlUtils.htmlUnescape(original);
    original = StringUtils.trimToEmpty(original);
    journalInfoDTO.setName(original);
    // 发表状态(P已发表/A已接收),默认已发表
    String publishStatus = StringUtils.defaultString(one.attributeValue("publish_state"));
    publishStatus = StringUtils.trimToEmpty(publishStatus);
    journalInfoDTO.setPublishStatus(StringUtils.isBlank(publishStatus) ? "P" : publishStatus);
    // 期号
    String volumeNo = StringUtils.defaultString(one.attributeValue("volume"));
    volumeNo = StringUtils.trimToEmpty(volumeNo);
    journalInfoDTO.setVolumeNo(volumeNo);
    // 卷号
    String issue = StringUtils.defaultString(one.attributeValue("issue"));
    issue = StringUtils.trimToEmpty(issue);
    journalInfoDTO.setIssue(issue);
    // 开始页码
    String startPage = StringUtils.defaultString(one.attributeValue("start_page"));
    // 结束页码
    String endPage = StringUtils.defaultString(one.attributeValue("end_page"));
    // 文章号
    String article_number = StringUtils.defaultString(one.attributeValue("article_number"));
    String pageNumber = PubParamUtils.buildPageNumber(startPage, endPage, article_number);
    pageNumber = StringUtils.trimToEmpty(pageNumber);
    journalInfoDTO.setPageNumber(pageNumber);
    // issn
    String issn = StringUtils.defaultString(one.attributeValue("issn"));
    issn = StringUtils.trimToEmpty(issn);
    journalInfoDTO.setISSN(issn);

    String jsonStr = JacksonUtils.jsonObjectSerializer(journalInfoDTO);
    Map<String, Object> map = JacksonUtils.json2HashMap(jsonStr);
    return map;
  }

  /**
   * 解析XX类型成果信息
   * 
   * @param root
   * @return
   */
  private static Map<String, Object> dealPubConfPaper(Element one) throws Exception {
    ConferencePaperDTO conferencePaperDTO = new ConferencePaperDTO();
    // 论文类别
    String paperType = StringUtils.defaultString(one.attributeValue("paper_type"));
    if (StringUtils.isBlank(paperType)) {
      paperType = StringUtils.defaultString(one.attributeValue("conf_type"));
    }
    paperType = StringUtils.trimToEmpty(paperType);
    conferencePaperDTO.setPaperType(buildPaperType(paperType));
    // 论文集名 original
    String papers = StringUtils.defaultString(one.attributeValue("original"));
    conferencePaperDTO.setPapers(papers);
    // 会议名称
    String name = StringUtils.defaultString(one.attributeValue("proceeding_title"));
    name = HtmlUtils.htmlUnescape(name);
    name = StringUtils.trimToEmpty(name);
    conferencePaperDTO.setName(name);
    // 会议组织者
    String organizer = StringUtils.defaultString(one.attributeValue("organizer"));
    organizer = StringUtils.trimToEmpty(organizer);
    conferencePaperDTO.setOrganizer(organizer);
    // 开始日期
    String startDate = StringUtils.defaultString(one.attributeValue("start_date"));
    startDate = StringUtils.trimToEmpty(startDate);
    startDate = startDate.replace("/", "-").replace(".", "-");
    conferencePaperDTO.setStartDate(startDate);
    // 结束日期
    String endDate = StringUtils.defaultString(one.attributeValue("end_date"));
    endDate = StringUtils.trimToEmpty(endDate);
    endDate = endDate.replace("/", "-").replace(".", "-");
    conferencePaperDTO.setEndDate(endDate);
    // 开始页码
    String startPage = StringUtils.defaultString(one.attributeValue("start_page"));
    // 结束页码
    String endPage = StringUtils.defaultString(one.attributeValue("end_page"));
    String pageNumber = PubParamUtils.buildPageNumber(startPage, endPage, "");
    pageNumber = StringUtils.trimToEmpty(pageNumber);
    conferencePaperDTO.setPageNumber(pageNumber);
    String jsonStr = JacksonUtils.jsonObjectSerializer(conferencePaperDTO);
    Map<String, Object> map = JacksonUtils.json2HashMap(jsonStr);
    return map;
  }

  private static PubConferencePaperTypeEnum buildPaperType(String paperType) {
    if (StringUtils.isBlank(paperType)) {
      return PubConferencePaperTypeEnum.NULL;
    }
    if ("A".equalsIgnoreCase(paperType)) {
      return PubConferencePaperTypeEnum.INVITED;
    }
    if ("E".equalsIgnoreCase(paperType)) {
      return PubConferencePaperTypeEnum.GROUP;
    }
    if ("C".equalsIgnoreCase(paperType)) {
      return PubConferencePaperTypeEnum.POSTER;
    }
    return PubConferencePaperTypeEnum.NULL;
  }

  /**
   * 解析XX类型成果信息 书籍章节和书著有点区别？？？
   * 
   * @param one
   * @return
   */
  private static Map<String, Object> dealPubBook(Element one, boolean isChina) throws Exception {
    BookInfoDTO bookInfoDTO = new BookInfoDTO();
    // 书名
    String name = StringUtils.defaultString(one.attributeValue("book_title"));
    name = HtmlUtils.htmlUnescape(name);
    name = StringUtils.trimToEmpty(name);
    bookInfoDTO.setName(name);
    // 书籍类型 no
    String type = StringUtils.defaultString(one.attributeValue("book_type"));
    type = StringUtils.trimToEmpty(type);
    bookInfoDTO.setType(buildBookType(type));
    // 出版社
    String publisher = StringUtils.defaultString(one.attributeValue("publisher"));
    publisher = StringUtils.trimToEmpty(publisher);
    bookInfoDTO.setPublisher(publisher);
    // 页数 no
    Integer totalPages = NumberUtils.toInt(one.attributeValue("total_pages"), 0);
    bookInfoDTO.setTotalPages(totalPages);
    // 总字数 no
    Integer totalWords = NumberUtils.toInt(one.attributeValue("total_words"), 0);
    bookInfoDTO.setTotalWords(totalWords);
    // 丛书名
    String seriesName = StringUtils.defaultString(one.attributeValue("series_name"));
    seriesName = HtmlUtils.htmlUnescape(seriesName);
    seriesName = StringUtils.trimToEmpty(seriesName);
    bookInfoDTO.setSeriesName(seriesName);
    // 书籍编辑
    String editors = StringUtils.defaultString(one.attributeValue("editors"));
    editors = StringUtils.trimToEmpty(editors);
    bookInfoDTO.setEditors(editors);
    // 章节号码
    String chapterNo = StringUtils.defaultString(one.attributeValue("chapter_no"));
    chapterNo = StringUtils.trimToEmpty(chapterNo);
    bookInfoDTO.setChapterNo(chapterNo);
    // 开始页码
    String startPage = StringUtils.defaultString(one.attributeValue("start_page"));
    // 结束页码
    String endPage = StringUtils.defaultString(one.attributeValue("end_page"));
    // 文章号
    String articleNo = StringUtils.defaultString(one.attributeValue("article_number"));
    String pageNumber = PubParamUtils.buildPageNumber(startPage, endPage, articleNo);
    pageNumber = StringUtils.trimToEmpty(pageNumber);
    bookInfoDTO.setPageNumber(pageNumber);
    // 书籍isbn
    String ISBN = StringUtils.defaultString(one.attributeValue("isbn"));
    ISBN = StringUtils.trimToEmpty(ISBN);
    bookInfoDTO.setISBN(ISBN);
    // 语种
    String language = getLanguage(one, isChina);
    bookInfoDTO.setLanguage(language);

    String jsonStr = JacksonUtils.jsonObjectSerializer(bookInfoDTO);
    Map<String, Object> map = JacksonUtils.json2HashMap(jsonStr);
    return map;
  }

  /**
   * 构建language对象
   *
   * @param one
   * @return
   */
  private static String getLanguage(Element one, boolean isChina) {
    String language = "";
    if (null == one) {
      return language;
    }
    String ctitle = one.attributeValue("ctitle");
    String etitle = one.attributeValue("etitle");
    String title =
        isChina ? StringUtils.isNotBlank(ctitle) ? ctitle : etitle : StringUtils.isNotBlank(etitle) ? etitle : ctitle;
    title = HtmlUtils.htmlUnescape(title);
    title = StringUtils.trimToEmpty(title);
    Locale locale = getLocale(title);
    if (Locale.CHINA.equals(locale)) {
      language = XmlUtil.getLanguageSpecificText(locale.getLanguage(), "中文", "Chinese");
    } else {
      language = XmlUtil.getLanguageSpecificText(locale.getLanguage(), "外文", "Foreign Language");
    }
    return language;
  }

  private static Locale getLocale(String title) {
    if (StringUtils.isEmpty(title)) {
      return Locale.US;
    }
    Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
    if (p.matcher(title).find()) {
      return Locale.CHINA;
    }
    return Locale.US;
  }

  private static PubBookTypeEnum buildBookType(String bookType) {
    if (StringUtils.isBlank(bookType)) {
      return PubBookTypeEnum.NULL;
    }
    if ("11".equalsIgnoreCase(bookType)) {
      return PubBookTypeEnum.MONOGRAPH;
    }
    if ("13".equalsIgnoreCase(bookType)) {
      return PubBookTypeEnum.TEXTBOOK;
    }
    if ("14".equalsIgnoreCase(bookType)) {
      return PubBookTypeEnum.COMPILE;
    }
    return PubBookTypeEnum.NULL;
  }

  /**
   * 解析成果奖励信息
   * 
   * @param root
   * @return
   */
  private static Map<String, Object> dealPubAward(Element one) throws Exception {
    AwardsInfoDTO awardsInfoDTO = new AwardsInfoDTO();
    // 奖励类别
    String award_category = StringUtils.defaultString(one.attributeValue("award_category"));
    award_category = StringUtils.trimToEmpty(award_category);
    awardsInfoDTO.setCategory(award_category);
    // 奖励等级
    String award_grade = StringUtils.defaultString(one.attributeValue("award_grade"));
    award_grade = StringUtils.trimToEmpty(award_grade);
    awardsInfoDTO.setGrade(award_grade);
    // 颁发机构
    String issuingAuthority = StringUtils.defaultString(one.attributeValue("issue_ins_name"));
    issuingAuthority = StringUtils.trimToEmpty(issuingAuthority);
    awardsInfoDTO.setIssuingAuthority(issuingAuthority);
    // 颁发机构ID
    String issueInsId = StringUtils.defaultString(one.attributeValue("issue_ins_id"));
    issueInsId = StringUtils.trimToEmpty(issueInsId);
    if (StringUtils.isBlank(issueInsId)) {
      awardsInfoDTO.setIssueInsId(null);
    } else {
      awardsInfoDTO.setIssueInsId(NumberUtils.toLong(issueInsId));
    }
    // 证书编号
    String certificateNo = StringUtils.defaultString(one.attributeValue("serial_number"));
    certificateNo = StringUtils.trimToEmpty(certificateNo);
    awardsInfoDTO.setCertificateNo(certificateNo);
    // 授奖日期
    String awardDate = getPublishDate(one, 1);
    awardsInfoDTO.setAwardDate(awardDate);

    String jsonStr = JacksonUtils.jsonObjectSerializer(awardsInfoDTO);
    Map<String, Object> map = JacksonUtils.json2HashMap(jsonStr);
    return map;
  }

  /**
   * 解析成果基本信息
   * 
   * @param one
   * @return
   */
  private static Map<String, Object> dealPubBase(Element one, boolean isChina) throws Exception {
    Map<String, Object> map = new HashMap<String, Object>();
    // 中文标题//外文标题
    String title = buildPubTile(one, isChina);
    title = HtmlUtils.htmlUnescape(title);
    title = StringUtils.trimToEmpty(title);
    map.put("title", title);

    // 中文摘要//外文摘要
    String summary = buildPubSummary(one, isChina);
    summary = HtmlUtils.htmlUnescape(summary);
    summary = StringUtils.trimToEmpty(summary);
    summary = summary.replace("<p>", "<br/>");
    map.put("summary", summary);

    // 中文关键词//外文关键词
    String ckeywords = one.attributeValue("ckeywords");
    String ekeywords = one.attributeValue("ekeywords");
    String keywords = isChina ? StringUtils.isNotBlank(ckeywords) ? ckeywords : ekeywords
        : StringUtils.isNotBlank(ekeywords) ? ekeywords : ckeywords;
    keywords = StringUtils.trimToEmpty(keywords);
    map.put("keywords", keywords);

    // 国家或地区ID 只有国家或地区名，需要在外面进行匹配才能获取到country_id
    String country = StringUtils.defaultString(one.attributeValue("country"));
    String city = StringUtils.defaultString(one.attributeValue("city"));
    country = StringUtils.trimToEmpty(country);
    city = StringUtils.trimToEmpty(city);
    map.put("country", country);
    map.put("city", city);

    // 作者
    // (ISI author_names存的是英文 author_names_spec存的是中文)
    // (CNKI author_names存的是中文 author_names_spec存的是英文)
    String author_names = StringUtils.defaultString(one.attributeValue("author_names"));
    String author_names_spec = StringUtils.defaultString(one.attributeValue("authors_names_spec"));
    String authorNames = getAuthorNames(author_names, author_names_spec, isChina);
    authorNames = buildAuthorNames(one, authorNames);
    map.put("authorNames", authorNames);

    // 作者简称
    String authorNamesAbbr = StringUtils.defaultString(one.attributeValue("author_names_abbr"));
    authorNamesAbbr = StringUtils.trimToEmpty(authorNamesAbbr);
    authorNamesAbbr = authorNamesAbbr.replace("　", "");
    map.put("authorNamesAbbr", authorNamesAbbr);

    // doi
    String doi = StringUtils.defaultString(one.attributeValue("doi"));
    doi = StringUtils.trimToEmpty(doi);
    map.put("doi", doi);

    // 基金标注
    String fundInfo = StringUtils.defaultString(one.attributeValue("fundinfo"));
    fundInfo = StringUtils.trimToEmpty(fundInfo);
    map.put("fundInfo", fundInfo);

    // "organization": "单位地址信息",
    String organization = StringUtils.defaultString(one.attributeValue("organization"));
    if (StringUtils.isBlank(organization)) {
      organization = StringUtils.defaultString(one.attributeValue("organization_spec"));
    }
    organization = StringUtils.trimToEmpty(organization);
    map.put("organization", organization);

    // 来源全文路径
    String srcFulltextUrl = StringUtils.defaultString(one.attributeValue("fulltext_url"));
    srcFulltextUrl = StringUtils.trimToEmpty(srcFulltextUrl);
    map.put("srcFulltextUrl", srcFulltextUrl);

    // citedUrl
    map.put("citedUrl", getCitedUrl(one));
    // sourceUrl
    String sourceUrl = getSourceUrl(one);
    map.put("sourceUrl", sourceUrl);

    // 成果类型
    Integer pubType = NumberUtils.toInt(one.attributeValue("pub_type"), 7);
    map.put("pubType", pubType);

    // srcDbId
    String sourceDbCode = getSourceDbCode(one);
    map.put("srcDbId", sourceDbCode);

    // citations 引用次数
    Integer citations = getCitations(one);
    map.put("citations", citations);

    // publishDate
    map.put("publishDate", getPublishDate(one, pubType));

    // source_id
    String sourceId = StringUtils.defaultString(one.attributeValue("source_id"));
    sourceId = StringUtils.trimToEmpty(sourceId);
    map.put("sourceId", sourceId);

    // HCP 高被引字段
    Integer HCP = NumberUtils.toInt(one.attributeValue("HCP"), 0);
    map.put("HCP", HCP);

    // HP 热门文章
    Integer HP = NumberUtils.toInt(one.attributeValue("HP"), 0);
    map.put("HP", HP);

    // OA Open Access
    String OA = StringUtils.defaultString(one.attributeValue("OA"));
    map.put("OA", OA);

    // 国家或地区ID 只有国家或地区名，需要在外面进行匹配才能获取到country_id
    country = StringUtils.defaultString(one.attributeValue("country"));
    city = StringUtils.defaultString(one.attributeValue("city"));
    country = StringUtils.trimToEmpty(country);
    city = StringUtils.trimToEmpty(city);
    map.put("country", country);
    map.put("city", city);

    // 成果是会议论文，取字段conf_venue
    if (pubType != null && pubType == 3) {
      String conf_venue = StringUtils.defaultString(one.attributeValue("conf_venue"));
      if (StringUtils.isEmpty(country)) {
        map.put("country", conf_venue);
      } else if (StringUtils.isEmpty(city)) {
        map.put("city", conf_venue);
      }
    }

    return map;
  }

  private static String buildAuthorNames(Element one, String authorNames) {
    Integer pubType = NumberUtils.toInt(one.attributeValue("pub_type"), 7);
    if (pubType == PublicationTypeEnum.STANDARD) {
      // 标准，起草人就为作者
      return StringUtils.defaultString(one.attributeValue("lblDrafterMan"));
    }
    if (pubType == PublicationTypeEnum.SOFTWARE_COPYRIGHT) {
      // 软件著作权，著作权人就为作者
      return StringUtils.defaultString(one.attributeValue("copyright_owner"));
    }
    return authorNames;
  }

  private static String buildPubSummary(Element one, boolean isChina) {
    Integer pubType = NumberUtils.toInt(one.attributeValue("pub_type"), 7);
    if (pubType == PublicationTypeEnum.STANDARD) {
      // 标准，将范围信息填充至摘要信息中
      String lblStdBound = StringUtils.defaultString(one.attributeValue("lblStdBound"));
      return lblStdBound;
    }
    String cabstract = one.attributeValue("cabstract");
    String eabstract = one.attributeValue("eabstract");
    String summary = isChina ? StringUtils.isNotBlank(cabstract) ? cabstract : eabstract
        : StringUtils.isNotBlank(eabstract) ? eabstract : cabstract;
    return summary;
  }

  /**
   * 主要是为了构建标准和软件著作权的标题信息
   * 
   * @param one
   * @return
   */
  private static String buildPubTile(Element one, boolean isChina) {
    Integer pubType = NumberUtils.toInt(one.attributeValue("pub_type"), 7);
    if (pubType == PublicationTypeEnum.STANDARD) {
      // 标准
      String lblCnName = StringUtils.defaultString(one.attributeValue("ctitle"));
      String lblEnName = StringUtils.defaultString(one.attributeValue("etitle"));
      return StringUtils.isNotBlank(lblCnName) ? lblCnName : lblEnName;
    }
    if (pubType == PublicationTypeEnum.SOFTWARE_COPYRIGHT) {
      String softwareName = StringUtils.defaultString(one.attributeValue("full_name_of_software"));
      return softwareName;
    }
    String ctitle = one.attributeValue("ctitle");
    String etitle = one.attributeValue("etitle");
    String title =
        isChina ? StringUtils.isNotBlank(ctitle) ? ctitle : etitle : StringUtils.isNotBlank(etitle) ? etitle : ctitle;
    return title;
  }

  // isChina true且author_names 是中文数据，则取author_names数据
  // isChina false且author_names 是中文数据，则取author_names_spec数据
  // isChina true且author_names 是英文数据，则取author_names_spec数据
  // isChina false且author_names 是英文数据，则取author_names数据
  private static String getAuthorNames(String author_names, String author_names_spec, boolean isChina) {
    String authorNames = "";
    if (isChina) {
      // 取中文
      if ("zh".equalsIgnoreCase(contentLocale(author_names))) {
        authorNames = StringUtils.isNotBlank(author_names) ? author_names : author_names_spec;
      } else {
        authorNames = StringUtils.isNotBlank(author_names_spec) ? author_names_spec : author_names;
      }
    } else {
      // 取英文
      if ("en".equalsIgnoreCase(contentLocale(author_names))) {
        authorNames = StringUtils.isNotBlank(author_names) ? author_names : author_names_spec;
      } else {
        authorNames = StringUtils.isNotBlank(author_names_spec) ? author_names_spec : author_names;
      }
    }

    authorNames = StringUtils.trimToEmpty(authorNames);
    authorNames = HtmlUtils.htmlUnescape(authorNames);
    // 去除特殊空格
    authorNames = authorNames.replace("　", "");
    return authorNames;
  }

  private static Integer getCitations(Element one) {
    String sourceDbCode = getSourceDbCode(one);
    Integer cite_times = NumberUtils.toInt(one.attributeValue("cite_times"));
    Integer ei_cite_times = NumberUtils.toInt(one.attributeValue("ei_cite_times"));
    Integer cnki_cite_times = NumberUtils.toInt(one.attributeValue("cnki_cite_times"));
    Integer cnipr_cite_times = NumberUtils.toInt(one.attributeValue("cnipr_cite_times"));
    Integer citaions = cite_times;
    if (StringUtils.isNotBlank(sourceDbCode)) {
      if (PubParamUtils.isISIDbCode(sourceDbCode)) {
        citaions = cite_times;
      }
      if (PubParamUtils.isEIDbCode(sourceDbCode)) {
        citaions = ei_cite_times;
      }
      if (PubParamUtils.isCNKIDbCode(sourceDbCode)) {
        citaions = cnki_cite_times;
      }
      if (PubParamUtils.isCNIPRDbCode(sourceDbCode)) {
        citaions = cnipr_cite_times;
      }
    }
    citaions = (citaions == null) ? 0 : citaions;
    return citaions;
  }

  /**
   * 解析成果成员信息
   * 
   * @param root
   * @return
   */
  @SuppressWarnings("unchecked")
  private static List<PubMemberDTO> dealPubMembers(Element root) throws Exception {
    PubMemberDTO pubMemberDTO = null;
    List<PubMemberDTO> list = new ArrayList<>();
    Element pub_members = root.element("pub_authors");
    if (pub_members == null) {
      return list;
    }
    List<Element> elements = pub_members.elements("author");
    if (elements != null && elements.size() > 0) {
      int index = 1;
      for (Element one : elements) {
        pubMemberDTO = new PubMemberDTO();
        // 作者唯一标识
        pubMemberDTO.setPsnId(NumberUtils.toLong(one.attributeValue("pm_id")));
        // 第一作者，作者编号为1的就是第一作者
        boolean first_author = false;
        if (index == 1) {
          first_author = true;
        }
        pubMemberDTO.setFirstAuthor(first_author);
        // 顺序号
        pubMemberDTO.setSeqNo(index++);
        // 作者名
        String name = StringUtils.defaultString(one.attributeValue("au"));
        name = StringUtils.trimToEmpty(name);
        pubMemberDTO.setName(name);
        // 作者Email
        pubMemberDTO.setEmail(StringUtils.defaultString(one.attributeValue("email")));
        // 作者身份
        String author_pos = StringUtils.defaultString(one.attributeValue("author_pos"));
        author_pos = StringUtils.trimToEmpty(author_pos);
        pubMemberDTO.setCommunicable("1".equals(author_pos) ? true : false);
        // 机构名称
        pubMemberDTO.setInsNames(new ArrayList<MemberInsDTO>());
        // 作者单位
        String dept = StringUtils.defaultString(one.attributeValue("dept"));
        pubMemberDTO.setDept(dept);

        list.add(pubMemberDTO);
      }
    }
    return list;
  }

  /**
   * 基准库中英文选取原则
   * 
   * @param one
   * @return
   */
  private static boolean isChina(Document document) {
    Element root = document.getRootElement();
    Element one = root.element("publication");
    if (null == one) {
      return false;
    }
    String sourceDbCode = getSourceDbCode(one);
    // 标准一 ：CNKI的成果只取中文
    if ("CNKIPAT".equalsIgnoreCase(sourceDbCode) || "CHINAJOURNAL".equalsIgnoreCase(sourceDbCode)) {
      // CNKI的成果
      return true;
    }
    if ("BAITEN".equalsIgnoreCase(sourceDbCode)) {
      return true;
    }
    if ("WanFang".equalsIgnoreCase(sourceDbCode)) {
      return true;
    }
    return false;
  }

  public static String contentLocale(String content) {
    if (StringUtils.isBlank(content)) {
      // 为空默认英文
      return "en";
    }
    Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
    if (p.matcher(content).find()) {
      return "zh";
    }
    return "en";
  }
}
