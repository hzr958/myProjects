package com.smate.center.task.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.smate.core.base.pub.util.PubParamUtils;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.sie.core.base.utils.pub.dom.PatAppliersBean;
import com.smate.sie.core.base.utils.pub.dto.AwardsInfoDTO;
import com.smate.sie.core.base.utils.pub.dto.BookInfoDTO;
import com.smate.sie.core.base.utils.pub.dto.ConferencePaperDTO;
import com.smate.sie.core.base.utils.pub.dto.JournalInfoDTO;
import com.smate.sie.core.base.utils.pub.dto.MemberInsDTO;
import com.smate.sie.core.base.utils.pub.dto.PatMemberDTO;
import com.smate.sie.core.base.utils.pub.dto.PatentInfoDTO;
import com.smate.sie.core.base.utils.pub.dto.PubAttachmentsDTO;
import com.smate.sie.core.base.utils.pub.dto.PubMemberDTO;
import com.smate.sie.core.base.utils.pub.dto.PubSituationDTO;
import com.smate.sie.core.base.utils.pub.dto.ThesisInfoDTO;

public class SiePubXMLToJsonUtils {

  /**
   * sie成果xml转Json
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
   * sie成果xml转Map
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
      /* Document document = DocumentHelper.parseText(pubXml); */
      result = dealWithXMLToMapDetail(pubXml);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }

  /**
   * sie成果xml转Map
   * 
   * @param pubXml 成果或专利xml
   * @return
   * @throws Exception
   */
  public static Map<String, Object> dealWithXMLToMapDetail(String pubXml) {
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
      result = dealPubBase(document, one);

      // 收录信息
      List<PubSituationDTO> pubInfoList = dealpubList2(document);
      result.put("situations", pubInfoList);

      // 文件附件 pubAttachments
      List<PubAttachmentsDTO> pubAttasMapList = dealPubAttachments(root);
      result.put("pubAttachments", pubAttasMapList);

      // 成果类型
      Integer pubTypeCode = getPubTypeCode(root);
      result.put("pubTypeCode", pubTypeCode);
      String pubTypeName = getPubTypeName(root);
      result.put("pubTypeName", pubTypeName);

      // 成员信息
      if (pubTypeCode != 5) {
        List<PubMemberDTO> pubMembersMapList = dealPubMembers(root);
        result.put("members", pubMembersMapList);
      } else {
        List<PatMemberDTO> patMembersMapList = dealPatMembers(root);
        result.put("members", patMembersMapList);
      }


      Map<String, Object> typeInfo = buildPubTypeInfo(pubXml, pubTypeCode, root);
      result.put("pubTypeInfo", typeInfo);

    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }

  public static Map<String, Object> buildPubTypeInfo(String pubXml, Integer pubType, Element root) {
    Map<String, Object> typeInfo = new HashMap<>();
    try {
      Element publicationXml = root.element("publication");
      if (publicationXml == null) {
        // 为null则通过传入的pubXml进行解析
        if (StringUtils.isBlank(pubXml)) {
          return typeInfo;
        }
        Document document = DocumentHelper.parseText(pubXml);
        root = document.getRootElement();
        Element publication = root.element("publication");
        if (null == publication) {
          return typeInfo;
        }
      }
      switch (pubType) {
        case 1:
          typeInfo = dealPubAward(root);
          break;
        case 2:
        case 10:
          typeInfo = dealPubBook(root);
          break;
        case 3:
          typeInfo = dealPubConfPaper(root);
          break;
        case 4:
          typeInfo = dealPubJournal(root);
          break;
        case 5:
          typeInfo = dealPubPatent(root);
          break;
        case 8:
          typeInfo = dealPubThesis(root);
          break;
        case 7:
          typeInfo = dealpubOther(root);
          break;
        default:
          typeInfo = dealpubOther(root);
          break;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return typeInfo;
  }

  /**
   * 解析专利类型成果信息
   * 
   * @param one
   * @return
   */
  private static Map<String, Object> dealPubPatent(Element root) throws Exception {
    Element one = root.element("pub_patent");
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
    // 专利类型
    String typeCode = StringUtils.defaultString(one.attributeValue("patent_type"));
    typeCode = StringUtils.trimToEmpty(typeCode);
    patentInfoDTO.setTypeCode(typeCode);
    patentInfoDTO.setTypeName(getPatTypeName(typeCode));

    // 申请日期
    Element pubNodeXml = root.element("publication");
    String applyDate = getPublishDate(pubNodeXml);
    patentInfoDTO.setApplicationDate(applyDate);
    // 法律状态
    String patentStatusCode = StringUtils.defaultString(one.attributeValue("patent_status"));
    patentStatusCode = StringUtils.trimToEmpty(patentStatusCode);
    patentInfoDTO.setPatentStatusCode(patentStatusCode);
    patentInfoDTO.setPatentStatusName(getPatentStatusName(patentStatusCode));

    // 授权号
    String authNo = StringUtils.defaultString(pubNodeXml.attributeValue("auth_no"));
    authNo = StringUtils.trimToEmpty(authNo);
    patentInfoDTO.setAuthNo(authNo);

    // 授权时间
    String authDate = StringUtils.defaultString(pubNodeXml.attributeValue("auth_date"));
    authDate = StringUtils.trimToEmpty(authDate);
    authDate = authDate.replace("/", "-").replace(".", "-");
    patentInfoDTO.setAuthDate(authDate);
    // IPC
    String ipc = StringUtils.defaultString(pubNodeXml.attributeValue("ipc"));
    ipc = StringUtils.trimToEmpty(ipc);
    patentInfoDTO.setIPC(ipc);

    // CPC
    String cpc = StringUtils.defaultString(pubNodeXml.attributeValue("cpc"));
    cpc = StringUtils.trimToEmpty(cpc);
    patentInfoDTO.setCPC(cpc);

    // 证书编号
    String ceritficateNo = StringUtils.defaultString(pubNodeXml.attributeValue("ceritficate_no"));
    ceritficateNo = StringUtils.trimToEmpty(ceritficateNo);
    patentInfoDTO.setCeritficateNo(ceritficateNo);

    // 发证单位
    String issuingAuthority = StringUtils.defaultString(pubNodeXml.attributeValue("issue_org"));
    issuingAuthority = StringUtils.trimToEmpty(issuingAuthority);
    patentInfoDTO.setIssuingAuthority(issuingAuthority);

    // 专利权人
    List<PatAppliersBean> appliers = dealPatAppliers(root);
    patentInfoDTO.setAppliers(appliers);

    String jsonStr = JacksonUtils.jsonObjectSerializer(patentInfoDTO);
    Map<String, Object> map = JacksonUtils.json2HashMap(jsonStr);
    return map;
  }

  @SuppressWarnings("unchecked")
  private static List<PatAppliersBean> dealPatAppliers(Element root) throws Exception {
    PatAppliersBean appliersBean = null;
    List<PatAppliersBean> list = new ArrayList<>();
    Element pat_appliers = root.element("pub_appliers");
    if (pat_appliers == null) {
      return list;
    }
    List<Element> elements = pat_appliers.elements("pub_applier");
    if (elements != null && elements.size() > 0) {
      int index = 1;
      for (Element one : elements) {
        appliersBean = new PatAppliersBean();
        // 顺序号
        appliersBean.setSeqNo(Long.valueOf(index++));
        // 专利权人名称
        String applierName = one.attributeValue("applier_name");
        appliersBean.setApplierName(applierName);
        // 专利权人Id
        String applierIdStr = one.attributeValue("applier_name_id");
        if (StringUtils.isNotBlank(applierIdStr)) {
          Long applierId = Long.valueOf(applierIdStr.trim());
          appliersBean.setApplierId(applierId);
        }

        list.add(appliersBean);
      }
    }
    return list;
  }

  private static String getPatentStatusName(String patentStatusCode) {
    if ("0".equalsIgnoreCase(patentStatusCode)) {
      return "申请";
    } else if ("1".equalsIgnoreCase(patentStatusCode)) {
      return "授权";
    } else if ("2".equalsIgnoreCase(patentStatusCode)) {
      return "视撤";
    } else if ("3".equalsIgnoreCase(patentStatusCode)) {
      return "有效";
    } else if ("4".equalsIgnoreCase(patentStatusCode)) {
      return "失效";
    } else {
      return "";
    }
  }

  private static String getPatTypeName(String patTypeCode) {
    if ("51".equalsIgnoreCase(patTypeCode)) {
      return "发明专利";
    } else if ("52".equalsIgnoreCase(patTypeCode)) {
      return "实用新型";
    } else if ("53".equalsIgnoreCase(patTypeCode)) {
      return "外观设计";
    } else if ("54".equalsIgnoreCase(patTypeCode)) {
      return "国际专利";
    } else {
      return "";
    }
  }

  /**
   * 解析成果其他信息
   * 
   * @param one
   * @return
   */
  private static Map<String, Object> dealpubOther(Element root) throws Exception {
    return null;
  }

  /**
   * 解析学位论文成果信息
   * 
   * @param root
   * @return
   */
  private static Map<String, Object> dealPubThesis(Element root) throws Exception {
    Element one = root.element("pub_thesis");
    ThesisInfoDTO thesisInfoDTO = new ThesisInfoDTO();
    // 学位
    String programme = StringUtils.defaultString(one.attributeValue("programme"));
    programme = StringUtils.trimToEmpty(programme);
    thesisInfoDTO.setDegreeCode(NumberUtils.toInt(programme));
    thesisInfoDTO.setDegreeName(getProgrammeName(NumberUtils.toInt(programme)));

    // 颁发单位
    String issuingAuthority = StringUtils.defaultString(one.attributeValue("issue_org"));
    issuingAuthority = StringUtils.trimToEmpty(issuingAuthority);
    thesisInfoDTO.setIssuingAuthority(issuingAuthority);

    // 部门
    String department = StringUtils.defaultString(one.attributeValue("department"));
    department = StringUtils.trimToEmpty(department);
    thesisInfoDTO.setDepartment(department);

    // 答辩日期(publication节点)
    Element pubNodeXml = root.element("publication");
    String defenseDate = getPublishDate(pubNodeXml);
    thesisInfoDTO.setDefenseDate(defenseDate);

    String jsonStr = JacksonUtils.jsonObjectSerializer(thesisInfoDTO);
    Map<String, Object> map = JacksonUtils.json2HashMap(jsonStr);
    return map;
  }

  private static String getProgrammeName(int degreeCode) {
    if (degreeCode == 1) {
      return "硕士";
    } else if (degreeCode == 2) {
      return "博士";
    } else if (degreeCode == 3) {
      return "其他";
    } else {
      return "";
    }
  }

  /**
   * 解析会议论文类型成果信息
   * 
   * @param root
   * @return
   */
  private static Map<String, Object> dealPubConfPaper(Element root) throws Exception {
    Element one = root.element("pub_conf_paper");
    ConferencePaperDTO conferencePaperDTO = new ConferencePaperDTO();
    // 会议名称
    String name = StringUtils.defaultString(one.attributeValue("conf_name"));
    conferencePaperDTO.setName(name);

    // 会议组织者
    String organizer = StringUtils.defaultString(one.attributeValue("conf_organizer"));
    conferencePaperDTO.setOrganizer(organizer);

    // 会议开始时间
    String startDate = StringUtils.defaultString(one.attributeValue("start_date"));
    startDate = StringUtils.trimToEmpty(startDate);
    startDate = startDate.replace("/", "-").replace(".", "-");
    conferencePaperDTO.setStartDate(startDate);
    // 会议结束时间
    String endDate = StringUtils.defaultString(one.attributeValue("end_date"));
    endDate = StringUtils.trimToEmpty(endDate);
    endDate = endDate.replace("/", "-").replace(".", "-");
    conferencePaperDTO.setEndDate(endDate);
    // 国家(xml->publication节点)
    Element pubNodeXml = root.element("publication");
    String countryName = StringUtils.defaultString(pubNodeXml.attributeValue("country_name"));
    conferencePaperDTO.setCountry(countryName);

    // 城市(xml->publication节点)
    String cityName = StringUtils.defaultString(pubNodeXml.attributeValue("city"));
    conferencePaperDTO.setCity(cityName);

    String jsonStr = JacksonUtils.jsonObjectSerializer(conferencePaperDTO);
    Map<String, Object> map = JacksonUtils.json2HashMap(jsonStr);
    return map;
  }

  /**
   * 解析期刊类型成果信息
   * 
   * @param one
   * @return
   */
  private static Map<String, Object> dealPubJournal(Element root) throws Exception {
    Element pubJourNal = root.element("pub_journal");
    Element publication = root.element("publication");
    JournalInfoDTO journalInfoDTO = new JournalInfoDTO();
    // 期刊名称
    String jname = StringUtils.defaultString(pubJourNal.attributeValue("jname"));
    journalInfoDTO.setName(jname);

    // 期刊ID
    String jid = StringUtils.defaultString(pubJourNal.attributeValue("jid"));
    if (StringUtils.isBlank(jid)) {
      journalInfoDTO.setJid(null);
    } else {
      journalInfoDTO.setJid(NumberUtils.toLong(jid));
    }

    // 发表状态
    String publishStatusCode = StringUtils.defaultString(publication.attributeValue("publish_state"));
    publishStatusCode = StringUtils.trimToEmpty(publishStatusCode);
    journalInfoDTO.setPublishStatusCode(publishStatusCode);
    if ("P".equalsIgnoreCase(publishStatusCode)) {
      journalInfoDTO.setPublishStatusName("已发表");
    } else if ("A".equalsIgnoreCase(publishStatusCode)) {
      journalInfoDTO.setPublishStatusName("已接收");
    } else {
      journalInfoDTO.setPublishStatusCode("");
      journalInfoDTO.setPublishStatusName("");
    }
    // 卷号
    String volumeNo = StringUtils.defaultString(publication.attributeValue("volume"));
    journalInfoDTO.setVolumeNo(volumeNo);

    // 期号
    String issue = StringUtils.defaultString(publication.attributeValue("issue"));
    journalInfoDTO.setIssue(issue);

    // 开始页码
    String startPage = StringUtils.defaultString(publication.attributeValue("start_page"));
    journalInfoDTO.setStartPage(startPage);

    // 结束页码
    String endPage = StringUtils.defaultString(publication.attributeValue("end_page"));
    journalInfoDTO.setEndPage(endPage);

    // 文章号
    String article_number = StringUtils.defaultString(publication.attributeValue("article_number"));
    journalInfoDTO.setArticleNo(article_number);

    // issn
    String issn = StringUtils.defaultString(pubJourNal.attributeValue("jissn"));
    issn = StringUtils.trimToEmpty(issn);
    journalInfoDTO.setISSN(issn);

    String jsonStr = JacksonUtils.jsonObjectSerializer(journalInfoDTO);
    Map<String, Object> map = JacksonUtils.json2HashMap(jsonStr);
    return map;
  }

  /**
   * 解析成果信息 书籍章节和书著
   * 
   * @param one
   * @return
   */
  private static Map<String, Object> dealPubBook(Element root) throws Exception {
    Element one = root.element("pub_book");
    BookInfoDTO bookInfoDTO = new BookInfoDTO();
    // 书名
    String name = StringUtils.defaultString(one.attributeValue("book_title"));
    bookInfoDTO.setName(name);

    // 书籍类型
    String type = StringUtils.defaultString(one.attributeValue("book_type"));
    type = StringUtils.trimToEmpty(type);
    bookInfoDTO.setTypeCode(type);
    String typeName = buildBookType(type);
    bookInfoDTO.setTypeName(typeName);

    // 语言language
    String language = StringUtils.defaultString(one.attributeValue("language"));
    language = StringUtils.trimToEmpty(language);
    if (StringUtils.isNotBlank(language)) {
      bookInfoDTO.setLanguageCode(language);
      if ("1".equalsIgnoreCase(language)) {
        bookInfoDTO.setLanguageName("中文");
      } else if ("2".equalsIgnoreCase(language)) {
        bookInfoDTO.setLanguageName("外文");
      }
    } else {
      bookInfoDTO.setLanguageCode("");
      bookInfoDTO.setLanguageName("");
    }
    // 出版社
    String publisher = StringUtils.defaultString(one.attributeValue("publisher"));
    bookInfoDTO.setPublisher(publisher);

    // isbn
    String Isbn = StringUtils.defaultString(one.attributeValue("isbn"));
    bookInfoDTO.setISBN(Isbn);

    // 总字数
    Integer totalWords = NumberUtils.toInt(one.attributeValue("total_words"), 0);
    bookInfoDTO.setTotalWords(totalWords);
    // 出版状态
    String publishStatusCode = StringUtils.defaultString(one.attributeValue("publication_status"));
    publishStatusCode = StringUtils.trimToEmpty(publishStatusCode);
    if (StringUtils.isNotBlank(publishStatusCode)) {
      bookInfoDTO.setPublishStatusCode(publishStatusCode);
      if ("0".equalsIgnoreCase(publishStatusCode)) {
        bookInfoDTO.setPublishStatusName("待出版");
      } else if ("1".equalsIgnoreCase(publishStatusCode)) {
        bookInfoDTO.setPublishStatusName("已出版");
      }
    } else {
      bookInfoDTO.setPublishStatusCode("");
      bookInfoDTO.setPublishStatusName("");
    }

    Element pubNodeXml = root.element("publication");
    // 开始页
    String startPage = StringUtils.defaultString(pubNodeXml.attributeValue("start_page"));
    startPage = StringUtils.trimToEmpty(startPage);
    bookInfoDTO.setStartPage(startPage);
    // 结束页
    String endPage = StringUtils.defaultString(pubNodeXml.attributeValue("end_page"));
    bookInfoDTO.setEndPage(endPage);

    // 文章号
    String article_number = StringUtils.defaultString(pubNodeXml.attributeValue("article_number"));
    bookInfoDTO.setArticleNo(article_number);

    String jsonStr = JacksonUtils.jsonObjectSerializer(bookInfoDTO);
    Map<String, Object> map = JacksonUtils.json2HashMap(jsonStr);
    return map;
  }

  private static String buildBookType(String bookType) {
    if ("11".equalsIgnoreCase(bookType)) {
      return "专著";
    }
    if ("13".equalsIgnoreCase(bookType)) {
      return "教科书";
    }
    if ("14".equalsIgnoreCase(bookType)) {
      return "编著";
    }
    if ("23".equalsIgnoreCase(bookType)) {
      return "译著";
    }
    if ("29".equalsIgnoreCase(bookType)) {
      return "其他";
    }
    return "";
  }

  /**
   * 解析成果奖励信息
   * 
   * @param root
   * @return
   */
  private static Map<String, Object> dealPubAward(Element root) throws Exception {
    Element one = root.element("pub_award");
    AwardsInfoDTO awardsInfoDTO = new AwardsInfoDTO();
    // 奖励类别代码
    String categoryCode = StringUtils.defaultString(one.attributeValue("award_category"));
    categoryCode = StringUtils.trimToEmpty(categoryCode);
    awardsInfoDTO.setCategoryCode(categoryCode);
    // 奖励类别名称
    String categoryName = StringUtils.defaultString(one.attributeValue("award_category_name"));
    if (StringUtils.isBlank(categoryName)) {
      categoryName = queryCateGoryName(categoryCode);
    }
    categoryName = StringUtils.trimToEmpty(categoryName);
    awardsInfoDTO.setCategoryName(categoryName);
    // 奖励等级代码
    String gradeCode = StringUtils.defaultString(one.attributeValue("award_grade"));
    gradeCode = StringUtils.trimToEmpty(gradeCode);
    awardsInfoDTO.setGradeCode(gradeCode);
    // 奖励等级
    String gradeName = StringUtils.defaultString(one.attributeValue("award_grade_name"));
    if (StringUtils.isBlank(gradeName)) {
      gradeName = queryGradeName(gradeCode);
    }
    gradeName = StringUtils.trimToEmpty(gradeName);
    awardsInfoDTO.setGradeName(gradeName);
    // 奖励单位
    String issuingAuthority = StringUtils.defaultString(one.attributeValue("issue_ins_name"));
    /* issuingAuthority = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty(issuingAuthority)); */
    awardsInfoDTO.setIssuingAuthority(issuingAuthority);
    // 授奖日期
    Element pubNodeXml = root.element("publication");
    String awardDate = getPublishDate(pubNodeXml);
    awardsInfoDTO.setAwardDate(awardDate);
    // 证书编号
    String certificateNo = StringUtils.defaultString(one.attributeValue("ceritficate_no"));
    /* certificateNo = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty(certificateNo)); */
    awardsInfoDTO.setCertificateNo(certificateNo);

    String jsonStr = JacksonUtils.jsonObjectSerializer(awardsInfoDTO);
    Map<String, Object> map = JacksonUtils.json2HashMap(jsonStr);
    return map;
  }

  // 根据类别查找长量表CONST_DISCTIONARY 获取
  private static String queryGradeName(String gradeCode) {
    if ("1".equals(gradeCode)) {
      return "国际学术奖";
    }
    if ("2".equals(gradeCode)) {
      return "国家一等奖";
    }
    if ("3".equals(gradeCode)) {
      return "国家二等奖";
    }
    if ("4".equals(gradeCode)) {
      return "省部一等奖";
    }
    if ("5".equals(gradeCode)) {
      return "省部二等奖";
    }
    if ("6".equals(gradeCode)) {
      return "其他";
    }
    return "";
  }

  // 根据类别查找常量表CONST_DICTIONARY 获取
  private static String queryCateGoryName(String categoryCode) {
    if ("4".equals(categoryCode)) {
      return "其他";
    }
    if ("3".equals(categoryCode)) {
      return "发明";
    }
    if ("2".equals(categoryCode)) {
      return "科技进步";
    }
    if ("1".equals(categoryCode)) {
      return "自然科学";
    }
    return "";
  }

  private static String getPubTypeName(Element root) throws Exception {
    Element one = root.element("pub_type");
    String name = one.attributeValue("zh_name");
    if (StringUtils.isNotBlank(name)) {
      return StringUtils.trimToEmpty(name);
    } else {
      return "期刊论文";
    }
  }

  private static Integer getPubTypeCode(Element root) throws Exception {
    Element one = root.element("pub_type");
    String codeStr = one.attributeValue("id");
    return NumberUtils.toInt(codeStr, 7);
  }

  @SuppressWarnings("unchecked")
  private static List<PubAttachmentsDTO> dealPubAttachments(Element root) throws Exception {
    PubAttachmentsDTO pubAttasDTO = null;
    List<PubAttachmentsDTO> list = new ArrayList<>();
    Element pub_attas = root.element("pub_attachments");
    if (pub_attas == null) {
      return list;
    }
    List<Element> elements = pub_attas.elements("pub_attachment");
    if (elements != null && elements.size() > 0) {
      int index = 1;
      for (Element one : elements) {
        pubAttasDTO = new PubAttachmentsDTO();
        // 顺序号
        pubAttasDTO.setSeqNo(index++);
        // 文件附件Id号
        pubAttasDTO.setFileId(NumberUtils.toLong(one.attributeValue("file_id")));
        // 文件附件名称
        String fileName = StringUtils.defaultString(one.attributeValue("file_name"));
        fileName = StringUtils.trimToEmpty(fileName);
        pubAttasDTO.setFileName(fileName);
        // 文件大小
        String fileSize = StringUtils.defaultString(one.attributeValue("file_size"));
        fileSize = StringUtils.trimToEmpty(fileSize);
        pubAttasDTO.setFileSize(fileSize);
        // 文件格式
        pubAttasDTO.setFileExt(StringUtils.defaultString(one.attributeValue("file_ext")));
        // 上传时间 uploadTime
        String uploadTime = StringUtils.defaultString(one.attributeValue("upload_date"));
        uploadTime = StringUtils.trimToEmpty(uploadTime);
        pubAttasDTO.setUploadTime(uploadTime);

        list.add(pubAttasDTO);
      }
    }
    return list;
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
    Element pub_members = root.element("pub_members");
    if (pub_members == null) {
      return list;
    }
    List<Element> elements = pub_members.elements("pub_member");
    if (elements != null && elements.size() > 0) {
      int index = 1;
      for (Element one : elements) {
        pubMemberDTO = new PubMemberDTO();
        // 作者唯一标识
        String psnIdStr = one.attributeValue("member_psn_id");// NumberUtils.toLong()
        Long psnId = StringUtils.isNotBlank(psnIdStr) ? Long.valueOf(psnIdStr.trim()) : null;
        pubMemberDTO.setPsnId(psnId);
        // 第一作者，作者编号为1的就是第一作者
        boolean first_author = false;
        if (index == 1) {
          first_author = true;
        }
        pubMemberDTO.setFirstAuthor(first_author);
        // 顺序号
        pubMemberDTO.setSeqNo(index++);
        // 作者名
        String name = StringUtils.defaultString(one.attributeValue("member_psn_name"));
        name = StringUtils.trimToEmpty(name);
        pubMemberDTO.setName(name);
        // 作者Email
        pubMemberDTO.setEmail(StringUtils.defaultString(one.attributeValue("email")));
        // 作者身份
        String author_pos = StringUtils.defaultString(one.attributeValue("author_pos"));
        author_pos = StringUtils.trimToEmpty(author_pos);
        pubMemberDTO.setCommunicable("1".equals(author_pos) ? true : false);
        // 作者单位Id
        /*
         * String dept = StringUtils.defaultString(one.attributeValue("ins_id")); if
         * (StringUtils.isNotBlank(dept)) { pubMemberDTO.setInsId(Long.valueOf(dept.trim())); }
         */
        // 作者单位
        String insName = StringUtils.defaultString(one.attributeValue("ins_name"));
        // pubMemberDTO.setInsName(insName);
        List<MemberInsDTO> insNames = new ArrayList<MemberInsDTO>();
        MemberInsDTO memIns = new MemberInsDTO();
        String dept = StringUtils.defaultString(one.attributeValue("ins_id"));
        if (StringUtils.isNotBlank(insName)) {
          memIns.setInsName(insName);
          if (StringUtils.isNotBlank(dept)) {
            memIns.setInsId(Long.valueOf(dept.trim()));
          }
        }
        insNames.add(memIns);
        pubMemberDTO.setInsNames(insNames);
        // pmId
        String pmId = StringUtils.defaultString(one.attributeValue("pm_id"));
        Long pmIdL = StringUtils.isNotBlank(pmId) ? Long.valueOf(pmId.trim()) : null;
        pubMemberDTO.setPmId(pmIdL);
        list.add(pubMemberDTO);
      }
    }
    return list;
  }

  /**
   * 解析专利成员信息
   * 
   * @param root
   * @return
   */
  @SuppressWarnings("unchecked")
  private static List<PatMemberDTO> dealPatMembers(Element root) throws Exception {
    PatMemberDTO patMemberDTO = null;
    List<PatMemberDTO> list = new ArrayList<>();
    Element pub_members = root.element("pub_members");
    if (pub_members == null) {
      return list;
    }
    List<Element> elements = pub_members.elements("pub_member");
    if (elements != null && elements.size() > 0) {
      int index = 1;
      for (Element one : elements) {
        patMemberDTO = new PatMemberDTO();
        // 作者唯一标识
        String psnIdStr = one.attributeValue("member_psn_id");// NumberUtils.toLong()
        Long psnId = StringUtils.isNotBlank(psnIdStr) ? Long.valueOf(psnIdStr.trim()) : null;
        patMemberDTO.setPsnId(psnId);
        // 第一作者，作者编号为1的就是第一作者
        boolean first_author = false;
        if (index == 1) {
          first_author = true;
        }
        patMemberDTO.setFirstAuthor(first_author);
        // 顺序号
        patMemberDTO.setSeqNo(index++);
        // 作者名
        String name = StringUtils.defaultString(one.attributeValue("member_psn_name"));
        name = StringUtils.trimToEmpty(name);
        patMemberDTO.setName(name);
        // 作者Email
        patMemberDTO.setEmail(StringUtils.defaultString(one.attributeValue("email")));
        // 作者身份
        String author_pos = StringUtils.defaultString(one.attributeValue("author_pos"));
        author_pos = StringUtils.trimToEmpty(author_pos);
        patMemberDTO.setCommunicable("1".equals(author_pos) ? true : false);
        // 作者单位Id
        String dept = StringUtils.defaultString(one.attributeValue("ins_id"));
        if (StringUtils.isNotBlank(dept)) {
          patMemberDTO.setInsId(Long.valueOf(dept.trim()));
        }
        // 作者单位
        String insName = StringUtils.defaultString(one.attributeValue("ins_name"));
        patMemberDTO.setInsName(insName);
        // pmId
        String pmId = StringUtils.defaultString(one.attributeValue("pm_id"));
        Long pmIdL = StringUtils.isNotBlank(pmId) ? Long.valueOf(pmId.trim()) : null;
        patMemberDTO.setPmId(pmIdL);
        list.add(patMemberDTO);
      }
    }
    return list;
  }

  /**
   * 解析成果列表收录信息
   * 
   * @param one
   * @return
   */
  private static List<PubSituationDTO> dealpubList2(Document document) throws Exception {
    Element root = document.getRootElement();
    Element one = root.element("pub_list");
    List<PubSituationDTO> list = new ArrayList<>();
    PubSituationDTO pubSituationDTO = null;
    // 根据pub_list节点拼接libraryNamesSource
    String libraryNamesSource = getLibraryNamesWithSource(root);
    // 根据pub_list节点拼接libraryNames
    String libraryNames = getLibraryNames(root);
    if (StringUtils.isNotBlank(libraryNamesSource)) {
      String[] situations = libraryNamesSource.split(",");
      for (String situation : situations) {
        if (situation.equalsIgnoreCase("SCI") || situation.equalsIgnoreCase("SCIE")) {
          situation = "SCIE";
        }
        if (StringUtils.isNotBlank(situation)) {
          pubSituationDTO = new PubSituationDTO();
          pubSituationDTO.setLibraryName(situation);
          pubSituationDTO.setSitStatus(true);
          pubSituationDTO.setSitOriginStatus(true);
          list.add(pubSituationDTO);
        }
      }
    } else if (StringUtils.isEmpty(libraryNamesSource) && StringUtils.isNotEmpty(libraryNames)) {
      String[] situations = libraryNames.split(",");
      for (String situation : situations) {
        if (situation.equalsIgnoreCase("SCI") || situation.equalsIgnoreCase("SCIE")) {
          situation = "SCIE";
        }
        if (StringUtils.isNotBlank(situation)) {
          pubSituationDTO = new PubSituationDTO();
          pubSituationDTO.setLibraryName(situation);
          pubSituationDTO.setSitStatus(true);
          pubSituationDTO.setSitOriginStatus(false);
          list.add(pubSituationDTO);
        }
      }
    }
    return list;
  }

  private static String getLibraryNames(Element root) {
    Element one = root.element("pub_list");
    if (one == null) {
      return "";
    }
    List<String> list = new ArrayList<>();
    if ("1".equals(one.attributeValue("list_ei"))) {
      list.add("EI");
    }
    if ("1".equals(one.attributeValue("list_sci"))) {
      list.add("SCIE");
    }
    if ("1".equals(one.attributeValue("list_istp"))) {
      list.add("ISTP");
    }
    if ("1".equals(one.attributeValue("list_ssci"))) {
      list.add("SSCI");
    }
    if ("1".equals(one.attributeValue("list_cssci"))) {
      list.add("CSSCI");
    }
    if ("1".equals(one.attributeValue("list_pku"))) {
      list.add("PKU");
    }
    if ("1".equals(one.attributeValue("list_cscd"))) {
      list.add("CSCD");
    }
    if ("1".equals(one.attributeValue("list_other"))) {
      list.add("OTHER");
    }
    String libraryNames = "";
    if (CollectionUtils.isNotEmpty(list)) {
      // 逗号拼接
      libraryNames = StringUtils.join(list.toArray(), ",");
    }
    return libraryNames;
  }

  private static String getLibraryNamesWithSource(Element root) {
    Element one = root.element("pub_list");
    if (one == null) {
      return "";
    }
    List<String> list = new ArrayList<>();
    if ("1".equals(one.attributeValue("list_ei_source"))) {
      list.add("EI");
    }
    if ("1".equals(one.attributeValue("list_sci_source"))) {
      list.add("SCIE");
    }
    if ("1".equals(one.attributeValue("list_istp_source"))) {
      list.add("ISTP");
    }
    if ("1".equals(one.attributeValue("list_ssci_source"))) {
      list.add("SSCI");
    }
    if ("1".equals(one.attributeValue("list_cssci_source"))) {
      list.add("CSSCI");
    }
    if ("1".equals(one.attributeValue("list_pku_source"))) {
      list.add("PKU");
    }
    if ("1".equals(one.attributeValue("list_cscd_source"))) {
      list.add("CSCD");
    }
    String libraryNames = "";
    if (CollectionUtils.isNotEmpty(list)) {
      // 逗号拼接
      libraryNames = StringUtils.join(list.toArray(), ",");
    }
    return libraryNames;
  }

  /**
   * 解析成果列表收录信息
   * 
   * @param one
   * @return
   */
  private static List<PubSituationDTO> dealpubList(Document document) throws Exception {
    Element root = document.getRootElement();
    Element one = root.element("publication");
    List<PubSituationDTO> list = new ArrayList<>();
    PubSituationDTO pubSituationDTO = null;
    String libraryName = getSourceDbName3(document, one);
    String srcId = one.attributeValue("source_id");
    srcId = StringUtils.trimToEmpty(srcId);
    String srcUrl = getSourceUrl(document, one);
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

  /**
   * 解析成果基本信息
   * 
   * @param one
   * @return
   */
  private static Map<String, Object> dealPubBase(Document document, Element one) throws Exception {
    Map<String, Object> map = new HashMap<String, Object>();
    // 中文标题
    String ctitle = one.attributeValue("zh_title");
    String etitle = one.attributeValue("en_title");
    String title = StringUtils.trimToEmpty(ctitle);
    /* title = StringEscapeUtils.escapeHtml4(title); */
    map.put("title", title);

    // 中文摘要
    String cabstract = one.attributeValue("zh_abstract");
    String summary = StringUtils.trimToEmpty(cabstract);
    /* summary = StringEscapeUtils.escapeHtml4(summary); */
    map.put("summary", summary);

    // 中文关键词
    String ckeywords = one.attributeValue("zh_keywords");
    String keywords = StringUtils.trimToEmpty(ckeywords);
    map.put("keywords", keywords);

    // 作者
    String author_names = StringUtils.defaultString(one.attributeValue("author_names"));
    author_names = author_names.replace(" ", "");
    map.put("authorNames", author_names);

    // doi
    String doi = StringUtils.defaultString(one.attributeValue("doi"));
    doi = StringUtils.trimToEmpty(doi);
    map.put("doi", doi);

    // organization
    String organization = StringUtils.defaultString(one.attributeValue("organization"));
    organization = StringUtils.trimToEmpty(organization);
    map.put("organization", organization);

    // fundinfo 基金标注
    String fundInfo = StringUtils.defaultString(one.attributeValue("fundinfo"));
    fundInfo = StringUtils.trimToEmpty(fundInfo);
    map.put("fundInfo", fundInfo);

    // 学科领域代码和名称
    String discipline_code = one.attributeValue("discipline_code");
    String discipline_name = StringUtils.defaultString(one.attributeValue("discipline_name"));
    map.put("disciplineCode", discipline_code);
    map.put("disciplineName", discipline_name);

    // 查看设置代码
    String isPublicCode = one.attributeValue("is_public");
    if (StringUtils.isEmpty(isPublicCode)) {
      isPublicCode = "1";
    }
    map.put("isPublicCode", isPublicCode);
    String isPublicName = getIsPublicName(NumberUtils.toInt(isPublicCode));
    map.put("isPublicName", isPublicName);

    // fulltextId
    Element root = document.getRootElement();
    Element fullTextNode = root.element("pub_fulltext");
    if (fullTextNode != null) {
      String strFileId = StringUtils.defaultString(fullTextNode.attributeValue("file_id"));
      Long fileId = null;
      if (StringUtils.isNotBlank(strFileId)) {
        fileId = Long.valueOf(strFileId.trim());
      }
      map.put("fulltextId", fileId);

      // fulltexName
      String fulltexName = StringUtils.defaultString(fullTextNode.attributeValue("file_name"));
      map.put("fulltexName", fulltexName);
    } else {
      map.put("fulltextId", null);
      map.put("fulltexName", "");
    }

    // 来源
    String desc = one.attributeValue("brief_desc");
    map.put("briefDesc", desc);

    // 引用次数
    Integer citations = getCitations(document, one);
    map.put("citations", citations);

    // 引用数最新更新时间
    String citeUpdateTime = one.attributeValue("isi_cited_update");
    map.put("citationsUpdateTime", citeUpdateTime);

    // 来源全文路径
    String srcFulltextUrl = StringUtils.defaultString(one.attributeValue("fulltext_url"));
    srcFulltextUrl = StringUtils.trimToEmpty(srcFulltextUrl);
    map.put("srcFulltextUrl", srcFulltextUrl);

    // srcDbId
    Element pubMeta = root.element("pub_meta");
    String sourceDbCode = pubMeta.attributeValue("source_db_id"); // ROL-6852-存在部分xml数据source_db_code节点不是来源代码，而是缩写，改为
    if (StringUtils.isBlank(sourceDbCode)) {
      sourceDbCode = one.attributeValue("source");
    }
    if (NumberUtils.isDigits(sourceDbCode)) {
      map.put("srcDbId", sourceDbCode);
    } else {
      map.put("srcDbId", "");
    }

    // source_id
    String sourceId = StringUtils.defaultString(one.attributeValue("source_id"));
    sourceId = StringUtils.trimToEmpty(sourceId);
    map.put("sourceId", sourceId);

    // publishDate
    map.put("publishDate", getPublishDate(one));

    // citedUrl
    map.put("citedUrl", getCitedUrl(document, one));

    // sourceUrl
    String sourceUrl = StringUtils.defaultString(one.attributeValue("tmpsource_url"));
    sourceUrl = StringUtils.trimToEmpty(sourceUrl);
    map.put("sourceUrl", sourceUrl);

    // 数据来源
    Integer dataFrom = getDataFrom(root);
    map.put("dataFrom", dataFrom);

    // HCP 高被引字段
    Integer HCP = 0;
    map.put("HCP", HCP);

    // HP 热门文章
    Integer HP = 0;
    map.put("HP", HP);

    // OA Open Access
    String OA = StringUtils.defaultString(one.attributeValue("OA"));
    map.put("OA", OA);

    // pubId
    Element pubNode = root.element("pub_meta");
    String id = pubNode.attributeValue("pub_id");
    Long pubId = StringUtils.isNotBlank(id) ? Long.valueOf(id.trim()) : null;
    map.put("pubId", pubId);

    // insId
    String insIdStr = pubNode.attributeValue("record_ins_id");
    Long insId = StringUtils.isNotBlank(insIdStr) ? Long.valueOf(insIdStr.trim()) : null;
    map.put("insId", insId);

    // 最后更新时间lastUpdateTime
    String lastUpdateTime = pubNode.attributeValue("last_update_date");
    lastUpdateTime = StringUtils.isNotBlank(lastUpdateTime) ? lastUpdateTime : "";
    map.put("lastUpdateTime", lastUpdateTime);

    return map;
  }

  private static Integer getDataFrom(Element root) {
    Element pubMeta = root.element("pub_meta");
    String dataFromStr = pubMeta.attributeValue("record_from");
    Integer dataFrom = null;
    if (StringUtils.isNotBlank(dataFromStr)) {
      dataFrom = NumberUtils.toInt(dataFromStr.trim());
    }
    return dataFrom;
  }

  private static String getSourceUrl(Document document, Element one) {
    Element root = document.getRootElement();
    Element pubMeta = root.element("pub_meta");
    String srcUrl = pubMeta.attributeValue("source_url");
    if (StringUtils.isBlank(srcUrl)) {
      srcUrl = one.attributeValue("tmpsource_url");
    }
    srcUrl = StringUtils.isBlank(srcUrl) ? "" : srcUrl;
    srcUrl = StringUtils.trimToEmpty(srcUrl);
    return srcUrl;
  }

  private static String getCitedUrl(Document document, Element one) {
    String citedUrl = StringUtils.defaultString(one.attributeValue("cite_record_url"));
    String sourceDbCode = getSourceDbName3(document, one);
    citedUrl = StringUtils.defaultString(one.attributeValue(sourceDbCode.toLowerCase() + "_cite_record_url"));
    if (StringUtils.isBlank(citedUrl)) {
      citedUrl = StringUtils.defaultString(one.attributeValue("tmpcite_record_url"));
    }
    citedUrl = StringUtils.trimToEmpty(citedUrl);
    return citedUrl;
  }

  private static String getPublishDate(Element one) {
    String publishDate = StringUtils.defaultString(one.attributeValue("publish_date"));
    if (StringUtils.isBlank(publishDate)) {
      publishDate = StringUtils.defaultString(one.attributeValue("pubyear"));
    }
    publishDate = publishDate.replace("/", "-");
    publishDate = publishDate.replace(".", "-");
    Pattern p = Pattern.compile("[a-zA-z]");
    if (!p.matcher(publishDate).find()) {
      publishDate = StringUtils.trimToEmpty(publishDate);
      return publishDate;
    }
    return "";
  }

  private static String getIsPublicName(int isPublicCode) {
    String isPublicName = "公开";
    if (isPublicCode != 1) {
      isPublicName = "非公开";
    }
    return isPublicName;
  }

  private static Integer getCitations(Document document, Element one) {
    String sourceDbCode = getSourceDbName3(document, one);
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

  private static String getSourceDbName(Document document, Element one) {
    Element root = document.getRootElement();
    Element pubMeta = root.element("pub_meta");
    String sourceDbCode = pubMeta.attributeValue("source_db_code");
    if (StringUtils.isBlank(sourceDbCode)) {
      sourceDbCode = one.attributeValue("source");
    }
    String sourceDbName = getSourceDbName2(sourceDbCode);
    if (sourceDbName.equalsIgnoreCase("SCI") || sourceDbName.equalsIgnoreCase("SCIE")) {
      sourceDbName = "SCIE";
    }
    return sourceDbName;
  }

  private static String getSourceDbName3(Document document, Element one) {
    Element root = document.getRootElement();
    Element pubMeta = root.element("pub_meta");
    String sourceDbName = pubMeta.attributeValue("zh_source_db_name");
    sourceDbName = StringUtils.trimToEmpty(sourceDbName);
    if (sourceDbName.equalsIgnoreCase("SCI") || sourceDbName.equalsIgnoreCase("SCIE")) {
      sourceDbName = "SCIE";
    }
    return sourceDbName;
  }

  private static String getSourceDbName2(String sourceDbCode) {
    if ("14".equalsIgnoreCase(sourceDbCode)) {
      return "EI";
    } else if ("16".equalsIgnoreCase(sourceDbCode)) {
      return "SCIE";
    } else if ("15".equalsIgnoreCase(sourceDbCode)) {
      return "ISTP";
    } else if ("17".equalsIgnoreCase(sourceDbCode)) {
      return "SSCI";
    } else if ("34".equalsIgnoreCase(sourceDbCode)) {
      return "CSSCI";
    } else if ("33".equalsIgnoreCase(sourceDbCode)) {
      return "CSCD";
    } else if ("".equalsIgnoreCase(sourceDbCode)) {
      return "";
    } else {
      return "OTHER";
    }
  }

  public static void main(String[] args) {
    String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"
        + "<data><pub_meta schema_version=\"3.0\" record_ins_id=\"810\" dbcache_xml_id=\"5369032\" tmpl_form=\"scholar\" last_update_psn_id=\"-1\" last_update_date=\"2018-12-29 12:40:45\" record_from=\"4\" source_db_id=\"16\" source_db_code=\"16\" zh_source_db_name=\"SCIE\" en_source_db_name=\"SCIE\" record_psn_id=\"-1\" create_psn_id=\"-1\" create_date=\"2018-12-29 12:40:45\" record_node_id=\"1\" source_url=\"http://apps.webofknowledge.com/InboundService.do?SID=@SID@&amp;uml_return_url=http%3A%2F%2Fpcs.webofknowledge.com%2Fuml%2Fuml_view.cgi%3Fproduct_sid%S2igJonHFDcff498A2H%26product%3DWOS%26product_st_thomas%3Dhttp%253A%252F%252Festi%252Eisiknowledge%252Ecom%253A8360%252Festi%252Fxrpc%26sort_opt%3DDate&amp;action=retrieve&amp;product=WOS&amp;mode=FullRecord&amp;viewType=fullRecord&amp;frmUML=1&amp;UT=000293141800090\" source_id=\"000293141800090\" isi_id=\"000293141800090\" isi_source_url=\"http://apps.webofknowledge.com/InboundService.do?SID=@SID@&amp;uml_return_url=http%3A%2F%2Fpcs.webofknowledge.com%2Fuml%2Fuml_view.cgi%3Fproduct_sid%S2igJonHFDcff498A2H%26product%3DWOS%26product_st_thomas%3Dhttp%253A%252F%252Festi%252Eisiknowledge%252Ecom%253A8360%252Festi%252Fxrpc%26sort_opt%3DDate&amp;action=retrieve&amp;product=WOS&amp;mode=FullRecord&amp;viewType=fullRecord&amp;frmUML=1&amp;UT=000293141800090\" pub_id=\"100000026379949\"/><publication cite_times=\"2\" author_names=\"Xu Yan; Liu Guang-Zhou; Wu Yao-Rui; Zhu Ming-Feng; Yu Zi; Wang Hong-Yan; Zhao En-Guang\" doi=\"10.1088/0256-307X/28/7/079701\" organization=\"[Xu Yan; Liu Guang-Zhou; Wu Yao-Rui; Zhu Ming-Feng; Wang Hong-Yan] Jilin   Univ, Ctr Theoret Phys, Changchun 130012, Peoples R China.   [Yu Zi] Nanjing Forestry Univ, Coll Sci, Nanjing 210037, Peoples R China.   [Zhao En-Guang] Chinese Acad Sci, Inst Theoret Phys, Beijing 100190,   Peoples R China. Xu, Y (reprint author), Jilin Univ, Ctr Theoret Phys, Changchun 130012, Peoples R China\" fundInfo=\"National Natural Science Foundation of China[10675024, 11075063];   National Fundamental Project for Subsidy Funds of Personnel   Training[J0730311]\" citation_index=\"SCI\" original=\"Chinese Physics Letters\" jid=\"37715\" publish_state=\"P\" volume=\"28\" issue=\"7\" article_number=\"079701\" issn=\"0256-307X\" fulltext_url=\"\" source=\"16\" tmpsource_url=\"http://apps.webofknowledge.com/InboundService.do?SID=@SID@&amp;uml_return_url=http%3A%2F%2Fpcs.webofknowledge.com%2Fuml%2Fuml_view.cgi%3Fproduct_sid%S2igJonHFDcff498A2H%26product%3DWOS%26product_st_thomas%3Dhttp%253A%252F%252Festi%252Eisiknowledge%252Ecom%253A8360%252Festi%252Fxrpc%26sort_opt%3DDate&amp;action=retrieve&amp;product=WOS&amp;mode=FullRecord&amp;viewType=fullRecord&amp;frmUML=1&amp;UT=000293141800090\" source_id=\"000293141800090\" publish_date=\"2011/7\" pubyear=\"2011-07\" tmpcite_record_url=\"http://apps.webofknowledge.com/CitingArticles.do?product=UA&amp;SID=@SID@&amp;search_mode=CitingArticles&amp;parentProduct=UA&amp;UT=WOS:000293141800090\" brief_desc=\"Chinese Physics Letters, 28(7), pp 079701, 2011.7\" brief_desc_en=\"Chinese Physics Letters, 28(7), pp 079701, 7.2011\" dup_pub_id=\"\" source_catalog=\"16\" is_public=\"1\" publish_year=\"2011\" publish_month=\"7\" publish_day=\"\" zh_title=\"Direct Urca Processes with Hyperons in Cooling Neutron @#%^%*0-0=\"\"+;;''“”Stars\" en_title=\"Direct Urca Processes with Hyperons in Cooling Neutron Stars\" zh_abstract=\"In the relativistic mean field approximation, the relativistic energy losses of the direct Urca processes with nucleons (N-DURCA) and hyperons (Y-DURCA) are studied in the degenerate baryon matter of neutron stars. We investigate the effects of hyperon degrees of freedom and the Y-DURCA processes on the N-DURCA processes, and the total neutrino emissivity of neutron star matter. The results show that the existence of hyperons decreases the abundance of protons and leptons, and can sharply suppress the neutrino emissivity of the N-DURCA processes.\" en_abstract=\"In the relativistic mean field approximation, the relativistic energy losses of the direct Urca processes with nucleons (N-DURCA) and hyperons (Y-DURCA) are studied in the degenerate baryon matter of neutron stars. We investigate the effects of hyperon degrees of freedom and the Y-DURCA processes on the N-DURCA processes, and the total neutrino emissivity of neutron star matter. The results show that the existence of hyperons decreases the abundance of protons and leptons, and can sharply suppress the neutrino emissivity of the N-DURCA processes.\" zh_keywords=\"\" en_keywords=\"\" cited_list=\"16\" cited_date=\"2018-12-29 12:40:45\" cited_url=\"http://apps.webofknowledge.com/CitingArticles.do?product=UA&amp;SID=@SID@&amp;search_mode=CitingArticles&amp;parentProduct=UA&amp;UT=WOS:000293141800090\" original_author_names=\"Xu Yan; Liu Guang-Zhou; Wu Yao-Rui; Zhu Ming-Feng; Yu Zi; Wang Hong-Yan; Zhao En-Guang\" is_valid=\"0\"/><pub_type id=\"4\" zh_name=\"期刊论文\" en_name=\"Journal Article\"/><pub_fulltext fulltext_url=\"\"/><pub_journal jname=\"Chinese Physics Letters\" jid=\"37715\" node_id=\"1\" en_name=\"Chinese Physics Letters\" jissn=\"0256-307X\"/><pub_list list_sci=\"1\" list_sci_source=\"1\"/><pub_members><pub_member seq_no=\"1\" member_psn_name=\"Xu Yan\" member_psn_id=\"\" ins_id=\"\" matched_ins_id=\"\" email=\"\" ins_name=\"\" author_pos=\"0\" first_author=\"1\" member_psn_acname=\"\" unit_name=\"\" unit_id=\"\" pm_id=\"66291357\"/><pub_member seq_no=\"2\" member_psn_name=\"Liu Guang-Zhou\" member_psn_id=\"\" ins_id=\"\" matched_ins_id=\"\" email=\"\" ins_name=\"\" author_pos=\"0\" member_psn_acname=\"\" unit_name=\"\" unit_id=\"\" pm_id=\"66291358\"/><pub_member seq_no=\"3\" member_psn_name=\"Wu Yao-Rui\" member_psn_id=\"\" ins_id=\"\" matched_ins_id=\"\" email=\"\" ins_name=\"\" author_pos=\"0\" member_psn_acname=\"\" unit_name=\"\" unit_id=\"\" pm_id=\"66291359\"/><pub_member seq_no=\"4\" member_psn_name=\"Zhu Ming-Feng\" member_psn_id=\"\" ins_id=\"\" matched_ins_id=\"\" email=\"\" ins_name=\"\" author_pos=\"0\" member_psn_acname=\"\" unit_name=\"\" unit_id=\"\" pm_id=\"66291360\"/><pub_member seq_no=\"5\" member_psn_name=\"Yu Zi\" member_psn_id=\"\" ins_id=\"\" matched_ins_id=\"\" email=\"\" ins_name=\"\" author_pos=\"0\" member_psn_acname=\"\" unit_name=\"\" unit_id=\"\" pm_id=\"66291361\"/><pub_member seq_no=\"6\" member_psn_name=\"Wang Hong-Yan\" member_psn_id=\"\" ins_id=\"\" matched_ins_id=\"\" email=\"\" ins_name=\"\" author_pos=\"0\" member_psn_acname=\"\" unit_name=\"\" unit_id=\"\" pm_id=\"66291362\"/><pub_member seq_no=\"7\" member_psn_name=\"Zhao En-Guang\" member_psn_id=\"\" ins_id=\"\" matched_ins_id=\"\" email=\"\" ins_name=\"\" author_pos=\"0\" member_psn_acname=\"\" unit_name=\"\" unit_id=\"\" pm_id=\"66291363\"/></pub_members><pub_errors/></data>";
    try {
      String json = dealWithXML(xml);
      json = XSSUtils.xssReplace(json);
      System.out.println(json);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
