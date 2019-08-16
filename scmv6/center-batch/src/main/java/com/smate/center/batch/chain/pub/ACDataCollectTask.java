package com.smate.center.batch.chain.pub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.model.sns.pub.PubXmlProcessContext;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.service.pub.AutoCompleteService;
import com.smate.center.batch.service.pub.IPubXmlTask;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.data.XmlUtil;


/**
 * @author yamingd 收集自动提示数据，存放到数据库
 */
public class ACDataCollectTask implements IPubXmlTask {

  /**
   * 
   */
  private final Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * 
   */
  private final String name = "acdata_collect";

  @Autowired
  private AutoCompleteService autoCompleteService;

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

    // ac_award_category
    String text = "";
    /*
     * String text = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_AWARD_XPATH, "award_category");
     * if (!XmlUtil.isEmpty(text)) { try { autoCompleteService.saveAcDataByTask(text, "award_category");
     * } catch (Exception e) { logger.error("保存AcAwardCategory", e); } }
     */
    /*
     * // ac_award_grade text = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_AWARD_XPATH,
     * "award_grade"); if (!XmlUtil.isEmpty(text)) { try { autoCompleteService.saveAcDataByTask(text,
     * "award_grade"); } catch (Exception e) { logger.error("保存AcAwardGrade", e); } }
     */
    // ac_award_issue_ins
    text = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_AWARD_XPATH, "issue_ins_name");
    if (!XmlUtil.isEmpty(text)) {
      try {
        autoCompleteService.saveAcDataByTask(text, "issue_ins_name");
      } catch (Exception e) {
        logger.error("保存AcAwardIssueIns", e);
      }
    }
    // ac_city
    text = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "city");
    if (!XmlUtil.isEmpty(text)) {
      try {
        autoCompleteService.saveAcDataByTask(text, "city");
      } catch (Exception e) {
        logger.error("保存AcCity", e);
      }
    }
    // ac_conf_name
    text = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_CONF_PAPER_XPATH, "conf_name");
    if (!XmlUtil.isEmpty(text)) {
      try {
        autoCompleteService.saveAcDataByTask(text, "conf_name");
      } catch (Exception e) {
        logger.error("保存AcConfName", e);
      }
    }
    // ac_conf_organizer
    text = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_CONF_PAPER_XPATH, "conf_organizer");
    if (!XmlUtil.isEmpty(text)) {
      try {
        autoCompleteService.saveAcDataByTask(text, "conf_organizer");
      } catch (Exception e) {
        logger.error("保存AcConfOrganizer", e);
      }
    }
    // ac_patent_org
    text = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "patent_org");
    if (!XmlUtil.isEmpty(text)) {
      try {
        autoCompleteService.saveAcDataByTask(text, "patent_org");
      } catch (Exception e) {
        logger.error("保存AcPatentOrg", e);
      }
    }
    // ac_thesis_org
    text = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_THESIS_XPATH, "issue_org");
    if (!XmlUtil.isEmpty(text)) {
      try {
        autoCompleteService.saveAcDataByTask(text, "issue_org");
      } catch (Exception e) {
        logger.error("保存AcIssueOrg", e);
      }
    }
    // ac_publisher
    text = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_BOOK_XPATH, "publisher");
    if (!XmlUtil.isEmpty(text)) {
      try {
        autoCompleteService.saveAcDataByTask(text, "publisher");
      } catch (Exception e) {
        logger.error("保存AcPublisher", e);
      }
    }
    return true;
  }

}
