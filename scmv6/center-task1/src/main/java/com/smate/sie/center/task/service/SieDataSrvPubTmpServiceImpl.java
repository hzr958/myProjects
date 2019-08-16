package com.smate.sie.center.task.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.transaction.Transactional;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.center.task.exception.ServiceException;
import com.smate.core.base.utils.dao.security.role.Sie6InsPortalDao;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.rol.Sie6InsPortal;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.sie.center.task.dao.SieDataSrvPubTmpDao;
import com.smate.sie.center.task.dao.SieDataSrvPubTmpRefreshDao;
import com.smate.sie.center.task.model.PubXmlDocument;
import com.smate.sie.center.task.model.SieDataSrvPubTmp;
import com.smate.sie.center.task.model.SieDataSrvPubTmpRefresh;
import com.smate.sie.core.base.utils.dao.pub.Sie6PublicationListDao;
import com.smate.sie.core.base.utils.dao.pub.SiePubMemberDao;
import com.smate.sie.core.base.utils.dao.pub.SiePublicationDao;
import com.smate.sie.core.base.utils.model.pub.Sie6PublicationList;
import com.smate.sie.core.base.utils.model.pub.SiePubMember;
import com.smate.sie.core.base.utils.model.pub.SiePublication;
import com.smate.sie.core.base.utils.pub.dom.BookInfoBean;
import com.smate.sie.core.base.utils.pub.dom.ConferencePaperBean;
import com.smate.sie.core.base.utils.pub.dom.JournalInfoBean;
import com.smate.sie.core.base.utils.pub.dom.PubDetailDOM;
import com.smate.sie.core.base.utils.pub.service.PubJsonPOService;

/***
 * 生成单位成果数据
 * 
 * @author 叶星源
 * @Date 20180911
 */
@SuppressWarnings("deprecation")
@Service("sieDataSrvPubTmpService")
@Transactional(rollbackOn = Exception.class)
public class SieDataSrvPubTmpServiceImpl implements SieDataSrvPubTmpService {
  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private SieDataSrvPubTmpRefreshDao sieDataSrvPubTmpRefreshDao;
  @Autowired
  private SieDataSrvPubTmpDao sieDataSrvPubTmpDao;
  @Autowired
  private PubJsonPOService pubJsonPOService;
  @Autowired
  private SiePublicationDao siePublicationDao;
  @Autowired
  private Sie6InsPortalDao sieInsPortalDao;
  @Autowired
  private Sie6PublicationListDao siePublicationListDao;
  @Autowired
  private SieDataSrvPubTmpRefreshDao SieDataSrvPubTmpRefreshDao;
  @Autowired
  private SiePubMemberDao siePubMemberDao;

  @Override
  public List<SieDataSrvPubTmpRefresh> getNeedRefreshData(int maxSize) {
    try {
      return this.sieDataSrvPubTmpRefreshDao.queryNeedRefresh(maxSize);
    } catch (Exception e) {
      logger.error("获取需要生成的单位成果数据失败", e);
      throw new ServiceException("", e);
    }
  }

  @Override
  public void saveSieDataSrvPubTmpRefresh(SieDataSrvPubTmpRefresh sieDataSrvPubTmpRefresh) {
    sieDataSrvPubTmpRefresh.setStatus(9);
    SieDataSrvPubTmpRefreshDao.save(sieDataSrvPubTmpRefresh);
  }


  /**
   * 处理Refresh表中的数据
   */
  @SuppressWarnings({"unused"})
  @Override
  public void refreshData(SieDataSrvPubTmpRefresh sieDataSrvPatTmpRefresh) {
    Long pubid = sieDataSrvPatTmpRefresh.getPubId();
    // 数据预处理
    SiePublication pub = siePublicationDao.get(pubid);
    // 获取json字符串，并且构造成PubDetailDOM对象
    PubDetailDOM detail = pubJsonPOService.getDOMByIdAndType(pubid, pub.getPubType());
    // 数据填充
    SieDataSrvPubTmp sieDataSrvPubTmp = new SieDataSrvPubTmp();
    String articleNo = "";
    // 特殊字段处理
    if (pub.getPubType() == 4) {
      JournalInfoBean journal = (JournalInfoBean) detail.getTypeInfo();
      String issn = journal.getISSN();
      String journalName = journal.getName();// 期刊名称
      sieDataSrvPubTmp.setJournalName(journalName);
      sieDataSrvPubTmp.setIssn(issn);
      articleNo = journal.getArticleNo();
    } else if (pub.getPubType() == 2 || pub.getPubType() == 10) {
      BookInfoBean book = (BookInfoBean) detail.getTypeInfo();
      articleNo = book.getArticleNo();
    } else if (pub.getPubType() == 3) {
      ConferencePaperBean conf = (ConferencePaperBean) detail.getTypeInfo();
      String meetingName = conf.getName();
      String organizer = conf.getOrganizer();
      String startDate = conf.getStartDate();
      String endDate = conf.getEndDate();
      sieDataSrvPubTmp.setMeetingTitle(StringEscapeUtils.escapeHtml4(meetingName));
      sieDataSrvPubTmp.setMeetingOrganizers(organizer);
      sieDataSrvPubTmp.setStartDate(formateDate(startDate));
      sieDataSrvPubTmp.setEndDate(formateDate(endDate));
      sieDataSrvPubTmp.setCountryName(conf.getCountry());
      sieDataSrvPubTmp.setCity(conf.getCity());
    }
    String zhKeyWords = detail.getKeywords();
    String zhAbstract = detail.getSummary();
    // pubid
    sieDataSrvPubTmp.setPubId(pubid);
    // title
    sieDataSrvPubTmp.setTitle(detail.getTitle());
    // issue
    sieDataSrvPubTmp.setIssue(ObjectUtils.toString(pub.getIssue()));
    // Volume
    sieDataSrvPubTmp.setVolume(ObjectUtils.toString(pub.getVolume()));
    sieDataSrvPubTmp.setStartPage(ObjectUtils.toString(pub.getStartPage()));
    sieDataSrvPubTmp.setEndPage(ObjectUtils.toString(pub.getEndPage()));
    sieDataSrvPubTmp.setDoi(ObjectUtils.toString(pub.getDoi()));
    sieDataSrvPubTmp.setAuthorName(ObjectUtils.toString(pub.getAuthorNames()));
    sieDataSrvPubTmp.setApplyTime(formateDate(detail.getPublishDate()));
    sieDataSrvPubTmp.setArticleNo(articleNo);
    String remark = StringEscapeUtils.escapeHtml4(detail.getFundInfo());
    if (remark.length() > 4000) {
      remark = remark.substring(0, 4000);
    }
    sieDataSrvPubTmp.setRemark(remark);
    sieDataSrvPubTmp.setKeyWords(zhKeyWords);
    sieDataSrvPubTmp.setSummary(zhAbstract);
    sieDataSrvPubTmp.setPubType(pub.getPubType());
    sieDataSrvPubTmp.setHttp(getPubAddress(pubid, pub.getInsId()));
    Sie6PublicationList sie6PublicationList = siePublicationListDao.get(pubid);
    if (sie6PublicationList != null) {
      sieDataSrvPubTmp.setListEi(sie6PublicationList.getListEi());
      sieDataSrvPubTmp.setListIstp(sie6PublicationList.getListIstp());
      sieDataSrvPubTmp.setListSci(sie6PublicationList.getListSci());
      sieDataSrvPubTmp.setListSsci(sie6PublicationList.getListSsci());
    } else {
      sieDataSrvPubTmp.setListEi(0);
      sieDataSrvPubTmp.setListIstp(0);
      sieDataSrvPubTmp.setListSci(0);
      sieDataSrvPubTmp.setListSsci(0);
    }
    sieDataSrvPubTmp.setZzPubMembers(getJsonAuthos(pub.getPubId()));
    sieDataSrvPubTmp.setCitedTimes(pub.getIsiCited());
    sieDataSrvPubTmpDao.saveOrUpdate(sieDataSrvPubTmp);
    sieDataSrvPatTmpRefresh.setStatus(1);
    SieDataSrvPubTmpRefreshDao.save(sieDataSrvPatTmpRefresh);
  }

  /**
   * 格式化数据日期
   */
  private String formateDate(String docToString) {
    Pattern pattern = Pattern.compile("年|月|日"); // 去掉空格符合换行符
    Matcher matcher = pattern.matcher(docToString);
    String result = matcher.replaceAll("/");
    int strLengh = result.length();
    if (strLengh > 0 && (result.lastIndexOf("/") == strLengh - 1)) {
      result = result.substring(0, strLengh - 1);
    }
    return result;
  }

  private String DocToString(PubXmlDocument pubXmlDoc, String xmlNodeAttribute, String paramName) {
    if (null != pubXmlDoc) {
      if (pubXmlDoc.getXmlNodeAttribute(xmlNodeAttribute, paramName) != null) {
        return pubXmlDoc.getXmlNodeAttribute(xmlNodeAttribute, paramName);
      }
    }
    return "";
  }

  private String getPubAddress(Long pubId, Long insId) {
    Sie6InsPortal sie6InsPortal = sieInsPortalDao.get(insId);
    StringBuilder sbl = new StringBuilder();
    if (sie6InsPortal != null) {
      sbl.append("https://");
      sbl.append(sie6InsPortal.getDomain());
      sbl.append("/pubweb/publication/view?des3Id=");
      sbl.append(ServiceUtil.encodeToDes3(pubId + ""));
    }
    return sbl.toString();
  }

  /**
   * 获取成果作者信息
   */
  private String getJsonAuthos(Long pubid) {
    List<Object> tempArr = new ArrayList<Object>();
    List<SiePubMember> siePubMemberList = siePubMemberDao.getMembersByPubId(pubid);
    for (SiePubMember siePubMember : siePubMemberList) {
      Integer authorPos = siePubMember.getAuthorPos();
      Map<String, Object> tempObj = new HashMap<String, Object>();
      Long seqNo = siePubMember.getSeqNo();
      tempObj.put("seq_no", seqNo != null ? seqNo : "");
      String memberName = siePubMember.getMemberName();
      tempObj.put("member_psn_name", memberName != null ? memberName : "");
      tempObj.put("author_pos", authorPos != null ? authorPos : 0);
      String email = siePubMember.getEmail();
      tempObj.put("email", email != null ? email : "");
      String insName = siePubMember.getInsName();
      tempObj.put("ins_name", insName != null ? insName : "");
      tempArr.add(tempObj);
    }
    String jsonStr = JacksonUtils.listToJsonStr(tempArr);
    return jsonStr;
  }
}
