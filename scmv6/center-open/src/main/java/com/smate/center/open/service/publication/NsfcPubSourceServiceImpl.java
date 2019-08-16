package com.smate.center.open.service.publication;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.model.publication.NsfcPubMember;
import com.smate.center.open.model.publication.PubXmlDocument;
import com.smate.center.open.model.publication.PublicationXml;
import com.smate.core.base.pub.enums.PublicationTypeEnum;
import com.smate.core.base.pub.model.Publication;
import com.smate.core.base.utils.constant.PubXmlConstants;

/**
 * 生成基金委成果XML格式中的source字段
 * 
 * @author ajb
 * 
 */
@Service("nsfcPubSourceService")
@Transactional(rollbackFor = Exception.class)
public class NsfcPubSourceServiceImpl implements NsfcPubSourceService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PublicationService publicationService;

  @Autowired
  private PublicationXmlService publicationXmlService;
  @Autowired
  private NsfcPubMemberService nsfcPubMemberService;

  @Autowired
  private PubMemberService pubMemberService;

  @Override
  public String getNsfcPubSource(Long pubId) throws Exception {
    try {
      Publication pub = this.publicationService.getPub(pubId);
      if (pub == null) {
        return "";
      }
      Integer pubType = pub.getPubType();
      if (pubType == null) {
        logger.error("该成果的pubType为空，请检查数据。pubId=" + pub.getPubId());
        return "";
      }
      switch (pubType.intValue()) {
        case PublicationTypeEnum.AWARD:
          return this.getAwardSource(pub);
        case PublicationTypeEnum.BOOK:
          return this.getBookSource(pub);
        case PublicationTypeEnum.CONFERENCE_PAPER:
          return this.getConferencePaperSource(pub);
        case PublicationTypeEnum.JOURNAL_ARTICLE:
          return this.getJournalArticleSource(pub);
        case PublicationTypeEnum.PATENT:
          return this.getPatentSource(pub);
        default:
          return this.getDefaultSource(pub);
      }
    } catch (Exception e) {
      logger.error("基金委获取成果ISIS来源格式出现错误,pubId=" + pubId, e);
      return "";
    }
  }

  /**
   * 专利
   * 
   * @param pub
   * @return
   * @throws Exception
   */
  private String getPatentSource(Publication pub) throws Exception {
    StringBuilder source = new StringBuilder();
    PublicationXml pubXml = this.publicationXmlService.getById(pub.getPubId());
    PubXmlDocument pubXmlDoc = new PubXmlDocument(pubXml.getXmlData());
    String startPubYear = pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "start_year");
    String startPubMonth = pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "start_month");
    String startPubDay = pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "start_day");
    String startDate = this.getBetweenStr(startPubYear, startPubMonth, ".");
    startDate = this.getBetweenStr(startDate, startPubDay, ".");

    String endPubYear = pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "end_year");
    String endPubMonth = pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "end_month");
    String endPubDay = pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "end_day");
    String endDate = this.getBetweenStr(endPubYear, endPubMonth, ".");
    endDate = this.getBetweenStr(endDate, endPubDay, ".");
    String date = this.getBetweenStr(startDate, endDate, "-");

    String countryName = pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "country_name");
    String patentNo = pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "patent_no");
    if (StringUtils.isBlank(patentNo)) {
      patentNo = pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "patent_open_no");
    }
    source = this.appendProperty(source, date, "，");
    source = this.appendProperty(source, countryName, "，");
    source = this.appendProperty(source, patentNo, "，");
    source = this.replaceLastSign(source, '。');
    return source.toString();
  }

  /**
   * 获取期刊论文的来源
   * 
   * @param pub
   * @return
   * @throws Exception
   */
  private String getJournalArticleSource(Publication pub) throws Exception {
    StringBuilder source = new StringBuilder();
    Integer pubYear = pub.getPublishYear();
    String startPage = pub.getStartPage();
    String endPage = pub.getEndPage();
    String volume = pub.getVolume();
    String issue = pub.getISSUE();
    PublicationXml pubXml = this.publicationXmlService.getById(pub.getPubId());
    PubXmlDocument pubXmlDoc = new PubXmlDocument(pubXml.getXmlData());
    String journalName = pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_XPATH, "jname");
    source = this.appendProperty(source, journalName, "，");
    source = this.appendProperty(source, pubYear, "，");
    source = this.appendProperty(source, volume, "");
    if (StringUtils.isNotBlank(issue)) {
      source.append("（" + issue + "）");
    }
    if (StringUtils.isNotBlank(volume) || StringUtils.isNotBlank(issue)) {
      source.append("：");
    }
    String startEndPage = this.getBetweenStr(startPage, endPage, "-");
    source = this.appendProperty(source, startEndPage, "。");
    source = this.replaceLastSign(source, '。');
    return source.toString();
  }

  /**
   * 获取奖励来源格式
   * 
   * @param pub
   * @return
   * @throws Exception
   */
  private String getAwardSource(Publication pub) throws Exception {
    StringBuilder source = new StringBuilder();
    Integer pubYear = pub.getPublishYear();
    PublicationXml pubXml = this.publicationXmlService.getById(pub.getPubId());
    PubXmlDocument pubXmlDoc = new PubXmlDocument(pubXml.getXmlData());
    String awardGrade = pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUB_AWARD_XPATH, "award_grade");
    String awardCategory = pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUB_AWARD_XPATH, "award_category");
    String issueInsName = pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUB_AWARD_XPATH, "issue_ins_name");
    source = this.appendProperty(source, issueInsName, "，");
    source = this.appendProperty(source, awardCategory, "，");
    source = this.appendProperty(source, awardGrade, "，");
    source = this.appendProperty(source, pubYear, "，");
    if (source.length() > 0) {
      source.setCharAt(source.length() - 1, '。');
    }
    List<NsfcPubMember> nsfcPubMemberList = this.nsfcPubMemberService.getNsfcPubMemberList(pub.getPubId());
    // List<PubMember> pubMemberList =
    // this.pubMemberService.getPubMemberList(pub.getId());
    if (CollectionUtils.isNotEmpty(nsfcPubMemberList)) {
      source.append("（");
      for (NsfcPubMember nsfcPubMember : nsfcPubMemberList) {
        source = this.appendProperty(source, nsfcPubMember.getName(), "，");
      }
      if (source.length() > 0) {
        source.setCharAt(source.length() - 1, '）');
      }
    }
    return source.toString();
  }

  /**
   * 专著
   * 
   * @param pub
   * @return
   * @throws Exception
   */
  private String getBookSource(Publication pub) throws Exception {
    StringBuilder source = new StringBuilder();
    PublicationXml pubXml = this.publicationXmlService.getById(pub.getPubId());
    PubXmlDocument pubXmlDoc = new PubXmlDocument(pubXml.getXmlData());
    Integer pubYear = pub.getPublishYear();
    String publisher = pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUB_BOOK_XPATH, "publisher");
    String totalWords = pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUB_BOOK_XPATH, "total_words");
    source = this.appendProperty(source, publisher, "，");
    source = this.appendProperty(source, totalWords, "字，");
    source = this.appendProperty(source, pubYear, "，");
    source = this.replaceLastSign(source, '。');
    return source.toString();
  }

  /**
   * 获取会议论文的来源
   * 
   * @param pub
   * @return
   * @throws Exception
   */
  private String getConferencePaperSource(Publication pub) throws Exception {
    StringBuilder source = new StringBuilder();
    // 会议名称
    PublicationXml pubXml = this.publicationXmlService.getById(pub.getPubId());
    PubXmlDocument pubXmlDoc = new PubXmlDocument(pubXml.getXmlData());
    String confName = pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUB_CONF_PAPER_XPATH, "conf_name");
    String city = pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "city");
    String startPage = pub.getStartPage();
    String endPage = pub.getEndPage();

    String startPubYear = pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUB_CONF_PAPER_XPATH, "start_year");
    String startPubMonth = pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUB_CONF_PAPER_XPATH, "start_month");
    String startPubDay = pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUB_CONF_PAPER_XPATH, "start_day");
    String startDate = this.getBetweenStr(startPubYear, startPubMonth, ".");
    startDate = this.getBetweenStr(startDate, startPubDay, ".");

    String endPubYear = pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUB_CONF_PAPER_XPATH, "end_year");
    String endPubMonth = pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUB_CONF_PAPER_XPATH, "end_month");
    String endPubDay = pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUB_CONF_PAPER_XPATH, "end_day");
    String endDate = this.getBetweenStr(endPubYear, endPubMonth, ".");
    endDate = this.getBetweenStr(endDate, endPubDay, ".");

    String date = this.getBetweenStr(startDate, endDate, "-");

    source = this.appendProperty(source, confName, "，");
    String startEndPage = this.getBetweenStr(startPage, endPage, "-");
    source = this.appendProperty(source, startEndPage, "，");
    source = this.appendProperty(source, city, "，");
    source = this.appendProperty(source, date, ".");
    source = this.replaceLastSign(source, '。');
    return source.toString();
  }

  /**
   * 获取默认的来源格式
   * 
   * @param pub
   * @return
   * @throws Exception
   */
  private String getDefaultSource(Publication pub) throws Exception {
    String source = pub.getBriefDesc();
    if (StringUtils.isBlank(source)) {
      source = pub.getBriefDescEn();
    }
    return source;
  }

  /**
   * 添加间隔符
   * 
   * @param source
   * @param property
   * @param sign
   * @return
   */
  private StringBuilder appendProperty(StringBuilder source, Object property, String sign) {
    if (property == null) {
      return source;
    }
    if (property instanceof String) {
      if (StringUtils.isNotBlank(property.toString())) {
        source.append(property.toString() + sign);
      }
    } else {
      source.append(property.toString() + sign);
    }
    return source;
  }

  /**
   * 连接字符
   * 
   * @param startStr
   * @param endStr
   * @param sign
   * @return
   */
  private String getBetweenStr(String startStr, String endStr, String sign) {
    startStr = startStr == null ? "" : startStr;
    endStr = endStr == null ? "" : endStr;
    if (StringUtils.isNotBlank(startStr) && StringUtils.isNotBlank(endStr)) {
      return startStr + sign + endStr;
    } else {
      return startStr + endStr;
    }

  }

  /**
   * 最后的字符
   * 
   * @param str
   * @param lastSign
   * @return
   */
  private StringBuilder replaceLastSign(StringBuilder str, char lastSign) {
    if (StringUtils.isBlank(str.toString())) {
      return str;
    }
    if (StringUtils.endsWith(str, "，") || StringUtils.endsWith(str, "：") || StringUtils.endsWith(str, "."))
      str.setCharAt(str.length() - 1, lastSign);
    return str;
  }
}
