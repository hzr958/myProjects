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

/**
 * 展开成果会议.
 * 
 * @author liqinghua
 * 
 */
public class ExtractConferenceTask implements IPubAssignXmlTask {

  private final String name = "ExtractConferenceTask";
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

    // 是否是会议论文
    return ConstPublicationType.PUB_CONFERECE_TYPE.equals(xmlDocument.getPubTypeId());
  }

  @Override
  public boolean run(PubXmlDocument xmlDocument, PubXmlProcessContext context) throws Exception {

    String confName =
        StringUtils.trimToEmpty(xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_CONF_PAPER_XPATH, "conf_name"));
    if (StringUtils.isBlank(confName)) {
      return true;
    }
    if (context.isCnkiImport()) {
      pubAssignCnkiXmlService.savePubConference(confName, context.getCurrentPubId(), context.getCurrentInsId());
    } else if (context.isIsiImport()) {
      pubAssignXmlService.savePubConference(confName, context.getCurrentPubId(), context.getCurrentInsId());
    } else if (context.isScopusImport()) {
      pubAssignSpsXmlService.savePubConference(confName, context.getCurrentPubId(), context.getCurrentInsId());
    } else if (context.isPubMedImport()) {
      pubAssignPubMedXmlService.savePubConference(confName, context.getCurrentPubId(), context.getCurrentInsId());
    } else if (context.isEiImport()) {
      pubAssignEiXmlService.savePubConference(confName, context.getCurrentPubId(), context.getCurrentInsId());
    }
    return true;
  }

}
