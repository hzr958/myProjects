package com.smate.web.psn.utils;

import org.apache.commons.lang3.StringUtils;

import com.smate.core.base.pub.enums.PublicationTypeEnum;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.web.psn.model.pub.PubSimple;
import com.smate.web.psn.model.pub.PubXmlDocument;

public class PubBriefUtils {

  public static String getSNSPubBriefString(PubSimple pub, Integer pubType, PubXmlDocument xmlDocument) {
    switch (pubType.intValue()) {
      case PublicationTypeEnum.AWARD:
        return getSNSAwardBrief(pub, xmlDocument);
      case PublicationTypeEnum.CONFERENCE_PAPER:
        return getSNSConferencePaperBrief(pub, xmlDocument);
      case PublicationTypeEnum.JOURNAL_ARTICLE:
        return getSNSJournalArticleBrief(pub, xmlDocument);
      case PublicationTypeEnum.PATENT:
        return getSNSPatentBrief(pub, xmlDocument);
      case PublicationTypeEnum.BOOK:
        return getSNSBookBrief(pub, xmlDocument);
      case PublicationTypeEnum.BOOK_CHAPTER:
        return getSNSBookChapterBrief(pub, xmlDocument);
      case PublicationTypeEnum.THESIS:
        return getSNSThesisBrief(pub, xmlDocument);
      case PublicationTypeEnum.OTHERS:
        return getSNSOthersBrief(pub, xmlDocument);
      default:
        return getSNSDefaultBrief(pub, xmlDocument);
    }
  }

  /**
   * 获取默认的成果Brief
   * 
   * @param pubType
   * @param xmlDocument
   * @return
   */
  private static String getSNSDefaultBrief(PubSimple pub, PubXmlDocument xmlDocument) {
    return pub.getSource();
  }

  /**
   * 获取“其他”类别的成果Brief
   * 
   * @param pubType
   * @param xmlDocument
   * @return
   */
  private static String getSNSOthersBrief(PubSimple pub, PubXmlDocument xmlDocument) {
    String brief = pub.getSource();
    if (StringUtils.isNotBlank(brief) && '.' == brief.charAt(brief.length() - 1)) {
      brief = brief.substring(0, brief.length() - 1);
    }
    return StringUtils.isNotBlank(brief) ? ", " + brief.replaceAll("，", ", ") : "";
  }

  /**
   * 获取“学位论文”类别成果Brief
   * 
   * @param pubType
   * @param xmlDocument
   * @return
   */
  private static String getSNSThesisBrief(PubSimple pub, PubXmlDocument xmlDocument) {
    String brief = pub.getSource();
    if (StringUtils.isNotBlank(brief) && '.' == brief.charAt(brief.length() - 1)) {
      brief = brief.substring(0, brief.length() - 1);
    }
    return StringUtils.isNotBlank(brief) ? ", " + brief.replaceAll("，", ", ") : "";
  }

  /**
   * 获取“书籍章节”类别成果Brief
   * 
   * @param pubType
   * @param xmlDocument
   * @return
   */
  private static String getSNSBookChapterBrief(PubSimple pub, PubXmlDocument xmlDocument) {
    String brief = pub.getSource();
    if (StringUtils.isNotBlank(brief) && '.' == brief.charAt(brief.length() - 1)) {
      brief = brief.substring(0, brief.length() - 1);
    }
    return StringUtils.isNotBlank(brief) ? ", " + brief.replaceAll("，", ", ") : "";
  }

  /**
   * 获取“书籍”类别成果Brief
   * 
   * @param pubType
   * @param xmlDocument
   * @return
   */
  private static String getSNSBookBrief(PubSimple pub, PubXmlDocument xmlDocument) {
    StringBuilder brief = new StringBuilder();
    String publisher = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_BOOK_XPATH, "publisher");
    // 已出版还是待出版
    String pubStatus = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_BOOK_XPATH, "publication_status");
    String pubDate = "";
    if (StringUtils.isNotBlank(pubStatus) && "1".equals(pubStatus)) {
      pubDate = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_date");
      if (StringUtils.isNotBlank(pubDate)) {
        pubDate = pubDate.replaceAll("/", ".");
      }
    }
    String totalWords = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_BOOK_XPATH, "total_words");
    brief.append(StringUtils.isNotBlank(publisher) ? ", " + publisher : "");
    brief.append(StringUtils.isNotBlank(totalWords) ? ", " + totalWords + "字" : "");
    brief.append(StringUtils.isNotBlank(pubDate) ? ", " + pubDate : "");
    return brief.toString();
  }

  /**
   * 获取“专利”类别成果Brief
   * 
   * @param pubType
   * @param xmlDocument
   * @return
   */
  private static String getSNSPatentBrief(PubSimple pub, PubXmlDocument xmlDocument) {
    StringBuilder brief = new StringBuilder();
    String country = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "patent_area");
    String patentNo = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "patent_no");
    String publishDate = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "publish_date");
    String patentStatus = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "patent_status");
    // 授权专利
    if ("0".equals(patentStatus)) {
      publishDate = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_date");
    }
    if (StringUtils.isNotBlank(publishDate)) {
      publishDate = publishDate.replaceAll("/", ".");
      brief.append(", " + publishDate);
    }
    if (StringUtils.isNotBlank(country)) {
      switch (country) {
        case "china":
          brief.append(", 中国专利");
          break;
        case "usa":
          brief.append(", 美国专利");
          break;
        case "europe":
          brief.append(", 欧洲专利");
          break;
        case "wipo":
          brief.append(", WIPO专利");
          break;
        case "japan":
          brief.append(", 日本专利");
          break;
        case "other":
          brief.append(", 其他国家专利");
          break;
        default:
          break;
      }
    }
    brief.append(StringUtils.isNotBlank(patentNo) ? ", " + patentNo : "");

    return brief.toString();
  }

  /**
   * 获取“期刊文章”类别成果Brief
   * 
   * @param pubType
   * @param xmlDocument
   * @return
   */
  private static String getSNSJournalArticleBrief(PubSimple pub, PubXmlDocument xmlDocument) {
    StringBuilder brief = new StringBuilder();
    String jname = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_XPATH, "jname");
    String publishDate = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_date");
    String volume = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "volume");
    String issue = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "issue");
    String startPage = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "start_page");
    String endPage = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "end_page");
    if (StringUtils.isNotBlank(publishDate)) {
      publishDate = publishDate.replaceAll("/", ".");
    }
    brief.append(StringUtils.isNotBlank(jname) ? ", " + jname : "");
    brief.append(StringUtils.isNotBlank(publishDate) ? ", " + publishDate : "");
    brief.append(StringUtils.isNotBlank(volume) ? ", " + volume : "");
    if (StringUtils.isNotBlank(volume)) {
      brief.append(StringUtils.isNotBlank(issue) ? "(" + issue + ")" : "");
    } else {
      brief.append(StringUtils.isNotBlank(issue) ? ", (" + issue + ")" : "");
    }
    if (StringUtils.isNotBlank(volume) || StringUtils.isNotBlank(issue)) {
      brief.append(StringUtils.isNotBlank(startPage) ? "：" + startPage : "");
    } else {
      brief.append(StringUtils.isNotBlank(startPage) ? ", " + startPage : "");
    }
    if (StringUtils.isNotBlank(startPage)) {
      brief.append(StringUtils.isNotBlank(endPage) ? " ~ " + endPage : "");
    } else {
      if (StringUtils.isNotBlank(volume) || StringUtils.isNotBlank(issue)) {
        brief.append(StringUtils.isNotBlank(endPage) ? "：" + endPage : "");
      } else {
        brief.append(StringUtils.isNotBlank(endPage) ? ", " + endPage : "");
      }
    }
    return brief.toString();
  }

  /**
   * 获取“会议论文”类别成果Brief
   * 
   * @param pubType
   * @param xmlDocument
   * @return
   */
  private static String getSNSConferencePaperBrief(PubSimple pub, PubXmlDocument xmlDocument) {
    StringBuilder brief = new StringBuilder();
    String confName = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_CONF_PAPER_XPATH, "conf_name");
    String country = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "country_name");
    String city = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "city");
    String startYear = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_CONF_PAPER_XPATH, "start_year");
    String startMonth = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_CONF_PAPER_XPATH, "start_month");
    String startDay = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_CONF_PAPER_XPATH, "start_day");
    String endYear = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_CONF_PAPER_XPATH, "end_year");
    String endMonth = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_CONF_PAPER_XPATH, "end_month");
    String endDay = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_CONF_PAPER_XPATH, "end_day");
    String startDate = getDateString(startYear, startMonth, startDay, ".");
    String endDate = getDateString(endYear, endMonth, endDay, ".");
    brief.append(StringUtils.isNotBlank(confName) ? ", " + confName : "");
    String addr = country + city;
    if (StringUtils.isNotBlank(addr)) {
      brief.append(", " + addr);
    }
    brief.append(StringUtils.isNotBlank(startDate) ? ", " + startDate : "");
    brief.append(StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate) ? " - " + endDate : "");
    return brief.toString();
  }

  /**
   * 获取“奖励”类别成果Brief
   * 
   * @param pubType
   * @param xmlDocument
   * @return
   */
  private static String getSNSAwardBrief(PubSimple pub, PubXmlDocument xmlDocument) {
    StringBuilder brief = new StringBuilder();
    String issueInsName = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_AWARD_XPATH, "issue_ins_name");
    String awardCategory = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_AWARD_XPATH, "award_category");
    String awardGrade = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_AWARD_XPATH, "award_grade");
    String publishDate = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_date");
    if (StringUtils.isNotBlank(publishDate)) {
      publishDate = publishDate.replaceAll("/", ".");
    }
    brief.append(StringUtils.isNotBlank(issueInsName) ? ", " + issueInsName : "");
    brief.append(StringUtils.isNotBlank(awardCategory) ? ", " + awardCategory : "");
    brief.append(StringUtils.isNotBlank(awardGrade) ? ", " + awardGrade : "");
    brief.append(StringUtils.isNotBlank(publishDate) ? ", " + publishDate : "");
    return brief.toString();
  }

  /**
   * 按给定了连接符拼接年、月、日，返回指定日期格式
   * 
   * @param year
   * @param month
   * @param day
   * @param connector
   * @return
   */
  private static String getDateString(String year, String month, String day, String connector) {
    StringBuilder startDate = new StringBuilder();
    startDate.append(year);
    if (StringUtils.isNotBlank(year) && StringUtils.isNotBlank(month)) {
      startDate.append(connector + month);
    }
    if (StringUtils.isNotBlank(year) && StringUtils.isNotBlank(month) && StringUtils.isNotBlank(day)) {
      startDate.append(connector + day);
    }
    return startDate.toString();
  }
}
