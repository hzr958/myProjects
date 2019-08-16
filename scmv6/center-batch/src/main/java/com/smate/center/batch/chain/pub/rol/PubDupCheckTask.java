package com.smate.center.batch.chain.pub.rol;

import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.model.rol.pub.PubRolSubmissionStatusEnum;
import com.smate.center.batch.model.rol.pub.PublicationRol;
import com.smate.center.batch.model.sns.pub.PubXmlProcessContext;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.service.pub.IPubXmlTask;
import com.smate.center.batch.service.rol.pub.PublicationRolService;

/**
 * XML查重触发.
 *
 */
public class PubDupCheckTask implements IPubXmlTask {

  private String name = "pub_dup_check";

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
    Long pubId = context.getCurrentPubId();
    PublicationRol pub = this.publicationRolService.getPublicationById(pubId);
    int status = pub.getStatus();
    if (status == PubRolSubmissionStatusEnum.APPROVED) {
      // 只有成果是通过审核的才查重，否则不用查重.
      publicationRolService.sendPubDupCheckQueue(context);
      return true;
    }
    return false;
  }

}
