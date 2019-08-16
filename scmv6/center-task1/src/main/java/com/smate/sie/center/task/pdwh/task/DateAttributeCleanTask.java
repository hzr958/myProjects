package com.smate.sie.center.task.pdwh.task;

import org.apache.commons.lang.StringUtils;

import com.smate.center.task.single.enums.pub.XmlOperationEnum;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.sie.center.task.pdwh.utils.XmlFragmentCleanerHelper;

/**
 * 把日期标准化.
 * 
 * @author yamingd
 */
public class DateAttributeCleanTask implements IPubXmlTask {

  /**
   * 
   */
  private final String name = "date_attribute_clean";

  /*
   * (non-Javadoc)
   * 
   * @see com.iris.scm.xml.IXmlTask#getName()
   */
  @Override
  public String getName() {
    return this.name;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.iris.scm.xml.IXmlTask#can(com.iris.scm.xml.XmlDocument,
   * com.iris.scm.xml.XmlProcessContext)
   */
  @Override
  public boolean can(PubXmlDocument xmlDocument, PubXmlProcessContext context) {
    // 导入的不执行
    if (XmlOperationEnum.Import.equals(context.getCurrentAction())) {
      return false;
    }
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.iris.scm.xml.IXmlTask#run(com.iris.scm.xml.XmlDocument,
   * com.iris.scm.xml.XmlProcessContext)
   */
  @Override
  public boolean run(PubXmlDocument xmlDocument, PubXmlProcessContext context) throws Exception {

    String chsDatePattern = PubXmlConstants.CHS_DATE_PATTERN;
    String engDatePattern = PubXmlConstants.ENG_DATE_PATTERN;
    String lang = context.getCurrentLanguage();
    String pattern = "zh".equalsIgnoreCase(lang) ? chsDatePattern : engDatePattern;

    // 导入使用中文格式
    if (context.isImport()) {
      lang = "zh";
      pattern = chsDatePattern;
    }
    // publish year/month /date
    String[] dates = null;

    // 接受日期
    // accpete year / month
    dates = XmlFragmentCleanerHelper.splitDateYearMonth(xmlDocument, PubXmlConstants.PUBLICATION_XPATH, "accept_date",
        pattern);
    if (!"".equals(dates[0])) {
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "accept_year", dates[0]);
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "accept_month", dates[1]);
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "accept_day", dates[2]);
    } else {
      String acceptYear = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "accept_year");
      if (!"".equals(acceptYear)) {
        XmlFragmentCleanerHelper.setDateValueFromYMD(xmlDocument, "/data/publication/@accept_", pattern);
      }
    }
    try {
      // pub patent effect_start_date & effect_end_date
      if (xmlDocument.isPatent()) {// 专利
        // 如果有开始日期
        if (xmlDocument.existsNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "start_date")) {
          // 生效开始日期
          dates = XmlFragmentCleanerHelper.splitDateYearMonth(xmlDocument, PubXmlConstants.PUB_PATENT_XPATH,
              "start_date", pattern);
          xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "start_year", dates[0]);
          xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "start_month", dates[1]);
          xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "start_day", dates[2]);
          // 生效结束日期
          dates = XmlFragmentCleanerHelper.splitDateYearMonth(xmlDocument, PubXmlConstants.PUB_PATENT_XPATH, "end_date",
              pattern);
          xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "end_year", dates[0]);
          xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "end_month", dates[1]);
          xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "end_day", dates[2]);
          // 申请日期
          // xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH,
          // "publish_date", Date);
          dates = XmlFragmentCleanerHelper.splitDateYearMonth(xmlDocument, PubXmlConstants.PUBLICATION_XPATH,
              "publish_date", pattern);
          xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "publish_year", dates[0]);
          xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "publish_month", dates[1]);
          xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "publish_day", dates[2]);
        }
        // 专利日期，如果仅输入年份
        if (xmlDocument.existsNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "publish_year")) {
          XmlFragmentCleanerHelper.setDateValueFromYMD(xmlDocument, "/data/pub_patent/@publish_", pattern);
        }
        if (xmlDocument.existsNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "start_year")) {
          XmlFragmentCleanerHelper.setDateValueFromYMD(xmlDocument, "/data/pub_patent/@start_", pattern);
        }
        if (xmlDocument.existsNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "end_year")) {
          XmlFragmentCleanerHelper.setDateValueFromYMD(xmlDocument, "/data/pub_patent/@end_", pattern);
        }
        // 如果有开始日期
        if (xmlDocument.existsNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "start_date")) {
          String startDate = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "start_date");
          String endDate = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "end_date");
          String publishDate = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "publish_date");
          // 设置publish_date，供预览或类型切换使用
          if (!"".equals(publishDate)) {
            xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_date", publishDate);
          }
          if (!"".equals(startDate)) {
            xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "start_date", startDate);
          }
          if (!"".equals(endDate)) {
            xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "end_date", endDate);
          }
        }
      } else if (xmlDocument.isConfPaper()) {// 会议论文
        if (xmlDocument.existsNodeAttribute(PubXmlConstants.PUB_CONF_PAPER_XPATH, "start_date")) {
          dates = XmlFragmentCleanerHelper.splitDateYearMonth(xmlDocument, PubXmlConstants.PUB_CONF_PAPER_XPATH,
              "start_date", pattern);
          if (StringUtils.isNotBlank(dates[0]))
            xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_CONF_PAPER_XPATH, "start_year", dates[0]);
          xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_CONF_PAPER_XPATH, "start_month", dates[1]);
          xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_CONF_PAPER_XPATH, "start_day", dates[2]);

          dates = XmlFragmentCleanerHelper.splitDateYearMonth(xmlDocument, PubXmlConstants.PUB_CONF_PAPER_XPATH,
              "end_date", pattern);
          xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_CONF_PAPER_XPATH, "end_year", dates[0]);
          xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_CONF_PAPER_XPATH, "end_month", dates[1]);
          xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_CONF_PAPER_XPATH, "end_day", dates[2]);
        }
        if (xmlDocument.existsNodeAttribute(PubXmlConstants.PUB_CONF_PAPER_XPATH, "start_year")) {
          XmlFragmentCleanerHelper.setDateValueFromYMD(xmlDocument, "/data/pub_conf_paper/@start_", pattern);
          XmlFragmentCleanerHelper.setDateValueFromYMD(xmlDocument, "/data/pub_conf_paper/@end_", pattern);
        }
      } else if (xmlDocument.isOther()) {// 其他类别
        pattern = chsDatePattern;// 英文状态下，日期字段也是yyyy-MM-dd格式的
        if (xmlDocument.existsNodeAttribute(PubXmlConstants.PUB_OTHER_XPATH, "pub_date")) {
          dates = XmlFragmentCleanerHelper.splitDateYearMonth(xmlDocument, PubXmlConstants.PUB_OTHER_XPATH, "pub_date",
              pattern);
          xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_OTHER_XPATH, "pub_year", dates[0]);
          xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_OTHER_XPATH, "pub_month", dates[1]);
          xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_OTHER_XPATH, "pub_day", dates[2]);

          dates = XmlFragmentCleanerHelper.splitDateYearMonth(xmlDocument, PubXmlConstants.PUB_OTHER_XPATH,
              "start_date", pattern);
          xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_OTHER_XPATH, "start_year", dates[0]);
          xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_OTHER_XPATH, "start_month", dates[1]);
          xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_OTHER_XPATH, "start_day", dates[2]);

          dates = XmlFragmentCleanerHelper.splitDateYearMonth(xmlDocument, PubXmlConstants.PUB_OTHER_XPATH, "end_date",
              pattern);
          xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_OTHER_XPATH, "end_year", dates[0]);
          xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_OTHER_XPATH, "end_month", dates[1]);
          xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_OTHER_XPATH, "end_day", dates[2]);
        }

        if (xmlDocument.existsNodeAttribute(PubXmlConstants.PUB_OTHER_XPATH, "pub_year")) {
          XmlFragmentCleanerHelper.setDateValueFromYMD(xmlDocument, "/data/pub_other/@pub_", pattern);
        } else if (xmlDocument.existsNodeAttribute(PubXmlConstants.PUB_OTHER_XPATH, "start_year")) {
          XmlFragmentCleanerHelper.setDateValueFromYMD(xmlDocument, "/data/pub_other/@start_", pattern);
          XmlFragmentCleanerHelper.setDateValueFromYMD(xmlDocument, "/data/pub_other/@end_", pattern);
        }
        // 发表日期的输入方法
        if (xmlDocument.existsNodeAttribute(PubXmlConstants.PUB_OTHER_XPATH, "di_mode")) {
          String dim = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_OTHER_XPATH, "di_mode");// $("pub_other@di_mode").value;
          if ("period".equalsIgnoreCase(dim)) {
            String sd = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_OTHER_XPATH, "start_date");
            String ed = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_OTHER_XPATH, "end_date");

            if (!"".equals(sd)) {
              xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_date", sd);
            } else {
              xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_date", ed);
            }
            // scm-6252 成果》其他》发表日期，切换日期格式项修改保存后，清空之前的内容
            xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_OTHER_XPATH, "pub_date", "");
            xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_OTHER_XPATH, "pub_year", "");
            xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_OTHER_XPATH, "pub_month", "");
            xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_OTHER_XPATH, "pub_day", "");
          } else if ("date".equalsIgnoreCase(dim)) {
            String sd = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_OTHER_XPATH, "pub_date");
            xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_date", sd);
            xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_OTHER_XPATH, "start_date", "");
            xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_OTHER_XPATH, "end_date", "");
            xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_OTHER_XPATH, "start_year", "");
            xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_OTHER_XPATH, "start_month", "");
            xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_OTHER_XPATH, "start_day", "");
            xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_OTHER_XPATH, "end_year", "");
            xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_OTHER_XPATH, "end_month", "");
            xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_OTHER_XPATH, "end_day", "");
          } else if ("year".equalsIgnoreCase(dim)) {
            xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_OTHER_XPATH, "pub_date", "");
            xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_OTHER_XPATH, "pub_year", "");
            xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_OTHER_XPATH, "pub_month", "");
            xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_OTHER_XPATH, "pub_day", "");
            xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_OTHER_XPATH, "start_date", "");
            xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_OTHER_XPATH, "end_date", "");
            xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_OTHER_XPATH, "start_year", "");
            xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_OTHER_XPATH, "start_month", "");
            xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_OTHER_XPATH, "start_day", "");
            xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_OTHER_XPATH, "end_year", "");
            xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_OTHER_XPATH, "end_month", "");
            xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_OTHER_XPATH, "end_day", "");
          }
        }
      } else if (xmlDocument.isJournalEditor()) {// 期刊编辑
        if (xmlDocument.existsNodeAttribute(PubXmlConstants.PUB_JOURNAL_EDITOR_XPATH, "start_date")) {
          dates = XmlFragmentCleanerHelper.splitDateYearMonth(xmlDocument, PubXmlConstants.PUB_JOURNAL_EDITOR_XPATH,
              "start_date", pattern);
          xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_EDITOR_XPATH, "start_year", dates[0]);
          xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_EDITOR_XPATH, "start_month", dates[1]);
          xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_EDITOR_XPATH, "start_day", dates[2]);

          dates = XmlFragmentCleanerHelper.splitDateYearMonth(xmlDocument, PubXmlConstants.PUB_JOURNAL_EDITOR_XPATH,
              "end_date", pattern);
          xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_EDITOR_XPATH, "end_year", dates[0]);
          xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_EDITOR_XPATH, "end_month", dates[1]);
          xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_EDITOR_XPATH, "end_day", dates[2]);
        }
        if (xmlDocument.existsNodeAttribute(PubXmlConstants.PUB_JOURNAL_EDITOR_XPATH, "start_date")) {
          XmlFragmentCleanerHelper.setDateValueFromYMD(xmlDocument, "/data/pub_journal_editor/@start_", pattern);
          XmlFragmentCleanerHelper.setDateValueFromYMD(xmlDocument, "/data/pub_journal_editor/@end_", pattern);
        }

        if (xmlDocument.existsNodeAttribute(PubXmlConstants.PUB_JOURNAL_EDITOR_XPATH, "start_date")) {
          String startDate = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_EDITOR_XPATH, "start_date");
          String endDate = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_EDITOR_XPATH, "end_date");
          if (!"".equals(endDate)) {
            xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_date", endDate);
          } else {
            xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_date", startDate);
          }
        }
      } else if (xmlDocument.isThesis()) {
      }
      String publishYear = "0";
      dates = XmlFragmentCleanerHelper.splitDateYearMonth(xmlDocument, PubXmlConstants.PUBLICATION_XPATH,
          "publish_date", pattern);
      if (!"".equals(dates[0])) {
        publishYear = dates[0];
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_year", dates[0]);
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_month", dates[1]);
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_day", dates[2]);
        XmlFragmentCleanerHelper.setDateValueFromYMD(xmlDocument, "/data/publication/@publish_", pattern);
      } else {
        publishYear = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_year");
        if (!"".equals(publishYear)) {
          XmlFragmentCleanerHelper.setDateValueFromYMD(xmlDocument, "/data/publication/@publish_", pattern);
        }
      }
    } catch (Exception e) {
      throw new SysServiceException("日期标准化出错");
    }

    return true;
  }
}
