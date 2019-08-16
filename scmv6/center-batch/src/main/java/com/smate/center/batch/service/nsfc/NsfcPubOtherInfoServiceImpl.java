package com.smate.center.batch.service.nsfc;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.center.batch.dao.nsfc.NsfcPubOtherInfoDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.nsfc.NsfcPubMember;
import com.smate.center.batch.model.sns.nsfc.NsfcPubOtherInfo;
import com.smate.center.batch.model.sns.pub.PubSimple;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.core.base.pub.enums.PublicationTypeEnum;
import com.smate.core.base.utils.constant.PubXmlConstants;

/**
 * 成果其他信息服务实现.
 * 
 * @author zhanglingling
 *
 */
@Service(value = "nsfcPubOtherInfoService")
public class NsfcPubOtherInfoServiceImpl implements NsfcPubOtherInfoService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private NsfcPubMemberService nsfcPubMemberService;
  @Autowired
  private NsfcPubOtherInfoDao nsfcPubOtherInfoDao;



  @Override
  public void saveNsfcPubOtherInfo(PubSimple pub, String xmlData, List<NsfcPubMember> nsfcPubMemberList)
      throws Exception {
    StringBuilder source = new StringBuilder();
    try {
      if (pub != null) {
        PubXmlDocument xmlDocument = new PubXmlDocument(xmlData);
        NsfcPubOtherInfo nsfcPubOtherInfo = nsfcPubOtherInfoDao.get(pub.getPubId());
        if (nsfcPubOtherInfo == null) {
          nsfcPubOtherInfo = new NsfcPubOtherInfo();
          nsfcPubOtherInfo.setPubId(pub.getPubId());
        }
        nsfcPubOtherInfo.setTypeId(pub.getPubType());
        nsfcPubOtherInfo.setPublishYear(pub.getPublishYear());
        nsfcPubOtherInfo.setPublishMonth(pub.getPublishMonth());
        nsfcPubOtherInfo.setPublishDay(pub.getPublishDay());
        nsfcPubOtherInfo.setStartPage(xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "start_page"));
        nsfcPubOtherInfo.setEndPage(xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "end_page"));
        switch (pub.getPubType()) {
          case PublicationTypeEnum.AWARD:// 奖励
            nsfcPubOtherInfo
                .setAwardCategory(xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_AWARD_XPATH, "award_category"));
            nsfcPubOtherInfo
                .setAwardGrade(xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_AWARD_XPATH, "award_grade"));
            nsfcPubOtherInfo
                .setIssueInsName(xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_AWARD_XPATH, "issue_ins_name"));
            source = this.appendProperty(source, nsfcPubOtherInfo.getIssueInsName(), "，");
            source = this.appendProperty(source, nsfcPubOtherInfo.getAwardCategory(), "，");
            source = this.appendProperty(source, nsfcPubOtherInfo.getAwardGrade(), "，");
            source = this.appendProperty(source, nsfcPubOtherInfo.getPublishYear(), "，");
            if (source.length() > 0) {
              source.setCharAt(source.length() - 1, '。');
            }
            if (CollectionUtils.isNotEmpty(nsfcPubMemberList)) {
              source.append("（");
              for (NsfcPubMember nsfcPubMember : nsfcPubMemberList) {
                source = this.appendProperty(source, nsfcPubMember.getName(), "，");
              }
              if (source.length() > 0) {
                source.setCharAt(source.length() - 1, '）');
              }
            }
            nsfcPubOtherInfo.setSource(source.toString());
            break;
          case PublicationTypeEnum.BOOK:// 书
            nsfcPubOtherInfo
                .setLanguage(xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "language"));
            String bookLanguage = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_BOOK_XPATH, "language");
            nsfcPubOtherInfo
                .setBookLanguage(StringUtils.isBlank(bookLanguage) ? null : NumberUtils.toInt(bookLanguage));
            String totalWords = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_BOOK_XPATH, "total_words");
            nsfcPubOtherInfo.setTotalWords(StringUtils.isBlank(totalWords) ? null : NumberUtils.toInt(totalWords));
            nsfcPubOtherInfo.setPublisher(xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_BOOK_XPATH, "publisher"));
            source = this.appendProperty(source, nsfcPubOtherInfo.getPublisher(), "，");
            source = this.appendProperty(source, nsfcPubOtherInfo.getTotalWords(), "字，");
            source = this.appendProperty(source, nsfcPubOtherInfo.getPublishYear(), "，");
            source = this.replaceLastSign(source, '。');
            nsfcPubOtherInfo.setSource(source.toString());
            String publicationStatus =
                xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_BOOK_XPATH, "publication_status");
            nsfcPubOtherInfo.setPublicationStatus(
                StringUtils.isBlank(publicationStatus) ? null : NumberUtils.toInt(publicationStatus));
            break;
          case PublicationTypeEnum.CONFERENCE_PAPER:// 会议论文
            nsfcPubOtherInfo
                .setPaperType(xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_CONF_PAPER_XPATH, "paper_type"));
            nsfcPubOtherInfo
                .setConfName(xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_CONF_PAPER_XPATH, "conf_name"));
            nsfcPubOtherInfo.setCity(xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "city"));
            String startEndPage3 =
                this.getBetweenStr(nsfcPubOtherInfo.getStartPage(), nsfcPubOtherInfo.getEndPage(), "-");
            String startYearStr = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_CONF_PAPER_XPATH, "start_year");
            nsfcPubOtherInfo.setStartYear(StringUtils.isBlank(startYearStr) ? null : NumberUtils.toInt(startYearStr));
            String startMonthStr = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_CONF_PAPER_XPATH, "start_month");
            nsfcPubOtherInfo
                .setStartMonth(StringUtils.isBlank(startMonthStr) ? null : NumberUtils.toInt(startMonthStr));
            String startDayStr = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_CONF_PAPER_XPATH, "start_day");
            nsfcPubOtherInfo.setStartDay(StringUtils.isBlank(startDayStr) ? null : NumberUtils.toInt(startDayStr));
            String startYear =
                nsfcPubOtherInfo.getStartYear() == null ? "" : nsfcPubOtherInfo.getStartYear().toString();
            String startMonth =
                nsfcPubOtherInfo.getStartMonth() == null ? "" : nsfcPubOtherInfo.getStartMonth().toString();
            String startDay = nsfcPubOtherInfo.getStartDay() == null ? "" : nsfcPubOtherInfo.getStartDay().toString();
            String startDate = this.getBetweenStr(startYear, startMonth, ".");
            startDate = this.getBetweenStr(startDate, startDay, ".");
            String endYearStr = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_CONF_PAPER_XPATH, "end_year");
            nsfcPubOtherInfo.setEndYear(StringUtils.isBlank(endYearStr) ? null : NumberUtils.toInt(endYearStr));
            String endMonthStr = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_CONF_PAPER_XPATH, "end_month");
            nsfcPubOtherInfo.setEndMonth(StringUtils.isBlank(endMonthStr) ? null : NumberUtils.toInt(endMonthStr));
            String endDayStr = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_CONF_PAPER_XPATH, "end_day");
            nsfcPubOtherInfo.setEndDay(StringUtils.isBlank(endDayStr) ? null : NumberUtils.toInt(endDayStr));
            String endYear = nsfcPubOtherInfo.getEndYear() == null ? "" : nsfcPubOtherInfo.getEndYear().toString();
            String endMonth = nsfcPubOtherInfo.getEndMonth() == null ? "" : nsfcPubOtherInfo.getEndMonth().toString();
            String endDay = nsfcPubOtherInfo.getEndDay() == null ? "" : nsfcPubOtherInfo.getEndDay().toString();
            String endDate = this.getBetweenStr(endYear, endMonth, ".");
            endDate = this.getBetweenStr(endDate, endDay, ".");
            String date = this.getBetweenStr(startDate, endDate, "-");
            source = this.appendProperty(source, nsfcPubOtherInfo.getConfName(), "，");
            source = this.appendProperty(source, startEndPage3, "，");
            source = this.appendProperty(source, nsfcPubOtherInfo.getCity(), "，");
            source = this.appendProperty(source, date, ".");
            source = this.replaceLastSign(source, '。');
            nsfcPubOtherInfo.setSource(source.toString());

            break;
          case PublicationTypeEnum.JOURNAL_ARTICLE:// 期刊论文
            nsfcPubOtherInfo.setIssue(xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "issue"));
            nsfcPubOtherInfo.setVolume(xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "volume"));
            nsfcPubOtherInfo.setJname(xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_XPATH, "jname"));
            source = this.appendProperty(source, nsfcPubOtherInfo.getJname(), "，");
            source = this.appendProperty(source, nsfcPubOtherInfo.getPublishYear(), "，");
            source = this.appendProperty(source, nsfcPubOtherInfo.getVolume(), "");
            if (StringUtils.isNotBlank(nsfcPubOtherInfo.getIssue())) {
              source.append("（" + nsfcPubOtherInfo.getIssue() + "）");
            }
            if (StringUtils.isNotBlank(nsfcPubOtherInfo.getVolume())
                || StringUtils.isNotBlank(nsfcPubOtherInfo.getIssue())) {
              source.append("：");
            }
            String startEndPage4 =
                this.getBetweenStr(nsfcPubOtherInfo.getStartPage(), nsfcPubOtherInfo.getEndPage(), "-");
            source = this.appendProperty(source, startEndPage4, "。");
            source = this.replaceLastSign(source, '。');
            nsfcPubOtherInfo.setSource(source.toString());
            break;
          case PublicationTypeEnum.PATENT:// 专利
            String patentStatus = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "patent_status");
            nsfcPubOtherInfo
                .setPatentStatus(StringUtils.isBlank(patentStatus) ? null : NumberUtils.toInt(patentStatus));
            String startYearStr1 = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "start_year");
            nsfcPubOtherInfo.setStartYear(StringUtils.isBlank(startYearStr1) ? null : NumberUtils.toInt(startYearStr1));
            String startMonthStr1 = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "start_month");
            nsfcPubOtherInfo
                .setStartMonth(StringUtils.isBlank(startMonthStr1) ? null : NumberUtils.toInt(startMonthStr1));
            String startDayStr1 = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "start_day");
            nsfcPubOtherInfo.setStartDay(StringUtils.isBlank(startDayStr1) ? null : NumberUtils.toInt(startDayStr1));
            String endYearStr1 = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "end_year");
            nsfcPubOtherInfo.setEndYear(StringUtils.isBlank(endYearStr1) ? null : NumberUtils.toInt(endYearStr1));
            String endMonthStr1 = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "end_month");
            nsfcPubOtherInfo.setEndMonth(StringUtils.isBlank(endMonthStr1) ? null : NumberUtils.toInt(endMonthStr1));
            String endDayStr1 = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "end_day");
            nsfcPubOtherInfo.setEndDay(StringUtils.isBlank(endDayStr1) ? null : NumberUtils.toInt(endDayStr1));
            nsfcPubOtherInfo
                .setCountryName(xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "country_name"));
            String startYear1 =
                nsfcPubOtherInfo.getStartYear() == null ? "" : nsfcPubOtherInfo.getStartYear().toString();
            String startMonth1 =
                nsfcPubOtherInfo.getStartMonth() == null ? "" : nsfcPubOtherInfo.getStartMonth().toString();
            String startDay1 = nsfcPubOtherInfo.getStartDay() == null ? "" : nsfcPubOtherInfo.getStartDay().toString();
            String startDate5 = this.getBetweenStr(startYear1, startMonth1, ".");
            startDate5 = this.getBetweenStr(startDate5, startDay1, ".");
            String endYear1 = nsfcPubOtherInfo.getEndYear() == null ? "" : nsfcPubOtherInfo.getEndYear().toString();
            String endMonth1 = nsfcPubOtherInfo.getEndMonth() == null ? "" : nsfcPubOtherInfo.getEndMonth().toString();
            String endDay1 = nsfcPubOtherInfo.getEndDay() == null ? "" : nsfcPubOtherInfo.getEndDay().toString();
            String endDate5 = this.getBetweenStr(endYear1, endMonth1, ".");
            endDate5 = this.getBetweenStr(endDate5, endDay1, ".");
            String date5 = this.getBetweenStr(startDate5, endDate5, "-");
            String patentNo = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "patent_no");
            if (StringUtils.isBlank(patentNo)) {
              patentNo = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "patent_open_no");
            }
            nsfcPubOtherInfo.setPatentNo(patentNo);
            source = this.appendProperty(source, date5, "，");
            source = this.appendProperty(source, nsfcPubOtherInfo.getCountryName(), "，");
            source = this.appendProperty(source, nsfcPubOtherInfo.getPatentNo(), "，");
            source = this.replaceLastSign(source, '。');
            nsfcPubOtherInfo.setSource(source.toString());
            break;
          default:
            nsfcPubOtherInfo
                .setSource(xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "brief_desc"));
            break;
        }
        nsfcPubOtherInfoDao.save(nsfcPubOtherInfo);
      }

    } catch (DocumentException e) {
      logger.error("保存成果其他信息出错了喔,pubId={}", pub.getPubId(), e);
      throw new ServiceException(e);
    }



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
