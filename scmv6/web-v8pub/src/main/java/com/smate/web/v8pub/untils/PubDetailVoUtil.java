package com.smate.web.v8pub.untils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.smate.core.base.pub.enums.PubThesisDegreeEnum;
import com.smate.core.base.pub.vo.PubDetailVO;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.web.v8pub.dom.AwardsInfoBean;
import com.smate.web.v8pub.dom.BookInfoBean;
import com.smate.web.v8pub.dom.ConferencePaperBean;
import com.smate.web.v8pub.dom.JournalInfoBean;
import com.smate.web.v8pub.dom.PatentInfoBean;
import com.smate.web.v8pub.dom.PubTypeInfoBean;
import com.smate.web.v8pub.dom.SoftwareCopyrightBean;
import com.smate.web.v8pub.dom.StandardInfoBean;
import com.smate.web.v8pub.dom.ThesisInfoBean;
import com.smate.web.v8pub.dom.sns.PubSnsDetailDOM;
import com.smate.web.v8pub.dto.AwardsInfoDTO;
import com.smate.web.v8pub.dto.BookInfoDTO;
import com.smate.web.v8pub.dto.ConferencePaperDTO;
import com.smate.web.v8pub.dto.JournalInfoDTO;
import com.smate.web.v8pub.dto.OtherInfoDTO;
import com.smate.web.v8pub.dto.PatentInfoDTO;
import com.smate.web.v8pub.dto.PubSituationDTO;
import com.smate.web.v8pub.dto.SoftwareCopyrightDTO;
import com.smate.web.v8pub.dto.StandardInfoDTO;
import com.smate.web.v8pub.dto.ThesisInfoDTO;

/**
 * 
 * 成果详情转换接口
 * 
 * @author aijiangbin
 * @date 2018年8月14日
 */
public class PubDetailVoUtil {
  private static Logger logger = LoggerFactory.getLogger(PubDetailVoUtil.class);

  /**
   * 构建成果信息
   * 
   * @param detailDOM
   * @param detailDOM
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public static PubDetailVO buildPubTypeInfo(PubSnsDetailDOM detailDOM) {
    int pubType = detailDOM.getPubType();
    PubDetailVO pubDetailVO = null;
    PubTypeInfoBean pubTypeInfoBean = detailDOM.getTypeInfo();
    pubDetailVO = new PubDetailVO();
    switch (pubType) {
      case 1:
        AwardsInfoBean awardsInfoBean = (AwardsInfoBean) pubTypeInfoBean;
        if (awardsInfoBean != null) {
          AwardsInfoDTO awardsInfo = new AwardsInfoDTO();
          awardsInfo.setCategory(awardsInfoBean.getCategory());
          awardsInfo.setIssuingAuthority(awardsInfoBean.getIssuingAuthority());
          awardsInfo.setIssueInsId(awardsInfoBean.getIssueInsId());
          awardsInfo.setCertificateNo(awardsInfoBean.getCertificateNo());
          awardsInfo.setAwardDate(awardsInfoBean.getAwardDate());
          awardsInfo.setGrade(awardsInfoBean.getGrade());
          pubDetailVO.setPubTypeInfo(awardsInfo);
        }
        break;
      case 2:
      case 10:
        BookInfoBean bookInfoBean = (BookInfoBean) pubTypeInfoBean;
        if (bookInfoBean != null) {
          BookInfoDTO bookInfo = new BookInfoDTO();
          bookInfo.setName(bookInfoBean.getName());
          bookInfo.setSeriesName(bookInfoBean.getSeriesName());
          bookInfo.setEditors(bookInfoBean.getEditors());
          bookInfo.setISBN(bookInfoBean.getISBN());
          bookInfo.setPublisher(bookInfoBean.getPublisher());
          bookInfo.setTotalWords(bookInfoBean.getTotalPages());
          bookInfo.setPageNumber(bookInfoBean.getPageNumber());
          bookInfo.setType(bookInfoBean.getType());
          bookInfo.setTotalPages(bookInfoBean.getTotalPages());
          bookInfo.setChapterNo(bookInfoBean.getChapterNo());
          bookInfo.setLanguage(bookInfoBean.getLanguage());
          bookInfo.setShowTotalPageOrPage(getShowTotalPage(bookInfoBean));
          bookInfo.setTotalWords(bookInfoBean.getTotalWords());
          pubDetailVO.setPubTypeInfo(bookInfo);
        }

        break;
      case 3:
        ConferencePaperBean conferencePaperBean = (ConferencePaperBean) pubTypeInfoBean;
        if (conferencePaperBean != null) {
          ConferencePaperDTO conferencePaper = new ConferencePaperDTO();
          conferencePaper.setPaperType(conferencePaperBean.getPaperType());
          conferencePaper.setName(conferencePaperBean.getName());
          conferencePaper.setOrganizer(conferencePaperBean.getOrganizer());
          conferencePaper.setStartDate(conferencePaperBean.getStartDate());
          conferencePaper.setEndDate(conferencePaperBean.getEndDate());
          conferencePaper.setPageNumber(conferencePaperBean.getPageNumber());
          conferencePaper.setPapers(conferencePaperBean.getPapers());
          pubDetailVO.setPubTypeInfo(conferencePaper);
        }

        break;
      case 4:
        JournalInfoBean journalInfoBean = (JournalInfoBean) pubTypeInfoBean;
        if (journalInfoBean != null) {
          JournalInfoDTO journalInfo = new JournalInfoDTO();
          journalInfo.setJid(journalInfoBean.getJid());
          journalInfo.setName(journalInfoBean.getName());
          journalInfo.setVolumeNo(journalInfoBean.getVolumeNo());
          journalInfo.setIssue(journalInfoBean.getIssue());
          journalInfo.setPageNumber(journalInfoBean.getPageNumber());
          journalInfo.setPublishStatus(journalInfoBean.getPublishStatus());
          journalInfo.setISSN(journalInfoBean.getISSN());
          pubDetailVO.setPubTypeInfo(journalInfo);
        }

        break;
      case 5:
        PatentInfoBean patentInfoBean = (PatentInfoBean) pubTypeInfoBean;
        if (patentInfoBean != null) {
          PatentInfoDTO patentInfo = new PatentInfoDTO();
          patentInfo.setType(patentInfoBean.getType());
          patentInfo.setArea(patentInfoBean.getArea());
          patentInfo.setStatus(patentInfoBean.getStatus());
          patentInfo.setApplier(patentInfoBean.getApplier());
          patentInfo.setPatentee(patentInfoBean.getPatentee());
          patentInfo.setApplicationDate(patentInfoBean.getApplicationDate());
          patentInfo.setStartDate(patentInfoBean.getStartDate());
          patentInfo.setEndDate(patentInfoBean.getEndDate());
          patentInfo.setApplicationNo(patentInfoBean.getApplicationNo());
          patentInfo.setPublicationOpenNo(patentInfoBean.getPublicationOpenNo());
          patentInfo.setIPC(patentInfoBean.getIPC());
          patentInfo.setCPC(patentInfoBean.getCPC());
          patentInfo.setTransitionStatus(patentInfoBean.getTransitionStatus());
          patentInfo.setPrice(patentInfoBean.getPrice());
          patentInfo.setIssuingAuthority(patentInfoBean.getIssuingAuthority());

          pubDetailVO.setPubTypeInfo(patentInfo);
        }

        break;
      case 7:
        break;
      case 8:
        ThesisInfoBean thesisInfoBean = (ThesisInfoBean) pubTypeInfoBean;
        if (thesisInfoBean != null) {
          ThesisInfoDTO thesisInfo = new ThesisInfoDTO();
          PubThesisDegreeEnum degreeEnum = thesisInfoBean.getDegree();
          thesisInfo.setDegree(degreeEnum);
          thesisInfo.setDefenseDate(thesisInfoBean.getDefenseDate());
          thesisInfo.setIssuingAuthority(thesisInfoBean.getIssuingAuthority());
          thesisInfo.setDepartment(thesisInfoBean.getDepartment());

          pubDetailVO.setPubTypeInfo(thesisInfo);
        }
        break;
      case 12:
        // 标准
        StandardInfoBean standardInfoBean = (StandardInfoBean) pubTypeInfoBean;
        if (standardInfoBean != null) {
          StandardInfoDTO standardInfo = new StandardInfoDTO();
          standardInfo.setPublishAgency(standardInfoBean.getPublishAgency());
          standardInfo.setStandardNo(standardInfoBean.getStandardNo());
          standardInfo.setTechnicalCommittees(standardInfoBean.getTechnicalCommittees());
          standardInfo.setType(standardInfoBean.getType());
          standardInfo.setIcsNo(standardInfoBean.getIcsNo());
          standardInfo.setDomainNo(standardInfoBean.getDomainNo());
          standardInfo.setImplementDate(standardInfoBean.getImplementDate());
          standardInfo.setObsoleteDate(standardInfoBean.getObsoleteDate());
          pubDetailVO.setPubTypeInfo(standardInfo);
        }
        break;
      case 13:
        // 软件著作权
        SoftwareCopyrightBean scBean = (SoftwareCopyrightBean) pubTypeInfoBean;
        if (scBean != null) {
          SoftwareCopyrightDTO scDto = new SoftwareCopyrightDTO();
          scDto.setRegisterNo(scBean.getRegisterNo());
          scDto.setAcquisitionType(scBean.getAcquisitionType());
          scDto.setScopeType(scBean.getScopeType());
          scDto.setCategoryNo(scBean.getCategoryNo());
          scDto.setFirstPublishDate(scBean.getFirstPublishDate());
          scDto.setPublicityDate(scBean.getPublicityDate());
          scDto.setRegisterDate(scBean.getRegisterDate());
          pubDetailVO.setPubTypeInfo(scDto);
        }
        break;
      default:
        break;
    }
    return pubDetailVO;
  }

  public static String getShowTotalPage(BookInfoBean bookInfoBean) {
    Integer totalPages = bookInfoBean.getTotalPages();
    String pageNumber = bookInfoBean.getPageNumber();
    if (totalPages != null && totalPages != 0) {
      return totalPages + "";
    } else {
      return pageNumber;
    }
  }

  public static PubDetailVO getBuilPubDetailVO(String pubJson) throws Exception {
    PubDetailVO pub = new PubDetailVO();
    Map<String, Object> map = null;
    try {
      map = JacksonUtils.json2Map(pubJson);
      if (map.get("pubId") == null) {
        logger.error("json字符串，不能解析成PubDetailVoUtil.PubDetailVO");
        return null;
      }
    } catch (IOException e) {
      logger.error("获取成果详情转化json出错", e);
      throw new Exception();
    }

    Integer pubType = Integer.valueOf(map.get("pubType") == null ? "7" : Objects.toString(map.get("pubType")));
    switch (pubType) {
      case 1: // 奖励
        pub = (PubDetailVO) JacksonUtils.jsonObject(pubJson, new TypeReference<PubDetailVO<AwardsInfoDTO>>() {});
        break;
      case 2:// 书籍
        pub = (PubDetailVO) JacksonUtils.jsonObject(pubJson, new TypeReference<PubDetailVO<BookInfoDTO>>() {});
        break;
      case 3:// 会议论文
        pub = (PubDetailVO) JacksonUtils.jsonObject(pubJson, new TypeReference<PubDetailVO<ConferencePaperDTO>>() {});
        break;
      case 4:// 期刊论文
        pub = (PubDetailVO) JacksonUtils.jsonObject(pubJson, new TypeReference<PubDetailVO<JournalInfoDTO>>() {});
        break;
      case 5:// 专利
        pub = (PubDetailVO) JacksonUtils.jsonObject(pubJson, new TypeReference<PubDetailVO<PatentInfoDTO>>() {});
        break;
      case 7:// 其他
        pub = (PubDetailVO) JacksonUtils.jsonObject(pubJson, new TypeReference<PubDetailVO<OtherInfoDTO>>() {});
        break;
      case 8:// 学位论文
        pub = (PubDetailVO) JacksonUtils.jsonObject(pubJson, new TypeReference<PubDetailVO<ThesisInfoDTO>>() {});
        break;
      case 10:// 书籍章节
        pub = (PubDetailVO) JacksonUtils.jsonObject(pubJson, new TypeReference<PubDetailVO<BookInfoDTO>>() {});
        break;
    }
    return pub;
  }

  /**
   * 得到年份
   * 
   * @param date
   * @return
   */
  public static String parseDateForYear(String date) {
    if (date == null) {
      return "";
    }
    String[] split = date.split("[-/]");
    return split != null && split.length > 0 ? split[0] : "";
  }

  /**
   * 得到月份
   * 
   * @param date
   * @return
   */
  public static String parseDateForMonth(String date) {
    if (date == null) {
      return "";
    }
    String[] split = date.split("[-/]");
    if (split.length > 1) {
      return split[1];
    }
    return "";
  }

  /**
   * 得到天数
   * 
   * @param date
   * @return
   */
  public static String parseDateForDay(String date) {
    if (date == null) {
      return "";
    }
    String[] split = date.split("[-/]");
    if (split.length > 2) {
      return split[2];
    }
    return "";
  }

  /**
   * 得到年份
   * 
   * @param date
   * @return
   */
  public static String parseDateForYear(Date date) {
    if (date == null) {
      return "";
    }
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    int year = calendar.get(calendar.YEAR);
    return "" + year;
  }

  /**
   * 得到月份
   * 
   * @param date
   * @return
   */
  public static String parseDateForMonth(Date date) {
    if (date == null) {
      return "";
    }
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    int month = calendar.get(calendar.MONTH) + 1;
    return "" + month;
  }

  /**
   * 得到天数
   * 
   * @param date
   * @return
   */
  public static String parseDateForDay(Date date) {
    if (date == null) {
      return "";
    }
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    int day = calendar.get(calendar.DAY_OF_MONTH);
    return "" + day;
  }

  /**
   * 填充是否是本人 {\\\"is_mine\\\":\\\"否\\\",\\\"is_message\\\":\\\"0
   * \\\",\\\"first_author\\\":\\\"1\\\",\\\"psn_name\\\":\\\"张含卓 \\\",\\\"email\\\":\\\"\\\"}
   * 
   * @param pubElement
   */
  /*
   * public static void fillIsOwnerByPubDetailVO(Map<String, Object> dataMap, PubDetailVO pubDetailVO)
   * {
   * 
   * List<PubMemberDTO> pubMemberList = pubDetailVO.getMembers(); boolean flag = false; if
   * (pubMemberList != null && pubMemberList.size() > 0) { for (int j = 0; j < pubMemberList.size();
   * j++) { boolean owner = pubMemberList.get(j).isOwner(); if (owner) { flag = true; break; } }
   * List<Map<String, String>> authorsList = new ArrayList<Map<String, String>>(); for (int j = 0; j <
   * pubMemberList.size(); j++) { Map<String, String> authorMap = new HashMap<String, String>();
   * PubMemberDTO pubMember = pubMemberList.get(j); String psnName = pubMember.getName(); // 作者名
   * String orgName = ""; // 作者机构 String autoOrgName = pubMember.getInsName(); // 作者机构 String email =
   * pubMember.getEmail(); // 邮箱 int isMessage = pubMember.getCommunicable() == null ? 0 :
   * pubMember.getCommunicable(); // 第一通信 // 作者 int firstAuthor = pubMember.getFirstAuthor() == null ?
   * 0 : pubMember.getFirstAuthor(); // 第一作者 int isMime = pubMember.getOwner() == null ? 0 :
   * pubMember.getOwner(); // 是否本人 // 作者名 authorMap.put("psn_name", psnName);
   * 
   * if (StringUtils.isNotBlank(orgName)) { authorMap.put("org_name", orgName); } else {
   * authorMap.put("org_name", autoOrgName); } authorMap.put("email", email);
   * 
   * authorMap.put("is_message", isMessage == 1 ? "1" : "0"); authorMap.put("first_author",
   * firstAuthor == 1 ? "1" : "0"); authorMap.put("is_mine", isMime == 1 ? "是" : "否");
   * authorsList.add(authorMap); } dataMap.put("pub_member", authorsList); } dataMap.put("owner", flag
   * ? "1" : "0"); }
   */

  /**
   * 得到成果收录情况
   * 
   * @return
   */
  /*
   * public static void fillListInfo(PubDetailVO pubDetailVO, Map<String, Object> dataMap) {
   * List<Sitation> sitations = pubDetailVO.getSitations(); String listInfoListStr = ""; if (sitations
   * != null && sitations.size() > 0) { for (Sitation sitation : sitations) { if (sitation.included) {
   * listInfoListStr += sitation.getLibrary() + ","; } } if (StringUtils.isNotBlank(listInfoListStr))
   * { listInfoListStr = listInfoListStr.substring(0, listInfoListStr.length() - 1).toLowerCase(); } }
   * dataMap.put("list_info", listInfoListStr); }
   */

  /**
   * 返回收录情况
   * 
   * @param pubDetailVO
   * @return
   */
  public static String fillListInfo(PubDetailVO pubDetailVO) {
    List<PubSituationDTO> sitations = pubDetailVO.getSituations();
    String listInfoListStr = "";
    if (sitations != null && sitations.size() > 0) {
      for (PubSituationDTO sitation : sitations) {
        if (sitation.isSitStatus()) {
          listInfoListStr += sitation.getLibraryName() + ",";
        }
      }
      if (StringUtils.isNotBlank(listInfoListStr)) {
        listInfoListStr = listInfoListStr.substring(0, listInfoListStr.length() - 1).toLowerCase();
      }
    }
    return listInfoListStr;
  }

  /**
   * 判断是否认证
   * 
   * @param dataMap
   */
  public static void isOrNotAuthenticated(Map<String, Object> dataMap, PubDetailVO pubDetailVO) {
    Integer dbId = pubDetailVO.getSourceDbId();
    String authenticated = "0";
    if (dbId != null) {
      if (dbId > 0) {
        authenticated = "1";
      }
    }
    dataMap.put("authenticated", authenticated);

  }

  /**
   * 填充创建时间
   * 
   * @param dataMap
   */
  public static void fillCreateTime(PubDetailVO pubDetailVO, Map<String, Object> dataMap) {
    if (pubDetailVO.getGmtCreate() != null) {
      SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
      String format = sf.format(pubDetailVO.getGmtCreate());
      dataMap.put("create_date", format);
    } else {
      dataMap.put("create_date", "");
    }
  }

  /**
   * 获取成果更新时间
   * 
   * @param dataMap
   * @param pubDetailVO
   */
  public static void buildPubUpdateTime(PubDetailVO pubDetailVO, Map<String, Object> dataMap) {
    Date updateDate = pubDetailVO.getGmtModified();
    String updateDateStr = "";
    if (updateDate != null) {
      updateDateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(updateDate);
    }
    dataMap.put("pub_update_time", updateDateStr);
  }

  public static void main(String[] args) {
    String str = "2015-55/5";
    String[] split = str.split("[-/]");
    for (String string : split) {
      System.out.println(string);
    }
  }

}
