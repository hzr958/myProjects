/**
 * 
 */
package com.smate.sie.center.task.pdwh.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.v8pub.dao.pdwh.PdwhPubSituationDAO;
import com.smate.center.task.v8pub.pdwh.po.PdwhPubSituationPO;
import com.smate.core.base.utils.constant.ConstDictionary;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.constant.SieConstPatType;
import com.smate.core.base.utils.dao.consts.Sie6ConstDictionaryDao;
import com.smate.core.base.utils.dao.consts.SieConstPatTypeDao;
import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.core.base.utils.string.IrisNumberUtils;
import com.smate.sie.center.task.pdwh.utils.XmlFragmentCleanerHelper;

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
  private PdwhPubSituationDAO pdwhPubSituationDAO;
  @Autowired
  private Sie6ConstDictionaryDao sie6ConstDictionaryDao;
  @Autowired
  private SieConstPatTypeDao sieConstPatTypeDao;

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public boolean can(PubXmlDocument xmlDocument, PubXmlProcessContext context) {
    return context.isImport();
  }

  @Override
  public boolean run(PubXmlDocument xmlDocument, PubXmlProcessContext context) throws SysServiceException {

    Element rootNode = (Element) xmlDocument.getRootNode();
    // 公共字段
    xmlDocument.copyAttributeValue(PubXmlConstants.PUBLICATION_XPATH, "source", "source_catalog");
    xmlDocument.copyAttributeValue(PubXmlConstants.PUBLICATION_XPATH, "pubyear", "publish_date");
    // 默认公开
    xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "is_public", "1");
    xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "is_public_code", "1");
    xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "is_public_name", "公开");
    // source_id
    // 导入时日期格式：yyyy-MM-dd
    String[] dates = XmlFragmentCleanerHelper.splitDateYearMonth(xmlDocument, PubXmlConstants.PUBLICATION_XPATH,
        "publish_date", PubXmlConstants.CHS_DATE_PATTERN);
    if (!"".equals(dates[0])) {
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_year", dates[0]);
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_month", dates[1]);
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_day", dates[2]);

      try {
        XmlFragmentCleanerHelper.setDateValueFromYMD(xmlDocument, PubXmlConstants.PUBLICATION_XPATH + "/@publish_",
            PubXmlConstants.CHS_DATE_PATTERN);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    // 全文url
    Element fullText = (Element) xmlDocument.getNode(PubXmlConstants.PUB_FULLTEXT_XPATH);
    if (fullText == null) {
      xmlDocument.createElement(PubXmlConstants.PUB_FULLTEXT_XPATH);
    }
    xmlDocument.copyAttributeValue(PubXmlConstants.PUBLICATION_XPATH, "fulltext_url",
        PubXmlConstants.PUB_FULLTEXT_XPATH, "fulltext_url");

    // 只存在中文
    xmlDocument.copyAttributeValue(PubXmlConstants.PUBLICATION_XPATH, "ctitle", "zh_title");
    xmlDocument.copyAttributeValue(PubXmlConstants.PUBLICATION_XPATH, "ctitle", "en_title");
    xmlDocument.copyAttributeValue(PubXmlConstants.PUBLICATION_XPATH, "cabstract", "zh_abstract");
    xmlDocument.copyAttributeValue(PubXmlConstants.PUBLICATION_XPATH, "cabstract", "en_abstract");

    String ckeywords = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "ckeywords");
    if (StringUtils.isNotBlank(ckeywords)) {
      ckeywords = KeepFiveKeywords(ckeywords);
    }
    xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_keywords", ckeywords);
    xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "en_keywords", ckeywords);

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
    String citeTimes = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "isi_cited");
    if (StringUtils.isNotBlank(citeTimes)) {
      try {
        Integer ct = Integer.valueOf(citeTimes);
        if (ct >= 0) {
          String citedDate = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
          xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "isi_cited_update", citedDate);
          xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "isi_cited", ct == 0 ? "" : citeTimes);
        }
      } catch (Exception e) {
        xmlDocument.setNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "isi_cited", "");
        logger.error("成果引用次数数据格式必须要正整数！！,citeTimes={}", citeTimes, e);
        throw new SysServiceException("成果引用次数数据格式必须要正整数！！citeTimes=" + citeTimes);
      }
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "cited_url",
          xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "cite_record_url"));
      xmlDocument.removeAttribute(PubXmlConstants.PUBLICATION_XPATH, "cite_record_url");
    }

    // 处理类型特定字段
    String original = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "original");
    if (xmlDocument.isAward()) {
      Element pubAward = null;
      pubAward = (Element) xmlDocument.getNode(PubXmlConstants.PUB_AWARD_XPATH);
      if (pubAward == null) {
        pubAward = rootNode.addElement("pub_award");
      }
      String[] attrs = new String[] {"award_category_name", "award_grade_name", "issue_ins_name"};
      for (int i = 0; i < attrs.length; i++) {
        xmlDocument.copyAttributeValue(PubXmlConstants.PUBLICATION_XPATH, attrs[i], PubXmlConstants.PUB_AWARD_XPATH,
            attrs[i]);
      }
      for (int i = 0; i < attrs.length; i++) {
        xmlDocument.removeAttribute(PubXmlConstants.PUBLICATION_XPATH, attrs[i]);
      }
      String awardCategory = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_AWARD_XPATH, "award_category_name");
      ConstDictionary constCategory =
          sie6ConstDictionaryDao.findConstByCategoryAndCode("award_category", awardCategory);
      if (constCategory != null) {
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_AWARD_XPATH, "award_category", constCategory.getSeqNo());
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_AWARD_XPATH, "award_category_code",
            constCategory.getSeqNo());
      } else {
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_AWARD_XPATH, "award_category", "");
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_AWARD_XPATH, "award_category_code", "");
      }
      String awardGrade = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_AWARD_XPATH, "award_grade_name");
      ConstDictionary constGrade = sie6ConstDictionaryDao.findConstByCategoryAndCode("award_grade", awardGrade);
      if (constGrade != null) {
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_AWARD_XPATH, "award_grade", constGrade.getSeqNo());
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_AWARD_XPATH, "award_grade_code", constGrade.getSeqNo());
      } else {
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_AWARD_XPATH, "award_grade", "");
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_AWARD_XPATH, "award_grade_code", "");
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

        try {
          XmlFragmentCleanerHelper.setDateValueFromYMD(xmlDocument, PubXmlConstants.PUBLICATION_XPATH + "/@accept_",
              PubXmlConstants.CHS_DATE_PATTERN);
        } catch (Exception e) {
          e.printStackTrace();
        }

      }
      if ("".equals(dates[0])) {
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_year", dates2[0]);
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_month", dates2[1]);
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_day", dates2[2]);

        try {
          XmlFragmentCleanerHelper.setDateValueFromYMD(xmlDocument, PubXmlConstants.PUBLICATION_XPATH + "/@publish_",
              PubXmlConstants.CHS_DATE_PATTERN);
        } catch (Exception e) {
          throw new SysServiceException("日期格式化出错");
        }

      }
      // 处理期刊的发表状态
      String publishState = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_state");

      if (StringUtils.isBlank(publishState)) {
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_state", "");
      }
    } else if (xmlDocument.isBook() || xmlDocument.isBookChapter()) {
      // category_value 类型
      Element pubBook = null;
      pubBook = (Element) xmlDocument.getNode(PubXmlConstants.PUB_BOOK_XPATH);
      if (pubBook == null) {
        pubBook = rootNode.addElement("pub_book");
      }
      // 著作类型
      String bookTpye = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "book_type");
      if ("MONOGRAPH".equals(bookTpye)) {
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_BOOK_XPATH, "book_type", "11");
      } else if ("TEXTBOOK".equals(bookTpye)) {
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_BOOK_XPATH, "book_type", "13");
      } else if ("COMPILE".equals(bookTpye)) {
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_BOOK_XPATH, "book_type", "14");
      } else {
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_BOOK_XPATH, "book_type", "");
      }
      xmlDocument.removeAttribute(PubXmlConstants.PUBLICATION_XPATH, "book_type");

      String[] attrs = new String[] {"publisher", "book_title", "isbn", "total_words"};
      for (String attr : attrs) {
        xmlDocument.copyAttributeValue(PubXmlConstants.PUBLICATION_XPATH, attr, PubXmlConstants.PUB_BOOK_XPATH, attr);
      }
      String language = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "language");
      if (StringUtils.isNotBlank(language)) {
        if ("中文".equals(language)) {
          xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_BOOK_XPATH, "language", "1");
        } else {
          xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_BOOK_XPATH, "language", "2");
        }
      } else {
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_BOOK_XPATH, "language", "");
      }
    } else if (xmlDocument.isThesis()) {
      Element ele = null;
      ele = (Element) xmlDocument.getNode(PubXmlConstants.PUB_THESIS_XPATH);
      if (ele == null) {
        ele = rootNode.addElement("pub_thesis");
      }
      String[] attrs = new String[] {"issue_org", "department", "programme"};
      for (String attr : attrs) {
        xmlDocument.copyAttributeValue(PubXmlConstants.PUBLICATION_XPATH, attr, PubXmlConstants.PUB_THESIS_XPATH, attr);
      }
      for (int i = 0; i < attrs.length; i++) {
        xmlDocument.removeAttribute(PubXmlConstants.PUBLICATION_XPATH, attrs[i]);
      }
      String programme = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_THESIS_XPATH, "programme");
      if ("MASTER".equals(programme)) {
        programme = "硕士";
      } else if ("DOCTOR".equals(programme)) {
        programme = "博士";
      } else if ("OTHER".equals(programme)) {
        programme = "其他";
      } else {
        programme = "其他";
      }
      programme = programme == null ? null : programme.trim();
      ConstDictionary cd = null;
      try {
        cd = sie6ConstDictionaryDao.findConstByCategoryAndCode("pub_thesis_programme", programme);
      } catch (Exception e) {
        throw new SysServiceException("匹配学位论文的学位常量出错");
      }
      if (cd != null) {
        ele.addAttribute("programme", cd.getKey().getCode());
      } else if (StringUtils.isNotBlank(programme)) {
        logger.error("找不到常数, Category='pub_thesis_programme' and Code=" + programme);
      }
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_THESIS_XPATH, "programme_name",
          cd != null ? cd.getZhCnName() : "");
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_THESIS_XPATH, "programme_code",
          cd != null ? cd.getKey().getCode() : "");
    } else if (xmlDocument.isPatent()) {
      Element ele = null;
      ele = (Element) xmlDocument.getNode(PubXmlConstants.PUB_PATENT_XPATH);
      if (ele == null) {
        ele = rootNode.addElement("pub_patent");
      }
      String patentCategoryNo =
          xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "patent_category_no");
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "ipc", StringUtils.trim(patentCategoryNo));
      xmlDocument.removeAttribute(PubXmlConstants.PUBLICATION_XPATH, "patent_category_no");
      String patentType = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "patent_category");
      if (StringUtils.isNotBlank(patentType) && NumberUtils.isNumber(patentType)) {
        SieConstPatType sieConstPatType = sieConstPatTypeDao.get(IrisNumberUtils.createInteger(patentType));
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "patent_type",
            null != sieConstPatType ? sieConstPatType.getTypeId().toString() : "");
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "patent_type_code",
            null != sieConstPatType ? sieConstPatType.getTypeId().toString() : "");
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "patent_type_name",
            null != sieConstPatType ? sieConstPatType.getName() : "");
      } else {
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "patent_type", "");
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "patent_type_code", "");
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "patent_type_name", "");
      }
      String startDate = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "start_date");
      String patentNo = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "patent_no");
      ele.addAttribute("start_date", startDate);
      ele.addAttribute("patent_no", patentNo);
      xmlDocument.removeAttribute(PubXmlConstants.PUBLICATION_XPATH, "patent_no");
      String legalStatus = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "patent_status");
      if ("授权".equals(legalStatus)) {
        ele.addAttribute("patent_status", "1");
        ele.addAttribute("patent_status_code", "1");
        ele.addAttribute("patent_status_name", "授权");
        xmlDocument.removeNode(PubXmlConstants.PUB_APPLIERS_XPATH);
        Element appliers = xmlDocument.createElement(PubXmlConstants.PUB_APPLIERS_XPATH);
        String patentAgentPerson =
            xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "patent_agent_person");
        String[] patentAgentPersons = patentAgentPerson.split(",|;");
        for (int i = 0; i < patentAgentPersons.length; i++) {
          // 构造专利权人节点
          buildPubAppliersData(appliers, patentAgentPersons[i], i + 1);
        }
        ele.addAttribute("patent_applier", patentAgentPerson);// 授权时，为专利权人
      } else if ("申请".equals(legalStatus)) {
        ele.addAttribute("patent_status", "0");
        ele.addAttribute("patent_status_code", "0");
        ele.addAttribute("patent_status_name", "申请");
      } else if ("视撤".equals(legalStatus)) {
        ele.addAttribute("patent_status", "2");
        ele.addAttribute("patent_status_code", "2");
        ele.addAttribute("patent_status_name", "视撤");
      } else if ("有效".equals(legalStatus)) {
        ele.addAttribute("patent_status", "3");
        ele.addAttribute("patent_status_code", "3");
        ele.addAttribute("patent_status_name", "有效");
      } else if ("失效".equals(legalStatus)) {
        ele.addAttribute("patent_status", "4");
        ele.addAttribute("patent_status_code", "4");
        ele.addAttribute("patent_status_name", "失效");
      } else {
        ele.addAttribute("patent_status", "");
        ele.addAttribute("patent_status_code", "");
        ele.addAttribute("patent_status_name", "");
      }

    } else if (xmlDocument.isOther()) {
      String pubyear = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "pubyear");
      Element ele = null;
      ele = (Element) xmlDocument.getNode(PubXmlConstants.PUB_OTHER_XPATH);
      if (ele == null) {
        ele = rootNode.addElement("pub_other");
      }
      ele.addAttribute("pub_date", pubyear);
      dates = XmlFragmentCleanerHelper.splitDateYearMonth(xmlDocument, PubXmlConstants.PUB_OTHER_XPATH, "pub_date",
          PubXmlConstants.CHS_DATE_PATTERN);
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_OTHER_XPATH, "pub_year", dates[0]);
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_OTHER_XPATH, "pub_month", dates[1]);
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_OTHER_XPATH, "pub_day", dates[2]);
    }
    // 移除基准库json数据中生成的节点
    xmlDocument.removeAttribute(PubXmlConstants.PUBLICATION_XPATH, "hp");
    xmlDocument.removeAttribute(PubXmlConstants.PUBLICATION_XPATH, "oa");
    xmlDocument.removeAttribute(PubXmlConstants.PUBLICATION_XPATH, "hcp");
    xmlDocument.removeAttribute(PubXmlConstants.PUBLICATION_XPATH, "pubId");
    xmlDocument.removeAttribute(PubXmlConstants.PUBLICATION_XPATH, "language");
    // 构造收录情况
    String xmlId = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "dbcache_xml_id");
    if (StringUtils.isNotBlank(xmlId)) {
      List<PdwhPubSituationPO> situationPO =
          pdwhPubSituationDAO.queryListByPdwhPubId(IrisNumberUtils.createLong(xmlId));
      Element pubList = null;
      pubList = (Element) xmlDocument.getNode(PubXmlConstants.PUB_LIST_XPATH);
      if (pubList == null) {
        pubList = rootNode.addElement("pub_list");
      }
      if (situationPO != null && situationPO.size() != 0) {
        xmlDocument.fillPubList(situationPO);
      }
    }
    return true;
  }

  private void buildPubAppliersData(Element pubMembers, String authName, int seqNo) throws SysServiceException {
    Element pubMember = pubMembers.addElement("pub_applier");
    pubMember.addAttribute("seq_no", String.valueOf(seqNo));
    pubMember.addAttribute("applier_name", authName);
    pubMember.addAttribute("applier_name_id", "");
  }

  private String KeepFiveKeywords(String ckeywords) {
    String[] splitArr = ckeywords.split(";");
    List<String> lkwList = new ArrayList<String>();
    for (String kw : splitArr) {
      String lkw = kw.trim().toLowerCase();
      lkw = StringUtils.substring(lkw, 0, 20);
      if (StringUtils.isBlank(lkw) || lkwList.contains(lkw)) {
        continue;
      }
      lkwList.add(lkw);
    }
    int length = lkwList.size() > 5 ? 5 : lkwList.size();
    StringBuilder resultStr = new StringBuilder();
    for (int i = 0; i < length; i++) {
      if (!StringUtils.isBlank(lkwList.get(i))) {
        if (i == 0) {
          resultStr.append(lkwList.get(i).trim());
        } else {
          resultStr.append(";");
          resultStr.append(lkwList.get(i).trim());
        }
      }
    }
    return resultStr.toString();
  }
}
