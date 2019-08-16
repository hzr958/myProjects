package com.smate.sie.center.task.pdwh.task;

import org.apache.commons.lang.ObjectUtils;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.core.base.utils.string.StringUtils;
import com.smate.sie.center.task.journal.model.SieJournal;
import com.smate.sie.center.task.journal.service.SieJournalService;

/***
 * 匹配期刊.
 * 
 * @author sjzhou
 *
 */
public class JournalMatchTask implements IPubXmlTask {
  /**
   * 
   */
  private final Logger logger = LoggerFactory.getLogger(getClass());
  /**
   * 
   */
  private final String name = "journal_match";
  @Autowired
  private SieJournalService sieJournalService;

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public boolean can(PubXmlDocument xmlDocument, PubXmlProcessContext context) {
    return context.getPubTypeId() == 4;
  }

  @Override
  public boolean run(PubXmlDocument xmlDocument, PubXmlProcessContext context) throws SysServiceException {

    Element rootNode = (Element) xmlDocument.getRootNode();
    String jname = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "original");
    jname = XmlUtil.formatJnlTitle(jname);
    String jissn = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "issn").trim();
    // 统一存大写
    if (StringUtils.isNotBlank(jissn)) {
      jissn = jissn.toUpperCase();
    }
    try {
      Element journalEle = (Element) xmlDocument.getNode(PubXmlConstants.PUB_JOURNAL_XPATH);
      if (journalEle == null) {
        journalEle = rootNode.addElement("pub_journal");
      }
      SieJournal journal = sieJournalService.addJournalByPubEnter(jname, jissn, context.getCurrentInsId());
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_XPATH, "base_jnl_id",
          journal.getMatchBaseJnlId() != null ? ObjectUtils.toString(journal.getMatchBaseJnlId()) : "");
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_XPATH, "jid",
          journal.getId() != null ? ObjectUtils.toString(journal.getId()) : "");
      jname = XmlUtil.changeSBCChar(journal.getZhName() != null ? journal.getZhName() : journal.getEnName());
      jissn = XmlUtil.buildStandardIssn(XmlUtil.changeSBCChar(journal.getIssn()));
      if (XmlUtil.isChinese(jname)) {
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_XPATH, "zh_name", jname);
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_XPATH, "jname", jname);
      } else {
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_XPATH, "en_name", jname);
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_XPATH, "jname", jname);
      }
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_XPATH, "jissn", jissn);
      // 删除
      xmlDocument.removeAttribute(PubXmlConstants.PUBLICATION_XPATH, "original");
      xmlDocument.removeAttribute(PubXmlConstants.PUBLICATION_XPATH, "issn");
    } catch (Exception e) {
      logger.error("匹配期刊失败，jname：" + jname + "issn：" + jissn, e);
      throw new SysServiceException("匹配期刊失败，jname：" + jname + "issn：" + jissn);
    }
    return true;
  }

}
