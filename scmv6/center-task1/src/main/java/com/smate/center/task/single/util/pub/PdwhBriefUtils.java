package com.smate.center.task.single.util.pub;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.single.oldXml.pub.PubXmlDocument;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.data.XmlUtil;

/**
 * 基准库publication_all表brief字段构造.
 * 
 * @author cwli
 * 
 */
public class PdwhBriefUtils {
  protected static final Logger logger = LoggerFactory.getLogger(PdwhBriefUtils.class);

  public static String getBrief(PubXmlDocument doc, String language) throws ServiceException {
    String brief = "";
    try {
      Integer typeId = NumberUtils
          .toInt(StringUtils.trimToEmpty(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "pub_type")));
      switch (typeId) {
        case 1:// 奖励
          brief = getBriefAward(doc, language);
          break;
        case 2:// 书籍
          brief = getBriefBook(doc, language);
          break;
        case 3:// 会议论文
          brief = getBriefConference(doc, language);
          break;
        case 4:// 期刊论文
          brief = getBriefJournal(doc, language);
          break;
        case 5:// 专利
          brief = getBriefPatent(doc, language);
          break;
        case 7:// 其它
          brief = getBriefOthers(doc, language);
          break;
        case 8:// 学位论文
          brief = getBriefThesis(doc, language);
          break;
        case 10:// 书籍章节
          brief = getBriefBookChapter(doc, language);
          break;
        default:
          break;
      }
    } catch (Exception e) {
      logger.error("获取基准库指定类型brief字段出错", e);
    }
    return brief;
  }

  /**
   * 获取奖励类型(1)的brief.
   * 
   * @param doc
   * @param language
   * @return
   * @throws ServiceException
   */
  private static String getBriefAward(PubXmlDocument doc, String language) throws ServiceException {
    String brief = "";
    try {
      List<String> params = new ArrayList<String>();
      String issueInsName =
          StringUtils.trimToEmpty(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "issue_ins_name"));
      String awardCategory =
          StringUtils.trimToEmpty(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "award_category"));
      String awardGrade =
          StringUtils.trimToEmpty(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "award_grade"));
      String pubyear = StringUtils.trimToEmpty(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "pubyear"));
      params.add(XmlUtil.formatJnlTitle(issueInsName));
      params.add(awardCategory);
      params.add(awardGrade);
      if (StringUtils.isNotBlank(pubyear)) {
        pubyear = pubyear.split(" ")[0];
      }
      pubyear = pubyear.replace("-", ".");
      pubyear = pubyear.replace("/", ".");
      params.add(pubyear);
      if ("zh".equalsIgnoreCase(language)) {
        brief = rebuildBrief(params, "奖励");
      } else {
        brief = rebuildBrief(params, "Award");
      }
    } catch (Exception e) {
      logger.error("基准构造brief字段出错", e);
    }
    return brief;
  }

  /**
   * 获取书/著作类型(2)的brief.
   * 
   * @param doc
   * @param language
   * @return
   * @throws ServiceException
   */
  private static String getBriefBook(PubXmlDocument doc, String language) throws ServiceException {
    String brief = "";
    try {
      List<String> params = new ArrayList<String>();
      String publisher =
          StringUtils.trimToEmpty(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publisher"));
      String totalPages =
          StringUtils.trimToEmpty(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "total_pages"));
      String pubyear = StringUtils.trimToEmpty(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "pubyear"));
      String lang = StringUtils.trimToEmpty(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "language"));
      params.add(publisher);
      params.add(totalPages);
      if (StringUtils.isNotBlank(pubyear)) {
        pubyear = pubyear.split(" ")[0];
      }
      pubyear = pubyear.replace("-", ".");
      pubyear = pubyear.replace("/", ".");
      params.add(pubyear);
      params.add(lang);
      if ("zh".equalsIgnoreCase(language)) {
        brief = rebuildBrief(params, "书/著作");
      } else {
        brief = rebuildBrief(params, "Book/Monograph");
      }
    } catch (Exception e) {
      logger.error("基准构造brief字段出错", e);
    }
    return brief;
  }

  /**
   * 获取会议类型(3)的brief.
   * 
   * @param doc
   * @param language
   * @return
   * @throws ServiceException
   */
  private static String getBriefConference(PubXmlDocument doc, String language) throws ServiceException {
    String brief = "";
    try {
      List<String> params = new ArrayList<String>();
      String proceeding_title =
          StringUtils.trimToEmpty(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "proceeding_title"));
      String start_date =
          StringUtils.trimToEmpty(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "start_date"));
      String end_date = StringUtils.trimToEmpty(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "end_date"));
      String start_page =
          StringUtils.trimToEmpty(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "start_page"));
      String end_page = StringUtils.trimToEmpty(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "end_page"));
      String city = StringUtils.trimToEmpty(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "city"));
      String pubyear = StringUtils.trimToEmpty(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "pubyear"));
      params.add(XmlUtil.formatJnlTitle(proceeding_title));
      if (StringUtils.isNotBlank(start_date)) {
        if (StringUtils.isNotBlank(end_date)) {
          params.add(start_date.replace("-", ".") + "-" + end_date.replace("-", "."));
        } else {
          params.add(start_date.replace("-", "."));
        }
      }
      rebuildBriefPage(params, start_page, end_page);
      params.add(city);
      if (StringUtils.isNotBlank(pubyear)) {
        pubyear = pubyear.split(" ")[0];
      }
      pubyear = pubyear.replace("-", ".");
      pubyear = pubyear.replace("/", ".");
      params.add(pubyear);
      if ("zh".equalsIgnoreCase(language)) {
        brief = rebuildBrief(params, "会议论文");
      } else {
        brief = rebuildBrief(params, "Conference Paper");
      }
    } catch (Exception e) {
      logger.error("基准构造brief字段出错", e);
    }
    return brief;
  }

  /**
   * 获取期刊类型(4)的brief.
   * 
   * @param doc
   * @param language
   * @return
   * @throws ServiceException
   */
  private static String getBriefJournal(PubXmlDocument doc, String language) throws ServiceException {
    String brief = "";
    try {
      List<String> params = new ArrayList<String>();
      String original = StringUtils.trimToEmpty(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "original"));
      String volume = StringUtils.trimToEmpty(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "volume"));
      String issue = StringUtils.trimToEmpty(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "issue"));
      String start_page =
          StringUtils.trimToEmpty(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "start_page"));
      String end_page = StringUtils.trimToEmpty(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "end_page"));
      String pubyear = StringUtils.trimToEmpty(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "pubyear"));
      params.add(XmlUtil.formatJnlTitle(original));
      if (StringUtils.isNotBlank(volume) && StringUtils.isNotBlank(issue)) {
        // params.add(volume);
        params.add(volume + "(" + issue + ")");
      } else if (StringUtils.isNotBlank(volume) && StringUtils.isBlank(issue)) {
        /*
         * if ("zh".equalsIgnoreCase(language)) { params.add(volume + "卷"); } else { params.add("Vol " +
         * volume); }
         */
        params.add(volume);
      } else if (StringUtils.isBlank(volume) && StringUtils.isNotBlank(issue)) {
        /*
         * if ("zh".equalsIgnoreCase(language)) { params.add(issue + "期"); } else { params.add("Issue " +
         * issue); }
         */
        params.add("(" + issue + ")");
      }
      rebuildBriefPage(params, start_page, end_page);
      if (StringUtils.isNotBlank(pubyear)) {
        pubyear = pubyear.split(" ")[0];
      }
      pubyear = pubyear.replace("-", ".");
      pubyear = pubyear.replace("/", ".");
      params.add(pubyear);
      if ("zh".equalsIgnoreCase(language)) {
        brief = rebuildBrief(params, "期刊论文");
      } else {
        brief = rebuildBrief(params, "Journal Article");
      }
    } catch (Exception e) {
      logger.error("基准构造brief字段出错", e);
    }
    return brief;
  }

  /**
   * 获取专利类型(5)的brief.
   * 
   * @param doc
   * @param language
   * @return
   * @throws ServiceException
   */
  private static String getBriefPatent(PubXmlDocument doc, String language) throws ServiceException {
    String brief = "";
    try {
      List<String> params = new ArrayList<String>();
      String start_date =
          StringUtils.trimToEmpty(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "start_date"));
      String country_name =
          StringUtils.trimToEmpty(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "country"));
      String issue_org =
          StringUtils.trimToEmpty(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "patent_agent_org"));
      String patent_no =
          StringUtils.trimToEmpty(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "patent_no"));
      if (StringUtils.isEmpty(patent_no)) {
        patent_no =
            StringUtils.trimToEmpty(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "patent_open_no"));
      }
      if (StringUtils.isNotBlank(start_date)) {
        start_date = start_date.split(" ")[0];
      }
      String stardate = start_date.replace("-", ".");
      stardate = stardate.replace("/", ".");
      params.add(stardate);
      params.add(XmlUtil.formatJnlTitle(country_name));
      params.add(issue_org);
      params.add(patent_no);
      if ("zh".equalsIgnoreCase(language)) {
        brief = rebuildBrief(params, "专利");
      } else {
        brief = rebuildBrief(params, "Patent");
      }
    } catch (Exception e) {
      logger.error("基准构造brief字段出错", e);
    }
    return brief;
  }

  /**
   * 获取其它类型(7)的brief.
   * 
   * @param doc
   * @param language
   * @return
   * @throws ServiceException
   */
  private static String getBriefOthers(PubXmlDocument doc, String language) throws ServiceException {
    String brief = "";
    try {
      List<String> params = new ArrayList<String>();
      String country_name =
          StringUtils.trimToEmpty(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "country"));
      String pubyear = StringUtils.trimToEmpty(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "pubyear"));
      params.add(XmlUtil.formatJnlTitle(country_name));
      if (StringUtils.isNotBlank(pubyear)) {
        pubyear = pubyear.split(" ")[0];
      }
      pubyear = pubyear.replace("-", ".");
      pubyear = pubyear.replace("/", ".");
      params.add(pubyear);
      if ("zh".equalsIgnoreCase(language)) {
        brief = rebuildBrief(params, "其它");
      } else {
        brief = rebuildBrief(params, "Others");
      }
    } catch (Exception e) {
      logger.error("基准构造brief字段出错", e);
    }
    return brief;
  }

  /**
   * 获取学位论文类型(8)的brief.
   * 
   * @param doc
   * @param language
   * @return
   * @throws ServiceException
   */
  private static String getBriefThesis(PubXmlDocument doc, String language) throws ServiceException {
    String brief = "";
    try {
      List<String> params = new ArrayList<String>();
      String thesis_dept_name =
          StringUtils.trimToEmpty(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "thesis_dept_name"));
      String thesis_programme =
          StringUtils.trimToEmpty(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "thesis_programme"));
      String thesis_ins_name =
          StringUtils.trimToEmpty(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "thesis_ins_name"));
      String country_name =
          StringUtils.trimToEmpty(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "country"));
      String pubyear = StringUtils.trimToEmpty(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "pubyear"));
      if (StringUtils.isNotEmpty(thesis_ins_name)
          && Pattern.compile("^\\S+[;；]+$").matcher(thesis_ins_name).matches()) {
        // 修正部分数据结尾可能会有多余中英文分号的情况
        thesis_ins_name = thesis_ins_name.replaceAll("[;；]+$", "");
      }
      params.add(thesis_dept_name);
      params.add(thesis_programme);
      params.add(thesis_ins_name);
      params.add(country_name);
      if (StringUtils.isNotBlank(pubyear)) {
        pubyear = pubyear.split(" ")[0];
      }
      pubyear = pubyear.replace("-", ".");
      pubyear = pubyear.replace("/", ".");
      params.add(pubyear);
      if ("zh".equalsIgnoreCase(language)) {
        brief = rebuildBrief(params, "学位论文");
      } else {
        brief = rebuildBrief(params, "Thesis");
      }
    } catch (Exception e) {
      logger.error("基准构造brief字段出错", e);
    }
    return brief;
  }

  /**
   * 获取书籍章节类型(10)的brief.
   * 
   * @param doc
   * @param language
   * @return
   * @throws ServiceException
   */
  private static String getBriefBookChapter(PubXmlDocument doc, String language) throws ServiceException {
    String brief = "";
    try {
      List<String> params = new ArrayList<String>();
      String book_title =
          StringUtils.trimToEmpty(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "book_title"));
      String publisher =
          StringUtils.trimToEmpty(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publisher"));
      String start_page =
          StringUtils.trimToEmpty(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "start_page"));
      String end_page = StringUtils.trimToEmpty(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "end_page"));
      String country_name =
          StringUtils.trimToEmpty(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "country_name"));
      String pubyear = StringUtils.trimToEmpty(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "pubyear"));
      params.add(XmlUtil.formatJnlTitle(book_title));
      params.add(publisher);
      rebuildBriefPage(params, start_page, end_page);
      params.add(country_name);
      if (StringUtils.isNotBlank(pubyear)) {
        pubyear = pubyear.split(" ")[0];
      }
      pubyear = pubyear.replace("-", ".");
      pubyear = pubyear.replace("/", ".");
      params.add(pubyear);
      if ("zh".equalsIgnoreCase(language)) {
        brief = rebuildBrief(params, "书籍章节");
      } else {
        brief = rebuildBrief(params, "Book Chapter");
      }
    } catch (Exception e) {
      logger.error("基准构造brief字段出错", e);
    }
    return brief;
  }

  private static void rebuildBriefPage(List<String> params, String start_page, String end_page) {
    if (StringUtils.isNotBlank(start_page) && StringUtils.isNotBlank(end_page)) {
      params.add("pp" + start_page + "-" + end_page);
    } else if (StringUtils.isNotBlank(start_page) && StringUtils.isBlank(end_page)) {
      params.add("pp" + start_page);
    } else if (StringUtils.isBlank(start_page) && StringUtils.isNotBlank(end_page)) {
      params.add("pp" + end_page);
    }
  }

  private static String rebuildBrief(List<String> params, String typeName) {
    String newbrief = "";
    for (int i = 0; i < params.size(); i++) {
      if (StringUtils.isBlank(params.get(i)))
        continue;
      newbrief += ", " + params.get(i);
    }
    // newbrief += "." + typeName;
    return newbrief.substring(1);
  }
}
