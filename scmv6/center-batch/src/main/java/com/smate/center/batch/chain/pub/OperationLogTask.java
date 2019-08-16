package com.smate.center.batch.chain.pub;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.enums.pub.PublicationOperationEnum;
import com.smate.center.batch.enums.pub.XmlOperationEnum;
import com.smate.center.batch.model.sns.pub.PubXmlProcessContext;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.service.pub.IPubXmlTask;
import com.smate.center.batch.service.pub.PublicationLogService;
import com.smate.core.base.utils.constant.PubXmlConstants;


/**
 * @author yamingd Xml操作日志记录Task
 */
public class OperationLogTask implements IPubXmlTask {

  private String name = "log_task";

  @Autowired
  private PublicationLogService publicationLogService;

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

    if (XmlOperationEnum.Import.equals(context.getCurrentAction())) {
      String dupPubId = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "dup_pub_id");
      if ("".equals(dupPubId)) {
        publicationLogService.logOp(pubId, context.getCurrentUserId(), context.getCurrentInsId(),
            PublicationOperationEnum.Import, null);
      } else {
        publicationLogService.logOp(pubId, context.getCurrentUserId(), context.getCurrentInsId(),
            PublicationOperationEnum.ImportOverwrite, null);
      }
    } else if (XmlOperationEnum.OfflineImport.equals(context.getCurrentAction())) {
      publicationLogService.logOp(pubId, context.getCurrentUserId(), context.getCurrentInsId(),
          PublicationOperationEnum.OfflineImport, null);
    } else if (XmlOperationEnum.Enter.equals(context.getCurrentAction())) {
      publicationLogService.logOp(pubId, context.getCurrentUserId(), context.getCurrentInsId(),
          PublicationOperationEnum.Create, null);
    } else if (XmlOperationEnum.Edit.equals(context.getCurrentAction())) {
      publicationLogService.logOp(pubId, context.getCurrentUserId(), context.getCurrentInsId(),
          PublicationOperationEnum.Update, null);
    } else if (XmlOperationEnum.PushFromIns.equals(context.getCurrentAction())) {
      Map<String, String> opDetail = new HashMap<String, String>();
      String recordInsId = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "record_ins_id");
      String insPubId = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "ins_pub_id");
      opDetail.put("record_ins_id", recordInsId);
      opDetail.put("ins_pub_id", insPubId);
      publicationLogService.logOp(pubId, context.getCurrentUserId(), context.getCurrentInsId(),
          PublicationOperationEnum.PushFromIns, opDetail);
    } else if (XmlOperationEnum.SyncFromSNS.equals(context.getCurrentAction())) {
      Map<String, String> opDetail = new HashMap<String, String>();
      String recordPsnId = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "rol_sync_from_sns"); // 成果所有人ID
      String snsPubId = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "rol_sync_from_pub"); // SNN成果ID
      opDetail.put("record_psn_id", recordPsnId);
      opDetail.put("sns_pub_id", snsPubId);
      publicationLogService.logOp(pubId, context.getCurrentUserId(), context.getCurrentInsId(),
          PublicationOperationEnum.SyncFromSNS, opDetail);
    }

    return true;
  }

}
