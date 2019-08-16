/**
 * 
 */
package com.smate.center.batch.chain.pub.rol;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.dao.pdwh.pubimport.PdwhPubSourceDbDao;
import com.smate.center.batch.model.pdwh.pubimport.PdwhPubSourceDb;
import com.smate.center.batch.model.sns.pub.ConstDictionary;
import com.smate.center.batch.model.sns.pub.PubXmlProcessContext;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.service.pub.IPubXmlTask;
import com.smate.center.batch.util.pub.XmlFragmentCleanerHelper;
import com.smate.core.base.utils.constant.PubXmlConstants;


/**
 * @author yamingd 导入字段映射
 */
public class ImportFieldMappingTask implements IPubXmlTask {

  /**
   * 
   */
  private final Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * 
   */
  private final String name = "import_field_mapping";

  @Autowired
  private PdwhPubSourceDbDao pdwhPubSourceDbDao;

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public boolean can(PubXmlDocument xmlDocument, PubXmlProcessContext context) {
    return context.isImport();
  }

  @Override
  public boolean run(PubXmlDocument xmlDocument, PubXmlProcessContext context) throws Exception {

    Element rootNode = (Element) xmlDocument.getRootNode();

    // 公共字段
    xmlDocument.copyAttributeValue(PubXmlConstants.PUBLICATION_XPATH, "source", "source_catalog");
    xmlDocument.copyAttributeValue(PubXmlConstants.PUBLICATION_XPATH, "pubyear", "publish_date");

    // source_id
    // 导入时日期格式：yyyy-MM-dd
    String[] dates = XmlFragmentCleanerHelper.splitDateYearMonth(xmlDocument, PubXmlConstants.PUBLICATION_XPATH,
        "publish_date", PubXmlConstants.CHS_DATE_PATTERN);
    if (!"".equals(dates[0])) {
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_year", dates[0]);
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_month", dates[1]);
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_day", dates[2]);

      XmlFragmentCleanerHelper.setDateValueFromYMD(xmlDocument, PubXmlConstants.PUBLICATION_XPATH + "/@publish_",
          PubXmlConstants.CHS_DATE_PATTERN);

    }

    // 全文url
    Element fullText = (Element) xmlDocument.getNode(PubXmlConstants.PUB_FULLTEXT_XPATH);
    if (fullText == null) {
      xmlDocument.createElement(PubXmlConstants.PUB_FULLTEXT_XPATH);
    }
    xmlDocument.copyAttributeValue(PubXmlConstants.PUBLICATION_XPATH, "fulltext_url",
        PubXmlConstants.PUB_FULLTEXT_XPATH, "fulltext_url");

    xmlDocument.copyAttributeValue(PubXmlConstants.PUBLICATION_XPATH, "ctitle", "zh_title");
    xmlDocument.copyAttributeValue(PubXmlConstants.PUBLICATION_XPATH, "etitle", "en_title");
    xmlDocument.copyAttributeValue(PubXmlConstants.PUBLICATION_XPATH, "cabstract", "zh_abstract");
    xmlDocument.copyAttributeValue(PubXmlConstants.PUBLICATION_XPATH, "eabstract", "en_abstract");
    xmlDocument.copyAttributeValue(PubXmlConstants.PUBLICATION_XPATH, "ckeywords", "zh_keywords");
    xmlDocument.copyAttributeValue(PubXmlConstants.PUBLICATION_XPATH, "ekeywords", "en_keywords");

    // 拷贝导入的pub_type
    String pubTypeId = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "pub_type");
    Element pubType = (Element) xmlDocument.getNode(PubXmlConstants.PUB_TYPE_XPATH);
    if (pubType == null) {
      pubType = xmlDocument.createElement(PubXmlConstants.PUB_TYPE_XPATH);
    }
    if (pubType != null) {
      pubType.addAttribute("id", pubTypeId);
    }
    String sourceUrl = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "source_url");
    Element pubMeta = (Element) xmlDocument.getNode(PubXmlConstants.PUB_META_XPATH);
    pubMeta.addAttribute("source_url", sourceUrl);

    // 数据库唯一标识符
    PubXmlDocument.parseDbSourceId(xmlDocument);
    // 数据库url标记
    PubXmlDocument.parseDbSourceUrl(xmlDocument);

    xmlDocument.removeAttribute(PubXmlConstants.PUBLICATION_XPATH, "ctitle", "etitle", "cabstract", "eabstract",
        "ckeywords", "ekeywords", "pub_type", "source_url");

    String citeTimes = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "cite_times");
    xmlDocument.copyAttributeValue(PubXmlConstants.PUBLICATION_XPATH, "source", "cited_list");
    if (StringUtils.isNotBlank(citeTimes)) {
      try {
        Integer ct = Integer.valueOf(citeTimes);
        if (ct >= 0) {
          String citedDate = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
          xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "cited_date", citedDate);
        }
      } catch (Exception e) {
        xmlDocument.setNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "cite_times", "");
        logger.error("成果引用次数数据格式必须要正整数！！,citeTimes={}", citeTimes, e);
      }
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "cited_url",
          xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "cite_record_url"));
      xmlDocument.removeAttribute(PubXmlConstants.PUBLICATION_XPATH, "cite_record_url");
    }

    String eiCiteTimes = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "ei_cite_times");
    if (StringUtils.isNotBlank(eiCiteTimes)) {
      try {
        Integer ect = Integer.valueOf(eiCiteTimes);
        if (ect >= 0) {
          String eiCitedDate = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
          xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "ei_cited_date", eiCitedDate);
        }
      } catch (Exception e) {
        xmlDocument.setNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "ei_cite_times", "");
        logger.error("成果引用次数数据格式必须要正整数！！,eiCiteTimes={}", eiCiteTimes, e);
      }
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "ei_cite_url",
          xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "ei_cite_record_url"));
      xmlDocument.removeAttribute(PubXmlConstants.PUBLICATION_XPATH, "ei_cite_record_url");
    }

    String cnkiCiteTimes = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "cnki_cite_times");
    if (StringUtils.isNotBlank(cnkiCiteTimes)) {
      try {
        Integer cnkiCt = Integer.valueOf(cnkiCiteTimes);
        if (cnkiCt >= 0) {
          String cnkiCitedDate = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
          xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "cnki_cited_date", cnkiCitedDate);
        }
      } catch (Exception e) {
        xmlDocument.setNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "cnki_cite_times", "");
        logger.error("成果引用次数数据格式必须要正整数！！,cnkiCiteTimes={}", cnkiCiteTimes, e);
      }
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "cnki_cited_url",
          xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "cnki_cite_record_url"));
      xmlDocument.removeAttribute(PubXmlConstants.PUBLICATION_XPATH, "cnki_cite_record_url");
    }
    String cniprCiteTimes = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "cnipr_cite_times");
    if (StringUtils.isNotBlank(cniprCiteTimes)) {
      try {
        Integer cniprCt = Integer.valueOf(cniprCiteTimes);
        if (cniprCt >= 0) {
          String cniprCitedDate = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
          xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "cnipr_cited_date", cniprCitedDate);
        }
      } catch (Exception e) {
        xmlDocument.setNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "cnipr_cite_times", "");
        logger.error("成果引用次数数据格式必须要正整数！！,cniprCiteTimes={}", cniprCiteTimes, e);
      }
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "cnipr_cited_url",
          xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "cnipr_cite_record_url"));
      xmlDocument.removeAttribute(PubXmlConstants.PUBLICATION_XPATH, "cnipr_cite_record_url");
    }
    // 处理类型特定字段

    String original = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "original");
    if (xmlDocument.isAward()) {
      Element pubAward = null;
      pubAward = (Element) xmlDocument.getNode(PubXmlConstants.PUB_AWARD_XPATH);
      if (pubAward == null) {
        pubAward = rootNode.addElement("pub_award");
      }
      String[] attrs = new String[] {"award_category", "award_grade", "issue_ins_name"};
      for (int i = 0; i < attrs.length; i++) {
        xmlDocument.copyAttributeValue(PubXmlConstants.PUBLICATION_XPATH, attrs[i], PubXmlConstants.PUB_AWARD_XPATH,
            attrs[i]);
      }
    } else if (xmlDocument.isConfPaper()) {
      // 文件导入 category_value paper_type_value organizer
      Element pubConfPaper = null;
      pubConfPaper = (Element) xmlDocument.getNode(PubXmlConstants.PUB_CONF_PAPER_XPATH);
      if (pubConfPaper == null) {
        pubConfPaper = rootNode.addElement("pub_conf_paper");
        String[] srcAttrs = new String[] {"proceeding_title", "organizer", "start_date", "end_date"};
        String[] desAttrs = new String[] {"conf_name", "conf_organizer", "start_date", "end_date"};
        for (int i = 0; i < srcAttrs.length; i++) {
          xmlDocument.copyAttributeValue(PubXmlConstants.PUBLICATION_XPATH, srcAttrs[i],
              PubXmlConstants.PUB_CONF_PAPER_XPATH, desAttrs[i]);
        }
      }
      // 论文类型
      if (StringUtils
          .isNotBlank(xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "category_value"))) {
        String[] value = StringUtils
            .split(xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "category_value"), "-");
        if (value.length == 2) {
          xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_CONF_PAPER_XPATH, "conf_category",
              StringUtils.trim(value[0]));
        }
      }
      // 论文类别
      if (StringUtils
          .isNotBlank(xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "paper_type_value"))) {
        String[] value = StringUtils
            .split(xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "paper_type_value"), "-");
        if (value.length == 2) {
          xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_CONF_PAPER_XPATH, "paper_type",
              StringUtils.trim(value[0]));
        }
      }
      String pubyear = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "pubyear");
      String startDate = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "start_date");
      String endDate = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "end_date");
      if ("".equals(pubyear)) {
        startDate = "".equals(startDate) ? pubyear : startDate;

        pubConfPaper.addAttribute("start_date", startDate);

        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_date",
            "".equals(startDate) ? endDate : startDate);
      }
    } else if (xmlDocument.isJournalArticle()) {
      if (!"".equals(original)) {
        Element pubJournalArticle = null;
        pubJournalArticle = (Element) xmlDocument.getNode(PubXmlConstants.PUB_JOURNAL_XPATH);
        if (pubJournalArticle == null) {
          pubJournalArticle = rootNode.addElement("pub_journal");
        }
        pubJournalArticle.addAttribute("jname", original);
        pubJournalArticle.addAttribute("jid",
            xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "jid"));
      }

      String[] dates2 = XmlFragmentCleanerHelper.splitDateYearMonth(xmlDocument, PubXmlConstants.PUBLICATION_XPATH,
          "accept_year", PubXmlConstants.CHS_DATE_PATTERN);
      if (!"".equals(dates2[0])) {
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "accept_year", dates2[0]);
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "accept_month", dates2[1]);
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "accept_day", dates2[2]);

        XmlFragmentCleanerHelper.setDateValueFromYMD(xmlDocument, PubXmlConstants.PUBLICATION_XPATH + "/@accept_",
            PubXmlConstants.CHS_DATE_PATTERN);

      }
      if ("".equals(dates[0])) {
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_year", dates2[0]);
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_month", dates2[1]);
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_day", dates2[2]);

        XmlFragmentCleanerHelper.setDateValueFromYMD(xmlDocument, PubXmlConstants.PUBLICATION_XPATH + "/@publish_",
            PubXmlConstants.CHS_DATE_PATTERN);

      }
      // 处理期刊的发表状态
      String publishState = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_state");

      if (StringUtils.isBlank(publishState)) {
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_state", "P");
      }
    } else if (xmlDocument.isBook() || xmlDocument.isBookChapter()) {
      // category_value 类型
      Element pubBook = null;
      pubBook = (Element) xmlDocument.getNode(PubXmlConstants.PUB_BOOK_XPATH);
      if (pubBook == null) {
        pubBook = rootNode.addElement("pub_book");
      }
      // 类型
      if (StringUtils
          .isNotBlank(xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "category_value"))) {
        String[] value = StringUtils
            .split(xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "category_value"), "-");
        if (value.length == 2) {
          xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_BOOK_XPATH, "book_type", StringUtils.trim(value[0]));
        }
      }
      String[] attrs = new String[] {"publisher", "book_title", "chapter_no", "series_name", "editors", "isbn",
          "total_pages", "total_words"};
      for (String attr : attrs) {
        xmlDocument.copyAttributeValue(PubXmlConstants.PUBLICATION_XPATH, attr, PubXmlConstants.PUB_BOOK_XPATH, attr);
      }
      xmlDocument.copyAttributeValue(PubXmlConstants.PUBLICATION_XPATH, "city", PubXmlConstants.PUB_BOOK_XPATH,
          "publish_place");
    } else if (xmlDocument.isThesis()) {
      // this.thesis_ins_name=""; //针对学位论文类型 ，存放学位授予单位
      // this.thesis_dept_name=""; //针对学位论文类型 ，存放学科专业名称
      // this.thesis_programme=""; //针对学位论文类型 ，存放学位
      Element ele = null;
      ele = (Element) xmlDocument.getNode(PubXmlConstants.PUB_THESIS_XPATH);
      if (ele == null) {
        ele = rootNode.addElement("pub_thesis");
      }
      String insName = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "thesis_ins_name");
      String deptName = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "thesis_dept_name");
      String programme = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "thesis_programme");
      programme = programme == null ? null : programme.trim();
      ele.addAttribute("issue_org", insName);
      ele.addAttribute("department", deptName);
      // ele.addAttribute("programme_name", programme);
      // ele.addAttribute("programme", programme);

      ConstDictionary cd = null;
      try {
        cd = context.getXmlServiceFactory().getConstDictionaryManage().findConstByCategoryAndCode("pub_thesis_degree",
            programme);
        if (cd == null) {
          cd = context.getXmlServiceFactory().getConstDictionaryManage().findConstByCategoryAndName("pub_thesis_degree",
              programme);
        }
        if (cd != null) {
          xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_THESIS_XPATH, "zh_programme_name", cd.getZhCnName());
          xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_THESIS_XPATH, "en_programme_name", cd.getEnUsName());
          xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_THESIS_XPATH, "programme", cd.getKey().getCode());
        }

      } catch (Exception e) {
        logger.error("读取学位常数错误", e);
      }
      if (cd != null) {
        ele.addAttribute("programme", cd.getKey().getCode());
      } else if (StringUtils.isNotBlank(programme)) {
        logger.error("找不到常数, Category='degree' and Code=" + programme);
      }
    } else if (xmlDocument.isPatent()) {
      Element ele = null;
      ele = (Element) xmlDocument.getNode(PubXmlConstants.PUB_PATENT_XPATH);
      if (ele == null) {
        ele = rootNode.addElement("pub_patent");
      }
      // 类型 category_value文件导入.
      if (StringUtils
          .isNotBlank(xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "category_value"))) {
        String[] value = StringUtils
            .split(xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "category_value"), "-");
        if (value.length == 2) {
          xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "patent_type", StringUtils.trim(value[0]));
        }
      }
      String[] srcAttrs = new String[] {"start_date", "end_date", "issue_org"};
      String[] desAttrs = new String[] {"start_date", "end_date", "patent_org"};
      for (int i = 0; i < srcAttrs.length; i++) {
        xmlDocument.copyAttributeValue(PubXmlConstants.PUBLICATION_XPATH, srcAttrs[i], PubXmlConstants.PUB_PATENT_XPATH,
            desAttrs[i]);
      }
      String pubyear = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "pubyear");
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_date", pubyear);
      String issueDate = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "patent_issue_date");
      String patentNo = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "patent_no");
      String patentApplier = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "patent_applier");// 文件导入时的申请人字段
      if (patentApplier == "") {// 检索时的申请人字段
        patentApplier = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "organization");
      }
      // String patentRegNo =
      // xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH,
      // "patent_reg_no");
      String patentCategory = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "patent_category");
      String patentMainCategoryNo =
          xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "patent_main_category_no");
      String patentCategoryNo = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "category_no");
      String patentPriority = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "patent_priority");
      String patentAgentOrg = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "patent_agent_org");
      String patentAgentPerson =
          xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "patent_agent_person");
      String patentOpenNo = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "patent_open_no");

      String patentType = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "patent_type");
      String patentIssueOrg = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "patent_org");
      ele.addAttribute("effective_start_date", issueDate);
      // ele.addAttribute("reg_no", patentRegNo);
      ele.addAttribute("patent_org", patentIssueOrg);
      ele.addAttribute("patent_no", patentNo);
      ele.addAttribute("category", patentCategory);
      if (StringUtils.isBlank(patentType)) {
        ele.addAttribute("patent_type", patentCategory);
      }
      ele.addAttribute("reg_date", pubyear);
      ele.addAttribute("main_category_no", patentMainCategoryNo);
      ele.addAttribute("category_no", patentCategoryNo);
      ele.addAttribute("priority", patentPriority);
      ele.addAttribute("agent_org", patentAgentOrg);
      ele.addAttribute("agent_person", patentAgentPerson);
      ele.addAttribute("patent_open_no", patentOpenNo);
      ele.addAttribute("patent_applier", patentApplier);

    } else if (xmlDocument.isOther()) {
      String pubyear = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "pubyear");
      Element ele = null;
      ele = (Element) xmlDocument.getNode(PubXmlConstants.PUB_OTHER_XPATH);
      if (ele == null) {
        ele = rootNode.addElement("pub_other");
      }
      if (!"".equals(pubyear)) {
        ele.addAttribute("pub_date", pubyear);
        ele.addAttribute("di_mode", "date");
      }
    }

    // 构造收录情况

    String xmlId = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "dbcache_xml_id");
    if (StringUtils.isNotBlank(xmlId)) {
      PdwhPubSourceDb pubSourceDb = pdwhPubSourceDbDao.get(Long.valueOf(xmlId));
      if (pubSourceDb != null) {
        xmlDocument.fillPubList(pubSourceDb, context.getSourceDbId().intValue());
      }
    }
    return true;
  }
}
