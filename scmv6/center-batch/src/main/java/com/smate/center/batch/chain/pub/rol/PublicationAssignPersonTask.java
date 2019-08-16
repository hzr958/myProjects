package com.smate.center.batch.chain.pub.rol;

import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.constant.PublicationRolStatusEnum;
import com.smate.center.batch.enums.pub.XmlOperationEnum;
import com.smate.center.batch.model.rol.pub.PublicationRol;
import com.smate.center.batch.model.sns.pub.PubXmlProcessContext;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.service.pub.IPubXmlTask;
import com.smate.center.batch.service.pub.mq.PubAssignMessageProducer;
import com.smate.center.batch.service.rol.pub.PublicationRolService;


/**
 * 导入时立刻指派人员.
 * 
 * @author yamingd
 * 
 */
public class PublicationAssignPersonTask implements IPubXmlTask {

  private String name = "pub_assign_person";

  @Autowired
  private PubAssignMessageProducer pubAssignMessageProducer;

  @Autowired
  private PublicationRolService publicationRolService;

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

    if (context.getCurrentPubId() == null || context.getCurrentInsId() == null) {
      throw new Exception("指派成果缺少必要参数: pubId 或 insId为空.");
    }

    Long pubId = context.getCurrentPubId();
    PublicationRol pub = this.publicationRolService.getPublicationById(pubId);
    int status = pub.getStatus();
    if (status == PublicationRolStatusEnum.APPROVED || status == PublicationRolStatusEnum.NEED_CONFIRM) {
      // 导入类型
      Integer importType = XmlOperationEnum.OfflineImport.equals(context.getCurrentAction()) ? 1 : 0;
      // 只有成果是通过审核的才指派.
      pubAssignMessageProducer.assignByPub(context.getCurrentInsId(), context.getCurrentUserId(),
          context.getCurrentPubId(), importType);
      return true;
    }

    return true;
  }

}
