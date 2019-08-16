package com.smate.sie.center.task.pdwh.task;

import com.smate.center.task.single.enums.pub.XmlOperationEnum;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.sie.center.task.pdwh.service.SiePatentLogService;
import com.smate.sie.center.task.pdwh.service.SiePublicationLogService;
import com.smate.sie.center.task.pub.enums.PublicationOperationEnum;
import org.springframework.beans.factory.annotation.Autowired;

/** @author yamingd Xml操作日志记录Task */
public class OperationLogTask implements IPubXmlTask {

  private String name = "log_task";

  @Autowired
  private SiePublicationLogService siePublicationLogService;
  @Autowired
  private SiePatentLogService patentLogService;

  @Override
  public boolean can(PubXmlDocument xmlDocument, PubXmlProcessContext context) {

    return true;
  }

  @Override
  public String getName() {

    return this.name;
  }

  @Override
  public boolean run(PubXmlDocument xmlDocument, PubXmlProcessContext context) throws Exception {

    long pubId = xmlDocument.getPubId();
    int pubType = xmlDocument.getPubTypeId();
    if (pubType != 5) {
      if (XmlOperationEnum.Import.equals(context.getCurrentAction())) {
        String dupPubId = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "dup_pub_id");
        if ("".equals(dupPubId)) {
          siePublicationLogService.logOp(pubId, context.getCurrentUserId(), context.getCurrentInsId(),
              PublicationOperationEnum.Import, null);
        } else {
          siePublicationLogService.logOp(pubId, context.getCurrentUserId(), context.getCurrentInsId(),
              PublicationOperationEnum.ImportOverwrite, null);
        }
      } else if (XmlOperationEnum.ImportPdwh.equals(context.getCurrentAction())) {
        String dupPubId = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "dup_pub_id");
        if ("".equals(dupPubId)) {
          siePublicationLogService.logOp(pubId, context.getCurrentUserId(), context.getCurrentInsId(),
              PublicationOperationEnum.ImportPdwh, null);
        } else {
          siePublicationLogService.logOp(pubId, context.getCurrentUserId(), context.getCurrentInsId(),
              PublicationOperationEnum.ImportPdwhOverwrite, null);
        }
      }
    } else {
      if (XmlOperationEnum.Import.name().equals(context.getCurrentAction().name())) {
        String dupPubId = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "dup_pub_id");
        if ("".equals(dupPubId)) {
          patentLogService.logOp(pubId, context.getCurrentUserId(), context.getCurrentInsId(),
              PublicationOperationEnum.Import, null);
        } else {
          patentLogService.logOp(pubId, context.getCurrentUserId(), context.getCurrentInsId(),
              PublicationOperationEnum.ImportOverwrite, null);
        }
      } else if (XmlOperationEnum.ImportPdwh.equals(context.getCurrentAction())) {
        String dupPubId = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "dup_pub_id");
        if ("".equals(dupPubId)) {
          patentLogService.logOp(pubId, context.getCurrentUserId(), context.getCurrentInsId(),
              PublicationOperationEnum.ImportPdwh, null);
        } else {
          patentLogService.logOp(pubId, context.getCurrentUserId(), context.getCurrentInsId(),
              PublicationOperationEnum.ImportPdwhOverwrite, null);
        }
      }
    }
    return true;
  }

}
