package com.smate.core.base.pub.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.web.util.HtmlUtils;
import com.smate.core.base.pub.enums.PubBookTypeEnum;
import com.smate.core.base.pub.enums.PubConferencePaperTypeEnum;
import com.smate.core.base.pub.enums.PubLibraryEnum;
import com.smate.core.base.pub.enums.PubPatentAreaEnum;
import com.smate.core.base.pub.enums.PubPatentTransitionStatusEnum;
import com.smate.core.base.pub.enums.PubThesisDegreeEnum;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.web.v8pub.dto.AwardsInfoDTO;
import com.smate.web.v8pub.dto.BookInfoDTO;
import com.smate.web.v8pub.dto.ConferencePaperDTO;
import com.smate.web.v8pub.dto.JournalInfoDTO;
import com.smate.web.v8pub.dto.MemberInsDTO;
import com.smate.web.v8pub.dto.PatentInfoDTO;
import com.smate.web.v8pub.dto.PubAccessoryDTO;
import com.smate.web.v8pub.dto.PubFulltextDTO;
import com.smate.web.v8pub.dto.PubMemberDTO;
import com.smate.web.v8pub.dto.PubSituationDTO;
import com.smate.web.v8pub.dto.ThesisInfoDTO;

/**
 * 个人库成果xml转json字符串工具类 number 类型的结果，空值都返回0 ；
 * 
 * @author zzx
 */
public class SnsPubXMLToJsonStrUtils {


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
   * 个人库成果xml转map
   * 
   * @param pubXml
   * @return
   * @throws Exception
   */
  public static Map<String, Object> dealWithXMLToMap(String pubXml) {
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      if (StringUtils.isBlank(pubXml)) {
        return null;
      }
      Document document = DocumentHelper.parseText(pubXml);
      Element root = document.getRootElement();
      boolean isChina = isChina(root);
      // 成果基本信息
      result = dealPubBase(root, isChina);
      // 成员信息
      List<PubMemberDTO> members = dealPubMembers(root);
      result.put("members", members);
      // 成果全文信息
      PubFulltextDTO pubFulltext = dealPubFulltext(root);
      result.put("fullText", pubFulltext);
      int pubType = Integer.parseInt(result.get("pubType").toString());
      // 收录信息
      List<PubSituationDTO> pubInfoList = dealpubList(root);
      result.put("situations", pubInfoList);
      // 附件信息
      List<PubAccessoryDTO> accessorys = dealPubAccessorys(root);
      result.put("accessorys", accessorys);
      Map<String, Object> typeInfo = null;
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
      result.put("pubTypeInfo", typeInfo);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }

  /**
   * sns库中英文选取原则 1. 作者名和标题，有一个有中文则取中文
   * 
   * @param root
   * @return
   */
  private static boolean isChina(Element root) {
    Element one = root.element("publication");
    if (one == null) {
      return false;
    }
    String authorName = StringUtils.defaultString(one.attributeValue("author_names"));
    String authorNameSpec = StringUtils.defaultString(one.attributeValue("authors_names_spec"));
    String zhTitle = StringUtils.defaultString(one.attributeValue("zh_title"));
    String enTitle = StringUtils.defaultString(one.attributeValue("en_title"));
    String unionTitle = zhTitle + enTitle;
    String unionAuthorName = authorName + authorNameSpec;
    Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
    if (StringUtils.isNotBlank(unionAuthorName) && p.matcher(unionAuthorName).find()) {
      return true;
    }

    if (StringUtils.isBlank(unionAuthorName) && StringUtils.isNotBlank(unionTitle) && p.matcher(unionTitle).find()) {
      return true;
    }
    return false;
  }

  /**
   * 解析成果列表信息
   * 
   * @param root
   * @return
   */
  private static List<PubSituationDTO> dealpubList(Element root) throws Exception {
    PubSituationDTO pubSituationDTO = null;
    List<PubSituationDTO> list = new ArrayList<>();
    Element pubList = root.element("pub_list");
    if (pubList == null) {
      return null;
    }
    Element pubMeta = root.element("pub_meta");
    if (pubMeta == null) {
      return null;
    }
    Element publication = root.element("publication");
    if (publication == null) {
      return null;
    }
    boolean isList = false; // 标示是不是other
    String list_ei = pubList.attributeValue("list_ei");
    String list_pku = pubList.attributeValue("list_pku");
    String list_sci = pubList.attributeValue("list_sci");
    String list_ssci = pubList.attributeValue("list_ssci");
    String list_istp = pubList.attributeValue("list_istp");
    String list_cssci = pubList.attributeValue("list_cssci");
    String list_ei_source = pubList.attributeValue("list_ei_source");
    String list_sci_source = pubList.attributeValue("list_sci_source");
    String list_ssci_source = pubList.attributeValue("list_ssci_source");
    String list_istp_source = pubList.attributeValue("list_istp_source");
    String list_cssci_source = pubList.attributeValue("list_cssci_source");
    String source_db_id = pubMeta.attributeValue("source_db_id");
    String source_url = getSourceUrl(root);
    String source_id = getSourceId(root);

    if (StringUtils.isNotBlank(list_ei) && "1".equalsIgnoreCase(list_ei)) {
      pubSituationDTO = new PubSituationDTO();
      pubSituationDTO.setLibraryName(PubLibraryEnum.EI.desc);
      pubSituationDTO.setSitStatus(true);
      pubSituationDTO
          .setSitOriginStatus((StringUtils.isNotBlank(list_ei_source) && "1".equalsIgnoreCase(list_ei_source)));
      if ("14".equals(source_db_id)) {
        pubSituationDTO.setSrcDbId(source_db_id);
        pubSituationDTO.setSrcId(source_id);
        pubSituationDTO.setSrcUrl(source_url);
        isList = true;
      }
      list.add(pubSituationDTO);
    }
    if (StringUtils.isNotBlank(list_sci) && "1".equalsIgnoreCase(list_sci)) {
      pubSituationDTO = new PubSituationDTO();
      pubSituationDTO.setLibraryName(PubLibraryEnum.SCIE.desc);
      pubSituationDTO.setSitStatus(true);
      pubSituationDTO
          .setSitOriginStatus((StringUtils.isNotBlank(list_sci_source) && "1".equalsIgnoreCase(list_sci_source)));
      if ("16".equalsIgnoreCase(source_db_id)) {
        pubSituationDTO.setSrcDbId(source_db_id);
        pubSituationDTO.setSrcId(source_id);
        pubSituationDTO.setSrcUrl(source_url);
        isList = true;
      }
      list.add(pubSituationDTO);
    }
    if (StringUtils.isNotBlank(list_ssci) && "1".equalsIgnoreCase(list_ssci)) {
      pubSituationDTO = new PubSituationDTO();
      pubSituationDTO.setLibraryName(PubLibraryEnum.SSCI.desc);
      pubSituationDTO.setSitStatus(true);
      pubSituationDTO
          .setSitOriginStatus((StringUtils.isNotBlank(list_ssci_source) && "1".equalsIgnoreCase(list_ssci_source)));
      if ("17".equalsIgnoreCase(source_db_id)) {
        pubSituationDTO.setSrcDbId(source_db_id);
        pubSituationDTO.setSrcId(source_id);
        pubSituationDTO.setSrcUrl(source_url);
        isList = true;
      }
      list.add(pubSituationDTO);
    }
    if (StringUtils.isNotBlank(list_istp) && "1".equalsIgnoreCase(list_istp)) {
      pubSituationDTO = new PubSituationDTO();
      pubSituationDTO.setLibraryName(PubLibraryEnum.ISTP.desc);
      pubSituationDTO.setSitStatus(true);
      pubSituationDTO
          .setSitOriginStatus((StringUtils.isNotBlank(list_istp_source) && "1".equalsIgnoreCase(list_istp_source)));
      if ("15".equalsIgnoreCase(source_db_id)) {
        pubSituationDTO.setSrcDbId(source_db_id);
        pubSituationDTO.setSrcId(source_id);
        pubSituationDTO.setSrcUrl(source_url);
        isList = true;
      }
      list.add(pubSituationDTO);
    }
    if (StringUtils.isNotBlank(list_cssci) && "1".equalsIgnoreCase(list_cssci)) {
      pubSituationDTO = new PubSituationDTO();
      pubSituationDTO.setLibraryName("CSSCI");
      pubSituationDTO.setSitStatus(true);
      pubSituationDTO
          .setSitOriginStatus((StringUtils.isNotBlank(list_cssci_source) && "1".equalsIgnoreCase(list_cssci_source)));
      list.add(pubSituationDTO);
    }

    if (StringUtils.isNotBlank(list_pku) && "1".equalsIgnoreCase(list_pku)) {
      pubSituationDTO = new PubSituationDTO();
      pubSituationDTO.setLibraryName(PubLibraryEnum.PKU.desc);
      pubSituationDTO.setSitStatus(true);
      pubSituationDTO.setSitOriginStatus(false);
      list.add(pubSituationDTO);
    }

    if (!isList && StringUtils.isNotBlank(source_db_id)) {
      pubSituationDTO = new PubSituationDTO();
      pubSituationDTO.setLibraryName(PubLibraryEnum.OTHER.desc);
      pubSituationDTO.setSitStatus(true);
      pubSituationDTO.setSitOriginStatus(false);
      pubSituationDTO.setSrcDbId(source_db_id);
      pubSituationDTO.setSrcId(source_id);
      pubSituationDTO.setSrcUrl(source_url);
      list.add(pubSituationDTO);
    }

    return list;
  }

  /**
   * 解析成果其他信息
   * 
   * @param root
   * @return
   */
  private static Map<String, Object> dealpubOther(Element root) throws Exception {
    return null;
  }

  /**
   * 解析XX类型成果信息
   * 
   * @param root
   * @return
   */
  private static Map<String, Object> dealPubThesis(Element root) throws Exception {
    ThesisInfoDTO thesisInfoDTO = new ThesisInfoDTO();
    Element one = root.element("pub_thesis");
    if (one == null) {
      return null;
    }
    // 学位
    String degree = StringUtils.defaultString(one.attributeValue("programme"));
    if (StringUtils.isBlank(degree)) {
      degree = StringUtils.defaultString(one.attributeValue("thesis_programme"));
    }
    degree = StringUtils.trimToEmpty(degree);
    thesisInfoDTO.setDegree(buildDegree(degree));
    // 颁发单位
    String issuingAuthority = StringUtils.defaultString(one.attributeValue("issue_org"));
    issuingAuthority = StringUtils.substring(issuingAuthority, 0, 100);
    issuingAuthority = StringUtils.trimToEmpty(issuingAuthority);
    thesisInfoDTO.setIssuingAuthority(issuingAuthority);
    // 部门
    String department = StringUtils.defaultString(one.attributeValue("department"));
    if (StringUtils.isBlank(department)) {
      department = StringUtils.defaultString(one.attributeValue("thesis_dept_name"));
    }
    department = StringUtils.trimToEmpty(department);
    department = StringUtils.substring(department, 0, 100);
    thesisInfoDTO.setDepartment(department);
    // 答辩日期
    String defenseDate = getPublishDate(root);
    thesisInfoDTO.setDefenseDate(defenseDate);
    // isbn
    Element publication = root.element("publication");
    if (publication != null) {
      String iSBN = StringUtils.defaultString(publication.attributeValue("isbn"));
      iSBN = StringUtils.trimToEmpty(iSBN);
      iSBN = StringUtils.substring(iSBN, 0, 20);
      thesisInfoDTO.setISBN(iSBN);
    }
    String jsonStr = JacksonUtils.jsonObjectSerializer(thesisInfoDTO);
    Map<String, Object> map = JacksonUtils.json2HashMap(jsonStr);
    return map;
  }

  private static PubThesisDegreeEnum buildDegree(String degree) {
    if (StringUtils.isBlank(degree)) {
      return PubThesisDegreeEnum.OTHER;
    }
    if ("MPhil".equalsIgnoreCase(degree)) {
      return PubThesisDegreeEnum.MASTER;
    }
    if ("PhD".equalsIgnoreCase(degree)) {
      return PubThesisDegreeEnum.DOCTOR;
    }
    if ("Others".equalsIgnoreCase(degree)) {
      return PubThesisDegreeEnum.OTHER;
    }
    return PubThesisDegreeEnum.OTHER;
  }

  /**
   * 解析专利类型成果信息
   * 
   * @param root
   * @return
   */
  private static Map<String, Object> dealPubPatent(Element root) throws Exception {
    PatentInfoDTO patentInfoDTO = new PatentInfoDTO();
    Element one = root.element("pub_patent");
    if (one == null) {
      return null;
    }
    // 申请(专利)号
    String applicationNo = StringUtils.defaultString(one.attributeValue("patent_no"));
    applicationNo = StringUtils.substring(applicationNo, 0, 20);
    applicationNo = StringUtils.trimToEmpty(applicationNo);
    patentInfoDTO.setApplicationNo(applicationNo);
    // 公开（公告）号
    String publicationOpenNo = StringUtils.defaultString(one.attributeValue("patent_open_no"));
    publicationOpenNo = StringUtils.substring(publicationOpenNo, 0, 20);
    publicationOpenNo = StringUtils.trimToEmpty(publicationOpenNo);
    patentInfoDTO.setPublicationOpenNo(publicationOpenNo);
    // IPC号
    String iPC = StringUtils.defaultString(one.attributeValue("main_category_no"));
    iPC = StringUtils.substring(iPC, 0, 20);
    iPC = StringUtils.trimToEmpty(iPC);
    patentInfoDTO.setIPC(iPC);
    // CPC号
    String cpc = StringUtils.defaultString(one.attributeValue("cpc_no"));
    cpc = StringUtils.substring(cpc, 0, 20);
    cpc = StringUtils.trimToEmpty(cpc);
    patentInfoDTO.setCPC(cpc);
    // 专利国家
    String area = StringUtils.defaultString(one.attributeValue("patent_area"));
    area = StringUtils.trimToEmpty(area);
    PubPatentAreaEnum patentArea = buildArea(area);
    patentInfoDTO.setArea(patentArea);
    // 专利类别 数字
    String type = StringUtils.defaultString(one.attributeValue("patent_type"));
    type = StringUtils.trimToEmpty(type);
    patentInfoDTO.setType(type);
    // 生效开始日期
    String startDate = StringUtils.defaultString(one.attributeValue("start_date"));
    if (StringUtils.isBlank(startDate)) {
      String start_year = StringUtils.defaultString(one.attributeValue("start_year"));
      start_year = StringUtils.trimToEmpty(start_year);
      String start_month = StringUtils.defaultString(one.attributeValue("start_month"));
      start_month = StringUtils.trimToEmpty(start_month);
      String start_day = StringUtils.defaultString(one.attributeValue("start_day"));
      start_day = StringUtils.trimToEmpty(start_day);
      startDate = PubParamUtils.buildDate(start_year, start_month, start_day);
    }
    startDate = StringUtils.trimToEmpty(startDate);
    startDate = startDate.replace("/", "-").replace(".", "-");
    patentInfoDTO.setStartDate(startDate);
    // 生效截止日期
    String endDate = StringUtils.defaultString(one.attributeValue("end_date"));
    if (StringUtils.isBlank(endDate)) {
      String end_year = StringUtils.defaultString(one.attributeValue("end_year"));
      end_year = StringUtils.trimToEmpty(end_year);
      String end_month = StringUtils.defaultString(one.attributeValue("end_month"));
      end_month = StringUtils.trimToEmpty(end_month);
      String end_day = StringUtils.defaultString(one.attributeValue("end_day"));
      end_day = StringUtils.trimToEmpty(end_day);
      endDate = PubParamUtils.buildDate(end_year, end_month, end_day);
    }
    endDate = StringUtils.trimToEmpty(endDate);
    endDate = endDate.replace("/", "-").replace(".", "-");
    patentInfoDTO.setEndDate(endDate);
    // 发证单位
    patentInfoDTO.setIssuingAuthority(buildIssuingAuthority(patentArea));
    // 专利状态(0申请，1授权)
    Integer status = NumberUtils.parseInt(one.attributeValue("patent_status"), null);
    if (status == null) {
      if (StringUtils.isNotBlank(startDate)) {
        // 授权
        status = 1;
      } else {
        // 申请
        status = 0;
      }
    }
    patentInfoDTO.setStatus(status);
    String applier = StringUtils.defaultString(one.attributeValue("patent_applier"));
    applier = StringUtils.substring(applier, 0, 60);
    applier = StringUtils.trimToEmpty(applier);
    if (status != null && status == 0) {
      // 申请人
      patentInfoDTO.setApplier(applier);
    } else {
      // 专利授权人
      patentInfoDTO.setPatentee(applier);
    }
    // 专利转化状态
    String transitionStatus = StringUtils.defaultString(one.attributeValue("patent_transition_status"));
    transitionStatus = StringUtils.trimToEmpty(transitionStatus);
    patentInfoDTO.setTransitionStatus(buildTransitionStatus(transitionStatus));
    // 交易金额
    String price = StringUtils.defaultString(one.attributeValue("patent_price"));
    price = StringUtils.substring(price, 0, 60);
    price = StringUtils.trimToEmpty(price);
    patentInfoDTO.setPrice(price);
    // 申请日期
    String applicationDate = getPublishDate(root);
    if (StringUtils.isBlank(applicationDate)) {
      String publish_year = StringUtils.defaultString(one.attributeValue("publish_year"));
      publish_year = StringUtils.trimToEmpty(publish_year);
      String publish_month = StringUtils.defaultString(one.attributeValue("publish_month"));
      publish_month = StringUtils.trimToEmpty(publish_month);
      String publish_day = StringUtils.defaultString(one.attributeValue("publish_day"));
      publish_day = StringUtils.trimToEmpty(publish_day);
      applicationDate = PubParamUtils.buildDate(publish_year, publish_month, publish_day);
    }
    patentInfoDTO.setApplicationDate(applicationDate);

    String jsonStr = JacksonUtils.jsonObjectSerializer(patentInfoDTO);
    Map<String, Object> map = JacksonUtils.json2HashMap(jsonStr);
    return map;
  }

  private static String buildIssuingAuthority(PubPatentAreaEnum patentArea) {
    String flag = patentArea.getValue();
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
      return PubPatentTransitionStatusEnum.TRANSFER;
    }
    return PubPatentTransitionStatusEnum.NONE;
  }

  private static PubPatentAreaEnum buildArea(String area) {
    if (StringUtils.isBlank(area)) {
      return PubPatentAreaEnum.OTHER;
    }
    if ("china".equalsIgnoreCase(area)) {
      return PubPatentAreaEnum.CHINA;
    }
    if ("usa".equalsIgnoreCase(area)) {
      return PubPatentAreaEnum.USA;
    }
    if ("europe".equalsIgnoreCase(area)) {
      return PubPatentAreaEnum.EUROPE;
    }
    if ("wipo".equalsIgnoreCase(area)) {
      return PubPatentAreaEnum.WIPO;
    }
    if ("japan".equalsIgnoreCase(area)) {
      return PubPatentAreaEnum.JAPAN;
    }
    if ("other".equalsIgnoreCase(area)) {
      return PubPatentAreaEnum.OTHER;
    }
    return PubPatentAreaEnum.OTHER;
  }

  /**
   * 解析期刊类型成果信息
   * 
   * @param root
   * @return
   */
  private static Map<String, Object> dealPubJournal(Element root) throws Exception {
    JournalInfoDTO journalInfoDTO = new JournalInfoDTO();
    Element one = root.element("pub_journal");
    if (one == null) {
      return null;
    }
    // 期刊ID
    Long jid = NumberUtils.parseLong(one.attributeValue("jid"), null);
    journalInfoDTO.setJid(jid);
    // 期刊名
    String name = StringUtils.defaultString(one.attributeValue("jname"));
    name = StringUtils.substring(name, 0, 250);
    name = StringUtils.trimToEmpty(name);
    journalInfoDTO.setName(name);
    Element publication = root.element("publication");
    if (publication != null) {
      // 发表状态(P已发表/A已接收)
      String publishStatus = StringUtils.defaultString(publication.attributeValue("publish_state"));
      publishStatus = StringUtils.trimToEmpty(publishStatus);
      journalInfoDTO.setPublishStatus(publishStatus);
      // 期号
      String volumeNo = StringUtils.defaultString(publication.attributeValue("volume"));
      volumeNo = StringUtils.substring(volumeNo, 0, 20);
      volumeNo = StringUtils.trimToEmpty(volumeNo);
      journalInfoDTO.setVolumeNo(volumeNo);
      // 卷号
      String issue = StringUtils.defaultString(publication.attributeValue("issue"));
      issue = StringUtils.substring(issue, 0, 20);
      issue = StringUtils.trimToEmpty(issue);
      journalInfoDTO.setIssue(issue);
      // 起始页码
      String startPage = StringUtils.defaultString(publication.attributeValue("start_page"));
      // 结束页码
      String endPage = StringUtils.defaultString(publication.attributeValue("end_page"));
      // 文章号
      String articleNo = StringUtils.defaultString(publication.attributeValue("article_number"));
      String pageNumber = PubParamUtils.buildPageNumber(startPage, endPage, articleNo);
      pageNumber = StringUtils.substring(pageNumber, 0, 100);
      pageNumber = StringUtils.trimToEmpty(pageNumber);
      journalInfoDTO.setPageNumber(pageNumber);
    }
    // issn号
    String jissn = StringUtils.defaultString(one.attributeValue("jissn"));
    jissn = StringUtils.trimToEmpty(jissn);
    journalInfoDTO.setISSN(jissn);

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
  private static Map<String, Object> dealPubConfPaper(Element root) throws Exception {
    ConferencePaperDTO conferencePaperDTO = new ConferencePaperDTO();
    Element one = root.element("pub_conf_paper");
    if (one == null) {
      return null;
    }
    // 论文类别
    String paperType = StringUtils.defaultString(one.attributeValue("paper_type"));
    paperType = StringUtils.substring(paperType, 0, 50);
    if (StringUtils.isBlank(paperType)) {
      paperType = StringUtils.defaultString(one.attributeValue("conf_type"));
    }
    paperType = StringUtils.trimToEmpty(paperType);
    conferencePaperDTO.setPaperType(buildPaperType(paperType));
    // 会议名称
    String name = StringUtils.defaultString(one.attributeValue("conf_name"));
    name = StringUtils.substring(name, 0, 500);
    name = StringUtils.trimToEmpty(name);
    conferencePaperDTO.setName(name);
    // 会议组织者
    String organizer = StringUtils.defaultString(one.attributeValue("conf_organizer"));
    organizer = StringUtils.substring(organizer, 0, 50);
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
    Element publication = root.element("publication");
    if (publication != null) {
      // 起始页码
      String startPage = StringUtils.defaultString(publication.attributeValue("start_page"));
      // 结束页码
      String endPage = StringUtils.defaultString(publication.attributeValue("end_page"));
      String pageNumber = PubParamUtils.buildPageNumber(startPage, endPage, "");
      pageNumber = StringUtils.substring(pageNumber, 0, 100);
      pageNumber = StringUtils.trimToEmpty(pageNumber);
      conferencePaperDTO.setPageNumber(pageNumber);
    }

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
   * 解析XX类型成果信息 书籍章节和书著
   * 
   * @param root
   * @return
   */
  private static Map<String, Object> dealPubBook(Element root) throws Exception {
    BookInfoDTO bookInfoDTO = new BookInfoDTO();
    Element one = root.element("pub_book");
    if (one == null) {
      return null;
    }
    // 书名
    String name = StringUtils.defaultString(one.attributeValue("book_title"));
    name = StringUtils.trimToEmpty(name);
    bookInfoDTO.setName(name);
    // 书籍类型
    String book_type = StringUtils.defaultString(one.attributeValue("book_type"));
    book_type = StringUtils.trimToEmpty(book_type);
    bookInfoDTO.setType(buildBookType(book_type));
    // 出版社
    String publisher = StringUtils.defaultString(one.attributeValue("publisher"));
    publisher = StringUtils.substring(publisher, 0, 50);
    publisher = StringUtils.trimToEmpty(publisher);
    bookInfoDTO.setPublisher(publisher);
    // 页数
    Integer totalPages = NumberUtils.parseInt(one.attributeValue("total_pages"), null);
    bookInfoDTO.setTotalPages(totalPages);
    // 总字数
    Integer totalWords = NumberUtils.parseInt(one.attributeValue("total_words"), null);
    bookInfoDTO.setTotalWords(totalWords);
    // 丛书名
    String seriesName = StringUtils.defaultString(one.attributeValue("series_name"));
    seriesName = StringUtils.substring(seriesName, 0, 200);
    seriesName = StringUtils.trimToEmpty(seriesName);
    bookInfoDTO.setSeriesName(seriesName);
    // 书籍编辑
    String editors = StringUtils.defaultString(one.attributeValue("editors"));
    editors = StringUtils.substring(editors, 0, 50);
    editors = StringUtils.trimToEmpty(editors);
    bookInfoDTO.setEditors(editors);
    // 章节号码
    String chapterNo = StringUtils.defaultString(one.attributeValue("chapter_no"));
    chapterNo = StringUtils.substring(chapterNo, 0, 50);
    chapterNo = StringUtils.trimToEmpty(chapterNo);
    bookInfoDTO.setChapterNo(chapterNo);
    Element publication = root.element("publication");
    if (publication != null) {
      // 起始页码
      String startPage = StringUtils.defaultString(publication.attributeValue("start_page"));
      // 结束页码
      String endPage = StringUtils.defaultString(publication.attributeValue("end_page"));
      // 文章号
      String articleNo = StringUtils.defaultString(publication.attributeValue("article_number"));
      String pageNumber = PubParamUtils.buildPageNumber(startPage, endPage, articleNo);
      pageNumber = StringUtils.substring(pageNumber, 0, 100);
      pageNumber = StringUtils.trimToEmpty(pageNumber);
      bookInfoDTO.setPageNumber(pageNumber);
      // 语种
      String language = StringUtils.defaultString(publication.attributeValue("language"));;
      language = StringUtils.substring(language, 0, 20);
      language = StringUtils.trimToEmpty(language);
      bookInfoDTO.setLanguage(buildLanguage(language));
    }
    // 书籍isbn
    String ISBN = StringUtils.defaultString(one.attributeValue("isbn"));
    ISBN = StringUtils.substring(ISBN, 0, 20);
    ISBN = StringUtils.trimToEmpty(ISBN);
    bookInfoDTO.setISBN(ISBN);

    String jsonStr = JacksonUtils.jsonObjectSerializer(bookInfoDTO);
    Map<String, Object> map = JacksonUtils.json2HashMap(jsonStr);
    return map;
  }


  private static String buildLanguage(String language) {
    if (StringUtils.isBlank(language)) {
      return "Foreign Languge";
    }
    if ("1".equals(language)) {
      return "中文";
    }
    if ("2".equals(language)) {
      return "外文";
    }
    return "外文";
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
  private static Map<String, Object> dealPubAward(Element root) throws Exception {
    AwardsInfoDTO awardsInfoDTO = new AwardsInfoDTO();
    Element one = root.element("pub_award");
    if (one == null) {
      return null;
    }
    // 奖励类别
    String category = StringUtils.defaultString(one.attributeValue("award_category"));
    category = StringUtils.substring(category, 0, 50);
    category = StringUtils.trimToEmpty(category);
    awardsInfoDTO.setCategory(category);
    // 奖励等级
    String grade = StringUtils.defaultString(one.attributeValue("award_grade"));
    grade = StringUtils.substring(grade, 0, 50);
    grade = StringUtils.trimToEmpty(grade);
    awardsInfoDTO.setGrade(grade);
    // 颁发机构
    String issuingAuthority = StringUtils.defaultString(one.attributeValue("issue_ins_name"));
    issuingAuthority = StringUtils.substring(issuingAuthority, 0, 50);
    issuingAuthority = StringUtils.trimToEmpty(issuingAuthority);
    awardsInfoDTO.setIssuingAuthority(issuingAuthority);
    // 颁发机构ID
    Long issueInsId = NumberUtils.parseLong(one.attributeValue("issue_ins_id"), null);
    awardsInfoDTO.setIssueInsId(issueInsId);
    // 证书编号
    String certificateNo = StringUtils.defaultString(one.attributeValue("serial_number"));
    certificateNo = StringUtils.substring(certificateNo, 0, 50);
    certificateNo = StringUtils.trimToEmpty(certificateNo);
    awardsInfoDTO.setCertificateNo(certificateNo);
    // 授奖日期 2016/6/6
    String awardDate = getPublishDate(root);
    awardsInfoDTO.setAwardDate(awardDate);

    String jsonStr = JacksonUtils.jsonObjectSerializer(awardsInfoDTO);
    Map<String, Object> map = JacksonUtils.json2HashMap(jsonStr);
    return map;
  }

  /**
   * 解析成果基本信息
   * 
   * @param root
   * @param isChina
   * @return
   */
  private static Map<String, Object> dealPubBase(Element root, boolean isChina) throws Exception {
    Map<String, Object> map = new HashMap<String, Object>();
    Element one = root.element("publication");
    if (one == null) {
      return map;
    }
    // 中文标题//外文标题
    String zh_title = StringUtils.defaultString(one.attributeValue("zh_title"));
    String en_title = StringUtils.defaultString(one.attributeValue("en_title"));
    String title = isChina ? StringUtils.isNotBlank(zh_title) ? zh_title : en_title
        : StringUtils.isNotBlank(en_title) ? en_title : zh_title;
    title = HtmlUtils.htmlUnescape(title);
    title = StringUtils.trimToEmpty(title);
    map.put("title", title);
    // 中文摘要//外文摘要
    String zh_abstract = StringUtils.defaultString(one.attributeValue("zh_abstract"));
    String en_abstract = StringUtils.defaultString(one.attributeValue("en_abstract"));
    String summary = isChina ? StringUtils.isNotBlank(zh_abstract) ? zh_abstract : en_abstract
        : StringUtils.isNotBlank(en_abstract) ? en_abstract : zh_abstract;
    summary = HtmlUtils.htmlUnescape(summary);
    summary = StringUtils.trimToEmpty(summary);
    map.put("summary", summary);
    // 中文关键词//外文关键词
    String zh_keywords = StringUtils.defaultString(one.attributeValue("zh_keywords"));
    String en_keywords = StringUtils.defaultString(one.attributeValue("en_keywords"));
    String keywords = isChina ? StringUtils.isNotBlank(zh_keywords) ? zh_keywords : en_keywords
        : StringUtils.isNotBlank(en_keywords) ? en_keywords : zh_keywords;
    keywords = HtmlUtils.htmlUnescape(keywords);
    keywords = StringUtils.trimToEmpty(keywords);
    map.put("keywords", keywords);
    // 国家或地区ID
    String country_id = StringUtils.defaultString(one.attributeValue("country_id"));
    Long countryId = null;
    if (StringUtils.isNotBlank(country_id)) {
      countryId = NumberUtils.parseLong(country_id);
      map.put("countryId", countryId);
    } else {
      map.put("countryId", "");
    }
    // 地区的话，countryId值不会更新，需要通过city去匹配countryId
    String city = StringUtils.defaultString(one.attributeValue("city"));
    city = StringUtils.trimToEmpty(city);
    map.put("city", city);
    // doi
    String doi = StringUtils.defaultString(one.attributeValue("doi"));
    doi = StringUtils.trimToEmpty(doi);
    map.put("doi", doi);
    // 发表日期
    String publishDate = getPublishDate(root);
    map.put("publishDate", publishDate);
    // 基金标注
    String fundInfo = StringUtils.defaultString(one.attributeValue("fundinfo"));
    fundInfo = HtmlUtils.htmlUnescape(fundInfo);
    fundInfo = StringUtils.trimToEmpty(fundInfo);
    map.put("fundInfo", fundInfo);
    // "organization": "单位地址信息",
    String organization = StringUtils.defaultString(one.attributeValue("organization"));
    organization = HtmlUtils.htmlUnescape(organization);
    organization = StringUtils.trimToEmpty(organization);
    map.put("organization", organization);
    Element pubFulltext = root.element("pub_fulltext");
    if (pubFulltext != null) {
      // 来源全文路径
      String srcFulltextUrl = StringUtils.defaultString(pubFulltext.attributeValue("fulltext_url"));
      srcFulltextUrl = StringUtils.trimToEmpty(srcFulltextUrl);
      map.put("srcFulltextUrl", srcFulltextUrl);
    }
    Element pubType = root.element("pub_type");
    if (pubType != null) {
      // 类别ID
      map.put("pubType", NumberUtils.toLong(pubType.attributeValue("id"), 7));
    } else {
      map.put("pubType", 7);
    }
    Element pubMeta = root.element("pub_meta");
    if (pubMeta != null) {
      map.put("pubId", NumberUtils.parseLong(pubMeta.attributeValue("pub_id"), null));
    }
    // 默认手工录了
    Integer record_from = NumberUtils.toInt(pubMeta.attributeValue("record_from"), 0);
    record_from = resetRecordFrom(record_from);
    map.put("recordFrom", record_from);
    // 来源的dbId
    String dbId = pubMeta.attributeValue("source_db_id");
    Integer srcDbId = null;
    if (StringUtils.isNotBlank(dbId)) {
      srcDbId = NumberUtils.toInt(dbId);
      map.put("srcDbId", srcDbId);
    } else {
      map.put("srcDbId", "");
    }
    // 引用次数
    String cite_times = StringUtils.defaultString(one.attributeValue("cite_times"));
    Integer citations = 0;
    if (StringUtils.isNotBlank(cite_times)) {
      citations = NumberUtils.toInt(cite_times);
    }
    citations = resetCitations(dbId, citations);
    map.put("citations", citations);
    // 来源的source_id;
    String source_id = getSourceId(root);
    map.put("sourceId", source_id);
    // 来源的url]
    String sourceUrl = getSourceUrl(root);
    sourceUrl = StringUtils.trimToEmpty(sourceUrl);
    map.put("sourceUrl", sourceUrl);
    // 引用url
    String citedUrl = getCitedUrl(root);
    map.put("citedUrl", citedUrl);
    // 作者名
    String author_names_zh = StringUtils.defaultString(one.attributeValue("author_names"));
    String author_names_en = StringUtils.defaultString(one.attributeValue("authors_names_spec"));
    String authorNames = isChina ? StringUtils.isNotBlank(author_names_zh) ? author_names_zh : author_names_en
        : StringUtils.isNotBlank(author_names_en) ? author_names_en : author_names_zh;
    authorNames = HtmlUtils.htmlUnescape(authorNames);
    authorNames = StringUtils.trimToEmpty(authorNames);
    map.put("authorNames", authorNames);

    // HCP 高被引字段
    Integer HCP = NumberUtils.toInt(one.attributeValue("HCP"), 0);
    map.put("HCP", HCP);

    // HP 热门文章
    Integer HP = NumberUtils.toInt(one.attributeValue("HP"), 0);
    map.put("HP", HP);

    // OA Open Access
    Integer OA = NumberUtils.toInt(one.attributeValue("OA"), 0);
    map.put("OA", OA);
    return map;
  }



  private static String getSourceUrl(Element root) {
    String sourceUrl = "";
    Element publication = root.element("publication");
    if (publication != null) {
      sourceUrl = StringUtils.defaultString(publication.attributeValue("source_url"));
      if (StringUtils.isBlank(sourceUrl)) {
        sourceUrl = StringUtils.defaultString(publication.attributeValue("tmpsource_url"));
      }
    }
    Element pubMeta = root.element("pub_meta");
    if (pubMeta != null) {
      if (StringUtils.isBlank(sourceUrl)) {
        sourceUrl = StringUtils.defaultString(pubMeta.attributeValue("source_url"));
      }
    }
    return sourceUrl;
  }

  private static Integer resetRecordFrom(Integer record_from) {
    if (record_from == 3) {
      return 6;
    }
    if (record_from == 6) {
      return 3;
    }
    return record_from;
  }

  private static Integer resetCitations(String dbId, Integer citations) {
    if (StringUtils.isBlank(dbId)) {
      return 0;
    }
    if ("15".equals(dbId) || "16".equals(dbId) || "17".equals(dbId)) {
      return citations;
    }
    return 0;
  }

  private static String getCitedUrl(Element root) {
    String citedUrl = "";
    Element publication = root.element("publication");
    if (publication != null) {
      citedUrl = StringUtils.defaultString(publication.attributeValue("cited_url"));
      if (StringUtils.isBlank(citedUrl)) {
        citedUrl = StringUtils.defaultString(publication.attributeValue("tmpcite_record_url"));
      }
      citedUrl = StringUtils.trimToEmpty(citedUrl);
    }
    return citedUrl;
  }

  private static String getPublishDate(Element root) {
    Element one = root.element("publication");
    if (one == null) {
      return "";
    }
    String publishDate = StringUtils.defaultString(one.attributeValue("publish_date"));
    if (StringUtils.isBlank(publishDate)) {
      publishDate = StringUtils.defaultString(one.attributeValue("pubyear"));
    }
    publishDate = StringUtils.trimToEmpty(publishDate);
    publishDate = publishDate.replace("/", "-");
    publishDate = publishDate.replace(".", "-");
    return publishDate;
  }

  private static String getSourceId(Element root) {
    Element one = root.element("publication");
    if (one == null) {
      return "";
    }
    String sourceId = StringUtils.defaultString(one.attributeValue("source_id"));
    if (StringUtils.isBlank(sourceId)) {
      Element pubMeta = root.element("pub_meta");
      sourceId = StringUtils.defaultString(pubMeta.attributeValue("source_id"));
    }
    sourceId = StringUtils.trimToEmpty(sourceId);
    return sourceId;
  }

  /**
   * 解析成果类型信息
   * 
   * @param root
   * @return
   */
  @SuppressWarnings("unused")
  private static Map<String, String> dealPubType(Element root) throws Exception {
    Map<String, String> map = new HashMap<String, String>();
    Element one = root.element("pub_type");
    if (one == null) {
      return map;
    }
    // 类别ID
    map.put("id", StringUtils.defaultString(one.attributeValue("id")));
    // 类别中文名
    map.put("zh_name", StringUtils.defaultString(one.attributeValue("zh_name")));
    // 类别英文名
    map.put("en_name", StringUtils.defaultString(one.attributeValue("en_name")));
    // 成果/文献/工作文档/项目标识
    map.put("article_type", StringUtils.defaultString(one.attributeValue("article_type")));
    return map;
  }

  /**
   * 解析成果全文信息
   * 
   * @param root
   * @return
   */
  private static PubFulltextDTO dealPubFulltext(Element root) throws Exception {
    PubFulltextDTO pubFulltextDTO = new PubFulltextDTO();
    Element one = root.element("pub_fulltext");
    if (one == null) {
      return null;
    }
    // 全文附件ID
    String file_id = StringUtils.defaultString(one.attributeValue("file_id"));
    if (StringUtils.isBlank(file_id)) {
      return null;
    }
    Long fileId = StringUtils.isNotBlank(file_id) ? NumberUtils.toLong(file_id) : null;
    pubFulltextDTO.setFileId(fileId);

    // 权限
    Integer permission = NumberUtils.toInt(one.attributeValue("permission"), 0);
    permission = (permission == 1) ? 2 : permission;
    pubFulltextDTO.setPermission(permission);

    // 原始全文url
    String srcFulltextUrl = StringUtils.defaultString(one.attributeValue("fulltext_url"));
    srcFulltextUrl = StringUtils.trimToEmpty(srcFulltextUrl);
    pubFulltextDTO.setSrcFulltextUrl(srcFulltextUrl);

    // 全文文件名
    String fileName = StringUtils.defaultString(one.attributeValue("file_name"));
    fileName = StringUtils.trimToEmpty(fileName);
    pubFulltextDTO.setFileName(fileName);

    // 上传时间
    String upload_date = StringUtils.defaultString(one.attributeValue("upload_date"));
    upload_date = StringUtils.trimToEmpty(upload_date);
    upload_date = upload_date.replace("/", "-").replace(".", "-");
    pubFulltextDTO.setGmtCreate(upload_date);
    pubFulltextDTO.setGmtModified(upload_date);
    return pubFulltextDTO;
  }

  /**
   * 解析成果附件信息
   * 
   * @param root
   * @return
   */
  @SuppressWarnings("unchecked")
  private static List<PubAccessoryDTO> dealPubAccessorys(Element root) throws Exception {

    List<PubAccessoryDTO> list = new ArrayList<>();
    Element pub_attachments = root.element("pub_attachments");
    if (pub_attachments == null) {
      return list;
    }
    List<Element> elements = pub_attachments.elements("pub_attachment");
    if (elements != null && elements.size() > 0) {
      PubAccessoryDTO pubAccessoryDTO = null;
      for (Element one : elements) {
        pubAccessoryDTO = new PubAccessoryDTO();
        String file_id = StringUtils.defaultString(one.attributeValue("file_id"));
        if (StringUtils.isBlank(file_id)) {
          continue;
        }
        // 附件的fileId
        Long fileId = NumberUtils.toLong(file_id);
        pubAccessoryDTO.setFileId(fileId);
        // 附件名
        String fileName = StringUtils.defaultString(one.attributeValue("file_name"));
        fileName = StringUtils.trimToEmpty(fileName);
        pubAccessoryDTO.setFileName(fileName);
        // 权限
        Integer permission = NumberUtils.toInt(one.attributeValue("permission"), 0);
        permission = (permission == 1) ? 2 : permission;
        pubAccessoryDTO.setPermission(permission);
        // 上传时间
        String upload_date = StringUtils.defaultString(one.attributeValue("upload_date"));
        upload_date = StringUtils.trimToEmpty(upload_date);
        upload_date = upload_date.replace("/", "-").replace(".", "-");
        pubAccessoryDTO.setGmtCreate(upload_date);
        pubAccessoryDTO.setGmtModified(upload_date);
        list.add(pubAccessoryDTO);
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
    List<PubMemberDTO> list = new ArrayList<>();
    Element pubMembers = root.element("pub_members");
    if (pubMembers == null) {
      return list;
    }
    List<Element> elements = pubMembers.elements("pub_member");
    if (elements != null && elements.size() > 0) {
      PubMemberDTO pubMemberDTO = null;
      int index = 1;
      for (Element one : elements) {
        pubMemberDTO = new PubMemberDTO();
        // 顺序号
        Integer seqNo = index++;
        pubMemberDTO.setSeqNo(seqNo);
        // psnId
        String member_psn_id = one.attributeValue("member_psn_id");
        Long psnId = StringUtils.isNotBlank(member_psn_id) ? NumberUtils.toLong(member_psn_id) : null;
        pubMemberDTO.setPsnId(psnId);
        // 作者Email
        String email = StringUtils.defaultString(one.attributeValue("email"));
        email = StringUtils.trimToEmpty(email);
        pubMemberDTO.setEmail(email);
        // 作者名
        String name = StringUtils.defaultString(one.attributeValue("member_psn_name"));
        name = StringUtils.trimToEmpty(name);
        pubMemberDTO.setName(name);
        // 机构名称
        List<MemberInsDTO> insNames = buildInsName(one);
        pubMemberDTO.setInsNames(insNames);
        // 作者单位
        String dept = StringUtils.defaultString(one.attributeValue("dept"));
        pubMemberDTO.setDept(dept);
        // 第一作者
        boolean firstAuthor = StringUtils.defaultString(one.attributeValue("first_author")).equals("1");
        pubMemberDTO.setFirstAuthor(firstAuthor);
        // 通讯作者
        boolean communicable = StringUtils.defaultString(one.attributeValue("author_pos")).equals("1");
        pubMemberDTO.setCommunicable(communicable);
        list.add(pubMemberDTO);
      }
    }
    return list;
  }

  private static List<MemberInsDTO> buildInsName(Element member) {
    List<MemberInsDTO> ins = new ArrayList<>();
    // insName 待确定，存在多个insName，还有insId
    MemberInsDTO mbean = null;
    if (null != member.attributeValue("ins_name")) {
      mbean = new MemberInsDTO();
      if (null != member.attributeValue("ins_id")) {
        mbean.setInsId(NumberUtils.parseLong(member.attributeValue("ins_id"), null));
      } else {
        mbean.setInsId(null);
      }
      mbean.setInsName(member.attributeValue("ins_name"));
      if (StringUtils.isNotBlank(mbean.getInsName())) {
        ins.add(mbean);
      }
    }
    if (null != member.attributeValue("ins_name1")) {
      mbean = new MemberInsDTO();
      if (null != member.attributeValue("ins_id1")) {
        mbean.setInsId(NumberUtils.parseLong(member.attributeValue("ins_id1"), null));
      } else {
        mbean.setInsId(null);
      }
      mbean.setInsName(member.attributeValue("ins_name1"));
      if (StringUtils.isNotBlank(mbean.getInsName())) {
        ins.add(mbean);
      }
    }
    if (null != member.attributeValue("ins_name2")) {
      mbean = new MemberInsDTO();
      if (null != member.attributeValue("ins_id2")) {
        mbean.setInsId(NumberUtils.parseLong(member.attributeValue("ins_id2"), null));
      } else {
        mbean.setInsId(null);
      }
      mbean.setInsName(member.attributeValue("ins_name2"));
      if (StringUtils.isNotBlank(mbean.getInsName())) {
        ins.add(mbean);
      }
    }
    if (null != member.attributeValue("ins_name3")) {
      mbean = new MemberInsDTO();
      if (null != member.attributeValue("ins_id3")) {
        mbean.setInsId(NumberUtils.parseLong(member.attributeValue("ins_id3"), null));
      } else {
        mbean.setInsId(null);
      }
      mbean.setInsName(member.attributeValue("ins_name3"));
      if (StringUtils.isNotBlank(mbean.getInsName())) {
        ins.add(mbean);
      }
    }
    if (null != member.attributeValue("ins_name4")) {
      mbean = new MemberInsDTO();
      if (null != member.attributeValue("ins_id4")) {
        mbean.setInsId(NumberUtils.parseLong(member.attributeValue("ins_id4"), null));
      } else {
        mbean.setInsId(null);
      }
      mbean.setInsName(member.attributeValue("ins_name4"));
      if (StringUtils.isNotBlank(mbean.getInsName())) {
        ins.add(mbean);
      }
    }
    if (null != member.attributeValue("ins_name5")) {
      mbean = new MemberInsDTO();
      if (null != member.attributeValue("ins_id5")) {
        mbean.setInsId(NumberUtils.parseLong(member.attributeValue("ins_id5"), null));
      } else {
        mbean.setInsId(null);
      }
      mbean.setInsName(member.attributeValue("ins_name5"));
      if (StringUtils.isNotBlank(mbean.getInsName())) {
        ins.add(mbean);
      }
    }
    return ins;
  }

}
