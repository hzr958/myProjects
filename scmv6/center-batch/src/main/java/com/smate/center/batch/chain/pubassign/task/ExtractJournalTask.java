package com.smate.center.batch.chain.pubassign.task;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.model.sns.pub.PubXmlProcessContext;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.service.rol.pubassign.PubAssignCnkiXmlService;
import com.smate.center.batch.service.rol.pubassign.PubAssignEiXmlService;
import com.smate.center.batch.service.rol.pubassign.PubAssignPubMedXmlService;
import com.smate.center.batch.service.rol.pubassign.PubAssignSpsXmlService;
import com.smate.center.batch.service.rol.pubassign.PubAssignXmlService;
import com.smate.center.batch.util.pub.ConstPublicationType;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.string.IrisNumberUtils;

/**
 * 展开成果期刊.
 * 
 * @author liqinghua
 * 
 */
public class ExtractJournalTask implements IPubAssignXmlTask {

  private final String name = "ExtractJournalTask";
  @Autowired
  private PubAssignXmlService pubAssignXmlService;
  @Autowired
  private PubAssignCnkiXmlService pubAssignCnkiXmlService;
  @Autowired
  private PubAssignSpsXmlService pubAssignSpsXmlService;
  @Autowired
  private PubAssignPubMedXmlService pubAssignPubMedXmlService;
  @Autowired
  private PubAssignEiXmlService pubAssignEiXmlService;

  @Override
  public String getName() {
    return name;
  }

  @Override
  public boolean can(PubXmlDocument xmlDocument, PubXmlProcessContext context) {

    // 是否是期刊文章
    return ConstPublicationType.PUB_JOURNAL_TYPE.equals(xmlDocument.getPubTypeId());
  }

  @Override
  public boolean run(PubXmlDocument xmlDocument, PubXmlProcessContext context) throws Exception {

    Long jid = IrisNumberUtils.createLong(xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_XPATH, "jid"));
    String jname = StringUtils.trimToEmpty(xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_XPATH, "jname"));
    if (jid == null || StringUtils.isBlank(jname)) {
      return true;
    }
    String issn = StringUtils.trimToEmpty(xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_XPATH, "jissn"));
    if (context.isCnkiImport()) {
      pubAssignCnkiXmlService.savePubJournal(jid, jname, issn, context.getCurrentPubId(), context.getCurrentInsId());
    } else if (context.isIsiImport()) {
      pubAssignXmlService.savePubJournal(jid, jname, issn, context.getCurrentPubId(), context.getCurrentInsId());
    } else if (context.isScopusImport()) {
      pubAssignSpsXmlService.savePubJournal(jid, jname, issn, context.getCurrentPubId(), context.getCurrentInsId());
    } else if (context.isPubMedImport()) {
      pubAssignPubMedXmlService.savePubJournal(jid, jname, issn, context.getCurrentPubId(), context.getCurrentInsId());
    } else if (context.isEiImport()) {
      pubAssignEiXmlService.savePubJournal(jid, jname, issn, context.getCurrentPubId(), context.getCurrentInsId());
    }

    return true;
  }

}
