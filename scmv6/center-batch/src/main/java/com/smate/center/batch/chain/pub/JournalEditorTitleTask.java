package com.smate.center.batch.chain.pub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.model.pdwh.pub.Journal;
import com.smate.center.batch.model.sns.pub.PubXmlProcessContext;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.service.pdwh.pub.JournalService;
import com.smate.center.batch.service.pub.IPubXmlTask;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.exception.BatchTaskException;



/**
 * @author yamingd 修正期刊编辑的标题
 */
public class JournalEditorTitleTask implements IPubXmlTask {
  /**
   * 
   */
  private final Logger logger = LoggerFactory.getLogger(getClass());
  /**
   * 
   */
  private final String name = "journal_editor_title";

  @Autowired
  private JournalService journalService;

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
    return xmlDocument.isJournalEditor();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.iris.scm.xml.IXmlTask#run(com.iris.scm.xml.XmlDocument,
   * com.iris.scm.xml.XmlProcessContext)
   */
  @Override
  public boolean run(PubXmlDocument xmlDocument, PubXmlProcessContext context) {

    String jid = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_XPATH, "jid");
    if (org.apache.commons.lang.StringUtils.isNumeric(jid)) {
      try {
        Journal j = journalService.getById(Long.parseLong(jid));
        if (j != null) {
          String cname = XmlUtil.filterNull(j.getZhName());
          String ename = XmlUtil.filterNull(j.getEnName());

          xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_XPATH, "zh_jname", cname);
          xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_XPATH, "en_jname", ename);
          xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_XPATH, "jissn", j.getIssn());

          // xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_title", cname);
          // xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "en_title", ename);
        } else {
          logger.error("读取不到期刊信息,jid=" + jid);
        }
      } catch (NumberFormatException e) {
        logger.error("NumberFormatException,jid=" + jid, e);
      } catch (BatchTaskException e) {
        logger.error("读取期刊信息错误,jid=" + jid, e);
      }
    }

    return true;
  }
}
