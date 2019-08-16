package com.smate.center.batch.chain.pub.rol;

import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.model.sns.pub.PubXmlProcessContext;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.service.pub.IPubXmlTask;
import com.smate.center.batch.service.rol.pubassign.PubAssignCniprXmlService;
import com.smate.center.batch.service.rol.pubassign.PubAssignCnkiPatXmlService;
import com.smate.center.batch.service.rol.pubassign.PubAssignCnkiXmlService;
import com.smate.center.batch.service.rol.pubassign.PubAssignEiXmlService;
import com.smate.center.batch.service.rol.pubassign.PubAssignPubMedXmlService;
import com.smate.center.batch.service.rol.pubassign.PubAssignSpsXmlService;
import com.smate.center.batch.service.rol.pubassign.PubAssignXmlService;

/**
 * 拆分指派成果需要的数据(在线导入、后台导入).
 * 
 * @author yamingd
 * 
 */
public class ExtractAssignDataToDbTask implements IPubXmlTask {

  private String name = "extract_assign_data_to_roldb";

  @Autowired
  private PubAssignXmlService pubAssignXmlService;
  @Autowired
  private PubAssignCnkiXmlService pubAssignCnkiXmlService;
  @Autowired
  private PubAssignSpsXmlService pubAssignSpsXmlService;
  @Autowired
  private PubAssignCniprXmlService pubAssignCniprXmlService;
  @Autowired
  private PubAssignCnkiPatXmlService pubAssignCnkiPatXmlService;
  @Autowired
  private PubAssignPubMedXmlService pubAssignPubMedXmlService;
  @Autowired
  private PubAssignEiXmlService pubAssignEiXmlService;

  @Override
  public boolean can(PubXmlDocument xmlDocument, PubXmlProcessContext context) {
    return context.isImport();
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public boolean run(PubXmlDocument xmlDocument, PubXmlProcessContext context) throws Exception {

    if (context.isCnkiImport()) {
      pubAssignCnkiXmlService.extractAssignData(xmlDocument, context);
    } else if (context.isIsiImport()) {
      pubAssignXmlService.extractAssignData(xmlDocument, context);
    } else if (context.isScopusImport()) {
      pubAssignSpsXmlService.extractAssignData(xmlDocument, context);
    } else if (context.isCniprImport()) {
      pubAssignCniprXmlService.extractAssignData(xmlDocument, context);
    } else if (context.isCnkiPatImport()) {
      pubAssignCnkiPatXmlService.extractAssignData(xmlDocument, context);
    } else if (context.isPubMedImport()) {
      pubAssignPubMedXmlService.extractAssignData(xmlDocument, context);
    } else if (context.isEiImport()) {
      pubAssignEiXmlService.extractAssignData(xmlDocument, context);
    }

    return true;
  }

}
